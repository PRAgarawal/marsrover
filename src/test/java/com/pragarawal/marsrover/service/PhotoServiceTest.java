package com.pragarawal.marsrover.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PhotoServiceTest {

    @Test
    void testNormalizeDate() throws DateParseException {
        Assertions.assertEquals("2017-02-27", PhotoService.normalizeDate("02/27/17"));
        Assertions.assertEquals("2018-06-02", PhotoService.normalizeDate("June 2, 2018"));
        Assertions.assertEquals("2016-07-13", PhotoService.normalizeDate("Jul-13-2016"));
        Assertions.assertEquals("2018-05-01", PhotoService.normalizeDate("April 31, 2018"));

        Assertions.assertThrows(DateParseException.class, () -> PhotoService.normalizeDate("02 26 2009"));
        Assertions.assertThrows(DateParseException.class, () -> PhotoService.normalizeDate("February 26 2009"));
        Assertions.assertThrows(DateParseException.class, () -> PhotoService.normalizeDate("Thursday July 13th, 2016"));
        Assertions.assertThrows(DateParseException.class, () -> PhotoService.normalizeDate("blah"));
    }

}
