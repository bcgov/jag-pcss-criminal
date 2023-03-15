package ca.bc.gov.open.pcsscriminalapplication.controller;

import ca.bc.gov.open.pcsscriminalapplication.Keys;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.one.GetFileAccessRequest;
import ca.bc.gov.open.wsdl.pcss.one.GetFileAccessResponse;
import ca.bc.gov.open.wsdl.pcss.two.GetFileAccess;
import ca.bc.gov.open.wsdl.pcss.two.GetFileAccessResponse2;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@Slf4j
@EnableConfigurationProperties(PcssProperties.class)
public class AccessController {

    private final RestTemplate restTemplate;
    private final PcssProperties pcssProperties;
    private final LogBuilder logBuilder;

    public AccessController(
            RestTemplate restTemplate, PcssProperties pcssProperties, LogBuilder logBuilder) {
        this.restTemplate = restTemplate;
        this.pcssProperties = pcssProperties;
        this.logBuilder = logBuilder;
    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_FILE_ACCESS)
    @ResponsePayload
    public ca.bc.gov.open.wsdl.pcss.two.GetFileAccessResponse getFileAccess(
            @RequestPayload GetFileAccess getFileAccess) throws JsonProcessingException {
        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_FILE_ACCESS);

        ca.bc.gov.open.wsdl.pcss.one.GetFileAccessRequest getFileAccessRequest =
                getFileAccess.getGetFileAccessRequest() != null
                                && getFileAccess.getGetFileAccessRequest().getGetFileAccessRequest()
                                        != null
                        ? getFileAccess.getGetFileAccessRequest().getGetFileAccessRequest()
                        : new ca.bc.gov.open.wsdl.pcss.one.GetFileAccessRequest();

        HttpEntity<GetFileAccessRequest> payload =
                new HttpEntity<>(getFileAccessRequest, new HttpHeaders());

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_FILE_ACCESS);

        try {
            HttpEntity<GetFileAccessResponse> resp =
                    restTemplate.exchange(
                            builder.build().toUri(),
                            HttpMethod.POST,
                            payload,
                            GetFileAccessResponse.class);
            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_FILE_ACCESS);
            ca.bc.gov.open.wsdl.pcss.two.GetFileAccessResponse getFileAccessResponse =
                    new ca.bc.gov.open.wsdl.pcss.two.GetFileAccessResponse();
            GetFileAccessResponse2 getFileAccessResponse2 = new GetFileAccessResponse2();
            getFileAccessResponse2.setGetFileAccessResponse(resp.getBody());
            getFileAccessResponse.setGetFileAccessResponse(getFileAccessResponse2);
            return getFileAccessResponse;
        } catch (Exception ex) {
            log.error(
                    logBuilder.writeLogMessage(
                            Keys.ORDS_ERROR_MESSAGE,
                            Keys.SOAP_METHOD_FILE_ACCESS,
                            getFileAccessRequest,
                            ex.getMessage()));
            throw new ORDSException();
        }
    }
}
