package com.wirusmx.mybudget.dialogs.plannerdialog;

import com.wirusmx.mybudget.dialogs.AbstractDialogController;

/**
 * @author Piunov M (aka WirusMX)
 */
class PlannerDialogController extends AbstractDialogController {
    private PlannerDialogModel model;
    private PlannerDialogView view;

    void setModel(PlannerDialogModel model) {
        this.model = model;
    }

    void setView(PlannerDialogView view) {
        super.setDialogView(view);
        this.view = view;
    }

    void showDialog() {
        view.init();
    }
}
