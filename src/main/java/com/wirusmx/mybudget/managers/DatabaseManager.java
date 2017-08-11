package com.wirusmx.mybudget.managers;

import com.wirusmx.mybudget.DefaultExceptionHandler;
import com.wirusmx.mybudget.exceptions.UndefinedDatabaseVersionException;
import com.wirusmx.mybudget.model.Model;
import com.wirusmx.mybudget.model.Note;
import com.wirusmx.mybudget.model.SimpleData;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Piunov M (aka WirusMX)
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private JdbcTemplate template;
    private final String databaseVersion;

    /**
     * Constructs new instance of <code>DatabaseManager</code>,
     * initializes and inspects database.
     *
     * @see #initDataBase()
     * @see #inspectDatabase()
     */
    private DatabaseManager(String databaseVersion) {
        ApplicationContext context = new FileSystemXmlApplicationContext("conf/spring_config.xml");
        template = (JdbcTemplate) context.getBean("template");

        this.databaseVersion = databaseVersion;

        initDataBase();
        inspectDatabase();
    }

    public static DatabaseManager getInstance() {
        return getInstance("");
    }

    public static DatabaseManager getInstance(String databaseVersion) {
        if (instance == null) {
            instance = new DatabaseManager(databaseVersion);
        }

        return instance;
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
                template.execute("INSERT OR REPLACE INTO application (id, version) VALUES (0, '" + databaseVersion + "');");
            } catch (Exception ex) {
                int count = template.queryForObject("SELECT COUNT(id) from application", Integer.class);
                if (count == 1) {
                    String version = template.queryForObject("SELECT version from application", String.class);
                    if (!databaseVersion.equals(version)) {
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
     * Extracts pares of (id, title) from <code>table</code>.
     *
     * @param tableName name of table which contains values for ComboBox.
     * @return ComboBox values.
     */
    public List<SimpleData> getComboBoxValues(String tableName) {

        return template.query("SELECT * FROM " + tableName, new RowMapper<SimpleData>() {
            @Override
            public SimpleData mapRow(ResultSet resultSet, int i) throws SQLException {
                return new SimpleData(resultSet.getInt("id"), resultSet.getString("title"));
            }
        });
    }

    /**
     * Insert new ComboBox value to database.
     *
     * @param value - value for inserting;
     * @param table - table for inserting.
     * @return id of inserted value in database table.
     */
    public int insertNewComboBoxValue(String value, String table) {
        template.execute("INSERT OR REPLACE INTO " + table + " (title) VALUES ('" + value + "');");

        return template.queryForObject("SELECT id from " + table + " WHERE title LIKE '" + value + "';", Integer.class);
    }

    /**
     * Insert new Note to database.
     *
     * @param note Note for insertion.
     * @throws RuntimeException if Note.itemTitle is empty.
     */
    public void insertNote(Note note) {
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
    }

    /**
     * Updates Note in database.
     *
     * @param note Note for updating.
     */
    public void updateNote(Note note) {
        template.execute("INSERT OR REPLACE INTO product (id, itemTitle, typeID, price, count, countTypeID, shopID, " +
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
    }

    public List<Note> getNotes(int selectedPeriodType, String selectedPeriod) {
        String period = "";

        if (selectedPeriodType != Model.PeriodType.ALL && !("".equals(selectedPeriod))) {
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

        return template.query("SELECT * FROM ((product INNER JOIN shops ON product.shopID = shops.id) " +
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
    }

    /**
     * Removes Note from database.
     *
     * @param note Note for removing.
     */
    public void removeNote(Note note) {
        template.execute("DELETE FROM product WHERE id=" + note.getId() + ";");
    }

    public List<String> getYears() {

        return template.query("SELECT year FROM product;", new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("year");
            }
        });
    }

    public List<String> getMonths() {
        return template.query("SELECT month, year FROM product;", new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("year") + "." + resultSet.getString("month");
            }
        });
    }

    public List<String> getDays() {

        return template.query("SELECT day, month, year FROM product;", new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("year") + "." + resultSet.getString("month")
                        + "." + resultSet.getString("day");
            }
        });
    }

    public List<String> getItemsTitles() {

        return template.query("SELECT itemTitle FROM product;", new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("itemTitle");
            }
        });
    }
}
