package com.wirusmx.mybudget.dialogs.noteeditdialog;

import com.wirusmx.mybudget.DefaultExceptionHandler;
import com.wirusmx.mybudget.managers.DatabaseManager;
import com.wirusmx.mybudget.model.CommonModel;
import com.wirusmx.mybudget.model.Note;
import com.wirusmx.mybudget.model.SimpleData;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Piunov M (aka WirusMX)
 */
class NoteEditDialogModel extends CommonModel {
    private Note currentNote;
    private int dialogResult = JOptionPane.NO_OPTION;

    NoteEditDialogModel(Note currentNote, DatabaseManager databaseManager) {
        super(databaseManager);
        this.currentNote = currentNote;
    }

    /**
     * Returns dialog result.
     *
     * @return <code>JOptionPane.YES_OPTION</code> value if button SAVE was clicked,
     * otherwise <code>JOptionPane.NO_OPTION</code>.
     */
    int getDialogResult() {
        return dialogResult;
    }

    /**
     * @return resulted <code>Note</code> or <code>null</code> if
     * user choose the Close action.
     */
    public Note getNote() {
        return currentNote;
    }

    void setPositiveDialogResult() {
        dialogResult = JOptionPane.YES_OPTION;
    }

    void updateNote(String itemTitle, SimpleData itemType, Float itemPrice, Float itemsCount,
                    SimpleData countType, SimpleData shop, SimpleData necessity, SimpleData quality,
                    boolean bySale, String day, String month, String year) {
        currentNote.update(itemTitle, itemType, itemPrice, itemsCount, countType, shop, necessity, quality,
                bySale, day, month, year);

    }

    /**
     * Insert new ComboBox value to database.
     *
     * @param value - value for inserting;
     * @param table - table for inserting.
     * @return id of inserted value in database table,
     * or -1 if any problems occurred during insertion operation.
     */
    int insertNewComboBoxValue(String value, String table) {
        int result = -1;
        try {
            result = databaseManager.insertNewComboBoxValue(value, table);
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(ex);
        }

        return result;
    }


    Set<String> getItemsSet() {
        List<String> result = new ArrayList<>();
        try {
            result = databaseManager.getItemsTitles();
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(ex);
        }

        return new TreeSet<>(result);
    }

    /**
     * Converts string value to true numeric format string.
     *
     * @param type                type of resulted value. Must
     *                            be {@link Integer} or {@link Float};
     * @param value               string value for converting;
     * @param decimalNumbersCount count of numbers after decimal point.
     * @return converted value or empty string if type not allowed.
     * @see #stringToIntegerFormat(String)
     * @see #stringToFloatFormat(String, int)
     */
    String stringToNumericFormat(Class type, String value, int decimalNumbersCount) {
        if (type.equals(Float.class)) {
            return stringToFloatFormat(value, decimalNumbersCount);
        }

        if (type.equals(Integer.class)) {
            return stringToIntegerFormat(value);
        }

        return "";
    }

    private String stringToFloatFormat(String text, int cutNum) {
        if (!text.matches("\\d+")) {
            text = text.replaceAll(",", ".");
            text = text.replaceAll("[^0-9.]", "");
            while (text.split("\\.").length > 2) {
                text = text.replaceFirst("\\.", "");
            }

            int pos = text.indexOf('.');
            if (pos >= 0 && text.length() - pos > cutNum + 1) {
                text = text.substring(0, pos + cutNum + 1);
            }

            if (pos == text.length() - 1 && text.length() - 1 > 0) {
                text = text.substring(0, pos);
            }

            if (pos == 0 && text.length() > 1) {
                text = "0" + text;
            }

            if (pos == 0 && text.length() - 1 == 0) {
                text = "";
            }
        }

        return text;
    }

    private String stringToIntegerFormat(String text) {
        if (!text.matches("\\d+")) {
            text = text.replaceAll("\\D", "");
        }

        return text;
    }

}
