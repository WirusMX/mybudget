package com.wirusmx.mybudget;

import com.wirusmx.mybudget.controller.MainController;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Class for handling exceptions by default.
 *
 * @author Piunov M (aka WirusMX)
 */
public class DefaultExceptionHandler {

    private static Logger logger = Logger.getLogger(DefaultExceptionHandler.class);

    private static boolean isLoggerReady = false;

    static {
        Properties loggerProperties = new Properties();
        try {
            loggerProperties.load(new FileInputStream("conf/log4j.properties"));
            PropertyConfigurator.configure(loggerProperties);
            isLoggerReady = true;
        } catch (IOException ignored) {

        }
    }

    public static void handleException(Throwable ex){
        handleException(null, ex, "");
    }

    public static void handleException(MainController controller, Throwable ex){
        handleException(controller, ex, "");
    }

    public static void handleException(MainController controller, Throwable ex, String userMessage){
        if (isLoggerReady){
            logger.error(ex.getMessage(), ex);
        }

        if (controller != null) {
            if (userMessage.isEmpty()) {
                controller.showErrorMessage(ex.getMessage());
            } else {
                controller.showErrorMessage(userMessage);
            }
        }
    }
}
