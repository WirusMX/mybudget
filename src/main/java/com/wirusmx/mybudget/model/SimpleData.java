package com.wirusmx.mybudget.model;

public class SimpleData {
    private final int id;
    private final String title;

    public SimpleData(int id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    public int getId() {
        return id;
    }

    String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleData that = (SimpleData) o;

        return id == that.id && (title != null ? title.equals(that.title) : that.title == null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }
}
