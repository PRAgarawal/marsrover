package com.pragarawal.marsrover.api;

import com.pragarawal.marsrover.model.Photo;
import com.pragarawal.marsrover.service.DateParseException;
import com.pragarawal.marsrover.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

/**
 *
 */
@RequestMapping("photo")
@RestController
public class PhotoController {

    private final PhotoService photoService;

    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    // TODO: How do I make DateParseException a client (400) error?
    @GetMapping(path = "{date}")
    public Photo getPhotoByDate(@PathVariable("date") String date) throws DateParseException {
        return photoService.getPhotoByDate(date);
    }

}
