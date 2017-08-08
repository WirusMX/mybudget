package com.wirusmx.mybudget.model.exceptions;

/**
 * @author Piunov M (aka WirusMX)
 */
public class UndefinedDatabaseVersionException extends RuntimeException {

    public UndefinedDatabaseVersionException(String s) {
        super(s);
    }
}
