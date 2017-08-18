package com.wirusmx.mybudget.dialogs.statisticsdialog;

import com.wirusmx.mybudget.managers.DatabaseManager;
import com.wirusmx.mybudget.model.CommonModel;
import com.wirusmx.mybudget.model.Note;

import java.util.List;

/**
 * @author Piunov M (aka WirusMX)
 */
class StatisticsDialogModel extends CommonModel{
    public StatisticsDialogModel(DatabaseManager databaseManager) {
        super(databaseManager);
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
}
