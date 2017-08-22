package com.wirusmx.mybudget.dialogs.optionsdialog;

import com.wirusmx.mybudget.dialogs.AbstractDialogController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Piunov M (aka WirusMX)
 */
class OptionsDialogController extends AbstractDialogController {
    private OptionsDialogView view;
    private OptionsDialogModel model;

    public void setModel(OptionsDialogModel model) {
        this.model = model;
    }

    void setView(OptionsDialogView view) {
        super.setDialogView(view);
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
}
