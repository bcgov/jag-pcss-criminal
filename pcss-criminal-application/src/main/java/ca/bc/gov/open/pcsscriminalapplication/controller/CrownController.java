package ca.bc.gov.open.pcsscriminalapplication.controller;

import ca.bc.gov.open.pcsscriminalapplication.Keys;
import ca.bc.gov.open.pcsscriminalapplication.exception.BadDateException;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.service.CrownValidator;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.one.GetCrownAssignmentRequest;
import ca.bc.gov.open.wsdl.pcss.one.SetCounselDetailCriminalRequest;
import ca.bc.gov.open.wsdl.pcss.one.SetCrownAssignmentRequest;
import ca.bc.gov.open.wsdl.pcss.one.SetCrownFileDetailRequest;
import ca.bc.gov.open.wsdl.pcss.two.GetCrownAssignment;
import ca.bc.gov.open.wsdl.pcss.two.GetCrownAssignmentResponse;
import ca.bc.gov.open.wsdl.pcss.two.GetCrownAssignmentResponse2;
import ca.bc.gov.open.wsdl.pcss.two.SetCounselDetailCriminal;
import ca.bc.gov.open.wsdl.pcss.two.SetCounselDetailCriminalResponse;
import ca.bc.gov.open.wsdl.pcss.two.SetCounselDetailCriminalResponse2;
import ca.bc.gov.open.wsdl.pcss.two.SetCrownAssignment;
import ca.bc.gov.open.wsdl.pcss.two.SetCrownAssignmentResponse;
import ca.bc.gov.open.wsdl.pcss.two.SetCrownAssignmentResponse2;
import ca.bc.gov.open.wsdl.pcss.two.SetCrownFileDetailResponse;
import ca.bc.gov.open.wsdl.pcss.two.SetCrownFileDetail;
import ca.bc.gov.open.wsdl.pcss.two.SetCrownFileDetailResponse2;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

import java.util.List;

@Slf4j
@Endpoint
@EnableConfigurationProperties(PcssProperties.class)
public class CrownController {

    private final RestTemplate restTemplate;
    private final PcssProperties pcssProperties;
    private final LogBuilder logBuilder;
    private final CrownValidator crownValidator;

