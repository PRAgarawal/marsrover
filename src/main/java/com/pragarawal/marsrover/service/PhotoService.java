package com.pragarawal.marsrover.service;

import com.pragarawal.marsrover.model.Photo;
import com.pragarawal.marsrover.nasa.NASARoverPhotoClient;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestClientException;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PhotoService {

    private static final String IMAGE_PATH = "%s/photos/image/%s";
    private static final String BASE_URL_PROPERTY = "BASE_URL";
    private static final String BASE_URL_DEFAULT = "http://localhost:8080";
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
    private Photo downloadImageFromNASA(String date) throws DateParseException, RestClientException {
        String formattedDate = formatDate(date);
        String imgSrc = photoClient.getFirstPhotoForDate(formattedDate);

        if (imgSrc == null || imgSrc.length() == 0) {
            return new Photo(formattedDate, "");
        }

        ClassLoader classLoader = getClass().getClassLoader();
        String url = classLoader.getResourceAsStream(".") + formattedDate + ".jpeg";
        Path path = Paths.get(url);

        // Use a cached file if possible
        if (Files.notExists(path)) {
            logger.info("creating new file: " + formattedDate + ".jpeg");
            try {
                InputStream in = new URL(imgSrc).openStream();
                File targetFile = new File(formattedDate + ".jpeg");
                java.nio.file.Files.copy(in, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                IOUtils.closeQuietly(in);
            } catch (IOException e) {
                logger.error("failed to download image", e);
                return null;
            }
        }

        String imgUrl = String.format(IMAGE_PATH, getBaseUrl(), formattedDate + ".jpeg");
        return new Photo(formattedDate, imgUrl);
    }

    /**
     * Get the base URL from the environment, or the default (localhost) if none is
     * configured.
     *
     * TODO: Is there a better/more Spring-canonical way of using the environment?
     * @return
     */
    private static String getBaseUrl() {
        String baseUrl = System.getenv(BASE_URL_PROPERTY);
        if (baseUrl == null) {
            return BASE_URL_DEFAULT;
        }

        return baseUrl;
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

    /**
     * Takes an unformatted date string and formats it into the form YYYY-MM-DD.
     *
     * @param rawDate the unformatted date
     * @return the formatted date
     * @throws DateParseException will be thrown for malformed dates that can't be
     * normalized
     */
    public static String formatDate(String rawDate) throws DateParseException {
        Date date = parseDateWithFormatterIndex(rawDate, 0);
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
