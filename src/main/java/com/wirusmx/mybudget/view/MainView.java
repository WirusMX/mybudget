package com.wirusmx.mybudget.view;

import com.wirusmx.mybudget.DefaultExceptionHandler;
import com.wirusmx.mybudget.controller.MainController;
import com.wirusmx.mybudget.managers.ResourcesManager;
import com.wirusmx.mybudget.model.Model;
import com.wirusmx.mybudget.model.Note;
import com.wirusmx.mybudget.model.SimpleData;
import com.wirusmx.mybudget.model.comparators.MyComparator;
import com.wirusmx.mybudget.view.dataviews.DataView;
import com.wirusmx.mybudget.view.dataviews.ListView;
import com.wirusmx.mybudget.view.dataviews.TableView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Set;

/**
 * Application main frame view.
 *
 * @author Piunov M (aka WirusMX)
 */
public class MainView extends JFrame {
    public static final String EDIT_TAG = "#!EDIT!#";

    private MainController controller;
    private ResourcesManager resourcesManager;

    private Class[] dataViews;

    private DataView currentDataView;
    private JLabel statLabel = new JLabel();
    private JPanel centerPanel;

    private JTextField searchTextField;
    private JButton resetButton;
    private JButton searchButton;

    public MainView(MainController controller, ResourcesManager resourcesManager) {
        this.controller = controller;
        this.resourcesManager = resourcesManager;
        dataViews = new Class[]{ListView.class, TableView.class};
    }

    public void init() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {

        }

        setTitle(resourcesManager.getProperty("application.title")
                + " v." + resourcesManager.getProperty("application.version"));
        setIconImage(resourcesManager.getImage("favicon").getImage());
        setBounds(0, 0, 1024, 600);
        setMinimumSize(new Dimension(900, 300));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        centerPanel = new JPanel(new BorderLayout());
        //noinspection unchecked
        setDataView(dataViews[controller.getDataViewID()]);

        JMenuBar mainMenu = new JMenuBar();
        addMainMenu(mainMenu);

        JMenuBar controlPanel = new JMenuBar();
        addControlPanel(controlPanel);

        JPanel panel = new JPanel(new BorderLayout(2, 2));
        panel.add(mainMenu, BorderLayout.NORTH);
        panel.add(controlPanel, BorderLayout.SOUTH);
        getContentPane().add(panel, BorderLayout.NORTH);

        centerPanel.add(statLabel, BorderLayout.SOUTH);
        add(centerPanel);

        update();

