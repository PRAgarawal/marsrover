package com.pragarawal.marsrover.nasa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Photo {

    @JsonProperty("img_src")
    private String imgSrc;

    public String getImgSrc() {
        return imgSrc;
    }

}
