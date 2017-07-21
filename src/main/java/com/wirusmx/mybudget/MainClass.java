package com.wirusmx.mybudget;

import com.wirusmx.mybudget.model.Model;
import com.wirusmx.mybudget.view.View;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Main class of application. Contains <code>public static void main(String[] args)</code>
 * method and nothing else.
 */
public class MainClass {
    public static void main(String[] args) {
        Controller controller = new Controller();

        ApplicationContext context = new FileSystemXmlApplicationContext("conf/spring_config.xml");
        Model model = (Model) context.getBean("sql_model");

        Properties properties = new Properties();
        String applicationTitle = "MyBudget";
        String applicationVersion = "";

        try {
            properties.load(new FileInputStream("conf/config.properties"));
            applicationTitle = properties.getProperty("application.title", applicationTitle);
            applicationVersion = properties.getProperty("application.version", applicationVersion);
        } catch (IOException e) {
            e.printStackTrace();
        }

        View view = new View(controller, applicationTitle, applicationVersion);

        controller.setModel(model);
        controller.setView(view);
        controller.startApplication();

    }
}