    public CrownController(RestTemplate restTemplate, PcssProperties pcssProperties, LogBuilder logBuilder, CrownValidator crownValidator) {
        this.restTemplate = restTemplate;
        this.pcssProperties = pcssProperties;
        this.logBuilder = logBuilder;
        this.crownValidator = crownValidator;
    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_CROWN_ASSIGNMENT)
    @ResponsePayload
    public GetCrownAssignmentResponse getCrownAssignment(@RequestPayload GetCrownAssignment getCrownAssignment) throws JsonProcessingException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_CROWN_ASSIGNMENT);

        GetCrownAssignmentRequest getCrownAssignmentRequest = getCrownAssignment.getGetCrownAssignmentRequest() != null
                && getCrownAssignment.getGetCrownAssignmentRequest().getGetCrownAssignmentRequest() != null
                ? getCrownAssignment.getGetCrownAssignmentRequest().getGetCrownAssignmentRequest()
                : new GetCrownAssignmentRequest();

        List<String> validationErrors = crownValidator.validateGetCrownAssignment(getCrownAssignmentRequest);
        if(!validationErrors.isEmpty()) {
            ca.bc.gov.open.wsdl.pcss.one.GetCrownAssignmentResponse innerErrorResponse = new ca.bc.gov.open.wsdl.pcss.one.GetCrownAssignmentResponse();
            innerErrorResponse.setResponseCd("-2");
            innerErrorResponse.setResponseMessageTxt(StringUtils.join(validationErrors, ","));

            log.warn(Keys.LOG_FAILED_VALIDATION, Keys.SOAP_METHOD_CROWN_ASSIGNMENT);

            return buildGetCrownAssignmentResponse(innerErrorResponse);
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


            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_CROWN_ASSIGNMENT);

            return buildGetCrownAssignmentResponse(response.getBody());

        } catch(Exception ex) {

            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_CROWN_ASSIGNMENT, getCrownAssignment, ex.getMessage()));
            throw new ORDSException();

        }
    }

    private GetCrownAssignmentResponse buildGetCrownAssignmentResponse(ca.bc.gov.open.wsdl.pcss.one.GetCrownAssignmentResponse getCrownAssignmentResponseInner) {

        GetCrownAssignmentResponse getCrownAssignmentResponse = new GetCrownAssignmentResponse();
        GetCrownAssignmentResponse2 getCrownAssignmentResponse2 = new GetCrownAssignmentResponse2();
        getCrownAssignmentResponse2.setGetCrownAssignmentResponse(getCrownAssignmentResponseInner);
        getCrownAssignmentResponse.setGetCrownAssignmentResponse(getCrownAssignmentResponse2);
        return getCrownAssignmentResponse;
    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_COUNSEL_DETAIL_CRIMINAL)
    @ResponsePayload
    public SetCounselDetailCriminalResponse setCounselDetailCriminal(@RequestPayload SetCounselDetailCriminal setCounselDetailCriminal) throws BadDateException, JsonProcessingException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_COUNSEL_DETAIL_CRIMINAL);

        SetCounselDetailCriminalRequest setCounselDetailCriminalRequest = setCounselDetailCriminal.getSetCounselDetailCriminalRequest() != null
                && setCounselDetailCriminal.getSetCounselDetailCriminalRequest().getSetCounselDetailCriminalRequest() != null
                ? setCounselDetailCriminal.getSetCounselDetailCriminalRequest().getSetCounselDetailCriminalRequest()
                : new SetCounselDetailCriminalRequest();

        if(setCounselDetailCriminalRequest.getRequestDtm() == null) {

            log.warn(logBuilder.writeLogMessage(Keys.DATE_ERROR_MESSAGE, Keys.SOAP_METHOD_COUNSEL_DETAIL_CRIMINAL, setCounselDetailCriminal, ""));
            throw new BadDateException();

        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_COUNSEL_DETAIL_CRIMINAL);

        HttpEntity<SetCounselDetailCriminalRequest> body = new HttpEntity<>(setCounselDetailCriminalRequest);

        try {

            log.debug(Keys.LOG_ORDS, Keys.ORDS_COUNSEL_DETAIL_CRIMINAL);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.SetCounselDetailCriminalResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            body,
                            ca.bc.gov.open.wsdl.pcss.one.SetCounselDetailCriminalResponse.class);

            SetCounselDetailCriminalResponse setCounselDetailCriminalResponse = new SetCounselDetailCriminalResponse();
            SetCounselDetailCriminalResponse2 setCounselDetailCriminalResponse2 = new SetCounselDetailCriminalResponse2();
            setCounselDetailCriminalResponse2.setSetCounselDetailCriminalResponse(response.getBody());
            setCounselDetailCriminalResponse.setSetCounselDetailCriminalResponse(setCounselDetailCriminalResponse2);

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_COUNSEL_DETAIL_CRIMINAL);

            return setCounselDetailCriminalResponse;

        } catch(Exception ex) {

            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.ORDS_COUNSEL_DETAIL_CRIMINAL, setCounselDetailCriminal, ex.getMessage()));
            throw new ORDSException();

        }


    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_SET_CROWN_ASSIGNMENT)
    @ResponsePayload
    public SetCrownAssignmentResponse setCrownAssignment(@RequestPayload SetCrownAssignment setCrownAssignment) throws BadDateException, JsonProcessingException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_SET_CROWN_ASSIGNMENT);

        SetCrownAssignmentRequest setCrownAssignmentRequest = setCrownAssignment.getSetCrownAssignmentRequest() != null
                && setCrownAssignment.getSetCrownAssignmentRequest().getSetCrownAssignmentRequest() != null
                ? setCrownAssignment.getSetCrownAssignmentRequest().getSetCrownAssignmentRequest()
                : new SetCrownAssignmentRequest();

        if(setCrownAssignmentRequest.getRequestDtm() == null) {

            log.warn(logBuilder.writeLogMessage(Keys.DATE_ERROR_MESSAGE, Keys.SOAP_METHOD_SET_CROWN_ASSIGNMENT, setCrownAssignment, ""));
            throw new BadDateException();

        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_CROWN_ASSIGNMENT);

        HttpEntity<SetCrownAssignmentRequest> body = new HttpEntity<>(setCrownAssignmentRequest);

        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_SET_CROWN_ASSIGNMENT);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.SetCrownAssignmentResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            body,
                            ca.bc.gov.open.wsdl.pcss.one.SetCrownAssignmentResponse.class);

            SetCrownAssignmentResponse setCrownAssignmentResponse = new SetCrownAssignmentResponse();
            SetCrownAssignmentResponse2 setCrownAssignmentResponse2 = new SetCrownAssignmentResponse2();
            setCrownAssignmentResponse2.setSetCrownAssignmentResponse(response.getBody());
            setCrownAssignmentResponse.setSetCrownAssignmentResponse(setCrownAssignmentResponse2);

            return setCrownAssignmentResponse;

        } catch(Exception ex) {

            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_SET_CROWN_ASSIGNMENT, setCrownAssignment, ex.getMessage()));
            throw new ORDSException();

        }

    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_CROWN_FILE_DETAIL)
    @ResponsePayload
    public SetCrownFileDetailResponse setCrownFileDetail(@RequestPayload SetCrownFileDetail setCrownFileDetail) throws BadDateException, JsonProcessingException {

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
