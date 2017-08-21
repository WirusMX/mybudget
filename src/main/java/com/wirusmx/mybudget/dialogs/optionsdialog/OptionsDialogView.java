package com.wirusmx.mybudget.dialogs.optionsdialog;

import com.wirusmx.mybudget.managers.ResourcesManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * @author Piunov M (aka WirusMX)
 */
class OptionsDialogView extends JDialog {
    private static final int DIALOG_WIDTH = 800;
    private static final int DIALOG_HEIGHT = 600;

    private JButton applyButton;

    private OptionsDialogController controller;
    private ResourcesManager resourcesManager;

    OptionsDialogView(OptionsDialogController controller, ResourcesManager resourcesManager) {
        this.controller = controller;
        this.resourcesManager = resourcesManager;
    }

    void init() {
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setModal(true);
        setLocationRelativeTo(null);
        setTitle("Настройки приложения");
        setIconImage(resourcesManager.getImage("settings").getImage());
        getRootPane().registerKeyboardAction(
                controller.getCloseDialogButtonActionListener(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );
        setResizable(false);
        setLayout(new BorderLayout());


        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(JTabbedPane.LEFT);
        tabbedPane.addTab("Поиск", getSearchOptionsPanel());

        add(tabbedPane, BorderLayout.CENTER);

        add(getButtonsPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    void setApplyButtonEnabled(boolean value){
        applyButton.setEnabled(value);
    }

    private JPanel getSearchOptionsPanel() {
        JPanel panel = new JPanel(null);

        JCheckBox useLiveSearch = new JCheckBox("Использовать \"живой\" поиск в главном окне");
        useLiveSearch.setBounds(10, 10, DIALOG_WIDTH - 10, 25);
        useLiveSearch.setSelected(controller.getOption("main.window.use.live.search", false));
        useLiveSearch.setName("main.window.use.live.search");
        useLiveSearch.addActionListener(controller.getComponentActionListener());
        panel.add(useLiveSearch);

        JCheckBox searchByShop = new JCheckBox("Поиск в том числе по магазинам");
        searchByShop.setBounds(10, 35, DIALOG_WIDTH - 10, 25);
        searchByShop.setSelected(controller.getOption("main.window.search.by.shop", false));
        searchByShop.setName("main.window.search.by.shop");
        searchByShop.addActionListener(controller.getComponentActionListener());
        panel.add(searchByShop);

        return panel;
    }



    private JPanel getButtonsPanel(){
        JPanel panel = new JPanel(new GridLayout(1, 5, 10, 10));
        panel.add(new JLabel());
        panel.add(new JLabel());

        JButton okButton = new JButton("ОК");
        okButton.addActionListener(controller.getOkButtonActionListener());
        panel.add(okButton);

        applyButton = new JButton("Применить");
        applyButton.addActionListener(controller.getApplyButtonActionListener());
        applyButton.setEnabled(false);
        panel.add(applyButton);

        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(controller.getCloseDialogButtonActionListener());
        panel.add(cancelButton);

        return panel;
    }
}
