package com.wirusmx.mybudget.dialogs.plannerdialog;

import com.wirusmx.mybudget.managers.ResourcesManager;

/**
 * @author Piunov M (aka WirusMX)
 */
public class PlannerDialog {
    public PlannerDialog() {
        PlannerDialogController controller = new PlannerDialogController();
        PlannerDialogModel model = new PlannerDialogModel(controller);
        PlannerDialogView view = new PlannerDialogView(controller, ResourcesManager.getInstance());

        controller.setModel(model);
        controller.setView(view);

        controller.showDialog();
    }
}
