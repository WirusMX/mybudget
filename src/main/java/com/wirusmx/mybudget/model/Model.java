package com.wirusmx.mybudget.model;

import com.wirusmx.mybudget.DefaultExceptionHandler;
import com.wirusmx.mybudget.controller.Controller;
import com.wirusmx.mybudget.model.comparators.*;
import com.wirusmx.mybudget.model.exceptions.UndefinedDatabaseVersionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.swing.*;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Piunov M (aka WirusMX)
 */
public class Model {
    private static final String USER_SETTINGS_PROPERTIES_PATH = "conf/user_settings.properties";
    private static final String TEXT_FILES_PREFIX = "/text/";
    private static final String IMAGES_FILES_PREFIX = "/images/";
    private static final String IMAGES_FILES_EXT = ".png";

    private Controller controller;
    private JdbcTemplate template;
    private String applicationVersion;

    private Properties userSettings;

    private MyComparator[] comparators;

    private int selectedPeriodType = PeriodType.ALL;
    private String selectedPeriod = "";
    private int selectedSortType = SortType.DATE;
    private int selectedSortOrder = MyComparator.REVERSE_ORDER;
    private String searchQuery = "";
    private int dataViewID = 0;

    Model(Controller controller, JdbcTemplate template, String applicationVersion) {
        this.controller = controller;
        this.template = template;
        this.applicationVersion = applicationVersion;
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
            c.setSortOrder(selectedSortOrder);
        }
    }

    public int getDataViewID() {
        return dataViewID;
    }

    public void setDataViewID(int dataViewID) {
        this.dataViewID = dataViewID;
        setUserSettingsValue("main.window.data.view.id", "" + dataViewID);
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    /**
     * Method initializes and inspects database, loads user settings from *.properties file,
     * initializes comparators, which are used for sorting Notes. <br>
     * <br>
     * It is recommended to use <code>init(false) in test scope.
     *
     * @see MyComparator
     * @see #initDataBase()
     * @see #inspectDatabase()
     */
    public void init() {
        init(true);
    }

    /**
     * Method initializes and inspects database, loads user settings from *.properties file
     * or sets default values (depends on loadUserSettings parameter),
     * initializes comparators, which are used for sorting Notes. <br>
     * It is recommended to use this method with <code>loadUserSettings = false</code>
     * in test scope.
     *
     * @param loadUserSettings if parameter is <code>true</code>,then user settings
     *                         will be loaded from *.properties file,
     *                         otherwise user settings will have default values.
     * @see MyComparator
     * @see #initDataBase()
     * @see #inspectDatabase()
     */
    void init(boolean loadUserSettings) {
        initDataBase();

        inspectDatabase();

        if (loadUserSettings) {
            loadUserSettings();
        }

        comparators = new MyComparator[]{
                new DateComparator(selectedSortOrder),
                new TitlesComparator(selectedSortOrder),
                new ItemTypeComparator(selectedSortOrder),
                new PriceComparator(selectedSortOrder),
                new ShopComparator(selectedSortOrder),
                new SaleComparator(selectedSortOrder)
        };
    }

    /**
     * Extracts pares of (id, title) from <code>table</code>.
     *
     * @param tableName name of table which contains values for ComboBox.
     * @return ComboBox values set with the default order.
     * @see ComboBoxValuesComparator
     */
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

    /**
     * Insert new ComboBox value to database.
     *
     * @param value - value for inserting;
     * @param table - table for inserting.
     * @return id of inserted value in database table,
     * or -1 if any problems occurred during insertion operation.
     */
    public int insertNewComboBoxValue(String value, String table) {
        int result = -1;
        try {
            template.execute("INSERT OR REPLACE INTO " + table + " (title) VALUES ('" + value + "');");
            result = template.queryForObject("SELECT id from " + table + " WHERE title LIKE '" + value + "';", Integer.class);
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(controller, ex, "Ошибка базы данных!");
        }

        return result;
    }

    /**
     * Insert new Note to database.
     *
     * @param note Note for insertion.
     * @throws RuntimeException if Note.itemTitle is empty.
     */
    public void insertNote(Note note) {
        try {
            if (note.getItemTitle().isEmpty()) {
                throw new RuntimeException("Empty item title");
            }

            template.execute("INSERT INTO product (itemTitle, typeID, price, count, countTypeID, shopID, " +
                    "necessityID, qualityID, bySale, day, month, year) " +
                    "VALUES ("
                    + "'" + note.getItemTitle() + "', "
                    + note.getType().getId() + ", "
                    + note.getPrice() + ", "
                    + note.getCount() + ", "
                    + note.getCountType().getId() + ", "
                    + note.getShop().getId() + ", "
                    + note.getNecessity().getId() + ", "
                    + note.getQuality().getId() + ", "
                    + (note.isBySale() ? 1 : 0) + ", "
                    + "'" + note.getDay() + "', "
                    + "'" + note.getMonth() + "', "
                    + "'" + note.getYear() + "');");
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(controller, ex, "Ошибка базы данных!");
        }
    }

    /**
     * Extracts Notes from database which matches the specified period.
     *
     * @param selectedPeriodType type of period;
     * @param selectedPeriod     sample period.
     * @return List of Notes which matches the specified period, without
     * sorting.
     */
    public List<Note> getNotes(int selectedPeriodType, String selectedPeriod) {
        return getNotes(selectedPeriodType, selectedPeriod, SortType.UNDEFINED, "");
    }

    /**
     * Extracts Notes from database which matches the previously
     * specified period and search query.
     *
     * @return List of Notes which matches the specified period and
     * search query, sorted by previously specified way.
     */
    public List<Note> getNotes() {
        return getNotes(selectedPeriodType, selectedPeriod, selectedSortType, searchQuery);
    }

    /**
     * Updates Note in database.
     *
     * @param note Note for updating.
     */
    public void updateNote(Note note) {
        try {
            template.execute("ERT OR REPLACE INTO product (id, itemTitle, typeID, price, count, countTypeID, shopID, " +
                    "necessityID, qualityID, bySale, day, month, year) " +
                    "VALUES ("
                    + note.getId() + ", "
                    + "'" + note.getItemTitle() + "', "
                    + note.getType().getId() + ", "
                    + note.getPrice() + ", "
                    + note.getCount() + ", "
                    + note.getCountType().getId() + ", "
                    + note.getShop().getId() + ", "
                    + note.getNecessity().getId() + ", "
                    + note.getQuality().getId() + ", "
                    + (note.isBySale() ? 1 : 0) + ", "
                    + "'" + note.getDay() + "', "
                    + "'" + note.getMonth() + "', "
                    + "'" + note.getYear() + "');");
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(controller, ex, "Ошибка базы данных!");
        }
    }

    /**
     * Removes Note from database.
     *
     * @param note Note for removing.
     */
    public void removeNote(Note note) {
        try {
            template.execute("DELETE FROM product WHERE id=" + note.getId() + ";");
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(controller, ex, "Ошибка базы данных!");
        }
    }

    /**
     * Reads UTF-8 text from the resource file.
     *
     * @param fileName resource file.
     * @return text from the resource file.
     */
    public String readTextFromResourceFile(String fileName) {
        String result = "";

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        getClass().getResourceAsStream(TEXT_FILES_PREFIX + fileName), "UTF-8"))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                result += line + "\n";
            }
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(ex);
        }
        return result;
    }

    /**
     * Loads resource image in PNG format.
     *
     * @param fileName image file name.
     * @return image or <code>null</code> if any problems happens.
     */
    public ImageIcon getResourceImage(String fileName) {
        ImageIcon image = null;

        try {
            image = new ImageIcon(getClass().getResource(IMAGES_FILES_PREFIX + fileName + IMAGES_FILES_EXT));
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(ex);
        }
        return image;
    }

    /**
     * Extracts periods of previously specified type from data base.
     *
     * @return set of periods.
     * @see #getYears()
     * @see #getMonths()
     * @see #getDays()
     */
    public Set<String> getPeriods() {
        return getPeriods(selectedPeriodType);
    }

    /**
     * Extracts periods of specified type from data base.
     *
     * @param selectedPeriodType type of period.
     * @return set of periods.
     * @see #getYears()
     * @see #getMonths()
     * @see #getDays()
     */
    public Set<String> getPeriods(int selectedPeriodType) {
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

    /**
     * Converts string value to true numeric format string.
     *
     * @param type                type of resulted value. Must
     *                            be {@link Integer} or {@link Float};
     * @param value               string value for converting;
     * @param decimalNumbersCount count of numbers after decimal point.
     * @return converted value or empty string if type not allowed.
     * @see #stringToIntegerFormat(String)
     * @see #stringToFloatFormat(String, int)
     */
    public String stringToNumericFormat(Class type, String value, int decimalNumbersCount) {
        if (type.equals(Float.class)) {
            return stringToFloatFormat(value, decimalNumbersCount);
        }

        if (type.equals(Integer.class)) {
            return stringToIntegerFormat(value);
        }

        return "";
    }

    /**
     * Creates database tables and inserts default values.
     *
     * @throws UndefinedDatabaseVersionException if method is unable
     *                                           to define database version;
     * @throws RuntimeException                  if any problems happens.
     */
    private void initDataBase() {
        try {
            template.execute("CREATE TABLE IF NOT EXISTS application (id INTEGER PRIMARY KEY AUTOINCREMENT, version TEXT);");

            try {
                template.execute("INSERT OR REPLACE INTO application (id, version) VALUES (0, '" + applicationVersion + "');");
            } catch (Exception ex) {
                int count = template.queryForObject("SELECT COUNT(id) from application", Integer.class);
                if (count == 1) {
                    String version = template.queryForObject("SELECT version from application", String.class);
                    if (!applicationVersion.equals(version)) {
                        updateDataBaseVersion();
                    }
                } else {
                    throw new UndefinedDatabaseVersionException("Unable to define database version");
                }
            }

            template.execute("CREATE TABLE IF NOT EXISTS product (id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    "itemTitle TEXT, typeID INTEGER, price REAL, count REAL, countTypeID INTEGER, shopID INTEGER, " +
                    "necessityID INTEGER, qualityID INTEGER, bySale INTEGER, day TEXT," +
                    "month TEXT, year TEXT);");

            template.execute("CREATE TABLE IF NOT EXISTS item_types (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "title TEXT);");
            template.execute("INSERT OR REPLACE INTO item_types (id, title) VALUES (0, 'Прочее');");

            template.execute("CREATE TABLE IF NOT EXISTS count_types (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "title TEXT);");
            template.execute("INSERT OR REPLACE INTO count_types (id, title) VALUES (0, 'шт.');");
            template.execute("INSERT OR REPLACE INTO count_types (id, title) VALUES (1, 'кг.');");
            template.execute("INSERT OR REPLACE INTO count_types (id, title) VALUES (2, 'л.');");

            template.execute("CREATE TABLE IF NOT EXISTS shops (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "title TEXT);");
            template.execute("INSERT OR REPLACE INTO shops (id, title) VALUES (0, 'Прочее');");
        } catch (Exception ex) {
            throw new RuntimeException("Database exception", ex);
        }

    }

    private void updateDataBaseVersion() {

    }

    /**
     * Removes rows from database tables, which contains unused or empty values.
     */
    private void inspectDatabase() {
        try {
            template.execute("DELETE FROM product WHERE itemTitle LIKE '';");
            template.execute("DELETE FROM shops WHERE id NOT IN (SELECT shopID FROM product) AND id > 0;");
            template.execute("DELETE FROM item_types WHERE id NOT IN (SELECT typeID FROM product) AND id > 0;");
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(ex);
        }
    }

    /**
     * Loads user settings from *.properties file.
     */
    private void loadUserSettings() {
        userSettings = new Properties();
        if (new File(USER_SETTINGS_PROPERTIES_PATH).exists()) {
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

        dataViewID = Integer.parseInt(getUserSettingsValue("main.window.data.view.id",
                "" + dataViewID));
    }

    private String stringToFloatFormat(String text, int cutNum) {
        if (!text.matches("\\d+")) {
            text = text.replaceAll(",", ".");
            text = text.replaceAll("[^0-9.]", "");
            while (text.split("\\.").length > 2) {
                text = text.replaceFirst("\\.", "");
            }

            int pos = text.indexOf('.');
            if (pos >= 0 && text.length() - pos > cutNum + 1) {
                text = text.substring(0, pos + cutNum + 1);
            }

            if (pos == text.length() - 1 && text.length() - 1 > 0) {
                text = text.substring(0, pos);
            }

            if (pos == 0 && text.length() > 1) {
                text = "0" + text;
            }

            if (pos == 0 && text.length() - 1 == 0) {
                text = "";
            }
        }

        return text;
    }

    private String stringToIntegerFormat(String text) {
        if (!text.matches("\\d+")) {
            text = text.replaceAll("\\D", "");
        }

        return text;
    }

    @SuppressWarnings("unchecked")
    List<Note> getNotes(int selectedPeriodType, String selectedPeriod, int selectedSortType, String searchQuery) {
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
            result = template.query("SELECT * FROM ((product INNER JOIN shops ON product.shopID = shops.id) " +
                    "INNER JOIN item_types ON product.typeID=item_types.id) " +
                    "INNER JOIN count_types ON product.countTypeID=count_types.id " + period + ";", new RowMapper<Note>() {
                @Override
                public Note mapRow(ResultSet resultSet, int i) throws SQLException {
                    return new Note(resultSet.getInt("id"),
                            resultSet.getString("itemTitle"),
                            new SimpleData(resultSet.getInt("typeID"), resultSet.getString(17)),
                            resultSet.getFloat("price"),
                            resultSet.getFloat("count"),
                            new SimpleData(resultSet.getInt("countTypeID"), resultSet.getString(19)),
                            new SimpleData(resultSet.getInt("shopID"), resultSet.getString(15)),
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
            DefaultExceptionHandler.handleException(controller, ex, "Ошибка базы данных!");
        }

        if (selectedSortType >= 0 && selectedSortType < comparators.length) {
            Collections.sort(result, comparators[selectedSortType]);
        }

        if (!"".equals(searchQuery)) {
            List<Note> temp = new ArrayList<>();
            String sq = searchQuery.toLowerCase();

            for (Note n : result) {
                if (n.getItemTitle().toLowerCase().contains(sq)) {
                    temp.add(n);
                }
            }

            result = temp;
        }

        return result;
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

    public Set<String> getItemsSet() {
        List<String> result = new ArrayList<>();
        try {
            result = template.query("SELECT itemTitle FROM product;", new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("itemTitle");
                }
            });
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(ex);
        }

        return new TreeSet<>(result);
    }

    /**
     * Compare two {@link SimpleData} values. Value with <code>id = 0</code>
     * is less then others. If values <code>id != 0</code>, they compare by title.
     */
    private class ComboBoxValuesComparator implements Comparator<SimpleData> {
        @Override
        public int compare(SimpleData o1, SimpleData o2) {
            if (o1.getId() == 0) {
                return -1;
            }

            if (o2.getId() == 0) {
                return 1;
            }

            return o1.toString().toLowerCase().compareTo(o2.toString().toLowerCase());
        }
    }

    public static class PeriodType {
        public static final int ALL = 0;
        public static final int YEAR = 1;
        public static final int MONTH = 2;
        public static final int DAY = 3;
    }

    public static class SortType {
        public static final int UNDEFINED = -1;
        public static final int DATE = 0;
        public static final int ITEM = 1;
        public static final int TYPE = 2;
        public static final int PRICE = 3;
        public static final int SHOP = 4;
        public static final int BY_SALE = 5;
    }

}