        setVisible(true);
    }

    /**
     * Updates data on main frame.
     */
    public void update() {
        java.util.List<Note> notes = controller.getNotes();
        currentDataView.setNotes(notes);

        float total = 0;
        float[] totalByNecessity = new float[2];

        for (Note n : notes) {
            total += n.getTotal();
            totalByNecessity[n.getNecessity().getId()] += n.getTotal();
        }

        statLabel.setText("Итого: " + String.format(Note.PRICE_FORMAT, total) + " руб., из них "
                + String.format(Note.PRICE_FORMAT, totalByNecessity[0]) + " руб. на товары высокой необходимости, "
                + String.format(Note.PRICE_FORMAT, totalByNecessity[1]) + " руб. - низкой необходимости");
    }

    public String getCurrentDataViewClass() {
        return currentDataView.getClass().getName();
    }

    public void showAboutDialog() {
        JLabel messageLabel = new JLabel(resourcesManager.getText("about"));
        messageLabel.setFont(new Font("Monospased", Font.PLAIN, 14));
        JOptionPane.showMessageDialog(this,
                messageLabel,
                "О программе", JOptionPane.PLAIN_MESSAGE);
    }

    public void showUsingRulesDialog() {
        JLabel messageLabel = new JLabel(resourcesManager.getText("using_rules"));
        messageLabel.setFont(new Font("Monospased", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(messageLabel);
        scrollPane.setPreferredSize(new Dimension(600, 500));

        JOptionPane.showMessageDialog(this,
                scrollPane,
                "Правила пользования ПО",
                JOptionPane.PLAIN_MESSAGE);
    }

    public void setDataView(Class<DataView> dataView) {
        try {
            if (currentDataView != null) {
                centerPanel.remove(currentDataView);
            }

            currentDataView = dataView.newInstance();

            currentDataView.addMouseListener(controller.getEditNoteActionListener(currentDataView));
            currentDataView.setComponentPopupMenu(createNotesListPopupMenu());

            centerPanel.add(currentDataView, BorderLayout.CENTER);
            revalidate();
            repaint();
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(ex);
        }
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


    public boolean isNoteSelected() {
        return currentDataView.getSelectedValue() != null;
    }

    public String getSearchQuery(){
        return searchTextField.getText();
    }

    public void resetSearchQuery(){
        searchTextField.setText("");
    }

    public void doSearchButtonClick(){
        searchButton.doClick();
    }

    public void doResetButtonClick(){
        resetButton.doClick();
    }

    private void addMainMenu(JMenuBar menuBar) {

        JMenu fileMenu = new JMenu("Файл");
        fileMenu.addMenuListener(controller.getEditMenuListener());

        fileMenu.add(
                createMenuItem(
                        "Новая запись",
                        KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK),
                        controller.getAddNewNoteButtonActionListener(),
                        "new"
                )
        );

        fileMenu.add(
                createMenuItem(
                        "Изменить запись",
                        EDIT_TAG,
                        KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK),
                        controller.getEditNoteActionListener(currentDataView),
                        "edit"
                )
        );

        fileMenu.add(
                createMenuItem(
                        "Дублировать",
                        EDIT_TAG,
                        KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK),
                        controller.getDuplicateNoteActionListener(currentDataView),
                        "duplicate"
                )
        );

        fileMenu.add(
                createMenuItem(
                        "Удалить запись",
                        EDIT_TAG,
                        KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK),
                        controller.getRemoveNoteButtonActionListener(currentDataView),
                        "remove"
                )
        );

        fileMenu.addSeparator();

        fileMenu.add(
                createMenuItem(
                        "Выход",
                        KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK),
                        controller.getExitButtonButtonActionListener(),
                        "exit"
                )
        );

        menuBar.add(fileMenu);

        JMenu viewMenu = new JMenu("Вид");
        viewMenu.addMenuListener(controller.getViewMenuListener());

        viewMenu.add(
                createMenuItem(
                        "Список",
                        ListView.class.getName(),
                        KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK),
                        controller.getDataViewButtonActionListener(dataViews[0], 0),
                        "list_view"
                )
        );

        viewMenu.add(
                createMenuItem(
                        "Таблица",
                        TableView.class.getName(),
                        KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK),
                        controller.getDataViewButtonActionListener(dataViews[1], 1),
                        "table_view"
                )
        );

        menuBar.add(viewMenu);

        JMenu toolsMenu = new JMenu("Инструменты");

        toolsMenu.add(
                createMenuItem(
                        "Статистика",
                        KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK),
                        controller.getStatisticsButtonActionListener(),
                        "stat"
                )
        );

        toolsMenu.addSeparator();


        toolsMenu.add(
                createMenuItem(
                        "Настройки",
                        KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK),
                        controller.getSettingsButtonActionListener(),
                        "settings"
                )
        );

        menuBar.add(toolsMenu);

        JMenu infoMenu = new JMenu("Справка");

        infoMenu.add(
                createMenuItem(
                        "О программе",
                        null,
                        controller.getAboutButtonActionListener(),
                        "about"
                )
        );

        infoMenu.add(
                createMenuItem(
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
        sortTypeComboBox.addItem(new SimpleData(Model.SortType.ITEM, "Товар"));
        sortTypeComboBox.addItem(new SimpleData(Model.SortType.TYPE, "Категория"));
        sortTypeComboBox.addItem(new SimpleData(Model.SortType.PRICE, "Цена"));
        sortTypeComboBox.addItem(new SimpleData(Model.SortType.SHOP, "Магазин"));
        sortTypeComboBox.addItem(new SimpleData(Model.SortType.BY_SALE, "Скидка"));

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

        searchTextField = new JTextField("");
        searchTextField.addKeyListener(controller.getSearchTextFieldKeyListener());
        menuBar.add(searchTextField);
        searchButton = new JButton("Поиск");
        searchButton.addActionListener(controller.getSearchButtonActionListener());
        menuBar.add(searchButton);

        menuBar.add(new JLabel(" "));

        resetButton = new JButton("Сбросить");
        resetButton.addActionListener(controller.getResetButtonActionListener());
        menuBar.add(resetButton);
    }

    private JPopupMenu createNotesListPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(
                createMenuItem(
                        "Новая запись",
                        null,
                        controller.getAddNewNoteButtonActionListener(),
                        "new"
                )
        );

        popupMenu.add(
                createMenuItem(
                        "Изменить запись",
                        EDIT_TAG,
                        null,
                        controller.getEditNoteActionListener(currentDataView),
                        "edit"
                )
        );

        popupMenu.add(
                createMenuItem(
                        "Дублировать",
                        EDIT_TAG,
                        null,
                        controller.getDuplicateNoteActionListener(currentDataView),
                        "duplicate"
                )
        );

        popupMenu.add(
                createMenuItem(
                        "Удалить запись",
                        EDIT_TAG,
                        null,
                        controller.getRemoveNoteButtonActionListener(currentDataView),
                        "remove"
                )
        );

        return popupMenu;
    }

    private JMenuItem createMenuItem(String title, KeyStroke keyStroke, ActionListener actionListener, String imageName) {
        return createMenuItem(title, null, keyStroke, actionListener, imageName);
    }

    private JMenuItem createMenuItem(String title, String name, KeyStroke keyStroke, ActionListener actionListener, String imageName) {
        JMenuItem menuItem = new JMenuItem(title);

        if (name != null) {
            menuItem.setName(name);
        }

        if (keyStroke != null) {
            menuItem.setAccelerator(keyStroke);
        }

        menuItem.addActionListener(actionListener);
        if (imageName != null) {
            menuItem.setIcon(resourcesManager.getImage(imageName));
        }

        return menuItem;
    }
}
