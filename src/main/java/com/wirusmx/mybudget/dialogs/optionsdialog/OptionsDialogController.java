package com.wirusmx.mybudget.dialogs.optionsdialog;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Piunov M (aka WirusMX)
 */
class OptionsDialogController {
    private OptionsDialogView view;
    private OptionsDialogModel model;

    public void setModel(OptionsDialogModel model) {
        this.model = model;
    }

    void setView(OptionsDialogView view) {
        this.view = view;
    }

    void showDialog() {
        view.init();
    }

    boolean getOption(String key, boolean defaultValue) {
        return model.getOption(key, defaultValue);
    }

    ComponentActionListener getComponentActionListener() {
        return new ComponentActionListener();
    }

    CloseDialogButtonActionListener getCloseDialogButtonActionListener() {
        return new CloseDialogButtonActionListener();
    }

    OkButtonActionListener getOkButtonActionListener() {
        return new OkButtonActionListener();
    }

    ApplyButtonActionListener getApplyButtonActionListener() {
        return new ApplyButtonActionListener();
    }

    private class ComponentActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            model.addToCache((Component) e.getSource());
            view.setApplyButtonEnabled(!model.isCacheEmpty());
        }
    }

    private class OkButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            model.saveCache();
            view.dispose();
        }
    }

    private class ApplyButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            model.saveCache();
            view.setApplyButtonEnabled(false);
        }
    }

    private class CloseDialogButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.dispose();
        }
    }
}
