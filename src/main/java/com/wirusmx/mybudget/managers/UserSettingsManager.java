package com.wirusmx.mybudget.managers;

import com.wirusmx.mybudget.DefaultExceptionHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Piunov M (aka WirusMX)
 */
public class UserSettingsManager {
    private static final String USER_SETTINGS_FILE_NAME = "conf/user_settings.properties";

    private Properties userSettings;

    private static UserSettingsManager instance;

    private UserSettingsManager() {
        userSettings = new Properties();
        if (new File(USER_SETTINGS_FILE_NAME).exists()) {
            try {
                userSettings.load(new FileInputStream(USER_SETTINGS_FILE_NAME));
            } catch (IOException e) {
                DefaultExceptionHandler.handleException(e);
            }
        }
    }

    public static UserSettingsManager getInstance() {
        if (instance == null) {
            instance = new UserSettingsManager();
        }

        return instance;
    }

    public String getValue(String key, String defaultValue) {
        return userSettings.getProperty(key, defaultValue);
    }

    public int getIntegerValue(String key, int defaultValue) {
        int result = defaultValue;
        try {
            result = Integer.parseInt(userSettings.getProperty(key));
        } catch (NumberFormatException ex) {
            DefaultExceptionHandler.handleException(ex);
        }

        return result;
    }

    public boolean getBooleanValue(String key, boolean defaultValue) {
        boolean result = defaultValue;

        try {
            result = "1".equals(userSettings.getProperty(key));
        } catch (NumberFormatException ex) {
            DefaultExceptionHandler.handleException(ex);
        }

        return result;
    }

    public void setValue(String key, String value) {
        userSettings.setProperty(key, value);
        saveUserSettings();
    }

    private void saveUserSettings() {
        try {
            userSettings.store(new FileOutputStream(USER_SETTINGS_FILE_NAME), "");
        } catch (IOException e) {
            DefaultExceptionHandler.handleException(e);
        }
    }
}
