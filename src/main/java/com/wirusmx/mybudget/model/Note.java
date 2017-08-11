package com.wirusmx.mybudget.model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Note {
    /**
     * Format for showing prices as <code>float</code> number value
     * with two digits after the decimal point.
     */
    public static final String PRICE_FORMAT = "%.2f";

    /**
     * Format for showing count as <code>float</code> number value
     * with three digits after the decimal point.
     */
    public static final String COUNT_FORMAT = "%.3f";

    private final int id;
    private String itemTitle;
    private SimpleData type;
    private float price;
    private float count;
    private SimpleData countType;
    private SimpleData shop;
    private SimpleData necessity;
    private SimpleData quality;
    private boolean bySale;
    private String day;
    private String month;
    private String year;

    /**
     * Constructs new <code>Note</code> with the default values.
     */
    public Note() {
        this(
                -1,
                "",
                new SimpleData(-2, ""),
                0f,
                0f,
                new SimpleData(0, ""),
                new SimpleData(-2, ""),
                new SimpleData(Necessity.HIGH, ""),
                new SimpleData(Quality.UNDEFINED, ""),
                false,
                "" + new GregorianCalendar().get(Calendar.DAY_OF_MONTH),
                "" + (new GregorianCalendar().get(Calendar.MONTH) + 1),
                "" + new GregorianCalendar().get(Calendar.YEAR)
        );

    }

    /**
     * Constructs new <code>Note</code> with the specific values.
     */
    public Note(int id, String itemTitle, SimpleData type, float price, float count, SimpleData countType,
                SimpleData shop, SimpleData necessity, SimpleData quality, boolean bySale,
                String day, String month, String year) {
        this.id = id;
        this.itemTitle = itemTitle;
        this.type = type;
        this.price = price;
        this.count = count;
        this.countType = countType;
        this.shop = shop;
        this.necessity = necessity;
        this.quality = quality;
        this.bySale = bySale;
        this.day = day.length() == 2 ? day : "0" + day;
        this.month = month.length() == 2 ? month : "0" + month;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public SimpleData getType() {
        return type;
    }

    public float getPrice() {
        return price;
    }

    /**
     * @return string representation of the price in specific format.
     * @see #PRICE_FORMAT
     */
    public String getPriceAsString() {
        return String.format(Locale.ENGLISH, PRICE_FORMAT, price);
    }

    public float getCount() {
        return count;
    }

    /**
     * @return string representation of the items count in specific format.
     * @see #COUNT_FORMAT
     */
    public String getCountAsString() {
        return String.format(Locale.ENGLISH, COUNT_FORMAT, count);
    }

    public SimpleData getCountType() {
        return countType;
    }

    /**
     * @return total value, which equals <code>Note.price * Note.count</code>.
     */
    public float getTotal() {
        return price * count;
    }

    /**
     * @return string representation of the total value in specific format.
     * @see #getTotal()
     * @see #PRICE_FORMAT
     */
    public String getTotalAsString() {
        return String.format(Locale.ENGLISH, PRICE_FORMAT, getTotal());
    }

    public SimpleData getShop() {
        return shop;
    }

    public SimpleData getNecessity() {
        return necessity;
    }

    public SimpleData getQuality() {
        return quality;
    }

    /**
     * @return specific string representation of the item quality, where:<br>
     * one star corresponds to Quality.LOW;<br>
     * two - Quality.MEDIUM;<br>
     * three - Quality.HIGH.<br>
     * If quality is Quality.UNDEFINED then returns empty string.<br>
     * @see Note.Quality
     */
    public String getQualityInStars() {
        String result = "";

        if (quality.getId() != Quality.UNDEFINED) {
            for (int i = 0; i < 4 - quality.getId(); i++) {
                result += "★";
            }
        }

        return result;
    }

    public boolean isBySale() {
        return bySale;
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    /**
     * @return date value in DD.MM.YYYY format
     */
    public String getDate() {
        return day + "." + month + "." + year;
    }

    public void update(String item, SimpleData type, float price, float count, SimpleData countType,
                       SimpleData shop, SimpleData necessity, SimpleData quality, boolean bySale,
                       String day, String month, String year) {
        this.itemTitle = item;
        this.type = type;
        this.price = price;
        this.count = count;
        this.countType = countType;
        this.shop = shop;
        this.necessity = necessity;
        this.quality = quality;
        this.bySale = bySale;
        this.day = day.length() == 2 ? day : "0" + day;
        this.month = month.length() == 2 ? month : "0" + month;
        this.year = year;
    }

    /**
     * @return string representation of the <code>Note</code>.
     */
    @Override
    public String toString() {
        String total = getTotalAsString() + " руб.";
        if (getCount() != 1f || isBySale()) {
            total += "(";

            if (getCount() != 1f) {
                total += getPriceAsString() + " руб./" + getCountType() + " ";
            }

            if (isBySale()) {
                total += "со скидкой";
            }

            total += ")";
        }

        String result = itemTitle + " (" + shop + "; " + total;

        result += "; " + getDate() + ")";

        result += "  " + getQualityInStars();

        return result;
    }

    /**
     * Class describes quality of the item.
     */
    public static class Quality {
        public static final int UNDEFINED = 0;
        public static final int HIGH = 1;
        public static final int MEDIUM = 2;
        public static final int LOW = 3;
    }

    /**
     * Class describes level of the item necessity.
     */
    public static class Necessity {
        public static final int HIGH = 0;
        public static final int LOW = 1;
    }
}
