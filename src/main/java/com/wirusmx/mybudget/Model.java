package com.wirusmx.mybudget;

public class Model {
    private Controller controller;

    public Model(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }
}
