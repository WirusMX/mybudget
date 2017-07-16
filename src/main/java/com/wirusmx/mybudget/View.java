package com.wirusmx.mybudget;

import javax.swing.*;

public class View extends JFrame {
    private Controller controller;

    public View(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }

    public void init() {

    }
}
