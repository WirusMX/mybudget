package com.wirusmx.mybudget.view;

import com.wirusmx.mybudget.Controller;
import com.wirusmx.mybudget.model.Note;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame {
    private static final String FRAME_TITLE = "СЕМЕЙНЫЙ БЮДЖЕТ v1.0";

    private Controller controller;
    private View thisView = this;
    private JList<Note> notesList;
    private DefaultListModel<Note> noteDefaultListModel;
    private JLabel statLabel = new JLabel();

    public View(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }

    public void init() {
        setTitle(FRAME_TITLE);
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
        int summ = 0;
        int[] necSum = new int[2];

        for (Note n : notes) {
            noteDefaultListModel.addElement(n);
            summ += n.getPrice();
            necSum[n.getNecessity().getId()] += n.getPrice();
        }

        statLabel.setText("Итого: " + summ + "руб., из них " + necSum[0] + "руб. - высокой необходимости, " +
                necSum[1] + "руб. - низкой необходимости");
    }

    private void addMainMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Файл");
        JMenuItem newNoteMenuItem = new JMenuItem("Новый");
        fileMenu.add(newNoteMenuItem);
        menuBar.add(fileMenu);
        getContentPane().add(menuBar, BorderLayout.NORTH);

    }

    private void addControlPanel() {
        JMenuBar menuBar = new JMenuBar();

        JButton newNoteButton = new JButton("+");
        newNoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NoteEditDialog noteEditDialog = new NoteEditDialog(thisView);
                Note note = noteEditDialog.getNote();
                if (note != null) {
                    controller.insertNote(note);
                    update();
                }
            }
        });
        menuBar.add(newNoteButton);

        menuBar.add(new JLabel(" "));

        menuBar.add(new JButton("/"));

        menuBar.add(new JLabel(" | "));

        menuBar.add(new JLabel("За период:"));
        JComboBox<String> periodTypeComboBox = new JComboBox<>();
        periodTypeComboBox.addItem("Все");
        periodTypeComboBox.addItem("Год");
        periodTypeComboBox.addItem("Месяц");
        periodTypeComboBox.addItem("День");
        menuBar.add(periodTypeComboBox);

        menuBar.add(new JComboBox<String>());

        menuBar.add(new JLabel(" | "));

        menuBar.add(new JLabel("Сортировка: "));
        JComboBox<String> sortTypeComboBox = new JComboBox<>();
        sortTypeComboBox.addItem("Дата");
        sortTypeComboBox.addItem("Продукт");
        sortTypeComboBox.addItem("Категория");
        sortTypeComboBox.addItem("Цена");
        sortTypeComboBox.addItem("Магазин");
        sortTypeComboBox.addItem("Скидка");
        menuBar.add(sortTypeComboBox);
        JComboBox<String> sortOrderComboBox = new JComboBox<>();
        sortOrderComboBox.addItem("Прямая (А->Я)");
        sortOrderComboBox.addItem("Обратная (Я->А)");
        menuBar.add(sortOrderComboBox);


        menuBar.add(new JLabel(" | "));

        menuBar.add(new JButton("Обновить"));

        menuBar.add(new JLabel(" | "));


        JTextField searchTextField = new JTextField("");
        menuBar.add(searchTextField);
        menuBar.add(new JButton("Поиск"));
        getContentPane().add(menuBar, BorderLayout.SOUTH);
    }
}
