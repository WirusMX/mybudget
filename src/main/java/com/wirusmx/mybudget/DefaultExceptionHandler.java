package com.wirusmx.mybudget;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class for handling exceptions by default
 *
 * @author Piunov M (aka WirusMX)
 */
public class DefaultExceptionHandler {

    public static void handleException(Exception ex) {

        System.out.println(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date())
                + " Exception in module '" + ex.getStackTrace()[0]
                + "': " + ex.getMessage());
    }
}
