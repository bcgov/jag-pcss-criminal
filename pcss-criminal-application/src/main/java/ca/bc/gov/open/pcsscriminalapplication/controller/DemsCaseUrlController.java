package ca.bc.gov.open.pcsscriminalapplication.controller;

import ca.bc.gov.open.pcsscriminalapplication.Keys;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.demsCaseUrl.GetDemsCaseMapping;
import ca.bc.gov.open.wsdl.pcss.demsCaseUrl.GetDemsCaseMappingResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
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
public class DemsCaseUrlController {
    private final RestTemplate restTemplate;
    private final PcssProperties pcssProperties;
    private final LogBuilder logBuilder;
    private final ObjectMapper objectMapper;

    public DemsCaseUrlController(
            RestTemplate restTemplate,
            PcssProperties pcssProperties,
            LogBuilder logBuilder,
            ObjectMapper objectMapper)
            throws JsonProcessingException {
        this.restTemplate = restTemplate;
        this.pcssProperties = pcssProperties;
        this.logBuilder = logBuilder;
        this.objectMapper = objectMapper;
    }

    @PayloadRoot(
            namespace = Keys.SOAP_DEMSCASEURL_NAMESPACE,
            localPart = Keys.SOAP_METHOD_DEMSCASE_MAPPING)
    @ResponsePayload
    public GetDemsCaseMappingResponse getDemsCaseMapping(
            @RequestPayload GetDemsCaseMapping getDemsCaseMapping) throws JsonProcessingException {
        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_DEMSCASE_MAPPING);

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(
                        pcssProperties.getHost() + Keys.ORDS_FILE_DEMS_CASE_URL);

        HttpEntity<GetDemsCaseMapping> body = new HttpEntity<>(getDemsCaseMapping);

        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_DEMSCASE_MAPPING);

            HttpEntity<GetDemsCaseMapping> response =
                    restTemplate.exchange(
                            builder.build().toUri(),
                            HttpMethod.POST,
                            body,
                            GetDemsCaseMapping.class);

            GetDemsCaseMappingResponse getDemsCaseMappingResponse =
                    new GetDemsCaseMappingResponse();

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_DEMSCASE_MAPPING);

            return getDemsCaseMappingResponse;

        } catch (Exception ex) {

            throw new ORDSException();
        }
    }
}
