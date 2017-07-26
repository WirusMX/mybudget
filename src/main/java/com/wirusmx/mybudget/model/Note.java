package com.wirusmx.mybudget.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Note {
    private final int id;
    private String item;
    private SimpleData type;
    private int price;
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
                0,
                new SimpleData(-2, ""),
                new SimpleData(0, ""),
                new SimpleData(0, ""),
                false,
                "" + new GregorianCalendar().get(Calendar.DAY_OF_MONTH),
                "" + (new GregorianCalendar().get(Calendar.MONTH) + 1),
                "" + new GregorianCalendar().get(Calendar.YEAR)
        );

    }

    Note(int id, String item, SimpleData type, int price,
         SimpleData shop, SimpleData necessity, SimpleData quality, boolean bySale,
         String day, String month, String year) {
        this.id = id;
        this.item = item;
        this.type = type;
        this.price = price;
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

    public int getPrice() {
        return price;
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

    public void update(String item, SimpleData type, int price,
                       SimpleData shop, SimpleData necessity, SimpleData quality, boolean bySale,
                       String day, String month, String year) {
        this.item = item;
        this.type = type;
        this.price = price;
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
        String result = item + " (" + shop.getTitle() + ", " + price + " руб., " + day + "." + month + "." + year + ")";
        if (bySale) {
            result += " со скидкой";
        }

        if (quality.getId() != Model.Quality.UNDEFINED) {
            result += "  ";
            for (int i = 0; i < 4 - quality.getId(); i++) {
                result += "★";
            }
        }

        return result;
    }


}
