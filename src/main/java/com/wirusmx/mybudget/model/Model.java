package com.wirusmx.mybudget.model;

import com.wirusmx.mybudget.Controller;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private Controller controller;

    public Model(Controller controller) {
        this.controller = controller;
        init();
    }

    private void init() {

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

    public List<Note> getNotes() {
        List<Note> result = new ArrayList<>();
        result.add(new Note(0, "1", new SimpleData(0, "Прочее"), 1,
                new SimpleData(0, "Прочее"), new SimpleData(0, "Прочее"), new SimpleData(0, "Прочее"), false));

        result.add(new Note(1, "2", new SimpleData(0, "Прочее"), 2,
                new SimpleData(0, "Прочее"), new SimpleData(0, "Прочее"), new SimpleData(0, "Прочее"), false));
        result.add(new Note(2, "3", new SimpleData(0, "Прочее"), 3,
                new SimpleData(0, "Прочее"), new SimpleData(0, "Прочее"), new SimpleData(0, "Прочее"), false));

        return result;
    }
}
