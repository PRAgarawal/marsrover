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
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PhotoService {

    private static final String IMAGE_PATH = "%s/photos/image/%s";
    private static final String BASE_URL_PROPERTY = "BASE_URL";
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
        return downloadImageFromNASA(date);
    }

    public void downloadPhotosFromFile() throws DateParseException, RestClientException {
        String[] dates;
        try {
            dates = readDatesFromFile("dates.txt");
        } catch (IOException e) {
            logger.error("failed to read dates.txt", e);
            return;
        }

        for (String date : dates) {
            downloadImageFromNASA(date);
        }
    }

    /**
     * Downloads a Mars rover image from the NASA API for the given date, and stores it
     * in the local resources directory to serve to our clients.
     *
     * @param date the [unformatted] date for which to download the NASA image
     * @return the Photo object with a URL pointing to the newly, locally cached image.
     * @throws DateParseException if the given date is formatted incorrectly.
     */
    private Photo downloadImageFromNASA(String date) throws DateParseException {
        String formattedDate = formatDate(date);
        String imgSrc = photoClient.getFirstPhotoForDate(formattedDate);
        ClassLoader classLoader = getClass().getClassLoader();

        try {
            InputStream in = new URL(imgSrc).openStream();
            Files.copy(in, Paths.get(classLoader.getResource(".").getFile() + formattedDate + ".jpeg"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error("failed to download image", e);
            return null;
        }

        String imgUrl = String.format(IMAGE_PATH, System.getenv(BASE_URL_PROPERTY), formattedDate + ".jpeg");
        return new Photo(formattedDate, imgUrl);
    }

    /**
     * Reads values (presumed to be dates) from the given file. Reads one date per line.
     *
     * @param fileName the file, assumed to be in the resources directory
     * @return an array of string values read from the file
     * @throws IOException
     */
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
