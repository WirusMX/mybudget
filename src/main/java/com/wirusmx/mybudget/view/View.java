package com.wirusmx.mybudget.view;

import com.wirusmx.mybudget.Controller;

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
        setBounds(0, 0, 800, 600);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Файл");

        JMenuItem newNoteMenu = new JMenuItem("Новая заметка");
        newNoteMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NoteEditDialog(thisView);
            }
        });

        JMenuItem exitMenu = new JMenuItem("Выход");
        exitMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thisView.dispose();
            }
        });

        fileMenu.add(newNoteMenu);
        fileMenu.addSeparator();
        fileMenu.add(exitMenu);

        menuBar.add(fileMenu);
        getContentPane().add(menuBar, BorderLayout.NORTH);

        setVisible(true);
    }
}
