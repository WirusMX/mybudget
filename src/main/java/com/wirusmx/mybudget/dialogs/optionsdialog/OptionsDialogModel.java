package com.wirusmx.mybudget.dialogs.optionsdialog;

import com.wirusmx.mybudget.managers.UserSettingsManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Piunov M (aka WirusMX)
 */
class OptionsDialogModel {
    private List<Component> editedComponentCache = new ArrayList<>();

    private UserSettingsManager userSettingsManager;

    OptionsDialogModel(UserSettingsManager userSettingsManager) {
        this.userSettingsManager = userSettingsManager;
    }

    void addToCache(Component component) {
        editedComponentCache.add(component);
    }

    boolean isCacheEmpty() {
        return editedComponentCache.isEmpty();
    }

    void saveCache() {
        for (Component c: editedComponentCache){
            if (c instanceof JCheckBox){
                JCheckBox checkBox = (JCheckBox) c;
                userSettingsManager.setValue(checkBox.getName(), "" + (checkBox.isSelected()?1:0));
            }
        }

        editedComponentCache.clear();
    }

    boolean getOption(String key, boolean defaultValue){
        return userSettingsManager.getBooleanValue(key, defaultValue);
    }
}
