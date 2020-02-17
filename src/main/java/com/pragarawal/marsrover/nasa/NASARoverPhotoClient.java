package com.pragarawal.marsrover.nasa;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * This class provides the ability to query the NASA Rover Photo API by date. It will
 * only return the first picture provided for any given date.
 *
 * TODO: Make this implement an interface? Is that the best way to mock this object in order to facilitate testing more PhotoService methods? How do I Java?
 */
public class NASARoverPhotoClient {
    private static final String NASA_ROVER_PHOTO_API_BASE_URL = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?earth_date=%s&page=1&api_key=%s";
    private static final String NASA_API_KEY_PROPERTY = "NASA_API_KEY";

    /**
     * This method will return the first image from the NASA rover photo API for the
     * provided date. If no images are available for the given date, the return value
     * will be null.
     *
     * @param date the formatted date. If this is not formatted properly (YYYY-MM-DD),
     *             no error will be thrown, but the result will be null.
     * @return the image URL of the first photo resource, if it exists
     * @throws RestClientException
     */
    public String getFirstPhotoForDate(String date) throws RestClientException {
        String url = String.format(NASA_ROVER_PHOTO_API_BASE_URL, date, System.getenv(NASA_API_KEY_PROPERTY));
        RestTemplate restTemplate = new RestTemplate();
        PhotosResponse response = restTemplate.getForObject(url, PhotosResponse.class);

        if (response != null && response.getPhotos().length > 0) {
            return response.getPhotos()[0].getImgSrc();
        }

        return "";
    }
}
