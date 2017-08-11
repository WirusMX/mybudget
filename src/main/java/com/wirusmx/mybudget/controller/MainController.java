package com.wirusmx.mybudget.controller;

import com.wirusmx.mybudget.DefaultExceptionHandler;
import com.wirusmx.mybudget.dialogs.noteeditdialog.NoteEditDialog;
import com.wirusmx.mybudget.dialogs.statisticsdialog.StatisticsDialog;
import com.wirusmx.mybudget.model.Model;
import com.wirusmx.mybudget.model.Note;
import com.wirusmx.mybudget.model.SimpleData;
import com.wirusmx.mybudget.view.MainView;
import com.wirusmx.mybudget.view.dataviews.DataView;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;
import java.util.Set;

/**
 * @author Piunov M (aka WirusMX)
 */
public class MainController {
    private MainView mainView;
    private Model model;

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void startApplication() {
        try {
            model.init();
            mainView.init();
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(this, ex);
        }
    }

    public List<Note> getNotes() {
        return model.getNotes();
    }

    private AddNewNoteButtonActionListener addNewNoteButtonActionListener = null;
    private ExitButtonButtonActionListener exitButtonButtonActionListener = null;
    private StatisticsButtonActionListener statisticsButtonActionListener = null;
    private SettingsButtonActionListener settingsButtonActionListener = null;
    private AboutButtonActionListener aboutButtonActionListener = null;
    private UsingRulesButtonActionListener usingRulesButtonActionListener = null;
    private PeriodTypeComboBoxItemListener periodTypeComboBoxItemListener = null;
    private PeriodComboBoxItemListener periodComboBoxItemListener = null;
    private SortTypeComboBoxItemListener sortTypeComboBoxItemListener = null;
    private SortOrderComboBoxItemListener sortOrderComboBoxItemListener = null;
    private SearchButtonActionListener searchButtonActionListener = null;
    private ResetButtonActionListener resetButtonActionListener = null;

    public AddNewNoteButtonActionListener getAddNewNoteButtonActionListener() {
        if (addNewNoteButtonActionListener == null) {
            addNewNoteButtonActionListener = new AddNewNoteButtonActionListener();
        }

        return addNewNoteButtonActionListener;
    }

    public EditNoteActionListener getEditNoteActionListener(DataView dataView) {
        return new EditNoteActionListener(dataView);
    }

    public RemoveNoteButtonActionListener getRemoveNoteButtonActionListener(DataView dataView) {
        return new RemoveNoteButtonActionListener(dataView);
    }

    public ExitButtonButtonActionListener getExitButtonButtonActionListener() {
        if (exitButtonButtonActionListener == null) {
            exitButtonButtonActionListener = new ExitButtonButtonActionListener();
        }

        return exitButtonButtonActionListener;
    }

    public DataViewButtonActionListener getDataViewButtonActionListener(Class dataViewClass, int id) {
        return new DataViewButtonActionListener(dataViewClass, id);
    }

    public StatisticsButtonActionListener getStatisticsButtonActionListener() {
        if (statisticsButtonActionListener == null) {
            statisticsButtonActionListener = new StatisticsButtonActionListener();
        }
        return statisticsButtonActionListener;
    }

    public SettingsButtonActionListener getSettingsButtonActionListener() {
        if (settingsButtonActionListener == null) {
            settingsButtonActionListener = new SettingsButtonActionListener();
        }
        return settingsButtonActionListener;
    }

    public AboutButtonActionListener getAboutButtonActionListener() {
        if (aboutButtonActionListener == null) {
            aboutButtonActionListener = new AboutButtonActionListener();
        }
        return aboutButtonActionListener;
    }

    public UsingRulesButtonActionListener getUsingRulesButtonActionListener() {
        if (usingRulesButtonActionListener == null) {
            usingRulesButtonActionListener = new UsingRulesButtonActionListener();
        }
        return usingRulesButtonActionListener;
    }

    public PeriodTypeComboBoxItemListener getPeriodTypeComboBoxItemListener(JComboBox<String> periodComboBox) {
        if (periodTypeComboBoxItemListener == null) {
            periodTypeComboBoxItemListener = new PeriodTypeComboBoxItemListener(periodComboBox);
        }
        return periodTypeComboBoxItemListener;
    }

    public PeriodComboBoxItemListener getPeriodComboBoxItemListener() {
        if (periodComboBoxItemListener == null) {
            periodComboBoxItemListener = new PeriodComboBoxItemListener();
        }
        return periodComboBoxItemListener;
    }

    public SortTypeComboBoxItemListener getSortTypeComboBoxItemListener() {
        if (sortTypeComboBoxItemListener == null) {
            sortTypeComboBoxItemListener = new SortTypeComboBoxItemListener();
        }
        return sortTypeComboBoxItemListener;
    }

    public SortOrderComboBoxItemListener getSortOrderComboBoxItemListener() {
        if (sortOrderComboBoxItemListener == null) {
            sortOrderComboBoxItemListener = new SortOrderComboBoxItemListener();
        }
        return sortOrderComboBoxItemListener;
    }

    public SearchButtonActionListener getSearchButtonActionListener(JTextField textField) {
        if (searchButtonActionListener == null) {
            searchButtonActionListener = new SearchButtonActionListener(textField);
        }
        return searchButtonActionListener;
    }

