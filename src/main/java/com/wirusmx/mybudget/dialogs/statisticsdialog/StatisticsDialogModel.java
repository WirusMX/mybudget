package com.wirusmx.mybudget.dialogs.statisticsdialog;

import com.wirusmx.mybudget.DefaultExceptionHandler;
import com.wirusmx.mybudget.managers.DatabaseManager;
import com.wirusmx.mybudget.model.Model;
import com.wirusmx.mybudget.model.Note;
import com.wirusmx.mybudget.model.SimpleData;

import java.util.*;

/**
 * @author Piunov M (aka WirusMX)
 */
class StatisticsDialogModel {
    private DatabaseManager databaseManager;

    StatisticsDialogModel(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Extracts Notes from database which matches the specified period.
     *
     * @param selectedPeriodType type of period;
     * @param selectedPeriod     sample period.
     * @return List of Notes which matches the specified period, without
     * sorting.
     */
    List<Note> getNotes(int selectedPeriodType, String selectedPeriod) {
        return databaseManager.getNotes(selectedPeriodType, selectedPeriod);
    }

    /**
     * Extracts pares of (id, title) from <code>table</code>.
     *
     * @param tableName name of table which contains values for ComboBox.
     * @return ComboBox values set with the default order.
     * @see ComboBoxValuesComparator
     */
    Set<SimpleData> getComboBoxValues(String tableName) {
        Set<SimpleData> values = new TreeSet<>(new ComboBoxValuesComparator());

        try {
            List<SimpleData> temp = databaseManager.getComboBoxValues(tableName);
            values.addAll(temp);
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(ex);
        }

        return values;
    }


    /**
     * Extracts periods of specified type from data base.
     *
     * @param selectedPeriodType type of period.
     * @return set of periods.
     */
    Set<String> getPeriods(int selectedPeriodType) {
        switch (selectedPeriodType) {
            case Model.PeriodType.YEAR:
                return getYears();
            case Model.PeriodType.MONTH:
                return getMonths();
            case Model.PeriodType.DAY:
                return getDays();
        }
        return new TreeSet<>();
    }

    private Set<String> getYears() {
        List<String> temp = new ArrayList<>();

        try {
            temp = databaseManager.getYears();
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(ex);
        }


        return new TreeSet<>(temp).descendingSet();
    }

    private Set<String> getMonths() {
        List<String> temp = new ArrayList<>();

        try {
            temp = databaseManager.getMonths();
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(ex);
        }
        return new TreeSet<>(temp).descendingSet();
    }

    private Set<String> getDays() {
        List<String> temp = new ArrayList<>();

        try {
            temp = databaseManager.getDays();
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(ex);
        }
        return new TreeSet<>(temp).descendingSet();
    }

    /**
     * Compare two {@link SimpleData} values. Value with <code>id = 0</code>
     * is less then others. If values <code>id != 0</code>, they compare by title.
     */
    private static class ComboBoxValuesComparator implements Comparator<SimpleData> {
        @Override
        public int compare(SimpleData o1, SimpleData o2) {
            if (o1.getId() == 0) {
                return -1;
            }

            if (o2.getId() == 0) {
                return 1;
            }

            return o1.toString().toLowerCase().compareTo(o2.toString().toLowerCase());
        }
    }
}
