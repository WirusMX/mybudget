package com.wirusmx.mybudget.dialogs.plannerdialog;

import com.wirusmx.mybudget.dialogs.AbstractDialogView;
import com.wirusmx.mybudget.managers.ResourcesManager;

/**
 * @author Piunov M (aka WirusMX)
 */
class PlannerDialogView extends AbstractDialogView {
    private static final int DIALOG_WIDTH = 1000;
    private static final int DIALOG_HEIGHT = 600;
    private static final String DIALOG_TITLE = "Настройки приложения";
    private static final String ICON_IMAGE = "settings";

    private PlannerDialogController controller;

    PlannerDialogView(PlannerDialogController controller, ResourcesManager resourcesManager) {
        super(DIALOG_WIDTH, DIALOG_HEIGHT, DIALOG_TITLE, ICON_IMAGE, controller, resourcesManager);
        this.controller = controller;
    }

    @Override
    protected void initDialogContent() {

    }
}
