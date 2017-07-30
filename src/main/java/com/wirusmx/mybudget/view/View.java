package com.wirusmx.mybudget.view;

import com.wirusmx.mybudget.controller.Controller;
import com.wirusmx.mybudget.model.Model;
import com.wirusmx.mybudget.model.Note;
import com.wirusmx.mybudget.model.SimpleData;
import com.wirusmx.mybudget.model.comparators.MyComparator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Set;

/**
 * @author Piunov M (aka WirusMX)
 */
public class View extends JFrame {
    private Controller controller;
    private String applicationTitle;
    private String applicationVersion;

    private JList<Note> notesList;
    private DefaultListModel<Note> noteDefaultListModel;
    private JLabel statLabel = new JLabel();


    public View(Controller controller, String applicationTitle, String applicationVersion) {
        this.controller = controller;
        this.applicationTitle = applicationTitle;
        this.applicationVersion = applicationVersion;
    }

    public void init() {
/*
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {

        }
*/
        setTitle(applicationTitle + " v." + applicationVersion);
        setIconImage(controller.getImage("favicon").getImage());
        setBounds(0, 0, 1024, 600);
        setMinimumSize(new Dimension(900, 300));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        noteDefaultListModel = new DefaultListModel<>();
        notesList = new JList<>(noteDefaultListModel);
        notesList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Note) {
                    if (((Note) value).getNecessity().getId() == 1 && ((Note) value).isBySale()) {
                        setBackground(Color.ORANGE);
                    } else {
                        if (((Note) value).isBySale()) {
                            setBackground(Color.GREEN);
                        } else {
                            if (((Note) value).getNecessity().getId() == 1) {
                                setBackground(Color.PINK);
                            }
                        }
                    }
                }

