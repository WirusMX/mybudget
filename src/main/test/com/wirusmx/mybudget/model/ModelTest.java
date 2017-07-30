package com.wirusmx.mybudget.model;


import org.junit.Assert;
import org.junit.Test;

public class ModelTest extends Assert {
    @Test
    public void stringToNumericFormatTest() {
        Model model = new Model(null, null, null);
        Assert.assertEquals("123.45", model.stringToNumericFormat(Float.class, "123.45"));
        Assert.assertEquals("123.45", model.stringToNumericFormat(Float.class, "123,45"));
        Assert.assertEquals("123.45", model.stringToNumericFormat(Float.class, "-123,45"));
        Assert.assertEquals("123.45", model.stringToNumericFormat(Float.class, "123,4567"));
        Assert.assertEquals("123.45", model.stringToNumericFormat(Float.class, "1,2,3,45"));
        Assert.assertEquals("123.45", model.stringToNumericFormat(Float.class, "1d2d3f,f4f5"));

        Assert.assertEquals("12345", model.stringToNumericFormat(Float.class, "12345."));

        Assert.assertEquals("0.12", model.stringToNumericFormat(Float.class, ".12"));
        Assert.assertEquals("0.12", model.stringToNumericFormat(Float.class, ".1234"));

        Assert.assertEquals("", model.stringToNumericFormat(Float.class, ""));
        Assert.assertEquals("", model.stringToNumericFormat(Float.class, "ddd.ccc"));

    }
}
