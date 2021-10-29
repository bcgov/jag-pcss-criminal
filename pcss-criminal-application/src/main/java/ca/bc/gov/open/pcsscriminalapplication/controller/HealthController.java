package ca.bc.gov.open.pcsscriminalapplication.controller;

import ca.bc.gov.open.pcsscriminalapplication.Keys;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.two.GetHealth;
import ca.bc.gov.open.wsdl.pcss.two.GetHealthResponse;
import ca.bc.gov.open.wsdl.pcss.two.GetPing;
import ca.bc.gov.open.wsdl.pcss.two.GetPingResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

import static ca.bc.gov.open.pcsscriminalapplication.Keys.*;


@Endpoint
@Slf4j
@EnableConfigurationProperties(PcssProperties.class)
public class HealthController {

    private final RestTemplate restTemplate;
    private final PcssProperties pcssProperties;
    private final LogBuilder logBuilder;

    public HealthController(RestTemplate restTemplate, PcssProperties pcssProperties, LogBuilder logBuilder) {
        this.restTemplate = restTemplate;
        this.pcssProperties = pcssProperties;
        this.logBuilder = logBuilder;
    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_HEALTH)
    @ResponsePayload
    public GetHealthResponse getHealth(@RequestPayload GetHealth getHealth)
            throws JsonProcessingException, ORDSException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + ORDS_HEALTH);

        try {
            HttpEntity<GetHealthResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            GetHealthResponse.class);

            return resp.getBody();
        } catch (Exception ex) {
            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, SOAP_METHOD_HEALTH, getHealth, ex.getMessage()));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_PING)
    @ResponsePayload
    public GetPingResponse getPing(@RequestPayload GetPing getPing) throws JsonProcessingException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_PING);
        try {
            HttpEntity<GetPingResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            GetPingResponse.class);

            return resp.getBody();
        } catch (Exception ex) {
            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_PING, getPing, ex.getMessage()));
            throw new ORDSException();
        }
    }
}
