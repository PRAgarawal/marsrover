package com.pragarawal.marsrover.nasa;

import com.pragarawal.marsrover.model.Photo;
import com.pragarawal.marsrover.service.RoverPhotoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class NASARoverPhotoClient implements RoverPhotoClient {
    private static final String NASA_ROVER_PHOTO_API_BASE_URL = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?earth_date=%s&page=1&api_key=%s";
    private static final String NASA_API_KEY_PROPERTY = "NASA_API_KEY";
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public Photo getPhotosForDate(String date) throws RestClientException {
        String url = String.format(NASA_ROVER_PHOTO_API_BASE_URL, date, System.getenv(NASA_API_KEY_PROPERTY));
        RestTemplate restTemplate = new RestTemplate();
        PhotosResponse response = restTemplate.getForObject(url, PhotosResponse.class);

        if (response != null && response.getPhotos().length > 0) {
            return new Photo(date, response.getPhotos()[0].getImgSrc());
        }

        return null;
    }
}
