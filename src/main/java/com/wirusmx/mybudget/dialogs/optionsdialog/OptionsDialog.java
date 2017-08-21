package com.wirusmx.mybudget.dialogs.optionsdialog;

import com.wirusmx.mybudget.managers.ResourcesManager;
import com.wirusmx.mybudget.managers.UserSettingsManager;

/**
 * @author Piunov M (aka WirusMX)
 */
public class OptionsDialog {
    public OptionsDialog() {
        OptionsDialogController controller = new OptionsDialogController();

        OptionsDialogView view = new OptionsDialogView(controller, ResourcesManager.getInstance());
        OptionsDialogModel model = new OptionsDialogModel(UserSettingsManager.getInstance());

        controller.setView(view);
        controller.setModel(model);

        controller.showDialog();
    }
}
