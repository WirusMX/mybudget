package com.wirusmx.mybudget.view;

import com.wirusmx.mybudget.Controller;

import javax.swing.*;

public class View extends JFrame {
    private static final String FRAME_TITLE = "СЕМЕЙНЫЙ БЮДЖЕТ v1.0";

    private Controller controller;


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

        setVisible(true);
        new AddNewItemDialod();
    }
}
