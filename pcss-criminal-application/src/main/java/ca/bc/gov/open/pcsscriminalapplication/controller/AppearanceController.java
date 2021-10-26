package ca.bc.gov.open.pcsscriminalapplication.controller;

import ca.bc.gov.open.pcsscriminalapplication.configuration.SoapConfig;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.wsdl.pcss.two.GetAppearanceCriminal;
import ca.bc.gov.open.wsdl.pcss.two.GetAppearanceCriminalResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

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

    @PayloadRoot(namespace = SoapConfig.SOAP_NAMESPACE, localPart = "getAppearanceCriminal")
    @ResponsePayload
    public GetAppearanceCriminalResponse getAppearanceCriminal(@RequestPayload GetAppearanceCriminal getAppearanceCriminal) {
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
