package com.wirusmx.mybudget.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Piunov M (aka WirusMX)
 */
public class NoteTest extends Assert {
    @Test
    public void getDateTest1() {
        Note note = new Note(0,
                "TestItem",
                new SimpleData(0, "TestType"),
                123.45f,
                1f,
                new SimpleData(0, "шт."),
                new SimpleData(0, "TestShop"),
                new SimpleData(Note.Necessity.HIGH, ""),
                new SimpleData(Note.Quality.UNDEFINED, ""),
                false,
                "1",
                "2",
                "1234");

        assertEquals("01.02.1234", note.getDate());
    }

    @Test
    public void getDateTest2() {
        Note note = new Note(0,
                "TestItem",
                new SimpleData(0, "TestType"),
                123.45f,
                1f,
                new SimpleData(0, "шт."),
                new SimpleData(0, "TestShop"),
                new SimpleData(Note.Necessity.HIGH, ""),
                new SimpleData(Note.Quality.UNDEFINED, ""),
                false,
                "01",
                "02",
                "1234");

        assertEquals("01.02.1234", note.getDate());
    }

    @Test
    public void getCountAsStringTest1(){
        Note note = new Note(0,
                "TestItem",
                new SimpleData(0, "TestType"),
                123.45f,
                1f,
                new SimpleData(0, "шт."),
                new SimpleData(0, "TestShop"),
                new SimpleData(Note.Necessity.HIGH, ""),
                new SimpleData(Note.Quality.UNDEFINED, ""),
                false,
                "01",
                "02",
                "1234");
        assertEquals("1.000", note.getCountAsString());
    }

    @Test
    public void getCountAsStringTest2(){
        Note note = new Note(0,
                "TestItem",
                new SimpleData(0, "TestType"),
                123.45f,
                1.1f,
                new SimpleData(0, "шт."),
                new SimpleData(0, "TestShop"),
                new SimpleData(Note.Necessity.HIGH, ""),
                new SimpleData(Note.Quality.UNDEFINED, ""),
                false,
                "01",
                "02",
                "1234");
        assertEquals("1.100", note.getCountAsString());
    }

    @Test
    public void getCountAsStringTest3(){
        Note note = new Note(0,
                "TestItem",
                new SimpleData(0, "TestType"),
                123.45f,
                1.1111f,
                new SimpleData(0, "шт."),
                new SimpleData(0, "TestShop"),
                new SimpleData(Note.Necessity.HIGH, ""),
                new SimpleData(Note.Quality.UNDEFINED, ""),
                false,
                "01",
                "02",
                "1234");
        assertEquals("1.111", note.getCountAsString());
    }

    @Test
    public void toStringTest1() {
        Note note = new Note(0,
                "TestItem",
                new SimpleData(0, "TestType"),
                123.45f,
                1f,
                new SimpleData(0, "шт."),
                new SimpleData(0, "TestShop"),
                new SimpleData(Note.Necessity.HIGH, ""),
                new SimpleData(Note.Quality.UNDEFINED, ""),
                false,
                "1",
                "2",
                "1234");

        assertEquals("TestItem (TestShop; 123.45 руб.; 01.02.1234)  ", note.toString());
    }

    @Test
    public void toStringTest2() {
        Note note = new Note(0,
                "TestItem",
                new SimpleData(0, "TestType"),
                123.45f,
                2f,
                new SimpleData(0, "шт."),
                new SimpleData(0, "TestShop"),
                new SimpleData(Note.Necessity.HIGH, ""),
                new SimpleData(Note.Quality.UNDEFINED, ""),
                false,
                "1",
                "2",
                "1234");

        assertEquals("TestItem (TestShop; 246.90 руб.(123.45 руб./шт. ); 01.02.1234)  ", note.toString());
    }

    @Test
    public void toStringTest3() {
        Note note = new Note(0,
                "TestItem",
                new SimpleData(0, "TestType"),
                123.45f,
                1f,
                new SimpleData(0, "шт."),
                new SimpleData(0, "TestShop"),
                new SimpleData(Note.Necessity.HIGH, ""),
                new SimpleData(Note.Quality.UNDEFINED, ""),
                true,
                "1",
                "2",
                "1234");

        assertEquals("TestItem (TestShop; 123.45 руб.(со скидкой); 01.02.1234)  ", note.toString());
    }

    @Test
    public void toStringTest4() {
        Note note = new Note(0,
                "TestItem",
                new SimpleData(0, "TestType"),
                123.45f,
                2f,
                new SimpleData(0, "шт."),
                new SimpleData(0, "TestShop"),
                new SimpleData(Note.Necessity.HIGH, ""),
                new SimpleData(Note.Quality.UNDEFINED, ""),
                true,
                "1",
                "2",
                "1234");

        assertEquals("TestItem (TestShop; 246.90 руб.(123.45 руб./шт. со скидкой); 01.02.1234)  ", note.toString());
    }

    @Test
    public void toStringTest5() {
        Note note = new Note(0,
                "TestItem",
                new SimpleData(0, "TestType"),
                123.45f,
                2f,
                new SimpleData(0, "шт."),
                new SimpleData(0, "TestShop"),
                new SimpleData(Note.Necessity.HIGH, ""),
                new SimpleData(Note.Quality.MEDIUM, ""),
                true,
                "1",
                "2",
                "1234");

        assertEquals("TestItem (TestShop; 246.90 руб.(123.45 руб./шт. со скидкой); 01.02.1234)  ★★", note.toString());
    }
}
