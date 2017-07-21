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
import java.util.ArrayList;
import java.util.Collections;

public class View extends JFrame {
    private Controller controller;
    private String applicationTitle;
    private String applicationVersion;

    private View thisView = this;
    private JList<Note> notesList;
    private DefaultListModel<Note> noteDefaultListModel;
    private JLabel statLabel = new JLabel();
    private MyComparator<Note>[] comparators
            = new MyComparator[]{
            new DateComparator(MyComparator.DIRECT_ORDER),
            new TitlesComparator(MyComparator.DIRECT_ORDER)
    };
    private int selectedComparator = 0;

    public View(Controller controller, String applicationTitle, String applicationVersion) {
        this.controller = controller;
        this.applicationTitle = applicationTitle;
        this.applicationVersion = applicationVersion;
    }

    public Controller getController() {
        return controller;
    }

    public void init() {
        setTitle(applicationTitle + " v." + applicationVersion);
        setBounds(0, 0, 1024, 600);
        setMinimumSize(new Dimension(900, 300));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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

        java.util.List<Note> notes = controller.getNotes();
        Collections.sort(notes, comparators[selectedComparator]);
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
        periodTypeComboBox.addItem(new SimpleData(0, "Все"));
        periodTypeComboBox.addItem(new SimpleData(1, "Год"));
        periodTypeComboBox.addItem(new SimpleData(2, "Месяц"));
        periodTypeComboBox.addItem(new SimpleData(3, "День"));
        periodTypeComboBox.setSelectedIndex(2);
        menuBar.add(periodTypeComboBox);

        JComboBox<SimpleData> periodComboBox = new JComboBox<>();
        periodTypeComboBox.addItemListener(new PeriodTypeItemListener(periodComboBox));
        menuBar.add(periodComboBox);

        menuBar.add(new JLabel(" | "));

        menuBar.add(new JLabel("Сортировка: "));
        JComboBox<SimpleData> sortTypeComboBox = new JComboBox<>();
        sortTypeComboBox.addItem(new SimpleData(0, "Дата"));
        sortTypeComboBox.addItem(new SimpleData(1, "Продукт"));
       // sortTypeComboBox.addItem(new SimpleData(2, "Категория"));
       // sortTypeComboBox.addItem(new SimpleData(3, "Цена"));
        //sortTypeComboBox.addItem(new SimpleData(4, "Магазин"));
       // sortTypeComboBox.addItem(new SimpleData(5, "Скидка"));
        sortTypeComboBox.addItemListener(new SortTypeItemListener());
        menuBar.add(sortTypeComboBox);

        JComboBox<SimpleData> sortOrderComboBox = new JComboBox<>();
        sortOrderComboBox.addItem(new SimpleData(1, "Прямая (А->Я)"));
        sortOrderComboBox.addItem(new SimpleData(-1, "Обратная (Я->А)"));
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

        private JComboBox<SimpleData> periodComboBox;

        public PeriodTypeItemListener(JComboBox<SimpleData> periodComboBox) {
            this.periodComboBox = periodComboBox;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (!(e.getSource() instanceof JComboBox)) {
                return;
            }

            JComboBox<SimpleData> periodTypeComboBox = (JComboBox) e.getSource();
            switch (((SimpleData) periodTypeComboBox.getSelectedItem()).getId()) {
                case 0: {
                    periodComboBox.setEnabled(false);
                    break;
                }
                case 1: {

                }
                case 2: {

                }
                case 3: {
                    periodComboBox.setEnabled(true);
                }
            }
        }
    }

    private class SortTypeItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (!(e.getSource() instanceof JComboBox)) {
                return;
            }

            JComboBox<SimpleData> sortTypeComboBox = (JComboBox) e.getSource();
            selectedComparator = ((SimpleData) sortTypeComboBox.getSelectedItem()).getId();
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
            for (MyComparator c: comparators){
                c.setOrder(((SimpleData)sortTypeComboBox.getSelectedItem()).getId());
            }
            update();
        }
    }

    private class SearchActionListener implements ActionListener {

        private JTextField searchTextField;

        public SearchActionListener(JTextField searchTextField) {
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
}
