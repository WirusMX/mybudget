package com.wirusmx.mybudget.view.dialogs;

import com.wirusmx.mybudget.controller.Controller;
import com.wirusmx.mybudget.model.Model;
import com.wirusmx.mybudget.model.SimpleData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Set;

/**
 * Statistics dialog. Shows data like a graph.
 *
 * @author Piunov M (aka WirusMX)
 */
public class StatisticsDialog extends JDialog {
    private Controller controller;

    private JPanel graphPanel;
    private DefaultTableModel tableModel;
    private JTable table;
    private int selectedPeriodType = Model.PeriodType.YEAR;
    private String selectedPeriod;
    private int selectedItemType;

    public StatisticsDialog(Controller controller) {
        this.controller = controller;
        init();
    }

    public void setSelectedPeriod(String selectedPeriod) {
        this.selectedPeriod = selectedPeriod;
    }

    public void setSelectedItemType(int selectedItemType) {
        this.selectedItemType = selectedItemType;
    }

    private void init() {
        setSize(1000, 600);
        setModal(true);
        setLocationRelativeTo(null);
        setIconImage(controller.getImage("stat").getImage());
        getRootPane().registerKeyboardAction(
                controller.getCloseStatisticsDialogButtonActionListener(this),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        setLayout(new BorderLayout(3, 3));

        JPanel controlPanel = new JPanel();

        controlPanel.add(new JLabel("Статистика расходов на "));

        JComboBox<SimpleData> itemTypeComboBox = new JComboBox<>();
        itemTypeComboBox.addItem(new SimpleData(-1, "все товары"));
        Set<SimpleData> itemTypes = controller.getComboBoxValues("item_types");
        for (SimpleData sd : itemTypes) {
            itemTypeComboBox.addItem(sd);
        }
        itemTypeComboBox.addItemListener(controller.getStatisticsDialogItemTypeComboBoxItemListener(this));
        controlPanel.add(itemTypeComboBox);

        selectedItemType = -1;

        controlPanel.add(new JLabel(" за "));

        JComboBox<String> periodComboBox = new JComboBox<>();
        java.util.Set<String> periods = controller.getPeriods(selectedPeriodType);
        for (String p : periods) {
            periodComboBox.addItem(p);
        }
        periodComboBox.addItemListener(controller.getStatisticsDialogPeriodComboBoxItemListener(this));

        selectedPeriod = (String) periodComboBox.getSelectedItem();

        controlPanel.add(periodComboBox);

        controlPanel.add(new JLabel(" год"));

        add(controlPanel, BorderLayout.NORTH);

        graphPanel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                if (!selectedPeriod.isEmpty()) {
                    draw((Graphics2D) g);
                }
            }
        };

