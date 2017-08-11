package com.wirusmx.mybudget.dialogs.statisticsdialog;

import com.wirusmx.mybudget.model.Model;
import com.wirusmx.mybudget.model.Note;
import com.wirusmx.mybudget.model.SimpleData;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Set;

/**
 * @author Piunov M (aka WirusMX)
 */
class StatisticsDialogController {
    private StatisticsDialogModel dialogModel;
    private StatisticsDialogView dialogView;

    void setDialogModel(StatisticsDialogModel dialogModel) {
        this.dialogModel = dialogModel;
    }

    void setDialogView(StatisticsDialogView dialogView) {
        this.dialogView = dialogView;
    }

    void showDialog() {
        dialogView.init();
    }


    Set<String> getPeriods(int periodType) {
        return dialogModel.getPeriods(periodType);
    }

    float[][] getStatistics(int periodType, String period, int itemType) {
        float[][] result;
        if (periodType == Model.PeriodType.YEAR) {
            result = new float[3][12];

            List<Note> notes = dialogModel.getNotes(periodType, period);

            for (Note n : notes) {
                if (itemType == -1 || n.getType().getId() == itemType) {
                    result[2][Integer.parseInt(n.getMonth()) - 1] += n.getTotal();
                    result[n.getNecessity().getId()][Integer.parseInt(n.getMonth()) - 1] += n.getTotal();
                }
            }
        } else {
            result = new float[3][0];
        }

        return result;
    }

    Set<SimpleData> getComboBoxValues(String tableName) {
        return dialogModel.getComboBoxValues(tableName);
    }

    StatisticsDialogPeriodComboBoxItemListener getStatisticsDialogPeriodComboBoxItemListener() {
        return new StatisticsDialogPeriodComboBoxItemListener();
    }

    StatisticsDialogItemTypeComboBoxItemListener getStatisticsDialogItemTypeComboBoxItemListener() {
        return new StatisticsDialogItemTypeComboBoxItemListener();
    }

    CloseStatisticsDialogButtonActionListener getCloseStatisticsDialogButtonActionListener() {
        return new CloseStatisticsDialogButtonActionListener();
    }

    private class StatisticsDialogPeriodComboBoxItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            JComboBox periodComboBox;
            if (e.getSource() instanceof JComboBox) {
                periodComboBox = (JComboBox) e.getSource();
            } else {
                return;
            }


            String period = (String) periodComboBox.getSelectedItem();

            dialogView.setSelectedPeriod(period);
            dialogView.update();
        }
    }

    private class StatisticsDialogItemTypeComboBoxItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            JComboBox periodComboBox;
            if (e.getSource() instanceof JComboBox) {
                periodComboBox = (JComboBox) e.getSource();
            } else {
                return;
            }


            int typeID = ((SimpleData) periodComboBox.getSelectedItem()).getId();

            dialogView.setSelectedItemType(typeID);
            dialogView.update();
        }
    }

    private class CloseStatisticsDialogButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dialogView.dispose();
        }
    }
}
