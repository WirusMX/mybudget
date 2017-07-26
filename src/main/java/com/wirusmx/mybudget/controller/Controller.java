package com.wirusmx.mybudget.controller;

import com.wirusmx.mybudget.model.Model;
import com.wirusmx.mybudget.model.Note;
import com.wirusmx.mybudget.model.SimpleData;
import com.wirusmx.mybudget.view.NoteEditDialog;
import com.wirusmx.mybudget.view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Set;

public class Controller {
    private Model model;
    private View view;

    public void setModel(Model model) {
        this.model = model;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void startApplication() {
        model.init();
        view.init();
    }

    public Set<SimpleData> getComboBoxValues(String tableName) {
        return model.getComboBoxValues(tableName);
    }

    public int insertNewValue(String value, String table) {
        return model.insertNewValue(value, table);
    }

    public List<Note> getNotes() {
        return model.getNotes();
    }

    private AddNewNoteButtonActionListener addNewNoteButtonActionListener = null;
    private EditNoteActionListener editNoteActionListener = null;
    private ExitButtonButtonActionListener exitButtonButtonActionListener = null;
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
            addNewNoteButtonActionListener = new AddNewNoteButtonActionListener(this);
        }

        return addNewNoteButtonActionListener;
    }

    public EditNoteActionListener getEditNoteActionListener(JList<Note> notesList) {
        if (notesList == null) {
            System.out.println("null");

        }
        if (editNoteActionListener == null) {
            editNoteActionListener = new EditNoteActionListener(this, notesList);
        }
        return editNoteActionListener;
    }

    public ExitButtonButtonActionListener getExitButtonButtonActionListener() {
        if (exitButtonButtonActionListener == null) {
            exitButtonButtonActionListener = new ExitButtonButtonActionListener();
        }

        return exitButtonButtonActionListener;
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

    public NumericTextFieldFocusListener getNumericTextFieldFocusListener(String defaultValue) {
        return new NumericTextFieldFocusListener(defaultValue);
    }

    public NoteEditDialogComboBoxItemListener getNoteEditDialogComboBoxItemListener(JDialog dialog, String table) {
        return new NoteEditDialogComboBoxItemListener(dialog, table);
    }

    public SaveNoteButtonActionListener getSaveNoteButtonActionListener(NoteEditDialog dialog, Note note, JTextField itemNameTextField,
                                                                        JComboBox itemTypeComboBox, JTextField itemPriceTextField,
                                                                        JComboBox shopComboBox, JComboBox necessityLevelComboBox,
                                                                        JComboBox qualityLevelComboBox, JCheckBox saleCheckBox,
                                                                        JTextField dayTextField, JTextField monthTextField,
                                                                        JTextField yearTextField) {
        return new SaveNoteButtonActionListener(dialog, note, itemNameTextField, itemTypeComboBox, itemPriceTextField,
                shopComboBox, necessityLevelComboBox, qualityLevelComboBox, saleCheckBox, dayTextField, monthTextField,
                yearTextField);

    }

    public CloseDialogButtonActionListener getCloseDialogButtonActionListener(NoteEditDialog dialog, Note note) {
        return new CloseDialogButtonActionListener(dialog, note);
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

    public ImageIcon getImage(String fileName) {
        return model.getImage(fileName);
    }

    private class AddNewNoteButtonActionListener implements ActionListener {
        private Controller controller;

        AddNewNoteButtonActionListener(Controller controller) {
            this.controller = controller;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            NoteEditDialog noteEditDialog = new NoteEditDialog(controller);
            Note note = noteEditDialog.getNote();
            if (noteEditDialog.getDialogResult() == JOptionPane.YES_OPTION && note != null) {
                model.insertNote(note);
                view.update();
            }
        }
    }

    private class EditNoteActionListener implements ActionListener, MouseListener {
        private Controller controller;
        private JList<Note> notesList;

        EditNoteActionListener(Controller controller, JList<Note> notesList) {
            this.controller = controller;
            this.notesList = notesList;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            doEdit();
        }

        private void doEdit() {
            Note note = notesList.getSelectedValue();
            if (note == null) {
                return;
            }
            NoteEditDialog noteEditDialog = new NoteEditDialog(controller, note);
            note = noteEditDialog.getNote();
            if (noteEditDialog.getDialogResult() == JOptionPane.YES_OPTION && note != null) {
                model.updateNote(note);
                view.update();
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
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

    private class ExitButtonButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.dispose();
        }
    }

    private class AboutButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(view,
                    model.readTextFromFile("about.txt"),
                    "О программе", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private class UsingRulesButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextArea messageTextArea = new JTextArea(model.readTextFromFile("using_rules.txt"));
            messageTextArea.setEditable(false);
            messageTextArea.setRows(150);

            JScrollPane scrollPane = new JScrollPane(messageTextArea);
            scrollPane.setSize(new Dimension(300, 500));

            JOptionPane.showMessageDialog(view,
                    scrollPane,
                    "Правила пользования ПО",
                    JOptionPane.PLAIN_MESSAGE);
        }
    }

    private class PeriodTypeComboBoxItemListener implements ItemListener {

        private JComboBox<String> periodComboBox;

        PeriodTypeComboBoxItemListener(JComboBox<String> periodComboBox) {
            this.periodComboBox = periodComboBox;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (!(e.getSource() instanceof JComboBox)) {
                return;
            }

            JComboBox<SimpleData> periodTypeComboBox = (JComboBox) e.getSource();
            int periodType = ((SimpleData) periodTypeComboBox.getSelectedItem()).getId();
            model.setSelectedPeriodType(periodType);

            view.setPeriodComboBoxValues(periodComboBox, periodType);
            if (periodType == Model.PeriodType.ALL) {
                view.update();
            }
        }
    }

    private class PeriodComboBoxItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (!(e.getSource() instanceof JComboBox)) {
                return;
            }

            JComboBox<SimpleData> periodComboBox = (JComboBox) e.getSource();
            String selectedPeriod = ((String) periodComboBox.getSelectedItem());
            if (selectedPeriod == null) {
                model.setSelectedPeriod("");
                return;
            }

            model.setSelectedPeriod(selectedPeriod);

            view.update();
        }
    }

    private class SortTypeComboBoxItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (!(e.getSource() instanceof JComboBox)) {
                return;
            }

            JComboBox<SimpleData> sortTypeComboBox = (JComboBox) e.getSource();
            int selectedSortType = ((SimpleData) sortTypeComboBox.getSelectedItem()).getId();

            model.setSelectedSortType(selectedSortType);
            view.update();
        }
    }

    private class SortOrderComboBoxItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (!(e.getSource() instanceof JComboBox)) {
                return;
            }

            JComboBox<SimpleData> sortTypeComboBox = (JComboBox) e.getSource();
            int sortOrder = ((SimpleData) sortTypeComboBox.getSelectedItem()).getId();

            model.setSelectedSortOrder(sortOrder);
            view.update();
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
            view.update();
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
            view.update();
        }
    }

    private class NumericTextFieldFocusListener implements FocusListener {
        private String defaultValue;

        NumericTextFieldFocusListener(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        @Override
        public void focusGained(FocusEvent e) {

        }

        @Override
        public void focusLost(FocusEvent e) {
            if (!(e.getSource() instanceof JTextField)) {
                return;
            }

            JTextField textField = (JTextField) e.getSource();

            String text = textField.getText();

            if (!text.matches("\\d+")) {
                text = text.replaceAll("\\D", "");
            }

            if (text.length() == 0) {
                text = defaultValue;
            }

            textField.setText(text);
        }
    }

    private class NoteEditDialogComboBoxItemListener implements ItemListener {
        JDialog dialog;
        private String table;

        NoteEditDialogComboBoxItemListener(JDialog dialog, String table) {
            this.dialog = dialog;
            this.table = table;
        }

        @SuppressWarnings({"unchecked"})
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (!(e.getSource() instanceof JComboBox)) {
                return;
            }

            JComboBox<SimpleData> cb = (JComboBox<SimpleData>) e.getSource();

            if (!(e.getItem() instanceof SimpleData)) {
                return;
            }

            if (e.getStateChange() != ItemEvent.SELECTED) {
                return;
            }

            SimpleData sd = (SimpleData) e.getItem();
            if (sd.getId() != -1) {
                return;
            }

            String newValue = JOptionPane.showInputDialog(dialog, "Введите новое значение:", "", JOptionPane.PLAIN_MESSAGE);
            if (newValue != null) {
                int id = insertNewValue(newValue, table);
                cb.insertItemAt(new SimpleData(id, newValue), 0);
            }
            cb.setSelectedIndex(0);
        }
    }

    private class SaveNoteButtonActionListener implements ActionListener {
        NoteEditDialog dialog;
        Note note;
        JTextField itemNameTextField;
        JComboBox itemTypeComboBox;
        JTextField itemPriceTextField;
        JComboBox shopComboBox;
        JComboBox necessityLevelComboBox;
        JComboBox qualityLevelComboBox;
        JCheckBox saleCheckBox;
        JTextField dayTextField;
        JTextField monthTextField;
        JTextField yearTextField;

        SaveNoteButtonActionListener(NoteEditDialog dialog, Note note, JTextField itemNameTextField,
                                     JComboBox itemTypeComboBox, JTextField itemPriceTextField,
                                     JComboBox shopComboBox, JComboBox necessityLevelComboBox,
                                     JComboBox qualityLevelComboBox, JCheckBox saleCheckBox,
                                     JTextField dayTextField, JTextField monthTextField,
                                     JTextField yearTextField) {
            this.dialog = dialog;
            this.note = note;
            this.itemNameTextField = itemNameTextField;
            this.itemTypeComboBox = itemTypeComboBox;
            this.itemPriceTextField = itemPriceTextField;
            this.shopComboBox = shopComboBox;
            this.necessityLevelComboBox = necessityLevelComboBox;
            this.qualityLevelComboBox = qualityLevelComboBox;
            this.saleCheckBox = saleCheckBox;
            this.dayTextField = dayTextField;
            this.monthTextField = monthTextField;
            this.yearTextField = yearTextField;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (itemNameTextField.getText().length() == 0) {
                JOptionPane.showMessageDialog(dialog, "Укажите товар!", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            note.update(itemNameTextField.getText(), (SimpleData) itemTypeComboBox.getSelectedItem(),
                    Integer.parseInt(itemPriceTextField.getText()), (SimpleData) shopComboBox.getSelectedItem(),
                    (SimpleData) necessityLevelComboBox.getSelectedItem(), (SimpleData) qualityLevelComboBox.getSelectedItem(),
                    saleCheckBox.isSelected(), dayTextField.getText(), monthTextField.getText(), yearTextField.getText());

            dialog.setDialogResult(JOptionPane.YES_OPTION);

            dialog.dispose();
        }
    }

    private class CloseDialogButtonActionListener implements ActionListener {
        NoteEditDialog dialog;
        Note note;

        CloseDialogButtonActionListener(NoteEditDialog dialog, Note note) {
            this.dialog = dialog;
            this.note = note;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            note = null;

            dialog.setDialogResult(JOptionPane.NO_OPTION);

            dialog.dispose();
        }
    }

}
