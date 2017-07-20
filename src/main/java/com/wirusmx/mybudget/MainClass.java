package com.wirusmx.mybudget;

import com.wirusmx.mybudget.model.Model;
import com.wirusmx.mybudget.model.SQLDatabase;
import com.wirusmx.mybudget.view.View;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Main class of application. Contains <code>public static void main(String[] args)</code>
 * method and nothing else.
 */
public class MainClass {
    public static void main(String[] args) {
        Controller controller = new Controller();

        ApplicationContext context = new FileSystemXmlApplicationContext("spring_config.xml");
        SQLDatabase database = (SQLDatabase) context.getBean("sqlDatabase");

        Model model = new Model(controller, database);
        View view = new View(controller);

        controller.setModel(model);
        controller.setView(view);
        controller.startApplication();

    }
}
