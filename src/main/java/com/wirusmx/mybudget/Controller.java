package com.wirusmx.mybudget;

import com.wirusmx.mybudget.model.Model;
import com.wirusmx.mybudget.model.Note;
import com.wirusmx.mybudget.model.SimpleData;
import com.wirusmx.mybudget.view.View;

import java.util.List;
import java.util.Set;

public class Controller {
    private Model model;
    private View view;

    public void setModel(Model model) {
        this.model = model;
    }

    void setView(View view) {
        this.view = view;
    }

    void startApplication() {
        model.init();
        view.init();
    }

    public Set<SimpleData> getComboBoxValues(String tableName) {
        return model.getComboBoxValues(tableName);
    }

    public int insertNewValue(String value, String table) {
        return model.insertNewValue(value, table);
    }

    public List<Note> getNotes() {
        return model.getNotes("");
    }

    public List<Note> getNotes(String period) {
        return model.getNotes(period);
    }

    public void insertNote(Note note) {
        model.insertNote(note);
    }

    public void updateNote(Note note) {
        model.updateNote(note);
    }

    public Set<String> getYears() {
        return model.getYears();
    }

    public Set<String> getMonths() {
        return model.getMonths();
    }

    public Set<String> getDays() {
        return model.getDays();
    }

    public String getUserSettingsValue(String key, String defaultValue){
        return model.getUserSettingsValue(key, defaultValue);
    }

    public void setUserSettingsValue(String key, String value) {
        model.setUserSettingsValue(key, value);
    }


}
