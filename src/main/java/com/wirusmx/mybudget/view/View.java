package com.wirusmx.mybudget.view;

import com.wirusmx.mybudget.Controller;
import com.wirusmx.mybudget.model.Note;
import com.wirusmx.mybudget.model.SimpleData;
import com.wirusmx.mybudget.model.comparators.DateComparator;
import com.wirusmx.mybudget.model.comparators.MyComparator;
import com.wirusmx.mybudget.model.comparators.TitlesComparator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class View extends JFrame {
    private Controller controller;
    private String applicationTitle;
    private String applicationVersion;

    private View thisView = this;
    private JList<Note> notesList;
    private DefaultListModel<Note> noteDefaultListModel;
    private JLabel statLabel = new JLabel();

    private MyComparator[] comparators;

    private int selectedPeriodType = PeriodType.ALL;
    private String selectedPeriod = "";
    private int selectedSortType = SortType.DATE;
    private int selectedSortOrder = MyComparator.REVERSE_ORDER;

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

        selectedPeriodType = Integer.parseInt(controller.getUserSettingsValue("main.window.period.type",
                "" + selectedPeriodType));
        selectedPeriod = controller.getUserSettingsValue("main.window.period", selectedPeriod);
        selectedSortType = Integer.parseInt(controller.getUserSettingsValue("main.window.sort.type",
                "" + selectedSortType));
        selectedSortOrder = Integer.parseInt(controller.getUserSettingsValue("main.window.sort.order",
                "" + selectedSortOrder));

        comparators = new MyComparator[]{
                new DateComparator(selectedSortOrder),
                new TitlesComparator(selectedSortOrder)
        };

        addMainMenu();

        addControlPanel();

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
        notesList.addMouseListener(new EditNoteActionListener());
        update();

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(notesList), BorderLayout.CENTER);

        centerPanel.add(statLabel, BorderLayout.SOUTH);

        add(centerPanel);

        setVisible(true);
    }


    public void update() {
        noteDefaultListModel.clear();

        java.util.List<Note> notes;

        if (selectedPeriodType == PeriodType.ALL) {
            notes = controller.getNotes();
        } else {
            notes = controller.getNotes(selectedPeriod);
        }

        Collections.sort(notes, comparators[selectedSortType]);
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
        newNoteMenuItem.addActionListener(new AddNewNoteActionListener());

        fileMenu.add(newNoteMenuItem);

        fileMenu.addSeparator();

        JMenuItem exitMenuItem = new JMenuItem("Выход");
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK));
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thisView.dispose();
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        getContentPane().add(menuBar, BorderLayout.NORTH);
    }

    private void addControlPanel() {
        JMenuBar menuBar = new JMenuBar();

        JButton newNoteButton = new JButton("+");
        newNoteButton.addActionListener(new AddNewNoteActionListener());
        menuBar.add(newNoteButton);

        menuBar.add(new JLabel(" "));

        JButton editButton = new JButton("/");
        editButton.addActionListener(new EditNoteActionListener());
        menuBar.add(editButton);

        menuBar.add(new JLabel(" | "));

        menuBar.add(new JLabel("За период:"));
        JComboBox<SimpleData> periodTypeComboBox = new JComboBox<>();
        periodTypeComboBox.addItem(new SimpleData(PeriodType.ALL, "Все"));
        periodTypeComboBox.addItem(new SimpleData(PeriodType.YEAR, "Год"));
        periodTypeComboBox.addItem(new SimpleData(PeriodType.MONTH, "Месяц"));
        periodTypeComboBox.addItem(new SimpleData(PeriodType.DAY, "День"));
        periodTypeComboBox.setSelectedIndex(selectedPeriodType);
        menuBar.add(periodTypeComboBox);

        JComboBox<String> periodComboBox = new JComboBox<>();
        setPeriodComboBoxValues(periodComboBox, selectedPeriodType);
        periodTypeComboBox.addItemListener(new PeriodTypeItemListener(periodComboBox));
        periodComboBox.addItemListener(new PeriodItemListener());
        menuBar.add(periodComboBox);

        menuBar.add(new JLabel(" | "));

        menuBar.add(new JLabel("Сортировка: "));
        JComboBox<SimpleData> sortTypeComboBox = new JComboBox<>();
        sortTypeComboBox.addItem(new SimpleData(SortType.DATE, "Дата"));
        sortTypeComboBox.addItem(new SimpleData(SortType.ITEM, "Продукт"));
        /*
        sortTypeComboBox.addItem(new SimpleData(SortType.TYPE, "Категория"));
        sortTypeComboBox.addItem(new SimpleData(SortType.PRICE, "Цена"));
        sortTypeComboBox.addItem(new SimpleData(SortType.SHOP, "Магазин"));
        sortTypeComboBox.addItem(new SimpleData(SortType.BY_SALE, "Скидка"));
        */
        sortTypeComboBox.setSelectedIndex(selectedSortType);
        sortTypeComboBox.addItemListener(new SortTypeItemListener());
        menuBar.add(sortTypeComboBox);

        JComboBox<SimpleData> sortOrderComboBox = new JComboBox<>();
        sortOrderComboBox.addItem(new SimpleData(MyComparator.DIRECT_ORDER, "Прямая (А->Я)"));
        sortOrderComboBox.addItem(new SimpleData(MyComparator.REVERSE_ORDER, "Обратная (Я->А)"));
        sortOrderComboBox.setSelectedIndex((selectedSortOrder + 2) % 3);
        sortOrderComboBox.addItemListener(new SortOrderItemListener());
        menuBar.add(sortOrderComboBox);


        menuBar.add(new JLabel(" | "));

        JButton updateButton = new JButton("Обновить");
        updateButton.addActionListener(new UpdateButtonActionListener());
        menuBar.add(updateButton);

        menuBar.add(new JLabel(" | "));


        JTextField searchTextField = new JTextField("");
        menuBar.add(searchTextField);
        JButton searchButton = new JButton("Поиск");
        searchButton.addActionListener(new SearchActionListener(searchTextField));
        menuBar.add(searchButton);
        getContentPane().add(menuBar, BorderLayout.SOUTH);
    }

    private void setPeriodComboBoxValues(JComboBox<String> periodComboBox, int periodType) {
        Set<String> values = new TreeSet<>();
        switch (periodType) {
            case PeriodType.ALL: {
                periodComboBox.setEnabled(false);
                break;
            }
            case PeriodType.YEAR: {
                periodComboBox.setEnabled(true);
                values = controller.getYears();
                break;
            }
            case PeriodType.MONTH: {
                periodComboBox.setEnabled(true);
                values = controller.getMonths();
                break;
            }
            case PeriodType.DAY: {
                values = controller.getDays();
                periodComboBox.setEnabled(true);
            }
        }

        periodComboBox.removeAllItems();

        int pos = -1;
        int i = 0;
        for (String s : values) {
            periodComboBox.addItem(s);
            if (selectedPeriod.equals(s)) {
                pos = i;
            }
            i++;
        }

        periodComboBox.setSelectedIndex(pos);

    }

    private class AddNewNoteActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            NoteEditDialog noteEditDialog = new NoteEditDialog(thisView);
            Note note = noteEditDialog.getNote();
            if (note != null) {
                controller.insertNote(note);
                update();
            }
        }
    }

    private class EditNoteActionListener implements ActionListener, MouseListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            doEdit();
        }

        private void doEdit() {
            Note note = notesList.getSelectedValue();
            if (note == null) {
                return;
            }
            NoteEditDialog noteEditDialog = new NoteEditDialog(thisView, note);
            note = noteEditDialog.getNote();
            if (note != null) {
                controller.updateNote(note);
                update();
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                doEdit();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    private class PeriodTypeItemListener implements ItemListener {

        private JComboBox<String> periodComboBox;

        PeriodTypeItemListener(JComboBox<String> periodComboBox) {
            this.periodComboBox = periodComboBox;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (!(e.getSource() instanceof JComboBox)) {
                return;
            }

            JComboBox<SimpleData> periodTypeComboBox = (JComboBox) e.getSource();
            int periodType = ((SimpleData) periodTypeComboBox.getSelectedItem()).getId();
            setPeriodComboBoxValues(periodComboBox, periodType);
            selectedPeriodType = periodType;
        }
    }

    private class PeriodItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (!(e.getSource() instanceof JComboBox)) {
                return;
            }

            JComboBox<SimpleData> periodComboBox = (JComboBox) e.getSource();
            selectedPeriod = ((String) periodComboBox.getSelectedItem());
            if (selectedPeriod == null){
                selectedPeriod = "";
                return;
            }
            controller.setUserSettingsValue("main.window.period.type", "" + selectedPeriodType);
            controller.setUserSettingsValue("main.window.period", selectedPeriod);
            update();
        }
    }

    private class SortTypeItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (!(e.getSource() instanceof JComboBox)) {
                return;
            }

            JComboBox<SimpleData> sortTypeComboBox = (JComboBox) e.getSource();
            selectedSortType = ((SimpleData) sortTypeComboBox.getSelectedItem()).getId();
            controller.setUserSettingsValue("main.window.sort.type", "" + selectedSortType);
            update();
        }
    }

    private class SortOrderItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (!(e.getSource() instanceof JComboBox)) {
                return;
            }

            JComboBox<SimpleData> sortTypeComboBox = (JComboBox) e.getSource();
            int sortOrder = ((SimpleData) sortTypeComboBox.getSelectedItem()).getId();
            for (MyComparator c : comparators) {
                c.setOrder(sortOrder);
            }

            controller.setUserSettingsValue("main.window.sort.order", "" + sortOrder);
            update();
        }
    }

    private class SearchActionListener implements ActionListener {

        private JTextField searchTextField;

        SearchActionListener(JTextField searchTextField) {
            this.searchTextField = searchTextField;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (searchTextField.getText().length() == 0) {
                return;
            }


            java.util.List<Note> notes = new ArrayList<>();
            for (int i = 0; i < noteDefaultListModel.getSize(); i++) {
                if (noteDefaultListModel.get(i).getItem().equalsIgnoreCase(searchTextField.getText())) {
                    notes.add(noteDefaultListModel.get(i));
                }
            }

            noteDefaultListModel.clear();

            for (Note n : notes) {
                noteDefaultListModel.addElement(n);
            }
        }
    }

    private class UpdateButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            update();
        }
    }

    @SuppressWarnings("WeakerAccess")
    private static class PeriodType {
        public static final int ALL = 0;
        public static final int YEAR = 1;
        public static final int MONTH = 2;
        public static final int DAY = 3;
    }

    @SuppressWarnings("WeakerAccess")
    private static class SortType {
        public static final int DATE = 0;
        public static final int ITEM = 1;
        public static final int TYPE = 2;
        public static final int PRICE = 3;
        public static final int SHOP = 4;
        public static final int BY_SALE = 5;
    }
}
