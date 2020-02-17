package com.pragarawal.marsrover.service;

import com.pragarawal.marsrover.model.Photo;
import com.pragarawal.marsrover.nasa.NASARoverPhotoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PhotoService {

    private static final String[] DATE_FORMATS = {
            "MM/dd/yy",
            "MMM dd, yyyy",
            "MMM-dd-yyyy",
    };
    private NASARoverPhotoClient photoClient;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public PhotoService() {
        this.photoClient = new NASARoverPhotoClient();
    }

    public Photo getPhotoByDate(String date) throws DateParseException, RestClientException {
        String formattedDate = formatDate(date);
        return photoClient.getPhotosForDate(formattedDate);
    }

    public void downloadPhotosFromFile() throws DateParseException, RestClientException {
        String[] dates;
        try {
            dates = readDatesFromFile("dates.txt");
        } catch (IOException e) {
            logger.error("Failed to read dates.txt", e);
            return;
        }

        for (String date : dates) {
            String formattedDate = formatDate(date);
            Photo photo = photoClient.getPhotosForDate(formattedDate);
        }
    }

    public static String[] readDatesFromFile(String fileName) throws IOException {
        Resource resource = new ClassPathResource(fileName);
        InputStream inputStream = resource.getInputStream();
        byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
        String data = new String(bdata, StandardCharsets.UTF_8);

        return data.split("\n");
    }

    public static String formatDate(String dateAsString) throws DateParseException {
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
        } catch (ParseException e) {
            return parseDateWithFormatterIndex(dateAsString, index+1);
        }

        return date;
    }

}
