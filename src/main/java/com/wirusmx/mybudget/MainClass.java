package com.wirusmx.mybudget;

import com.wirusmx.mybudget.controller.MainController;
import com.wirusmx.mybudget.managers.DatabaseManager;
import com.wirusmx.mybudget.managers.ResourcesManager;
import com.wirusmx.mybudget.managers.UserSettingsManager;
import com.wirusmx.mybudget.model.Model;
import com.wirusmx.mybudget.view.MainView;

/**
 * Main class of application. Contains <code>public static void main(String[] args)</code>
 * method and nothing else.
 *
 * @author Piunov M (aka WirusMX)
 */
public class MainClass {
    public static void main(String[] args) {
        ResourcesManager resourcesManager = ResourcesManager.getInstance();
        DatabaseManager databaseManager = DatabaseManager.getInstance(resourcesManager.getProperty("application.version"));
        UserSettingsManager userSettingsManager = UserSettingsManager.getInstance();

        MainController controller = new MainController(userSettingsManager);

        Model model = new Model(controller, databaseManager, userSettingsManager);

        MainView view = new MainView(controller, resourcesManager);

        controller.setModel(model);
        controller.setMainView(view);
        controller.startApplication();
    }
}
