package com.mpesa.sampleapi.exception;

import io.github.openpaydev.mpesa.core.exceptions.MpesaApiException;
import io.github.openpaydev.mpesa.core.exceptions.MpesaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MpesaException.class)
    public ResponseEntity<Map<String, String>> handleMpesaException(MpesaException ex) {
        log.error("An M-Pesa API error occurred: ", ex);

        if (ex instanceof MpesaApiException) {
            MpesaApiException apiException = (MpesaApiException) ex;
            Map<String, String> errorResponse = Map.of(
                    "error", "M-Pesa API Error",
                    "message", apiException.getMessage(),
                    "statusCode", String.valueOf(apiException.getStatusCode()),
                    "responseBody", apiException.getResponseBody()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_GATEWAY);
        }

        Map<String, String> errorResponse = Map.of(
                "error", "An SDK error occurred.",
                "message", ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}