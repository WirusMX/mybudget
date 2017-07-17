package com.wirusmx.mybudget.model;

public class Note {
    private final int id;
    private String item;
    private SimpleData type;
    private int price;
    private SimpleData shop;
    private SimpleData necessity;
    private SimpleData quality;
    private boolean bySale;
    private long update;

    public Note() {
        this(
                -1,
                "",
                new SimpleData(-2, ""),
                0,
                new SimpleData(-2, ""),
                new SimpleData(0, ""),
                new SimpleData(0, ""),
                false
        );

    }

    public Note(int id, String item, SimpleData type, int price,
                SimpleData shop, SimpleData necessity, SimpleData quality, boolean bySale) {
        this.id = id;
        this.item = item;
        this.type = type;
        this.price = price;
        this.shop = shop;
        this.necessity = necessity;
        this.quality = quality;
        this.bySale = bySale;
        this.update = System.currentTimeMillis();
    }

    public int getId() {
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

    public long getUpdate() {
        return update;
    }

    public void update(String item, SimpleData type, int price,
                       SimpleData shop, SimpleData necessity, SimpleData quality, boolean bySale){
        this.item = item;
        this.type = type;
        this.price = price;
        this.shop = shop;
        this.necessity = necessity;
        this.quality = quality;
        this.bySale = bySale;
        this.update = System.currentTimeMillis();
    }

    public void update(){
        this.update = System.currentTimeMillis();
    }
}
