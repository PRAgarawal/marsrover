package com.pragarawal.marsrover.service;

import com.pragarawal.marsrover.model.Photo;

/**
 * This is an interface for fetching photos from the NASA API. The primary reason for its
 * creation is to facilitate testing (mocking) the NASA API functionality.
 */
public interface RoverPhotoClient {

    Photo getPhotosForDate(String date);

}
