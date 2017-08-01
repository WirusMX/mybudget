package com.wirusmx.mybudget.model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Note {
    public static final String PRICE_FORMAT = "%.2f";
    public static final String COUNT_FORMAT = "%.3f";

    private final int id;
    private String item;
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

    Note(int id, String item, SimpleData type, float price, float count, SimpleData countType,
         SimpleData shop, SimpleData necessity, SimpleData quality, boolean bySale,
         String day, String month, String year) {
        this.id = id;
        this.item = item;
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

    int getId() {
        return id;
    }

    public String getItem() {
        return item;
    }

    public SimpleData getType() {
        return type;
    }

    public float getPrice() {
        return price;
    }

    public String getPriceAsString() {
        return String.format(Locale.ENGLISH, PRICE_FORMAT, price);
    }

    public float getCount() {
        return count;
    }

    public String getCountAsString() {
        return String.format(Locale.ENGLISH, COUNT_FORMAT, count);
    }

    public SimpleData getCountType(){
        return countType;
    }

    /**
     * @return total value, which equals <code>Note.price * Note.count</code>.
     */
    public float getTotal() {
        return price * count;
    }

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

    public String getDate() {
        return day + "." + month + "." + year;
    }

    public void update(String item, SimpleData type, float price, float count, SimpleData countType,
                       SimpleData shop, SimpleData necessity, SimpleData quality, boolean bySale,
                       String day, String month, String year) {
        this.item = item;
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

    @Override
    public String toString() {
        String total = getTotalAsString() + " руб.";
        if (getCount() != 1f || isBySale()){
            total += "(";

            if (getCount() != 1f){
                total += getPriceAsString() + " руб./" + getCountType() + " ";
            }

            if (isBySale()){
                total += "со скидкой";
            }

            total += ")";
        }

        String result = item + " (" + shop.getTitle() + "; " + total;

        result += "; " + getDate() + ")";

        result += "  " + getQualityInStars();

        return result;
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
