package com.wirusmx.mybudget.dialogs.noteeditdialog;

import com.wirusmx.mybudget.DefaultExceptionHandler;
import com.wirusmx.mybudget.model.Note;
import com.wirusmx.mybudget.model.SimpleData;

import javax.swing.*;
import java.awt.event.*;
import java.util.Set;

/**
 * @author Piunov M (aka WirusMX)
 */
class NoteEditDialogController {
    private NoteEditDialogView dialogView;
    private NoteEditDialogModel dialogModel;

    void setDialogView(NoteEditDialogView dialogView) {
        this.dialogView = dialogView;
    }

    void setDialogModel(NoteEditDialogModel dialogModel) {
        this.dialogModel = dialogModel;
    }

    void showDialog() {
        dialogView.init();
    }

    Set<SimpleData> getComboBoxValues(String tableName) {
        return dialogModel.getComboBoxValues(tableName);
    }

    ItemsTitleTextFieldKeyListener getItemsTitleTextFieldKeyListener(JComboBox<String> itemsComboBox) {
        return new ItemsTitleTextFieldKeyListener(itemsComboBox);
    }

    ItemsComboBoxItemListener getItemsComboBoxItemListener(JTextField textField) {
        return new ItemsComboBoxItemListener(textField);
    }

    NumericTextFieldFocusListener getNumericTextFieldFocusListener(Class type, String defaultValue, int cutNum) {
        return new NumericTextFieldFocusListener(type, defaultValue, cutNum);
    }

    TotalCalcTextFieldFocusListener getTotalCalcTextFieldFocusListener(JTextField priceTextField, JTextField countTextField) {
        return new TotalCalcTextFieldFocusListener(priceTextField, countTextField);
    }

    SelectAllTextFocusListener getSelectAllTextFocusListener() {
        return new SelectAllTextFocusListener();
    }

    CountTypeComboBoxItemListener getCountTypeComboBoxItemListener(JLabel label) {
        return new CountTypeComboBoxItemListener(label);
    }

    NoteEditDialogComboBoxItemListener getNoteEditDialogComboBoxItemListener(String table) {
        return new NoteEditDialogComboBoxItemListener(table);
    }


    SaveNoteButtonActionListener getSaveNoteButtonActionListener() {
        return new SaveNoteButtonActionListener();

    }

    CloseNoteEditDialogButtonActionListener getCloseDialogButtonActionListener() {
        return new CloseNoteEditDialogButtonActionListener();
    }

    Note getCurrentNote() {
        return dialogModel.getNote();
    }

    private class ItemsTitleTextFieldKeyListener implements KeyListener {

        private JComboBox<String> itemsComboBox;

        ItemsTitleTextFieldKeyListener(JComboBox<String> itemsComboBox) {
            this.itemsComboBox = itemsComboBox;
        }

