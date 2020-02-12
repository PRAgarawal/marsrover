package com.pragarawal.marsrover.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.Date;

public class Photo {

    @NotBlank
    private final String formattedDate;

    @NotBlank
    private final String url;

    public Photo(@JsonProperty("date") String date,
                 @JsonProperty("url") String url) {
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
