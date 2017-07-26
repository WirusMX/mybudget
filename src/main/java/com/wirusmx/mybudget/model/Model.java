package com.wirusmx.mybudget.model;

import com.wirusmx.mybudget.DefaultExceptionHandler;
import com.wirusmx.mybudget.controller.Controller;
import com.wirusmx.mybudget.model.comparators.DateComparator;
import com.wirusmx.mybudget.model.comparators.MyComparator;
import com.wirusmx.mybudget.model.comparators.TitlesComparator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.swing.*;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Model {
    private static final String USER_SETTINGS_PROPERTIES_PATH = "conf/user_settings.properties";
    private static final String TEXT_FILES_PREFIX = "/text/";
    private static final String IMAGES_FILES_PREFIX = "/images/";
    private static final String IMAGES_FILES_EXT = ".png";

    private JdbcTemplate template;
    private Controller controller;

    private Properties userSettings;

    private MyComparator[] comparators;

    private int selectedPeriodType = PeriodType.ALL;
    private String selectedPeriod = "";
    private int selectedSortType = SortType.DATE;
    private int selectedSortOrder = MyComparator.REVERSE_ORDER;
    private String searchQuery = "";

    public Model(JdbcTemplate template, Controller controller) {
        this.template = template;
        this.controller = controller;
    }

    public int getSelectedPeriodType() {
        return selectedPeriodType;
    }

    public void setSelectedPeriodType(int selectedPeriodType) {
        this.selectedPeriodType = selectedPeriodType;
    }

    public String getSelectedPeriod() {
        return selectedPeriod;
    }

    public void setSelectedPeriod(String selectedPeriod) {
        this.selectedPeriod = selectedPeriod;
        setUserSettingsValue("main.window.period.type", "" + selectedPeriodType);
        setUserSettingsValue("main.window.period", selectedPeriod);
    }

    public int getSelectedSortType() {
        return selectedSortType;
    }

    public void setSelectedSortType(int selectedSortType) {
        this.selectedSortType = selectedSortType;
        setUserSettingsValue("main.window.sort.type", "" + selectedSortType);
    }

    public int getSelectedSortOrder() {
        return selectedSortOrder;
    }

    public void setSelectedSortOrder(int selectedSortOrder) {
        this.selectedSortOrder = selectedSortOrder;
        setUserSettingsValue("main.window.sort.order", "" + selectedSortOrder);
        for (MyComparator c : comparators) {
            c.setOrder(selectedSortOrder);
        }
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public void init() {
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

        userSettings = new Properties();
        if (new java.io.File(USER_SETTINGS_PROPERTIES_PATH).exists()) {
            try {
                userSettings.load(new FileInputStream(USER_SETTINGS_PROPERTIES_PATH));
            } catch (IOException e) {
                DefaultExceptionHandler.handleException(e);
            }
        }
        selectedPeriodType = Integer.parseInt(getUserSettingsValue("main.window.period.type",
                "" + selectedPeriodType));
        selectedPeriod = getUserSettingsValue("main.window.period", selectedPeriod);
        selectedSortType = Integer.parseInt(getUserSettingsValue("main.window.sort.type",
                "" + selectedSortType));
        selectedSortOrder = Integer.parseInt(getUserSettingsValue("main.window.sort.order",
                "" + selectedSortOrder));

        comparators = new MyComparator[]{
                new DateComparator(selectedSortOrder),
                new TitlesComparator(selectedSortOrder)
        };
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
        return template.queryForObject("SELECT id from " + table + " WHERE title LIKE '" + value + "';", Integer.class);
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

        String period = "";

        List<Note> result = new ArrayList<>();

        if (selectedPeriodType != PeriodType.ALL && !("".equals(selectedPeriod))) {
            String[] parts = selectedPeriod.split("\\D");
            if (parts.length >= 1) {
                period = "WHERE year LIKE '" + parts[0] + "' ";
            }

            if (parts.length >= 2) {
                period += "AND month LIKE '" + parts[1] + "' ";
            }

            if (parts.length == 3) {
                period += "AND day LIKE '" + parts[2] + "' ";
            }
        }

        try {
            result = template.query("SELECT * FROM (product INNER JOIN shops ON product.shopID = shops.id) " +
                    "INNER JOIN item_types ON product.typeID=item_types.id " + period + ";", new RowMapper<Note>() {
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

        Collections.sort(result, comparators[selectedSortType]);

        if (!"".equals(searchQuery)) {
            List<Note> temp = new ArrayList<>();
            String sq = searchQuery.toLowerCase();

            for (Note n : result) {
                if (n.getItem().toLowerCase().contains(sq)) {
                    temp.add(n);
                }
            }

            result = temp;
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

    public String readTextFromFile(String fileName) {
        String result = "";

        try (BufferedReader reader
                     = new BufferedReader(
                new InputStreamReader(
                        getClass().getResourceAsStream(TEXT_FILES_PREFIX + fileName)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result += line + "\n";
            }
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(ex);
        }
        return result;
    }

    public ImageIcon getImage(String fileName) {
        ImageIcon image = null;

        try {
            image = new ImageIcon(getClass().getResource(IMAGES_FILES_PREFIX + fileName + IMAGES_FILES_EXT));
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(ex);
        }
        return image;
    }

    public Set<String> getPeriods() {
        switch (selectedPeriodType) {
            case PeriodType.YEAR:
                return getYears();
            case PeriodType.MONTH:
                return getMonths();
            case PeriodType.DAY:
                return getDays();
        }

        return new TreeSet<>();
    }

    private Set<String> getYears() {
        List<String> temp = new ArrayList<>();

        try {
            temp = template.query("SELECT year FROM product;", new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("year");
                }
            });
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(ex);
        }


        return new TreeSet<>(temp).descendingSet();
    }

    private Set<String> getMonths() {
        List<String> temp = new ArrayList<>();

        try {
            temp = template.query("SELECT month, year FROM product;", new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("year") + "." + resultSet.getString("month");
                }
            });
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(ex);
        }
        return new TreeSet<>(temp).descendingSet();
    }

    private Set<String> getDays() {
        List<String> temp = new ArrayList<>();

        try {
            temp = template.query("SELECT day, month, year FROM product;", new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("year") + "." + resultSet.getString("month")
                            + "." + resultSet.getString("day");
                }
            });
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(ex);
        }
        return new TreeSet<>(temp).descendingSet();
    }

    private String getUserSettingsValue(String key, String defaultValue) {
        return userSettings.getProperty(key, defaultValue);
    }

    private void setUserSettingsValue(String key, String value) {
        userSettings.setProperty(key, value);
        saveUserSettings();
    }

    private void saveUserSettings() {
        try {
            userSettings.store(new FileOutputStream(USER_SETTINGS_PROPERTIES_PATH), "");
        } catch (IOException e) {
            DefaultExceptionHandler.handleException(e);
        }
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

    @SuppressWarnings("WeakerAccess")
    public static class PeriodType {
        public static final int ALL = 0;
        public static final int YEAR = 1;
        public static final int MONTH = 2;
        public static final int DAY = 3;
    }

    @SuppressWarnings("WeakerAccess")
    public static class SortType {
        public static final int DATE = 0;
        public static final int ITEM = 1;
        public static final int TYPE = 2;
        public static final int PRICE = 3;
        public static final int SHOP = 4;
        public static final int BY_SALE = 5;
    }

    public static class Quality {
        public static final int UNDEFINED = 0;
        public static final int HIGH = 1;
        public static final int MEDIUM = 2;
        public static final int LOW = 3;
    }

    public static class Necessity {
        public static final int HIGH = 0;
        public static final int LOW = 1;
    }
}