        add(graphPanel, BorderLayout.CENTER);

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(getWidth(), 75));

        add(scrollPane, BorderLayout.SOUTH);

        update();
        graphPanel.repaint();

        setVisible(true);

    }

    public void update() {
        if (selectedPeriod.isEmpty()) {
            return;
        }

        tableModel.setColumnIdentifiers(new String[]{" ", "01", "02", "03", "04", "05", "06", "07", "08", "09",
                "10", "11", "12"});

        float[][] values = controller.getStatistics(selectedPeriodType, selectedPeriod, selectedItemType);
        String[] rowHeaders = new String[]{"Выс. необх.", "Низк. необх.", "Всего"};

        for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }

        for (int i = 0; i < 3; i++) {
            String[] row = new String[values[0].length + 1];
            row[0] = rowHeaders[i];
            for (int j = 1; j < row.length; j++) {
                row[j] = "" + values[i][j - 1];
            }
            tableModel.addRow(row);
        }

        graphPanel.repaint();
    }

    private void draw(Graphics2D graphics) {
        int panelWidth = graphPanel.getWidth();
        int panelHeight = graphPanel.getHeight();

        drawField(graphics, panelWidth, panelHeight);

        int[] x = new int[12];
        int[] y = new int[11];

        float[][] values = controller.getStatistics(selectedPeriodType, selectedPeriod, selectedItemType);

        float maxValue = values[2][0];
        for (float i : values[2]) {
            if (i > maxValue) {
                maxValue = i;
            }
        }

        int x0 = 70;
        int y0 = panelHeight - 50;
        int yMin = 10;
        int xMax = panelWidth - 10;

        drawGrid(graphics, panelWidth, x, y, x0, y0, yMin);
        drawXLabels(graphics, x, y0);
        drawYLabels(graphics, x, y, x0, yMin, maxValue);
        drawLegend(graphics, x0, y0, xMax);
        drawStatistics(graphics, x, y0 - y[y.length - 1], values, maxValue, y0);
    }

    private void drawField(Graphics2D graphics, int panelWidth, int panelHeight) {
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, panelWidth, panelHeight);

        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(2.0f));
        graphics.drawRect(0, 0, panelWidth, panelHeight);
    }

    private void drawGrid(Graphics2D graphics, int xMax, int[] x, int[] y, int x0, int y0, int yMin) {
        graphics.setStroke(new BasicStroke(3.0f));
        graphics.drawLine(x0, yMin, x0, y0);
        graphics.drawLine(x0, y0, xMax, y0);

        int dx = (xMax - x0 - 10) / x.length;
        int dy = (y0 - yMin) / y.length;

        for (int i = 0; i < x.length; i++) {
            x[i] = x0 + dx * i;
        }

        for (int i = 0; i < y.length; i++) {
            y[i] = y0 - dy * i;
        }

        graphics.setStroke(new BasicStroke(2.0f));

        for (int i = 1; i < x.length; i++) {
            graphics.drawLine(x[i], y0 - 2, x[i], y0 + 3);
            graphics.setColor(Color.LIGHT_GRAY);
            graphics.drawLine(x[i], y0 - 2, x[i], y[y.length - 1]);
            graphics.setColor(Color.BLACK);
        }

        for (int i = 1; i < y.length; i++) {
            graphics.drawLine(x0 - 2, y[i], x0 + 3, y[i]);
            graphics.setColor(Color.LIGHT_GRAY);
            graphics.drawLine(x0 + 3, y[i], x[x.length - 1], y[i]);
            graphics.setColor(Color.BLACK);
        }

    }

    private void drawXLabels(Graphics2D graphics, int[] x, int y0) {
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < x.length; i++) {
            graphics.drawString((i > 8 ? "" + (i + 1) : "0" + (i + 1)), x[i] - 7, y0 + 20);
        }

        graphics.drawString("Месяцы", x[x.length - 1] + 10, y0 - 10);
    }

    private void drawYLabels(Graphics2D graphics, int[] x, int[] y, int x0, int yMin, float maxValue) {
        graphics.setColor(Color.BLACK);

        for (int i = 1; i < y.length; i++) {
            graphics.drawString("" + String.format("%.3f", i * maxValue / 10000f), x0 - 60, y[i] + 5);
        }

        graphics.drawString("Расходы (тыс. руб.)", x0 + 10, yMin + 10);
    }

    private void drawLegend(Graphics2D graphics, int x0, int y0, int xMax) {
        int lineY = y0 + 40;
        int labelY = y0 + 43;
        int offset = (xMax - x0) / 3;

        Color[] colors = new Color[]{Color.GREEN, Color.RED, Color.BLUE};

        String[] labels = new String[]{" - высокой необходимости", " - низкой необходимости", " - всего"};

        for (int i = 0; i < 3; i++) {
            graphics.setColor(colors[i]);
            graphics.drawLine(x0 + offset * i, lineY, x0 + offset * i + 20, lineY);
            graphics.setColor(Color.BLACK);
            graphics.drawString(labels[i], x0 + offset * i + 25, labelY);
        }

    }

    private void drawStatistics(Graphics2D graphics, int[] x, int hundredPercent, float[][] values, float maxValue, int y0) {
        Color[] colors = new Color[]{Color.GREEN, Color.RED, Color.BLUE};
        for (int j = 0; j < 3; j++) {
            int prevX = 0;
            int prevY = 0;
            for (int i = 0; i < x.length; i++) {
                graphics.setColor(colors[j]);
                int currentX = x[i];
                int currentY = y0 - (int) (((values[j][i]) / maxValue) * hundredPercent);
                graphics.fillOval(currentX - 5, currentY - 7, 10, 10);
                if (i > 0) {
                    graphics.drawLine(prevX, prevY, currentX, currentY);

                }

                prevX = currentX;
                prevY = currentY;
            }
        }
    }
}
