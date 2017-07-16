package com.wirusmx.mybudget;

import com.wirusmx.mybudget.view.View;

public class Controller {
    private Model model;
    private View view;

    public void setModel(Model model) {
        this.model = model;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void startApplication() {
        view.init();
    }
}