        @Override
        public void keyTyped(KeyEvent e) {
            JTextField textField;

            if (e.getSource() instanceof JTextField) {
                textField = (JTextField) e.getSource();
            } else {
                return;
            }

            if (itemsComboBox.isPopupVisible()) {
                itemsComboBox.hidePopup();
            }

            itemsComboBox.removeAllItems();

            if (textField.getText().length() < 2) {
                return;
            }

            Set<String> items = dialogModel.getItemsSet();


            for (String s : items) {
                if (s.toLowerCase().contains(textField.getText().toLowerCase())) {
                    itemsComboBox.addItem(s);
                }
            }

            if (itemsComboBox.getItemCount() > 0) {
                itemsComboBox.setSelectedIndex(-1);
                itemsComboBox.showPopup();
            }

        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getExtendedKeyCode() == KeyEvent.VK_DOWN) {
                itemsComboBox.requestFocusInWindow();
            }
        }
    }

    private class ItemsComboBoxItemListener implements ItemListener {

        JTextField itemTextField;

        ItemsComboBoxItemListener(JTextField itemTextField) {
            this.itemTextField = itemTextField;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (!(e.getSource() instanceof JComboBox) || e.getStateChange() == ItemEvent.DESELECTED) {
                return;
            }

            JComboBox comboBox = (JComboBox) e.getSource();

            if (comboBox.isPopupVisible()) {
                itemTextField.setText((String) comboBox.getSelectedItem());
                itemTextField.requestFocusInWindow();
            }

        }
    }

    private class NumericTextFieldFocusListener implements FocusListener {
        private String defaultValue;
        private Class type;
        int cutNum;

        NumericTextFieldFocusListener(Class type, String defaultValue, int cutNum) {
            this.defaultValue = defaultValue;
            this.type = type;
            this.cutNum = cutNum;
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

            text = dialogModel.stringToNumericFormat(type, text, cutNum);

            if (text.length() == 0) {
                text = defaultValue;
            }

            textField.setText(text);
        }
    }

    private class TotalCalcTextFieldFocusListener implements FocusListener {
        private JTextField priceTextField;
        private JTextField countTextField;

        TotalCalcTextFieldFocusListener(JTextField priceTextField, JTextField countTextField) {
            this.priceTextField = priceTextField;
            this.countTextField = countTextField;
        }

        @Override
        public void focusGained(FocusEvent e) {
            if (!(e.getSource() instanceof JTextField)) {
                return;
            }

            float price;
            float count;

            try {
                price = Float.parseFloat(priceTextField.getText());
                count = Float.parseFloat(countTextField.getText());
            } catch (Exception ex) {
                DefaultExceptionHandler.handleException(ex);
                return;
            }

            JTextField totalTextField = (JTextField) e.getSource();

            if (price > 0f && count > 0f) {
                totalTextField.setText(String.format(Note.PRICE_FORMAT, price * count));
            }

        }

        @Override
        public void focusLost(FocusEvent e) {
            if (!(e.getSource() instanceof JTextField)) {
                return;
            }

            JTextField totalTextField = (JTextField) e.getSource();


            float price;
            float count;
            float total;

            try {
                price = Float.parseFloat(priceTextField.getText());
                count = Float.parseFloat(countTextField.getText());
                total = Float.parseFloat(totalTextField.getText());
            } catch (Exception ex) {
                DefaultExceptionHandler.handleException(ex);
                return;
            }

            if (price == 0f && count > 0f) {
                priceTextField.setText(String.format(Note.PRICE_FORMAT, total / count));
            }

            if (price > 0f && count == 0f) {
                countTextField.setText(String.format(Note.COUNT_FORMAT, total / price));
            }

        }
    }

    private class SelectAllTextFocusListener implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {
            if (e.getSource() instanceof JTextField) {
                ((JTextField) e.getSource()).selectAll();
            }
        }

        @Override
        public void focusLost(FocusEvent e) {

        }
    }

    private class CountTypeComboBoxItemListener implements ItemListener {
        private JLabel label;
        private String prefix = "Цена (руб./";

        CountTypeComboBoxItemListener(JLabel label) {
            this.label = label;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            label.setText(prefix + e.getItem().toString() + "):");
        }
    }

    private class NoteEditDialogComboBoxItemListener implements ItemListener {
        private String table;

        NoteEditDialogComboBoxItemListener(String table) {
            this.table = table;
        }

        @SuppressWarnings("unchecked")
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

            String newValue = JOptionPane.showInputDialog(dialogView, "Введите новое значение:", "", JOptionPane.PLAIN_MESSAGE);
            if (newValue != null && !newValue.isEmpty()) {
                int id = dialogModel.insertNewComboBoxValue(newValue, table);
                if (id >= 0) {
                    cb.insertItemAt(new SimpleData(id, newValue), 0);
                }
            }
            cb.setSelectedIndex(0);
        }
    }

    private class SaveNoteButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (dialogView.getItemTitle().length() == 0) {
                JOptionPane.showMessageDialog(dialogView, "Укажите товар!", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (dialogView.getItemsCount() == 0f) {
                JOptionPane.showMessageDialog(dialogView, "Укажите количество товара!", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            dialogModel.updateNote(dialogView.getItemTitle(),
                    dialogView.getItemType(),
                    dialogView.getItemPrice(),
                    dialogView.getItemsCount(),
                    dialogView.getCountType(),
                    dialogView.getShop(),
                    dialogView.getNecessity(),
                    dialogView.getQuality(),
                    dialogView.isBySale(),
                    dialogView.getDay(),
                    dialogView.getMonth(),
                    dialogView.getYear());

            dialogModel.setPositiveDialogResult();

            dialogView.dispose();
        }
    }

    private class CloseNoteEditDialogButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dialogView.dispose();
        }
    }
}
