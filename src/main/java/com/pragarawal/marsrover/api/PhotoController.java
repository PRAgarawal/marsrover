package com.pragarawal.marsrover.api;

import com.pragarawal.marsrover.model.Photo;
import com.pragarawal.marsrover.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("photo")
@RestController
public class PhotoController {
    private final PhotoService photoService;

    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping(path = "{date}")
    public Photo getPhotoByDate(@PathVariable("date") String date) {
        return photoService.getPhotoByDate(date);
    }
}
