package ca.bc.gov.open.pcsscriminalapplication.controller;

import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.server.endpoint.annotation.Endpoint;

@Slf4j
@Endpoint
@EnableConfigurationProperties(PcssProperties.class)
public class AppearanceController {

    private final RestTemplate restTemplate;
    private final PcssProperties pcssProperties;
    private final ObjectMapper objectMapper;

    public AppearanceController(RestTemplate restTemplate, PcssProperties pcssProperties, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.pcssProperties = pcssProperties;
        this.objectMapper = objectMapper;
    }

    public Object getAppearanceCriminal() {
        return null;
    }

    public Object getAppearanceCriminalApprMethod() {
        return null;
    }

    public Object getAppearanceCriminalApprMethodSecure() {
        return null;
    }

    public Object getAppearanceCriminalCount() {
        return null;
    }

    public Object getAppearanceCriminalCountSecure() {
        return null;
    }

    public Object getAppearanceCriminalResource() {
        return null;
    }

    public Object getAppearanceCriminalSecure() {
        return null;
    }

    public Object setAppearanceCriminal() {
        return null;
    }

    public Object setAppearanceMethodCriminal() {
        return null;
    }

}
