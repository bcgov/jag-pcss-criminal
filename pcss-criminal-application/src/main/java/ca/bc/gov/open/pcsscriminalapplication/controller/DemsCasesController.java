package ca.bc.gov.open.pcsscriminalapplication.controller;

import static ca.bc.gov.open.pcsscriminalapplication.exception.ServiceFaultException.handleError;

import ca.bc.gov.open.pcsscriminalapplication.Keys;
import ca.bc.gov.open.pcsscriminalapplication.properties.DemsProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.DateUtils;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.demsCaseUrl.GetDemsCasesRequest;
import ca.bc.gov.open.wsdl.pcss.demsCaseUrl.GetDemsCasesResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
@EnableConfigurationProperties(DemsProperties.class)
public class DemsCasesController {
    private final RestTemplate restTemplate;
    private final DemsProperties demsProperties;
    private final LogBuilder logBuilder;
    private final ObjectMapper objectMapper;

    public DemsCasesController(
            @Qualifier("restTemplateDEMS") RestTemplate restTemplate,
            DemsProperties demsProperties,
            LogBuilder logBuilder,
            ObjectMapper objectMapper)
            throws JsonProcessingException {
        this.restTemplate = restTemplate;
        this.demsProperties = demsProperties;
        this.logBuilder = logBuilder;
        this.objectMapper = objectMapper;
    }

    @PayloadRoot(
            namespace = Keys.SOAP_DEMSCASEURL_NAMESPACE,
            localPart = Keys.SOAP_METHOD_DEMSCASE_REQUEST)
    @ResponsePayload
    public GetDemsCasesResponse getDemsCaseMapping(
            @RequestPayload GetDemsCasesRequest getDemsCasesRequest)
            throws JsonProcessingException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_DEMSCASE_REQUEST);

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(
                                demsProperties.getHost() + Keys.ORDS_FILE_DEMS_CASE_URL)
                        .queryParam(
                                "requestAgencyIdentifierId",
                                getDemsCasesRequest.getRequestAgencyIdentifierId())
                        .queryParam("requestPartId", getDemsCasesRequest.getRequestPartId())
                        .queryParam(
                                "requestDtm",
                                DateUtils.formatORDSDate(getDemsCasesRequest.getRequestDtm()))
                        .queryParam("applicationCd", getDemsCasesRequest.getApplicationCd())
                        .queryParam(
                                "justinNo", String.join(",", getDemsCasesRequest.getJustinNo()));

        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_DEMSCASE_REQUEST);

            HttpEntity<GetDemsCasesResponse> response =
                    restTemplate.exchange(
                            builder.build().toUri(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            GetDemsCasesResponse.class);

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_DEMSCASE_REQUEST);

            return response.getBody();
        } catch (Exception ex) {

            log.error(
                    logBuilder.writeLogMessage(
                            Keys.ORDS_ERROR_MESSAGE,
                            Keys.SOAP_METHOD_DEMSCASE_REQUEST,
                            getDemsCasesRequest,
                            ex.getMessage()));

            throw handleError(ex, new ca.bc.gov.open.wsdl.pcss.demsCaseUrl.Error());
        }
    }
}
