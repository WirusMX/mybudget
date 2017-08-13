package com.wirusmx.mybudget.managers;

import com.wirusmx.mybudget.DefaultExceptionHandler;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author Piunov M (aka WirusMX)
 */
public class ResourcesManager {
    private static final String TEXT_FILES_PREFIX = "/text/";
    private static final String TEXT_FILES_EXT = ".txt/";

    private static final String IMAGES_FILES_PREFIX = "/images/";
    private static final String IMAGES_FILES_EXT = ".png";

    private static final String PROPERTIES_FILES_PREFIX = "/properties/";
    private static final String PROPERTIES_FILES_EXT = ".properties";

    private static ResourcesManager instance;

    private ResourcesManager() {
    }

    public static ResourcesManager getInstance() {
        if (instance == null) {
            instance = new ResourcesManager();
        }

        return instance;
    }

    public String getProperty(String property) {
        if (property == null) {
            return "";
        }

        int pos = property.indexOf('.');
        if (pos <= 0 || pos == property.length() - 1) {
            return "";
        }

        String propertiesFileName = property.substring(0, pos);
        property = property.substring(pos + 1);

        Properties properties = new Properties();
        try {
            properties.load(
                    getClass().getResourceAsStream(PROPERTIES_FILES_PREFIX + propertiesFileName + PROPERTIES_FILES_EXT));
        } catch (IOException e) {
            return "";
        }

        return properties.getProperty(property, "");
    }

    /**
     * Reads UTF-8 text from the resource file.
     *
     * @param fileName resource file.
     * @return text from the resource file.
     */
    public String getText(String fileName) {
        String result = "";

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        getClass().getResourceAsStream(TEXT_FILES_PREFIX + fileName + TEXT_FILES_EXT), "UTF-8"))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                result += line + "\n";
            }
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(ex);
        }
        return result;
    }

    /**
     * Loads resource image in PNG format.
     *
     * @param fileName image file name.
     * @return image or <code>null</code> if any problems happens.
     */
    public ImageIcon getImage(String fileName) {
        ImageIcon image = null;

        try {
            image = new ImageIcon(getClass().getResource(IMAGES_FILES_PREFIX + fileName + IMAGES_FILES_EXT));
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(ex);
        }
        return image;
    }
}