    public ResetButtonActionListener getResetButtonActionListener(JTextField textField) {
        if (resetButtonActionListener == null) {
            resetButtonActionListener = new ResetButtonActionListener(textField);
        }
        return resetButtonActionListener;
    }



    public int getSelectedPeriodType() {
        return model.getSelectedPeriodType();
    }

    public String getSelectedPeriod() {
        return model.getSelectedPeriod();
    }

    public int getSelectedSortType() {
        return model.getSelectedSortType();
    }

    public int getSelectedSortOrder() {
        return model.getSelectedSortOrder();
    }

    public Set<String> getPeriods() {
        return model.getPeriods();
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(mainView, "ОШИБКА: " + message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    public int getDataViewID() {
        return model.getDataViewID();
    }

    private class AddNewNoteButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            NoteEditDialog noteEditDialog = new NoteEditDialog();

            Note note = noteEditDialog.getNote();
            if (noteEditDialog.getDialogResult() == JOptionPane.YES_OPTION && note != null) {
                model.insertNote(note);
                mainView.update();
            }

        }
    }

    private class EditNoteActionListener implements ActionListener, MouseListener {
        private DataView dataView;

        EditNoteActionListener(DataView dataView) {
            this.dataView = dataView;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            doEdit();
        }

        private void doEdit() {

            Note note = dataView.getSelectedValue();
            if (note == null) {
                return;
            }

            NoteEditDialog noteEditDialog = new NoteEditDialog(note);

            note = noteEditDialog.getNote();
            if (noteEditDialog.getDialogResult() == JOptionPane.YES_OPTION && note != null) {
                model.updateNote(note);
                mainView.update();
            }

        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                doEdit();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    private class RemoveNoteButtonActionListener implements ActionListener {
        private DataView dataView;

        RemoveNoteButtonActionListener(DataView dataView) {
            this.dataView = dataView;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Note note = dataView.getSelectedValue();
            if (note == null) {
                return;
            }

            model.removeNote(note);
            mainView.update();
        }
    }

    private class ExitButtonButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainView.dispose();
        }
    }

    private class DataViewButtonActionListener implements ActionListener {
        private Class dataViewClass;
        private int id;

        DataViewButtonActionListener(Class dataViewClass, int id) {
            this.dataViewClass = dataViewClass;
            this.id = id;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void actionPerformed(ActionEvent e) {
            model.setDataViewID(id);
            mainView.setDataView(dataViewClass);
            mainView.update();
        }
    }

    private class SettingsButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private class StatisticsButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new StatisticsDialog();
        }
    }

    private class AboutButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainView.showAboutDialog();
        }
    }

    private class UsingRulesButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainView.showUsingRulesDialog();
        }
    }

    private class PeriodTypeComboBoxItemListener implements ItemListener {

        private JComboBox<String> periodComboBox;

        PeriodTypeComboBoxItemListener(JComboBox<String> periodComboBox) {
            this.periodComboBox = periodComboBox;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            JComboBox periodTypeComboBox;
            if (e.getSource() instanceof JComboBox) {
                periodTypeComboBox = (JComboBox) e.getSource();
            } else {
                return;
            }

            int periodType = ((SimpleData) periodTypeComboBox.getSelectedItem()).getId();
            model.setSelectedPeriodType(periodType);

            mainView.setPeriodComboBoxValues(periodComboBox, periodType);
            if (periodType == Model.PeriodType.ALL) {
                mainView.update();
            }
        }
    }

    private class PeriodComboBoxItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            JComboBox periodComboBox;
            if (e.getSource() instanceof JComboBox) {
                periodComboBox = (JComboBox) e.getSource();
            } else {
                return;
            }

            String selectedPeriod = ((String) periodComboBox.getSelectedItem());
            if (selectedPeriod == null) {
                model.setSelectedPeriod("");
                return;
            }

            model.setSelectedPeriod(selectedPeriod);

            mainView.update();
        }
    }

    private class SortTypeComboBoxItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            JComboBox sortTypeComboBox;

            if (e.getSource() instanceof JComboBox) {
                sortTypeComboBox = (JComboBox) e.getSource();
            } else {
                return;
            }


            int selectedSortType = ((SimpleData) sortTypeComboBox.getSelectedItem()).getId();

            model.setSelectedSortType(selectedSortType);
            mainView.update();
        }
    }

    private class SortOrderComboBoxItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            JComboBox sortTypeComboBox;

            if (e.getSource() instanceof JComboBox) {
                sortTypeComboBox = (JComboBox) e.getSource();
            } else {
                return;
            }

            int sortOrder = ((SimpleData) sortTypeComboBox.getSelectedItem()).getId();

            model.setSelectedSortOrder(sortOrder);
            mainView.update();
        }
    }

    private class SearchButtonActionListener implements ActionListener {

        private JTextField searchTextField;

        SearchButtonActionListener(JTextField searchTextField) {
            this.searchTextField = searchTextField;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            model.setSearchQuery(searchTextField.getText());
            mainView.update();
        }
    }

    private class ResetButtonActionListener implements ActionListener {
        JTextField textField;

        ResetButtonActionListener(JTextField textField) {
            this.textField = textField;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            textField.setText("");
            model.setSearchQuery("");
            mainView.update();
        }
    }


}
