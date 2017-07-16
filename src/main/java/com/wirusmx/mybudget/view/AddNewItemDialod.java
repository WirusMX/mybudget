package com.wirusmx.mybudget.view;

import javax.swing.*;

public class AddNewItemDialod extends JDialog {
    public AddNewItemDialod() {
        init();
    }

    private void init() {
        setSize(500, 350);
        setLayout(null);
        JLabel label1 = new JLabel("Товар: ");
        label1.setBounds(10, 10, 50, 25);
        add(label1);

        JTextField itemNameTextField = new JTextField();
        itemNameTextField.setBounds(110, 10, getWidth() - 20 - 110, 25);
        add(itemNameTextField);

        JLabel label2 = new JLabel("Тип: ");
        label2.setBounds(10, 40, 50, 25);
        add(label2);

        JComboBox itemTypeComboBox = new JComboBox();
        itemTypeComboBox.setBounds(110, 40, getWidth() - 20 - 110, 25);
        add(itemTypeComboBox);

        JLabel label3 = new JLabel("Стоимость: ");
        label3.setBounds(10, 70, 90, 25);
        add(label3);

        JTextField itemPriceTextField = new JTextField();
        itemPriceTextField.setBounds(110, 70, getWidth() - 20 - 110, 25);
        add(itemPriceTextField);

        JLabel label4 = new JLabel("Магазин: ");
        label4.setBounds(10, 100, 90, 25);
        add(label4);

        JComboBox shopComboBox = new JComboBox();
        shopComboBox.setBounds(110, 100, getWidth() - 20 - 110, 25);
        add(shopComboBox);

        JLabel label5 = new JLabel("Необходимость: ");
        label5.setBounds(10, 130, 100, 25);
        add(label5);

        JComboBox necessityLevelComboBox = new JComboBox();
        necessityLevelComboBox.addItem("Высокая");
        necessityLevelComboBox.addItem("Средняя");
        necessityLevelComboBox.addItem("Низкая");
        necessityLevelComboBox.setBounds(110, 130, getWidth() - 20 - 110, 25);
        add(necessityLevelComboBox);

        setVisible(true);
    }
}
