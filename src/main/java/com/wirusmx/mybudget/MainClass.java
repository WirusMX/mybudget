package com.wirusmx.mybudget;

import com.wirusmx.mybudget.model.Model;
import com.wirusmx.mybudget.view.View;

/**
 * Main class of application. Contains <code>public static void main(String[] args)</code>
 * method and nothing else.
 */
public class MainClass {
    public static void main(String[] args) {
        Controller controller = new Controller();

        Model model = new Model(controller);
        View view = new View(controller);

        controller.setModel(model);
        controller.setView(view);
        controller.startApplication();

    }
}
