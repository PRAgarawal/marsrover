package com.pragarawal.marsrover.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class Photo {

    @JsonProperty("date")
    private String formattedDate;

    @JsonProperty("url")
    private String url;

    public Photo(String date, String url) {
        this.formattedDate = date;
        this.url = url;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public String getUrl() {
        return url;
    }

}
