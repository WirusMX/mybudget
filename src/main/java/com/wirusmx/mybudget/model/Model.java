package com.wirusmx.mybudget.model;

import com.wirusmx.mybudget.DefaultExceptionHandler;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Model {
    private JdbcTemplate template;


    public Model(JdbcTemplate template) {
        this.template = template;
        init();
    }

    private void init() {
        template.execute("CREATE TABLE IF NOT EXISTS product (id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "itemTitle TEXT, typeID INTEGER, price INTEGER, shopID INTEGER, " +
                "necessityID INTEGER, qualityID INTEGER, bySale INTEGER, day TEXT," +
                "month TEXT, year TEXT);");

        template.execute("CREATE TABLE IF NOT EXISTS item_types (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT);");
        template.execute("INSERT OR REPLACE INTO item_types (id, title) VALUES (0, 'Прочее')");

        template.execute("CREATE TABLE IF NOT EXISTS shops (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT);");
        template.execute("INSERT OR REPLACE INTO shops (id, title) VALUES (0, 'Прочее')");
    }

    public Set<SimpleData> getComboBoxValues(String tableName) {
        Set<SimpleData> values = new TreeSet<>(new ComboBoxValuesComparator());

        try {
            List<SimpleData> temp = template.query("SELECT * FROM " + tableName, new RowMapper<SimpleData>() {
                @Override
                public SimpleData mapRow(ResultSet resultSet, int i) throws SQLException {
                    return new SimpleData(resultSet.getInt("id"), resultSet.getString("title"));
                }
            });
            values.addAll(temp);
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(ex);
        }

        return values;
    }

    public int insertNewValue(String value, String table) {
        template.execute("INSERT OR REPLACE INTO " + table + " (title) VALUES ('" + value + "');");
        Integer id = template.queryForObject("SELECT id from " + table + " WHERE title LIKE '" + value + "';", Integer.class);
        return id;
    }

    public void insertNote(Note note) {
        template.execute("INSERT INTO product (itemTitle, typeID, price, shopID, " +
                "necessityID, qualityID, bySale, day, month, year) " +
                "VALUES ("
                + "'" + note.getItem() + "', "
                + note.getType().getId() + ", "
                + note.getPrice() + ", "
                + note.getShop().getId() + ", "
                + note.getNecessity().getId() + ", "
                + note.getQuality().getId() + ", "
                + (note.isBySale() ? 1 : 0) + ", "
                + "'" + note.getDay() + "', "
                + "'" + note.getMonth() + "', "
                + "'" + note.getYear() + "');");

    }

    public List<Note> getNotes() {
        List<Note> result = new ArrayList<>();

        try {
            result = template.query("SELECT * FROM (product INNER JOIN shops ON product.shopID = shops.id) " +
                    "INNER JOIN item_types ON product.typeID=item_types.id;", new RowMapper<Note>() {
                @Override
                public Note mapRow(ResultSet resultSet, int i) throws SQLException {
                    return new Note(resultSet.getInt("id"),
                            resultSet.getString("itemTitle"),
                            new SimpleData(resultSet.getInt("typeID"), resultSet.getString(15)),
                            resultSet.getInt("price"),
                            new SimpleData(resultSet.getInt("shopID"), resultSet.getString(13)),
                            new SimpleData(resultSet.getInt("necessityID"), ""),
                            new SimpleData(resultSet.getInt("qualityID"), ""),
                            resultSet.getInt("bySale") == 1,
                            resultSet.getString("day"),
                            resultSet.getString("month"),
                            resultSet.getString("year")
                    );
                }
            });
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(ex);
        }
        return result;
    }

    public void updateNote(Note note) {
        template.execute("INSERT OR REPLACE INTO product (id, itemTitle, typeID, price, shopID, " +
                "necessityID, qualityID, bySale, day, month, year) " +
                "VALUES ("
                + note.getId() + ", "
                + "'" + note.getItem() + "', "
                + note.getType().getId() + ", "
                + note.getPrice() + ", "
                + note.getShop().getId() + ", "
                + note.getNecessity().getId() + ", "
                + note.getQuality().getId() + ", "
                + (note.isBySale() ? 1 : 0) + ", "
                + "'" + note.getDay() + "', "
                + "'" + note.getMonth() + "', "
                + "'" + note.getYear() + "');");
    }

    private class ComboBoxValuesComparator implements Comparator<SimpleData> {
        @Override
        public int compare(SimpleData o1, SimpleData o2) {
            if (o1.getId() == 0) {
                return -1;
            }

            if (o2.getId() == 0) {
                return 1;
            }

            return o1.getTitle().toLowerCase().compareTo(o2.getTitle().toLowerCase());
        }
    }


}
