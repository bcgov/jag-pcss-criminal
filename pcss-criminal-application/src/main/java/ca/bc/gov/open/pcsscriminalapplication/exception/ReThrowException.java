package ca.bc.gov.open.pcsscriminalapplication.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReThrowException extends RuntimeException {

    String logMessage;

    public ReThrowException() {
        super(
                "An error response was received from server please check that your request is of valid form");
    }

    public ReThrowException(String logMessage, String message) {
        super(message);
        this.logMessage = logMessage;
    }

    public String getLog() {
        return logMessage;
    }
}
