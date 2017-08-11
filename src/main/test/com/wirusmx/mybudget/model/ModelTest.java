package com.wirusmx.mybudget.model;


import com.wirusmx.mybudget.model.comparators.MyComparator;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ModelTest extends Assert {
    @Test
    public void getNotesTest(){
        List<Note> list = new ArrayList<>();
        list.add(new Note(0,
                "TestItem1",
                new SimpleData(0, "TestType"),
                123.45f,
                1f,
                new SimpleData(0, "шт."),
                new SimpleData(0, "TestShop"),
                new SimpleData(Note.Necessity.HIGH, ""),
                new SimpleData(Note.Quality.MEDIUM, ""),
                true,
                "01",
                "02",
                "1234")
        );

        list.add(new Note(2,
                "ItemForTest",
                new SimpleData(0, "TestType"),
                123.45f,
                1f,
                new SimpleData(0, "шт."),
                new SimpleData(0, "TestShop"),
                new SimpleData(Note.Necessity.HIGH, ""),
                new SimpleData(Note.Quality.MEDIUM, ""),
                true,
                "01",
                "02",
                "1234")
        );

        list.add(new Note(3,
                "test",
                new SimpleData(0, "TestType"),
                123.45f,
                1f,
                new SimpleData(0, "шт."),
                new SimpleData(0, "TestShop"),
                new SimpleData(Note.Necessity.HIGH, ""),
                new SimpleData(Note.Quality.MEDIUM, ""),
                true,
                "01",
                "02",
                "1234")
        );

        list.add(new Note(4,
                "test_item",
                new SimpleData(0, "TestType"),
                123.45f,
                1f,
                new SimpleData(0, "шт."),
                new SimpleData(0, "TestShop"),
                new SimpleData(Note.Necessity.HIGH, ""),
                new SimpleData(Note.Quality.MEDIUM, ""),
                true,
                "01",
                "02",
                "1234")
        );

        JdbcTemplate template = mock(JdbcTemplate.class);
        when(template.query(anyString(), any(RowMapper.class))).thenReturn(list);

        Model model = new Model(null, null, null);
        model.init(false);
        model.setSelectedSortOrder(MyComparator.DIRECT_ORDER);
        List<Note> notes = model.getNotes(Model.PeriodType.DAY, "1234.02.01", Model.SortType.ITEM, "item");

        assertEquals(3, notes.size());

        assertEquals("ItemForTest", notes.get(0).getItemTitle());
        assertEquals("test_item", notes.get(1).getItemTitle());
        assertEquals("TestItem1", notes.get(2).getItemTitle());
    }

    @Test
    public void getComboBoxValuesTest(){
        List<SimpleData> list = new ArrayList<>();
        list.add(new SimpleData(0, "Other"));
        list.add(new SimpleData(1, "zzzzz"));
        list.add(new SimpleData(2, "aaaaa"));
        list.add(new SimpleData(2, "aaaaa"));
        list.add(new SimpleData(3, "fffff"));

        JdbcTemplate template = mock(JdbcTemplate.class);
        when(template.query(anyString(), any(RowMapper.class))).thenReturn(list);

        Model model = new Model(null, null, null);
        Set<SimpleData> comboBoxValues = model.getComboBoxValues("");

        assertEquals(4, comboBoxValues.size());

        Iterator<SimpleData> iterator = comboBoxValues.iterator();
        assertEquals("Other", iterator.next().toString());
        assertEquals("aaaaa", iterator.next().toString());
        assertEquals("fffff", iterator.next().toString());
        assertEquals("zzzzz", iterator.next().toString());
    }

    @Test
    public void stringToNumericFormatTest() {
        /*
        Model model = new Model(null, null, null);
        Assert.assertEquals("123.45", model.stringToNumericFormat(Float.class, "123.45", 2));
        Assert.assertEquals("123.456", model.stringToNumericFormat(Float.class, "123.456", 3));
        Assert.assertEquals("123.45", model.stringToNumericFormat(Float.class, "123,45", 2));
        Assert.assertEquals("123.45", model.stringToNumericFormat(Float.class, "-123,45", 2));
        Assert.assertEquals("123.45", model.stringToNumericFormat(Float.class, "123,4567", 2));
        Assert.assertEquals("123.45", model.stringToNumericFormat(Float.class, "1,2,3,45", 2));
        Assert.assertEquals("123.45", model.stringToNumericFormat(Float.class, "1d2d3f,f4f5", 2));

        Assert.assertEquals("12345", model.stringToNumericFormat(Float.class, "12345.", 2));

        Assert.assertEquals("0.12", model.stringToNumericFormat(Float.class, ".12", 2));
        Assert.assertEquals("0.12", model.stringToNumericFormat(Float.class, ".1234", 2));

        Assert.assertEquals("", model.stringToNumericFormat(Float.class, "", 2));
        Assert.assertEquals("", model.stringToNumericFormat(Float.class, "ddd.ccc", 2));

        Assert.assertEquals("12345", model.stringToNumericFormat(Integer.class, "123.45", 0));
        Assert.assertEquals("12345", model.stringToNumericFormat(Integer.class, "-12345", 0));
        Assert.assertEquals("12345", model.stringToNumericFormat(Integer.class, "12 34 b5", 0));
        */
    }

}