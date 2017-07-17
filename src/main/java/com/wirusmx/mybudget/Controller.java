package com.wirusmx.mybudget;

import com.wirusmx.mybudget.model.Model;
import com.wirusmx.mybudget.model.SimpleData;
import com.wirusmx.mybudget.view.View;

import java.util.List;

public class Controller {
    private Model model;
    private View view;

    public void setModel(Model model) {
        this.model = model;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void startApplication() {
        view.init();
    }

    public List<SimpleData> getItemTypes(String tableName) {
        return model.getItemTypes(tableName);
    }

    public int insertNewValue(String value, String table) {
        return model.insertNewValue(value, table);
    }
}
