package ca.bc.gov.open.pcsscriminalapplication.controller;

import ca.bc.gov.open.pcsscriminalapplication.Keys;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.service.CrownValidator;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.one.*;
import ca.bc.gov.open.wsdl.pcss.two.GetCrownAssignment;
import ca.bc.gov.open.wsdl.pcss.two.GetCrownAssignmentResponse;
import ca.bc.gov.open.wsdl.pcss.two.GetCrownAssignmentResponse2;
import ca.bc.gov.open.wsdl.pcss.two.SetCounselDetailCriminal;
import ca.bc.gov.open.wsdl.pcss.two.SetCounselDetailCriminalResponse;
import ca.bc.gov.open.wsdl.pcss.two.SetCounselDetailCriminalResponse2;
import ca.bc.gov.open.wsdl.pcss.two.SetCrownAssignment;
import ca.bc.gov.open.wsdl.pcss.two.SetCrownAssignmentResponse;
import ca.bc.gov.open.wsdl.pcss.two.SetCrownAssignmentResponse2;
import ca.bc.gov.open.wsdl.pcss.two.SetCrownFileDetail;
import ca.bc.gov.open.wsdl.pcss.two.SetCrownFileDetailResponse;
import ca.bc.gov.open.wsdl.pcss.two.SetCrownFileDetailResponse2;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
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

@Slf4j
@Endpoint
@EnableConfigurationProperties(PcssProperties.class)
public class CrownController {

    private final RestTemplate restTemplate;
    private final PcssProperties pcssProperties;
    private final LogBuilder logBuilder;
    private final CrownValidator crownValidator;

