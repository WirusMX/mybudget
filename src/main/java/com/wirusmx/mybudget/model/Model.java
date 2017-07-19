package com.wirusmx.mybudget.model;

import com.wirusmx.mybudget.Controller;

import java.util.*;

public class Model {
    private Controller controller;

    public Model(Controller controller) {
        this.controller = controller;
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
        return Collections.unmodifiableSet(types.get(tableName));
    }

    public int insertNewValue(String value, String table) {
        int id = types.get(table).size();
        types.get(table).add(new SimpleData(id, value));
        return id;
    }

    public List<Note> getNotes() {

        return notes;
    }

    public void insertNote(Note note) {
        notes.add(note);
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
