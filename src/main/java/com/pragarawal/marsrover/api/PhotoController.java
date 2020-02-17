package com.pragarawal.marsrover.api;

import com.pragarawal.marsrover.model.Photo;
import com.pragarawal.marsrover.service.DateParseException;
import com.pragarawal.marsrover.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RequestMapping("photos")
@RestController
public class PhotoController {

    private final PhotoService photoService;

    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    // TODO: How do I make DateParseException a client (400) error?
    @GetMapping(path = "{date}")
    public Photo getPhotoByDate(@PathVariable("date") String date) throws DateParseException, RestClientException {
        return photoService.getPhotoByDate(date);
    }

    @GetMapping
    public void cachePhotosFromFile() throws DateParseException, RestClientException {
        photoService.downloadPhotosFromFile();
    }

    /**
     * This method will serve an image from the local `resources` directory. These will
     * be images that were originally downloaded from NASA.
     *
     * @param fileName An exact match of an image file downloaded to the resources
     *                 directory
     * @param response
     * @throws IOException
     */
    @RequestMapping(path = "image/{fileName}", method = RequestMethod.GET,
            produces = MediaType.IMAGE_JPEG_VALUE)
    public void streamImage(@PathVariable("fileName") String fileName, HttpServletResponse response) throws IOException {
        File imgFile = new File(fileName);

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(new FileInputStream(imgFile), response.getOutputStream());
    }

}
