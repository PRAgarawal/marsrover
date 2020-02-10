package com.pragarawal.marsrover.service;

import com.pragarawal.marsrover.model.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoService {
    @Autowired
    public PhotoService() {
    }

    public Photo getPhotoByDate(String date) {
        return new Photo("test", "test");
    }
}
