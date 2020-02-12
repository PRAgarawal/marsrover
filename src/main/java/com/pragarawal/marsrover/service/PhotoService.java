package com.pragarawal.marsrover.service;

import com.pragarawal.marsrover.model.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PhotoService {

    private static final String dateFormats[] = {
            "MM/dd/yy",
            "MMM dd, yyyy",
            "MMM-dd-yyyy",
    };

    @Autowired
    public PhotoService() {
    }

    public Photo getPhotoByDate(String date) throws DateParseException {
        return new Photo(normalizeDate(date), "test");
    }

    public static String normalizeDate(String dateAsString) throws DateParseException {
        Date date = parseDateWithFormatterIndex(dateAsString, 0);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    private static Date parseDateWithFormatterIndex(String dateAsString, int index) throws DateParseException {
        if (index >= dateFormats.length) {
            throw new DateParseException();
        }
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormats[index]);
        Date date;

        try {
            date = formatter.parse(dateAsString);
            System.out.println(date);
            System.out.println(formatter.format(date));
        } catch (ParseException e) {
            return parseDateWithFormatterIndex(dateAsString, index+1);
        }

        return date;
    }

}
