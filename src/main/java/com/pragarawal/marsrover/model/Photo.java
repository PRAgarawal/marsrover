package com.pragarawal.marsrover.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class Photo {
    @NotBlank
    private final String date;

    @NotBlank
    private final String url;


    public Photo(@JsonProperty("date") String date,
                 @JsonProperty("url") String url) {
        this.date = date;
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }
}
