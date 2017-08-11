package com.wirusmx.mybudget.dialogs.statisticsdialog;

import com.wirusmx.mybudget.managers.DatabaseManager;
import com.wirusmx.mybudget.managers.ResourcesManager;

/**
 * @author Piunov M (aka WirusMX)
 */
public class StatisticsDialog {
    public StatisticsDialog() {
        StatisticsDialogController controller = new StatisticsDialogController();
        StatisticsDialogView view = new StatisticsDialogView(controller, ResourcesManager.getInstance());
        StatisticsDialogModel model = new StatisticsDialogModel(DatabaseManager.getInstance());

        controller.setDialogModel(model);
        controller.setDialogView(view);

        controller.showDialog();
    }
}
