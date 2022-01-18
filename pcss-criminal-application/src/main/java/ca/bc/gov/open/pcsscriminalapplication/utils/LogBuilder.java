package ca.bc.gov.open.pcsscriminalapplication.utils;

import ca.bc.gov.open.pcsscriminalapplication.model.OrdsErrorLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class LogBuilder {

    private final ObjectMapper objectMapper;

    public LogBuilder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String writeLogMessage(
            String errorMessage, String method, Object requestObject, String exception)
            throws JsonProcessingException {

        return objectMapper.writeValueAsString(
                new OrdsErrorLog(errorMessage, method, exception, requestObject));
    }
}
