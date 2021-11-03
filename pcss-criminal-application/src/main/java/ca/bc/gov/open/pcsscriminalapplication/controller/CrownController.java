package ca.bc.gov.open.pcsscriminalapplication.controller;

import ca.bc.gov.open.pcsscriminalapplication.Keys;
import ca.bc.gov.open.pcsscriminalapplication.exception.BadDateException;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.one.GetCrownAssignmentRequest;
import ca.bc.gov.open.wsdl.pcss.one.SetCrownFileDetailRequest;
import ca.bc.gov.open.wsdl.pcss.two.GetCrownAssignment;
import ca.bc.gov.open.wsdl.pcss.two.GetCrownAssignmentResponse;
import ca.bc.gov.open.wsdl.pcss.two.GetCrownAssignmentResponse2;
import ca.bc.gov.open.wsdl.pcss.two.SetCrownFileDetailResponse;
import ca.bc.gov.open.wsdl.pcss.two.SetCrownFileDetail;
import ca.bc.gov.open.wsdl.pcss.two.SetCrownFileDetailResponse2;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Endpoint
@EnableConfigurationProperties(PcssProperties.class)
public class CrownController {

    private final RestTemplate restTemplate;
    private final PcssProperties pcssProperties;
    private final LogBuilder logBuilder;

    public CrownController(RestTemplate restTemplate, PcssProperties pcssProperties, LogBuilder logBuilder) {
        this.restTemplate = restTemplate;
        this.pcssProperties = pcssProperties;
        this.logBuilder = logBuilder;
    }

    public GetCrownAssignmentResponse getCrownAssignment(@RequestBody GetCrownAssignment getCrownAssignment) throws BadDateException, JsonProcessingException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_CROWN_ASSIGNMENT);

        GetCrownAssignmentRequest getCrownAssignmentRequest = getCrownAssignment.getGetCrownAssignmentRequest() != null
                && getCrownAssignment.getGetCrownAssignmentRequest().getGetCrownAssignmentRequest() != null
                ? getCrownAssignment.getGetCrownAssignmentRequest().getGetCrownAssignmentRequest()
                : new GetCrownAssignmentRequest();

        if(getCrownAssignmentRequest.getRequestDtm() == null || getCrownAssignmentRequest.getSinceDt() == null) {

            log.warn(logBuilder.writeLogMessage(Keys.DATE_ERROR_MESSAGE, Keys.SOAP_METHOD_CROWN_ASSIGNMENT, getCrownAssignment, ""));
            throw new BadDateException();

        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_CROWN_ASSIGNMENT)
                    .queryParam(Keys.QUERY_PART_ID, getCrownAssignmentRequest.getRequestPartId())
                    .queryParam(Keys.QUERY_REQUEST_DATE, getCrownAssignmentRequest.getRequestDtm())
                    .queryParam(Keys.QUERY_AGENT_ID, getCrownAssignmentRequest.getRequestAgencyIdentifierId())
                    .queryParam(Keys.QUERY_JUSTIN_NO, getCrownAssignmentRequest.getJustinNo())
                    .queryParam(Keys.QUERY_SINCE_DATE, getCrownAssignmentRequest.getSinceDt());

        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_CROWN_ASSIGNMENT);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.GetCrownAssignmentResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.one.GetCrownAssignmentResponse.class);

            GetCrownAssignmentResponse getCrownAssignmentResponse = new GetCrownAssignmentResponse();
            GetCrownAssignmentResponse2 getCrownAssignmentResponse2 = new GetCrownAssignmentResponse2();
            getCrownAssignmentResponse2.setGetCrownAssignmentResponse(response.getBody());
            getCrownAssignmentResponse.setGetCrownAssignmentResponse(getCrownAssignmentResponse2);

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_CROWN_ASSIGNMENT);

            return getCrownAssignmentResponse;

        } catch(Exception ex) {

            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_CROWN_ASSIGNMENT, getCrownAssignment, ex.getMessage()));
            throw new ORDSException();

        }
    }

    public void setCounselDetailCriminal() {

    }

    public void setCrownAssignment() {

    }

    public SetCrownFileDetailResponse setCrownFileDetail(@RequestBody SetCrownFileDetail setCrownFileDetail) throws BadDateException, JsonProcessingException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_CROWN_FILE_DETAIL);

        SetCrownFileDetailRequest setCrownFileDetailRequest = setCrownFileDetail.getSetCrownFileDetailRequest() != null
                && setCrownFileDetail.getSetCrownFileDetailRequest().getSetCrownFileDetailRequest() != null
                ? setCrownFileDetail.getSetCrownFileDetailRequest().getSetCrownFileDetailRequest()
                : new SetCrownFileDetailRequest();

        if(setCrownFileDetailRequest.getRequestDtm() == null) {
            log.warn(logBuilder.writeLogMessage(Keys.DATE_ERROR_MESSAGE, Keys.SOAP_METHOD_CROWN_FILE_DETAIL, setCrownFileDetail, ""));
            throw new BadDateException();
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_CROWN_FILE_DETAIL);

        HttpEntity<SetCrownFileDetailRequest> body = new HttpEntity<>(setCrownFileDetailRequest);

        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_CROWN_FILE_DETAIL);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.SetCrownFileDetailResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            body,
                            ca.bc.gov.open.wsdl.pcss.one.SetCrownFileDetailResponse.class);

            SetCrownFileDetailResponse setCrownFileDetailResponse = new SetCrownFileDetailResponse();
            SetCrownFileDetailResponse2 setCrownFileDetailResponse2 = new SetCrownFileDetailResponse2();
            setCrownFileDetailResponse2.setSetCrownFileDetailResponse(response.getBody());
            setCrownFileDetailResponse.setSetCrownFileDetailResponse(setCrownFileDetailResponse2);

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_CROWN_FILE_DETAIL);

            return setCrownFileDetailResponse;

        } catch(Exception ex) {
            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_CROWN_FILE_DETAIL, setCrownFileDetailRequest, ex.getMessage()));
            throw new ORDSException();
        }

    }
}
