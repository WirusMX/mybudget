package com.wirusmx.mybudget.view;

import com.wirusmx.mybudget.controller.Controller;
import com.wirusmx.mybudget.model.Model;
import com.wirusmx.mybudget.model.Note;
import com.wirusmx.mybudget.model.SimpleData;
import com.wirusmx.mybudget.model.comparators.MyComparator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Set;

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

    Controller getController() {
        return controller;
    }

    public void init() {
        setTitle(applicationTitle + " v." + applicationVersion);
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

        addMainMenu();

        addControlPanel();

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

        int summ = 0;
        int[] necSum = new int[2];

        for (Note n : notes) {
            noteDefaultListModel.addElement(n);
            summ += n.getPrice();
            necSum[n.getNecessity().getId()] += n.getPrice();
        }

        statLabel.setText("Итого: " + summ + " руб., из них " + necSum[0] + " руб. - высокой необходимости, " +
                necSum[1] + " руб. - низкой необходимости");
    }

    private void addMainMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Файл");

        JMenuItem newNoteMenuItem = new JMenuItem("Новая запись");
        newNoteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        newNoteMenuItem.addActionListener(controller.getAddNewNoteButtonActionListener());
        fileMenu.add(newNoteMenuItem);

        JMenuItem editNoteMenuItem = new JMenuItem("Изменить запись");
        editNoteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        editNoteMenuItem.addActionListener(controller.getEditNoteActionListener(notesList));
        fileMenu.add(editNoteMenuItem);

        fileMenu.addSeparator();

        JMenuItem exitMenuItem = new JMenuItem("Выход");
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK));
        exitMenuItem.addActionListener(controller.getExitButtonButtonActionListener());
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        JMenu infoMenu = new JMenu("Справка");

        JMenuItem aboutMenuItem = new JMenuItem("О программе");
        aboutMenuItem.addActionListener(controller.getAboutButtonButtonActionListener());
        infoMenu.add(aboutMenuItem);

        JMenuItem usingRulesMenuItem = new JMenuItem("Правила пользования ПО");
        usingRulesMenuItem.addActionListener(controller.getUsingRulesButtonActionListener());
        infoMenu.add(usingRulesMenuItem);

        menuBar.add(infoMenu);

        getContentPane().add(menuBar, BorderLayout.NORTH);
    }

    private void addControlPanel() {
        JMenuBar menuBar = new JMenuBar();

        JButton newNoteButton = new JButton("+");
        newNoteButton.addActionListener(controller.getAddNewNoteButtonActionListener());
        menuBar.add(newNoteButton);

        menuBar.add(new JLabel(" "));

        JButton editButton = new JButton("/");
        editButton.addActionListener(controller.getEditNoteActionListener(notesList));
        menuBar.add(editButton);

        menuBar.add(new JLabel(" | "));

        menuBar.add(new JLabel("За период:"));
        JComboBox<SimpleData> periodTypeComboBox = new JComboBox<>();
        periodTypeComboBox.addItem(new SimpleData(Model.PeriodType.ALL, "Все"));
        periodTypeComboBox.addItem(new SimpleData(Model.PeriodType.YEAR, "Год"));
        periodTypeComboBox.addItem(new SimpleData(Model.PeriodType.MONTH, "Месяц"));
        periodTypeComboBox.addItem(new SimpleData(Model.PeriodType.DAY, "День"));
        periodTypeComboBox.setSelectedIndex(controller.getSelectedPeriodType());
        menuBar.add(periodTypeComboBox);

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

        JComboBox<SimpleData> sortOrderComboBox = new JComboBox<>();
        sortOrderComboBox.addItem(new SimpleData(MyComparator.DIRECT_ORDER, "Прямая (А->Я)"));
        sortOrderComboBox.addItem(new SimpleData(MyComparator.REVERSE_ORDER, "Обратная (Я->А)"));
        sortOrderComboBox.setSelectedIndex((controller.getSelectedSortOrder() + 2) % 3);
        sortOrderComboBox.addItemListener(controller.getSortOrderComboBoxItemListener());
        menuBar.add(sortOrderComboBox);


        menuBar.add(new JLabel(" | "));

        JButton updateButton = new JButton("Обновить");
        updateButton.addActionListener(controller.getUpdateButtonActionListener());
        menuBar.add(updateButton);

        menuBar.add(new JLabel(" | "));


        JTextField searchTextField = new JTextField("");
        menuBar.add(searchTextField);
        JButton searchButton = new JButton("Поиск");
        searchButton.addActionListener(controller.getSearchButtonActionListener(searchTextField));
        menuBar.add(searchButton);
        getContentPane().add(menuBar, BorderLayout.SOUTH);
    }

    public void setPeriodComboBoxValues(JComboBox<String> periodComboBox, int periodType) {
        periodComboBox.setEnabled(periodType != Model.PeriodType.ALL);
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
}
