package net.bossmannchristoph.lucidsearchtoolkit.web.exception;

import net.bossmannchristoph.lucidsearchtoolkit.web.api.model.Response;
import net.bossmannchristoph.lucidsearchtoolkit.web.api.model.ResponseMessage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;

@RestControllerAdvice
public class GlobalExceptionHandler {

    Logger LOGGER = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = {ApplicationException.class})
    public ResponseEntity<Response>handleApplicationException(ApplicationException e) {
        Response errorMessage = new ResponseMessage(e.getMessage(), e.getStatus(), HttpStatus.BAD_REQUEST.value());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        LOGGER.log(Level.WARN, "Application Exception: " + e.getStatus() + ", " + e.getMessage());
        return new ResponseEntity<>(errorMessage, headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestValueException.class)
    public ResponseEntity<Response> MissingRequestValueException(MissingRequestValueException ex) {
        Response errorMessage = new ResponseMessage(ex.getMessage(), "MISSING_VALUE", HttpStatus.BAD_REQUEST.value());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        LOGGER.log(Level.WARN, "MissingRequestValueException: " + ex.getMessage());
        return new ResponseEntity<>(errorMessage, headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> Exception(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        LOGGER.log(Level.ERROR, "Unknown error occurred: \n" + sw);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(new ResponseMessage(
                "Unexpected error occurred, see server logs for more details!",
                "UNEXPECTED_TECHNICAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR.value()), headers,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
