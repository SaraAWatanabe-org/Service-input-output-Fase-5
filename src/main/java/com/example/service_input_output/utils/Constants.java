package com.example.service_input_output.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static class EmailConstants {
        public static final String SUBJECT = "Notification from video processing request";
        public static final String SUCCESS_MESSAGE = "Your video process request has been finished successfully. You can download it from: %s";
        public static final String ERROR_MESSAGE = "Something went wrong during your video processing request. Please make another request.";
    }

}