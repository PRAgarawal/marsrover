package com.pragarawal.marsrover.service;

public class DateParseException extends Exception {

    DateParseException() {
        super("failed to recognize date format");
    }

}