    public CrownController(
            RestTemplate restTemplate,
            PcssProperties pcssProperties,
            LogBuilder logBuilder,
            CrownValidator crownValidator) {
        this.restTemplate = restTemplate;
        this.pcssProperties = pcssProperties;
        this.logBuilder = logBuilder;
        this.crownValidator = crownValidator;
    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_CROWN_ASSIGNMENT)
    @ResponsePayload
    public GetCrownAssignmentResponse getCrownAssignment(
            @RequestPayload GetCrownAssignment getCrownAssignment) throws JsonProcessingException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_CROWN_ASSIGNMENT);

        GetCrownAssignmentRequest getCrownAssignmentRequest =
                getCrownAssignment.getGetCrownAssignmentRequest() != null
                                && getCrownAssignment
                                                .getGetCrownAssignmentRequest()
                                                .getGetCrownAssignmentRequest()
                                        != null
                        ? getCrownAssignment
                                .getGetCrownAssignmentRequest()
                                .getGetCrownAssignmentRequest()
                        : new GetCrownAssignmentRequest();

        List<String> validationErrors =
                crownValidator.validateGetCrownAssignment(getCrownAssignmentRequest);
        if (!validationErrors.isEmpty()) {
            ca.bc.gov.open.wsdl.pcss.one.GetCrownAssignmentResponse innerErrorResponse =
                    new ca.bc.gov.open.wsdl.pcss.one.GetCrownAssignmentResponse();
            innerErrorResponse.setResponseCd("-2");
            innerErrorResponse.setResponseMessageTxt(StringUtils.join(validationErrors, ","));

            log.warn(Keys.LOG_FAILED_VALIDATION, Keys.SOAP_METHOD_CROWN_ASSIGNMENT);

            return buildGetCrownAssignmentResponse(innerErrorResponse);
        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(
                                pcssProperties.getHost() + Keys.ORDS_CROWN_ASSIGNMENT)
                        .queryParam(
                                Keys.QUERY_PART_ID, getCrownAssignmentRequest.getRequestPartId())
                        .queryParam(
                                Keys.QUERY_REQUEST_DATE, getCrownAssignmentRequest.getRequestDtm())
                        .queryParam(
                                Keys.QUERY_AGENT_ID,
                                getCrownAssignmentRequest.getRequestAgencyIdentifierId())
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

        } catch (Exception ex) {

            log.error(
                    logBuilder.writeLogMessage(
                            Keys.ORDS_ERROR_MESSAGE,
                            Keys.SOAP_METHOD_CROWN_ASSIGNMENT,
                            getCrownAssignment,
                            ex.getMessage()));
            throw new ORDSException();
        }
    }

    private GetCrownAssignmentResponse buildGetCrownAssignmentResponse(
            ca.bc.gov.open.wsdl.pcss.one.GetCrownAssignmentResponse
                    getCrownAssignmentResponseInner) {

        GetCrownAssignmentResponse getCrownAssignmentResponse = new GetCrownAssignmentResponse();
        GetCrownAssignmentResponse2 getCrownAssignmentResponse2 = new GetCrownAssignmentResponse2();
        getCrownAssignmentResponse2.setGetCrownAssignmentResponse(getCrownAssignmentResponseInner);
        getCrownAssignmentResponse.setGetCrownAssignmentResponse(getCrownAssignmentResponse2);
        return getCrownAssignmentResponse;
    }

    @PayloadRoot(
            namespace = Keys.SOAP_NAMESPACE,
            localPart = Keys.SOAP_METHOD_COUNSEL_DETAIL_CRIMINAL)
    @ResponsePayload
    public SetCounselDetailCriminalResponse setCounselDetailCriminal(
            @RequestPayload SetCounselDetailCriminal setCounselDetailCriminal)
            throws JsonProcessingException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_COUNSEL_DETAIL_CRIMINAL);

        SetCounselDetailCriminalRequest setCounselDetailCriminalRequest =
                setCounselDetailCriminal.getSetCounselDetailCriminalRequest() != null
                                && setCounselDetailCriminal
                                                .getSetCounselDetailCriminalRequest()
                                                .getSetCounselDetailCriminalRequest()
                                        != null
                        ? setCounselDetailCriminal
                                .getSetCounselDetailCriminalRequest()
                                .getSetCounselDetailCriminalRequest()
                        : new SetCounselDetailCriminalRequest();

        List<String> validationErrors =
                crownValidator.validateSetCounselDetailCriminal(setCounselDetailCriminalRequest);
        if (!validationErrors.isEmpty()) {

            ca.bc.gov.open.wsdl.pcss.one.SetCounselDetailCriminalResponse innerErrorResponse =
                    new ca.bc.gov.open.wsdl.pcss.one.SetCounselDetailCriminalResponse();
            innerErrorResponse.setResponseCd(Keys.FAILED_VALIDATION.toString());
            innerErrorResponse.setResponseMessageTxt(StringUtils.join(validationErrors, ","));

            log.warn(Keys.LOG_FAILED_VALIDATION, Keys.SOAP_METHOD_COUNSEL_DETAIL_CRIMINAL);

            return buildSetCounselDetailCriminalResponse(innerErrorResponse);
        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(
                        pcssProperties.getHost() + Keys.ORDS_COUNSEL_DETAIL_CRIMINAL);

        HttpEntity<SetCounselDetailCriminalRequest> body =
                new HttpEntity<>(setCounselDetailCriminalRequest);

        try {

            log.debug(Keys.LOG_ORDS, Keys.ORDS_COUNSEL_DETAIL_CRIMINAL);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.SetCounselDetailCriminalResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            body,
                            ca.bc.gov.open.wsdl.pcss.one.SetCounselDetailCriminalResponse.class);

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_COUNSEL_DETAIL_CRIMINAL);

            return buildSetCounselDetailCriminalResponse(response.getBody());

        } catch (Exception ex) {

            log.error(
                    logBuilder.writeLogMessage(
                            Keys.ORDS_ERROR_MESSAGE,
                            Keys.ORDS_COUNSEL_DETAIL_CRIMINAL,
                            setCounselDetailCriminal,
                            ex.getMessage()));
            throw new ORDSException();
        }
    }

    private SetCounselDetailCriminalResponse buildSetCounselDetailCriminalResponse(
            ca.bc.gov.open.wsdl.pcss.one.SetCounselDetailCriminalResponse innerResponse) {

        SetCounselDetailCriminalResponse setCounselDetailCriminalResponse =
                new SetCounselDetailCriminalResponse();
        SetCounselDetailCriminalResponse2 setCounselDetailCriminalResponse2 =
                new SetCounselDetailCriminalResponse2();
        setCounselDetailCriminalResponse2.setSetCounselDetailCriminalResponse(innerResponse);
        setCounselDetailCriminalResponse.setSetCounselDetailCriminalResponse(
                setCounselDetailCriminalResponse2);

        return setCounselDetailCriminalResponse;
    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_SET_CROWN_ASSIGNMENT)
    @ResponsePayload
    public SetCrownAssignmentResponse setCrownAssignment(
            @RequestPayload SetCrownAssignment setCrownAssignment) throws JsonProcessingException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_SET_CROWN_ASSIGNMENT);

        SetCrownAssignmentRequest setCrownAssignmentRequest =
                setCrownAssignment.getSetCrownAssignmentRequest() != null
                                && setCrownAssignment
                                                .getSetCrownAssignmentRequest()
                                                .getSetCrownAssignmentRequest()
                                        != null
                        ? setCrownAssignment
                                .getSetCrownAssignmentRequest()
                                .getSetCrownAssignmentRequest()
                        : new SetCrownAssignmentRequest();

        List<String> validationErrors =
                crownValidator.validateSetCrownAssignment(setCrownAssignmentRequest);
        if (!validationErrors.isEmpty()) {

            ca.bc.gov.open.wsdl.pcss.one.SetCrownAssignmentResponse innerErrorResponse =
                    new ca.bc.gov.open.wsdl.pcss.one.SetCrownAssignmentResponse();
            innerErrorResponse.setResponseCd(Keys.FAILED_VALIDATION.toString());
            innerErrorResponse.setResponseMessageTxt(StringUtils.join(validationErrors, ","));

            log.warn(Keys.LOG_FAILED_VALIDATION, Keys.SOAP_METHOD_SET_CROWN_ASSIGNMENT);

            return buildSetCrownAssignmentResponse(innerErrorResponse);
        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(
                        pcssProperties.getHost() + Keys.ORDS_CROWN_ASSIGNMENT);

        HttpEntity<SetCrownAssignmentRequest> body = new HttpEntity<>(setCrownAssignmentRequest);

        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_SET_CROWN_ASSIGNMENT);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.SetCrownAssignmentResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            body,
                            ca.bc.gov.open.wsdl.pcss.one.SetCrownAssignmentResponse.class);

            SetCrownAssignmentResponse setCrownAssignmentResponse =
                    buildSetCrownAssignmentResponse(response.getBody());

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_CROWN_FILE_DETAIL);

            return setCrownAssignmentResponse;

        } catch (Exception ex) {

            log.error(
                    logBuilder.writeLogMessage(
                            Keys.ORDS_ERROR_MESSAGE,
                            Keys.SOAP_METHOD_SET_CROWN_ASSIGNMENT,
                            setCrownAssignment,
                            ex.getMessage()));
            throw new ORDSException();
        }
    }

    private SetCrownAssignmentResponse buildSetCrownAssignmentResponse(
            ca.bc.gov.open.wsdl.pcss.one.SetCrownAssignmentResponse innerResponse) {

        SetCrownAssignmentResponse setCrownAssignmentResponse = new SetCrownAssignmentResponse();
        SetCrownAssignmentResponse2 setCrownAssignmentResponse2 = new SetCrownAssignmentResponse2();
        setCrownAssignmentResponse2.setSetCrownAssignmentResponse(innerResponse);
        setCrownAssignmentResponse.setSetCrownAssignmentResponse(setCrownAssignmentResponse2);

        return setCrownAssignmentResponse;
    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_CROWN_FILE_DETAIL)
    @ResponsePayload
    public SetCrownFileDetailResponse setCrownFileDetail(
            @RequestPayload SetCrownFileDetail setCrownFileDetail) throws JsonProcessingException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_CROWN_FILE_DETAIL);

        SetCrownFileDetailRequest setCrownFileDetailRequest =
                setCrownFileDetail.getSetCrownFileDetailRequest() != null
                                && setCrownFileDetail
                                                .getSetCrownFileDetailRequest()
                                                .getSetCrownFileDetailRequest()
                                        != null
                        ? setCrownFileDetail
                                .getSetCrownFileDetailRequest()
                                .getSetCrownFileDetailRequest()
                        : new SetCrownFileDetailRequest();

        List<String> validationErrors =
                crownValidator.validateSetCrownFileDetail(setCrownFileDetailRequest);
        if (!validationErrors.isEmpty()) {

            ca.bc.gov.open.wsdl.pcss.one.SetCrownFileDetailResponse innerErrorResponse =
                    new ca.bc.gov.open.wsdl.pcss.one.SetCrownFileDetailResponse();
            innerErrorResponse.setResponseCd(Keys.FAILED_VALIDATION.toString());
            innerErrorResponse.setResponseMessageTxt(StringUtils.join(validationErrors, ","));

            log.warn(Keys.LOG_FAILED_VALIDATION, Keys.SOAP_METHOD_SET_CROWN_ASSIGNMENT);

            return buildSetCrownFileDetailResponse(innerErrorResponse);
        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(
                        pcssProperties.getHost() + Keys.ORDS_CROWN_FILE_DETAIL);

        HttpEntity<SetCrownFileDetailRequest> body = new HttpEntity<>(setCrownFileDetailRequest);

        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_CROWN_FILE_DETAIL);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.SetCrownFileDetailResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            body,
                            ca.bc.gov.open.wsdl.pcss.one.SetCrownFileDetailResponse.class);

            SetCrownFileDetailResponse setCrownFileDetailResponse =
                    buildSetCrownFileDetailResponse(response.getBody());

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_CROWN_FILE_DETAIL);

            return setCrownFileDetailResponse;

        } catch (Exception ex) {
            log.error(
                    logBuilder.writeLogMessage(
                            Keys.ORDS_ERROR_MESSAGE,
                            Keys.SOAP_METHOD_CROWN_FILE_DETAIL,
                            setCrownFileDetailRequest,
                            ex.getMessage()));
            throw new ORDSException();
        }
    }

    private SetCrownFileDetailResponse buildSetCrownFileDetailResponse(
            ca.bc.gov.open.wsdl.pcss.one.SetCrownFileDetailResponse innerResponse) {

        SetCrownFileDetailResponse setCrownFileDetailResponse = new SetCrownFileDetailResponse();
        SetCrownFileDetailResponse2 setCrownFileDetailResponse2 = new SetCrownFileDetailResponse2();
        setCrownFileDetailResponse2.setSetCrownFileDetailResponse(innerResponse);
        setCrownFileDetailResponse.setSetCrownFileDetailResponse(setCrownFileDetailResponse2);

        return setCrownFileDetailResponse;
    }
}
