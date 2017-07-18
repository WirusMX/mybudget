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

        DefaultListModel<Note> noteDefaultListModel = new DefaultListModel<>();
        JList<Note> notesList = new JList<>(noteDefaultListModel);
        java.util.List<Note> notes = controller.getNotes();
        for (Note n: notes){
            noteDefaultListModel.addElement(n);
        }

        add(notesList);

        setVisible(true);
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
                new NoteEditDialog(thisView);
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
