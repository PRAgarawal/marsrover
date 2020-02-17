package com.pragarawal.marsrover.nasa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PhotosResponse {

    @JsonProperty("photos")
    private Photo[] photos;

    public Photo[] getPhotos() {
        return photos;
    }

}