                return c;
            }
        });

        notesList.addMouseListener(controller.getEditNoteActionListener(notesList));
        notesList.setComponentPopupMenu(getNotesListPopupMenu());

        JMenuBar mainMenu = new JMenuBar();

        addMainMenu(mainMenu);

        JMenuBar controlPanel = new JMenuBar();

        addControlPanel(controlPanel);

        JPanel panel = new JPanel(new BorderLayout(2, 2));
        panel.add(mainMenu, BorderLayout.NORTH);
        panel.add(controlPanel, BorderLayout.SOUTH);

        getContentPane().add(panel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(notesList), BorderLayout.CENTER);
        centerPanel.add(statLabel, BorderLayout.SOUTH);
        add(centerPanel);

        update();

        setVisible(true);
    }


    public void update() {
        noteDefaultListModel.clear();

        java.util.List<Note> notes = controller.getNotes();

        float summ = 0;
        float[] necSum = new float[2];

        for (Note n : notes) {
            noteDefaultListModel.addElement(n);
            summ += n.getPrice();
            necSum[n.getNecessity().getId()] += n.getPrice();
        }

        statLabel.setText("Итого: " + String.format(Note.PRICE_FORMAT, summ) + " руб., из них "
                + String.format(Note.PRICE_FORMAT, necSum[0]) + " руб. на продукты высокой необходимости, "
                + String.format(Note.PRICE_FORMAT, necSum[1]) + " руб. - низкой необходимости");
    }

    public void setPeriodComboBoxValues(JComboBox<String> periodComboBox, int periodType) {
        periodComboBox.setVisible(periodType != Model.PeriodType.ALL);
        periodComboBox.removeAllItems();

        Set<String> values = controller.getPeriods();

        int pos = -1;
        int i = 0;
        for (String s : values) {
            periodComboBox.addItem(s);
            if (controller.getSelectedPeriod().equals(s)) {
                pos = i;
            }
            i++;
        }

        periodComboBox.setSelectedIndex(pos);
    }

    private void addMainMenu(JMenuBar menuBar) {

        JMenu fileMenu = new JMenu("Файл");

        fileMenu.add(
                getMenuItem(
                        "Новая запись",
                        KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK),
                        controller.getAddNewNoteButtonActionListener(),
                        "new"
                )
        );

        fileMenu.add(
                getMenuItem(
                        "Изменить запись",
                        KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK),
                        controller.getEditNoteActionListener(notesList),
                        "edit"
                )
        );

        fileMenu.add(
                getMenuItem(
                        "Удалить запись",
                        KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK),
                        controller.getRemoveNoteButtonActionListener(notesList),
                        "remove"
                )
        );

        fileMenu.addSeparator();

        fileMenu.add(
                getMenuItem(
                        "Выход",
                        KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK),
                        controller.getExitButtonButtonActionListener(),
                        "exit"
                )
        );

        menuBar.add(fileMenu);

        JMenu toolsMenu = new JMenu("Инструменты");

        toolsMenu.add(
                getMenuItem(
                        "Статистика",
                        KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK),
                        controller.getStatisticsButtonButtonActionListener(),
                        "stat"
                )
        );

        toolsMenu.addSeparator();

        /*
        toolsMenu.add(
                getMenuItem(
                        "Настройки",
                        KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK),
                        controller.getSettingsButtonButtonActionListener(),
                        "settings"
                )
        );*/

        menuBar.add(toolsMenu);

        JMenu infoMenu = new JMenu("Справка");

        infoMenu.add(
                getMenuItem(
                        "О программе",
                        null,
                        controller.getAboutButtonActionListener(),
                        "about"
                )
        );

        infoMenu.add(
                getMenuItem(
                        "Правила пользования ПО",
                        null,
                        controller.getUsingRulesButtonActionListener(),
                        "using_rules"
                )
        );

        menuBar.add(infoMenu);


    }

    private void addControlPanel(JMenuBar menuBar) {
        menuBar.add(new JLabel("Показать записи за период: "));
        JComboBox<SimpleData> periodTypeComboBox = new JComboBox<>();
        periodTypeComboBox.addItem(new SimpleData(Model.PeriodType.ALL, "Все"));
        periodTypeComboBox.addItem(new SimpleData(Model.PeriodType.YEAR, "Год"));
        periodTypeComboBox.addItem(new SimpleData(Model.PeriodType.MONTH, "Месяц"));
        periodTypeComboBox.addItem(new SimpleData(Model.PeriodType.DAY, "День"));
        periodTypeComboBox.setSelectedIndex(controller.getSelectedPeriodType());
        menuBar.add(periodTypeComboBox);

        menuBar.add(new JLabel(" "));

        JComboBox<String> periodComboBox = new JComboBox<>();
        setPeriodComboBoxValues(periodComboBox, controller.getSelectedPeriodType());
        periodTypeComboBox.addItemListener(controller.getPeriodTypeComboBoxItemListener(periodComboBox));
        periodComboBox.addItemListener(controller.getPeriodComboBoxItemListener());
        menuBar.add(periodComboBox);

        menuBar.add(new JLabel(" | "));

        menuBar.add(new JLabel("Сортировка: "));
        JComboBox<SimpleData> sortTypeComboBox = new JComboBox<>();
        sortTypeComboBox.addItem(new SimpleData(Model.SortType.DATE, "Дата"));
        sortTypeComboBox.addItem(new SimpleData(Model.SortType.ITEM, "Продукт"));
        /*
        sortTypeComboBox.addItem(new SimpleData(SortType.TYPE, "Категория"));
        sortTypeComboBox.addItem(new SimpleData(SortType.PRICE, "Цена"));
        sortTypeComboBox.addItem(new SimpleData(SortType.SHOP, "Магазин"));
        sortTypeComboBox.addItem(new SimpleData(SortType.BY_SALE, "Скидка"));
        */
        sortTypeComboBox.setSelectedIndex(controller.getSelectedSortType());
        sortTypeComboBox.addItemListener(controller.getSortTypeComboBoxItemListener());
        menuBar.add(sortTypeComboBox);

        menuBar.add(new JLabel(" "));

        JComboBox<SimpleData> sortOrderComboBox = new JComboBox<>();
        sortOrderComboBox.addItem(new SimpleData(MyComparator.DIRECT_ORDER, "Прямая (А->Я)"));
        sortOrderComboBox.addItem(new SimpleData(MyComparator.REVERSE_ORDER, "Обратная (Я->А)"));
        sortOrderComboBox.setSelectedIndex((controller.getSelectedSortOrder() + 2) % 3);
        sortOrderComboBox.addItemListener(controller.getSortOrderComboBoxItemListener());
        menuBar.add(sortOrderComboBox);

        menuBar.add(new JLabel(" | "));

        JTextField searchTextField = new JTextField("");
        menuBar.add(searchTextField);
        JButton searchButton = new JButton("Поиск");
        searchButton.addActionListener(controller.getSearchButtonActionListener(searchTextField));
        menuBar.add(searchButton);

        menuBar.add(new JLabel(" "));

        JButton resetButton = new JButton("Сбросить");
        resetButton.addActionListener(controller.getResetButtonActionListener(searchTextField));
        menuBar.add(resetButton);
    }

    private JPopupMenu getNotesListPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        popupMenu.add(
                getMenuItem(
                        "Новая запись",
                        KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK),
                        controller.getAddNewNoteButtonActionListener(),
                        "new"
                )
        );

        popupMenu.add(
                getMenuItem(
                        "Изменить запись",
                        KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK),
                        controller.getEditNoteActionListener(notesList),
                        "edit"
                )
        );

        popupMenu.add(
                getMenuItem(
                        "Удалить запись",
                        KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK),
                        controller.getRemoveNoteButtonActionListener(notesList),
                        "remove"
                )
        );

        return popupMenu;
    }

    private JMenuItem getMenuItem(String title, KeyStroke keyStroke, ActionListener actionListener, String imageName) {
        JMenuItem menuItem = new JMenuItem(title);
        if (keyStroke != null) {
            menuItem.setAccelerator(keyStroke);
        }
        menuItem.addActionListener(actionListener);
        if (imageName != null) {
            menuItem.setIcon(controller.getImage(imageName));
        }
        return menuItem;
    }

}
