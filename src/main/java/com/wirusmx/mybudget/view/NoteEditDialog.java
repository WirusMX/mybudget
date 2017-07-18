package com.wirusmx.mybudget.view;

import com.wirusmx.mybudget.model.Note;
import com.wirusmx.mybudget.model.SimpleData;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;

public class NoteEditDialog extends JDialog {

    private static final int FIRST_COL_X_POS = 10;
    private static final int SECOND_COL_X_POS = 140;
    private static final int ELEMENT_HEIGHT = 25;
    private static final int LABEL_WIDTH = 130;
    private static final int Y0 = 20;
    private static final int DELTA_Y = 30;

    private final JDialog thisDialog = this;

    private View parent;
    private Note note;

    public NoteEditDialog(View parent) {
        this(parent, new Note());
    }

    public NoteEditDialog(View parent, Note note) {
        this.parent = parent;
        this.note = note;
        init();
    }

    public Note getNote() {
        return note;
    }

    private void init() {
        setSize(500, 300);
        setLayout(null);
        setModal(true);
        setLocationRelativeTo(null);
        setUndecorated(true);

        int elementWidth = getWidth() - 20 - SECOND_COL_X_POS;

        JLabel label1 = new JLabel("Товар: ");
        label1.setBounds(FIRST_COL_X_POS, Y0, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(label1);

        final JTextField itemNameTextField = new JTextField(note.getItem());
        itemNameTextField.setBounds(SECOND_COL_X_POS, Y0, elementWidth, ELEMENT_HEIGHT);
        add(itemNameTextField);

        JLabel label2 = new JLabel("Тип: ");
        label2.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(label2);

        final JComboBox<SimpleData> itemTypeComboBox = new JComboBox<>();
        addValuesToComboBox(itemTypeComboBox, "item_types");
        selectValue(itemTypeComboBox, note.getType());
        itemTypeComboBox.addItemListener(new ComboBoxItemListener("item_types"));
        itemTypeComboBox.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y, elementWidth, ELEMENT_HEIGHT);
        add(itemTypeComboBox);

        JLabel label3 = new JLabel("Стоимость (руб.): ");
        label3.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * 2, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(label3);

        final JTextField itemPriceTextField = new JTextField("" + note.getPrice());
        itemPriceTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                if (!itemPriceTextField.getText().matches("\\d+")){
                    itemPriceTextField.setText(itemPriceTextField.getText().replaceAll("\\D", ""));
                }

                if (itemPriceTextField.getText().length() == 0){
                    itemPriceTextField.setText("0");
                }
            }
        });

        itemPriceTextField.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y * 2, elementWidth, ELEMENT_HEIGHT);
        add(itemPriceTextField);

        JLabel label4 = new JLabel("Магазин: ");
        label4.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * 3, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(label4);

        final JComboBox<SimpleData> shopComboBox = new JComboBox<>();
        addValuesToComboBox(shopComboBox, "shops");
        selectValue(shopComboBox, note.getShop());
        shopComboBox.addItemListener(new ComboBoxItemListener("shops"));
        shopComboBox.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y * 3, elementWidth, ELEMENT_HEIGHT);
        add(shopComboBox);

        JLabel label5 = new JLabel("Необходимость: ");
        label5.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * 4, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(label5);

        final JComboBox<SimpleData> necessityLevelComboBox = new JComboBox<>();
        necessityLevelComboBox.addItem(new SimpleData(0, "Высокая"));
        necessityLevelComboBox.addItem(new SimpleData(1, "Средняя"));
        necessityLevelComboBox.addItem(new SimpleData(2, "Низкая"));
        necessityLevelComboBox.setSelectedIndex(note.getNecessity().getId() % 3);
        necessityLevelComboBox.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y * 4, elementWidth, ELEMENT_HEIGHT);
        add(necessityLevelComboBox);

        JLabel label6 = new JLabel("Качество: ");
        label6.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * 5, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(label6);

        final JComboBox<SimpleData> qualityLevelComboBox = new JComboBox<>();
        qualityLevelComboBox.addItem(new SimpleData(0, "-"));
        qualityLevelComboBox.addItem(new SimpleData(1, "Высокое"));
        qualityLevelComboBox.addItem(new SimpleData(2, "Среднее"));
        qualityLevelComboBox.addItem(new SimpleData(3, "Низкое"));
        qualityLevelComboBox.setSelectedIndex(note.getQuality().getId() % 4);
        qualityLevelComboBox.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y * 5, elementWidth, ELEMENT_HEIGHT);
        add(qualityLevelComboBox);

        final JCheckBox sale = new JCheckBox("Со скидкой ");
        sale.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * 6, LABEL_WIDTH, ELEMENT_HEIGHT);
        sale.setSelected(note.isBySale());
        add(sale);

        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (itemNameTextField.getText().length() == 0){
                    JOptionPane.showMessageDialog(thisDialog, "Укажите товар!", "", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                note.update(itemNameTextField.getText(), (SimpleData) itemTypeComboBox.getSelectedItem(),
                        Integer.parseInt(itemPriceTextField.getText()), (SimpleData)shopComboBox.getSelectedItem(),
                        (SimpleData) necessityLevelComboBox.getSelectedItem(), (SimpleData)qualityLevelComboBox.getSelectedItem(),
                        sale.isSelected());
                thisDialog.dispose();
            }
        });
        saveButton.setBounds(50, Y0 + DELTA_Y * 6 + 50, 200, 30);
        add(saveButton);

        JButton closeButton = new JButton("Закрыть");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                note = null;
                thisDialog.dispose();
            }
        });
        closeButton.setBounds(280, Y0 + DELTA_Y * 6 + 50, 200, 30);
        add(closeButton);

        setVisible(true);
    }

    private void selectValue(JComboBox<SimpleData> comboBox, SimpleData value) {
        if (value == null) {
            return;
        }

        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (value.equals(comboBox.getItemAt(i))) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
    }

    private void addValuesToComboBox(JComboBox<SimpleData> comboBox, String tableName) {
        List<SimpleData> types = parent.getController().getItemTypes(tableName);
        types.add(new SimpleData(-1, "Добавить новый"));
        for (SimpleData d : types) {
            comboBox.addItem(d);
        }
    }

    private class ComboBoxItemListener implements ItemListener {
        private String table;

        public ComboBoxItemListener(String table) {
            this.table = table;
        }

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

            String s = JOptionPane.showInputDialog(thisDialog, "Введите новое значение:", "", JOptionPane.PLAIN_MESSAGE);
            if (s != null) {
                int id = parent.getController().insertNewValue(s, table);
                cb.insertItemAt(new SimpleData(id, s), 0);
            }
            cb.setSelectedIndex(0);
        }
    }
}
