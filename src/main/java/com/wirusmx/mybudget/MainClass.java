package com.wirusmx.mybudget;

import com.wirusmx.mybudget.controller.Controller;
import com.wirusmx.mybudget.model.Model;
import com.wirusmx.mybudget.view.View;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Main class of application. Contains <code>public static void main(String[] args)</code>
 * method and nothing else.
 */
public class MainClass {
    public static void main(String[] args) {
        try {
            ApplicationContext context = new FileSystemXmlApplicationContext("conf/spring_config.xml");
            Model model = (Model) context.getBean("sql_model");
            View view = (View) context.getBean("gui_view");
            Controller controller = (Controller) context.getBean("controller");

            controller.setModel(model);
            controller.setView(view);
            controller.startApplication();
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(ex);
        }
    }
}
