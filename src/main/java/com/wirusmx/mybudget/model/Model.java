package com.wirusmx.mybudget.model;

import com.wirusmx.mybudget.Controller;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private Controller controller;

    public Model(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }

    public List<SimpleData> getItemTypes(String tableName) {
        ArrayList<SimpleData> result = new ArrayList<>();
        result.add(new SimpleData(0, "Прочее"));
        return result;
    }

    public int insertNewValue(String value, String table) {
        return -2;
    }
}
