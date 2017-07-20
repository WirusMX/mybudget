package com.wirusmx.mybudget.model;

import com.wirusmx.mybudget.Controller;

import java.util.*;

public class Model {
    private Controller controller;
    private SQLDatabase database;

    public Model(Controller controller, SQLDatabase database) {
        this.controller = controller;
        this.database = database;
        init();
    }

    private void init() {

    }

    private List<Note> notes = new ArrayList<>();
    private Map<String, Set<SimpleData>> types;

    {
        types = new HashMap<>();
        types.put("item_types", new TreeSet<>(new ComboBoxValuesComparator()));
        types.put("shops", new TreeSet<>(new ComboBoxValuesComparator()));

        for (String key: types.keySet()){
            types.get(key).add(new SimpleData(0, "Прочее"));
        }

    }

    public Set<SimpleData> getComboBoxValues(String tableName) {
        return database.getComboBoxValues(tableName);
    }

    public int insertNewValue(String value, String table) {

        return database.insertNewValue(value, table);
    }

    public List<Note> getNotes() {

        return database.getNotes();
    }

    public void insertNote(Note note) {
        database.insertNote(note);
    }

    public void updateNote(Note note) {

    }

    private class ComboBoxValuesComparator implements Comparator<SimpleData>{
        @Override
        public int compare(SimpleData o1, SimpleData o2) {
            if (o1.getId() == 0){
                return -1;
            }

            if (o2.getId() == 0){
                return 1;
            }

            return o1.getTitle().toLowerCase().compareTo(o2.getTitle().toLowerCase());
        }
    }
}
