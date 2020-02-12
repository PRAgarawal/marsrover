package com.pragarawal.marsrover.service;

import com.pragarawal.marsrover.model.Photo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PhotoService {

    final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final String DATE_FORMATS[] = {
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

    public void downloadPhotosFromFile() {
        try {
            String[] dates = readDatesFromFile();
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }
    }

    public String[] readDatesFromFile() throws IOException {
        Resource resource = new ClassPathResource("dates.txt");
        InputStream inputStream = resource.getInputStream();
        byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
        String data = new String(bdata, StandardCharsets.UTF_8);

        return data.split("\n");
    }

    public static String normalizeDate(String dateAsString) throws DateParseException {
        Date date = parseDateWithFormatterIndex(dateAsString, 0);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    private static Date parseDateWithFormatterIndex(String dateAsString, int index) throws DateParseException {
        if (index >= DATE_FORMATS.length) {
            throw new DateParseException();
        }
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMATS[index]);
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
