package com.pragarawal.marsrover.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class PhotoServiceTest {

    @Test
    void testFormatDate() throws DateParseException {
        Assertions.assertEquals("2017-02-27", PhotoService.formatDate("02/27/17"));
        Assertions.assertEquals("2018-06-02", PhotoService.formatDate("June 2, 2018"));
        Assertions.assertEquals("2016-07-13", PhotoService.formatDate("Jul-13-2016"));
        Assertions.assertEquals("2018-05-01", PhotoService.formatDate("April 31, 2018"));

        Assertions.assertThrows(DateParseException.class, () -> PhotoService.formatDate("02 26 2009"));
        Assertions.assertThrows(DateParseException.class, () -> PhotoService.formatDate("February 26 2009"));
        Assertions.assertThrows(DateParseException.class, () -> PhotoService.formatDate("Thursday July 13th, 2016"));
        Assertions.assertThrows(DateParseException.class, () -> PhotoService.formatDate("blah"));
    }

    @Test
    void testReadDatesFromFile() throws IOException {
        String[] dates = PhotoService.readDatesFromFile("dates.txt");

        Assertions.assertEquals(4, dates.length);
        if (dates.length == 4) {
            Assertions.assertEquals("02/27/17", dates[0]);
            Assertions.assertEquals("June 2, 2018", dates[1]);
            Assertions.assertEquals("Jul-13-2016", dates[2]);
            Assertions.assertEquals("April 31, 2018", dates[3]);
        }
    }

}
