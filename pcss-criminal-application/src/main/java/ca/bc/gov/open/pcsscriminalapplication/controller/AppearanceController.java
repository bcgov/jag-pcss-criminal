package ca.bc.gov.open.pcsscriminalapplication.controller;

import ca.bc.gov.open.pcsscriminalapplication.Keys;
import ca.bc.gov.open.pcsscriminalapplication.exception.BadDateException;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.model.OrdsErrorLog;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.wsdl.pcss.secure.two.*;
import ca.bc.gov.open.wsdl.pcss.two.*;
import ca.bc.gov.open.wsdl.pcss.two.GetAppearanceCriminalApprMethodResponse;
import ca.bc.gov.open.wsdl.pcss.two.GetAppearanceCriminalCountResponse;
import ca.bc.gov.open.wsdl.pcss.two.GetAppearanceCriminalResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_APPEARANCE)
    @ResponsePayload
    public GetAppearanceCriminalResponse getAppearanceCriminal(@RequestPayload GetAppearanceCriminal getAppearanceCriminal) throws BadDateException, JsonProcessingException {

        log.info("Get Appearance Criminal Request Received");

        ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalRequest getAppearanceCriminalRequest =
                getAppearanceCriminal.getGetAppearanceCriminalRequest() != null
                && getAppearanceCriminal.getGetAppearanceCriminalRequest().getGetAppearanceCriminalRequest() != null
                ? getAppearanceCriminal.getGetAppearanceCriminalRequest().getGetAppearanceCriminalRequest()
                : new ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalRequest();

        if (getAppearanceCriminalRequest.getRequestDtm() == null) {

            log.warn(writeLogMessage(Keys.DATE_ERROR_MESSAGE, Keys.SOAP_METHOD_APPEARANCE, getAppearanceCriminal, ""));
            throw new BadDateException();

        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_APPEARANCE)
                        .queryParam(Keys.QUERY_AGENT_ID, getAppearanceCriminalRequest.getRequestAgencyIdentifierId())
                        .queryParam(Keys.QUERY_PART_ID, getAppearanceCriminalRequest.getRequestPartId())
                        .queryParam(Keys.QUERY_REQUEST_DATE, getAppearanceCriminalRequest.getRequestDtm())
                        .queryParam(Keys.QUERY_JUSTIN_NO, getAppearanceCriminalRequest.getJustinNo())
                        .queryParam(Keys.QUERY_FUTURE_FLAG, getAppearanceCriminalRequest.getFutureYN())
                        .queryParam(Keys.QUERY_HISTORY_FLAG, getAppearanceCriminalRequest.getHistoryYN());

        try {

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResponse.class);

            GetAppearanceCriminalResponse out = new GetAppearanceCriminalResponse();
            GetAppearanceCriminalResponse2 one = new GetAppearanceCriminalResponse2();
            one.setGetAppearanceCriminalResponse(resp.getBody());
            out.setGetAppearanceCriminalResponse(one);
            return out;

        } catch (Exception ex) {

            log.error(writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_APPEARANCE, getAppearanceCriminalRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_APPEARANCE_APPR_METHOD)
    @ResponsePayload
    public GetAppearanceCriminalApprMethodResponse getAppearanceCriminalApprMethod(@RequestPayload GetAppearanceCriminalApprMethod getAppearanceCriminalApprMethod) throws JsonProcessingException, BadDateException {

        log.info("Get Appearance Criminal Appr Method");

        ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalApprMethodRequest getAppearanceCriminalApprMethodRequest =
                getAppearanceCriminalApprMethod.getGetAppearanceCriminalApprMethodRequest() != null
                && getAppearanceCriminalApprMethod.getGetAppearanceCriminalApprMethodRequest().getGetAppearanceCriminalApprMethodRequest() != null
                ? getAppearanceCriminalApprMethod.getGetAppearanceCriminalApprMethodRequest().getGetAppearanceCriminalApprMethodRequest()
                : new ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalApprMethodRequest();

        if (getAppearanceCriminalApprMethodRequest.getRequestDtm() == null) {

            log.warn(writeLogMessage(Keys.DATE_ERROR_MESSAGE, Keys.SOAP_METHOD_APPEARANCE_APPR_METHOD, getAppearanceCriminalApprMethod, ""));
            throw new BadDateException();

        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_APPEARANCE)
                        .queryParam(Keys.QUERY_AGENT_ID, getAppearanceCriminalApprMethodRequest.getRequestAgencyIdentifierId())
                        .queryParam(Keys.QUERY_PART_ID, getAppearanceCriminalApprMethodRequest.getRequestPartId())
                        .queryParam(Keys.QUERY_REQUEST_DATE, getAppearanceCriminalApprMethodRequest.getRequestDtm())
                        .queryParam(Keys.QUERY_APPEARANCE_ID, getAppearanceCriminalApprMethodRequest.getAppearanceId());

        try {

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalApprMethodResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalApprMethodResponse.class);

            GetAppearanceCriminalApprMethodResponse out = new GetAppearanceCriminalApprMethodResponse();
            GetAppearanceCriminalApprMethodResponse2 one = new GetAppearanceCriminalApprMethodResponse2();
            one.setGetAppearanceCriminalApprMethodResponse(resp.getBody());
            out.setGetAppearanceCriminalApprMethodResponse(one);
            return out;

        } catch (Exception ex) {

            log.error(writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_APPEARANCE_APPR_METHOD, getAppearanceCriminalApprMethodRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_APPEARANCE_APPR_METHOD_SECURE)
    @ResponsePayload
    public GetAppearanceCriminalApprMethodSecureResponse getAppearanceCriminalApprMethodSecure(@RequestPayload
                             GetAppearanceCriminalApprMethodSecure getAppearanceCriminalApprMethodSecure) throws JsonProcessingException, BadDateException {

        log.info("Get Appearance Criminal Appr Method Secure");

        ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalApprMethodSecureRequest getAppearanceCriminalApprMethodSecureRequest =
                getAppearanceCriminalApprMethodSecure.getGetAppearanceCriminalApprMethodSecureRequest() != null
                        && getAppearanceCriminalApprMethodSecure.getGetAppearanceCriminalApprMethodSecureRequest().getGetAppearanceCriminalApprMethodSecureRequest() != null
                        ? getAppearanceCriminalApprMethodSecure.getGetAppearanceCriminalApprMethodSecureRequest().getGetAppearanceCriminalApprMethodSecureRequest()
                        : new  ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalApprMethodSecureRequest();

        if (getAppearanceCriminalApprMethodSecureRequest.getRequestDtm() == null) {

            log.warn(writeLogMessage(Keys.DATE_ERROR_MESSAGE, Keys.SOAP_METHOD_APPEARANCE_APPR_METHOD, getAppearanceCriminalApprMethodSecure, ""));
            throw new BadDateException();

        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_APPEARANCE)
                        .queryParam(Keys.QUERY_AGENT_ID, getAppearanceCriminalApprMethodSecureRequest.getRequestAgencyIdentifierId())
                        .queryParam(Keys.QUERY_PART_ID, getAppearanceCriminalApprMethodSecureRequest.getRequestPartId())
                        .queryParam(Keys.QUERY_REQUEST_DATE, getAppearanceCriminalApprMethodSecureRequest.getRequestDtm())
                        .queryParam(Keys.QUERY_APPEARANCE_CD, getAppearanceCriminalApprMethodSecureRequest.getApplicationCd())
                        .queryParam(Keys.QUERY_APPEARANCE_ID, getAppearanceCriminalApprMethodSecureRequest.getAppearanceId());

        try {

            HttpEntity<ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalApprMethodResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalApprMethodResponse.class);

            GetAppearanceCriminalApprMethodSecureResponse out = new GetAppearanceCriminalApprMethodSecureResponse();
            ca.bc.gov.open.wsdl.pcss.secure.two.GetAppearanceCriminalApprMethodResponse one = new ca.bc.gov.open.wsdl.pcss.secure.two.GetAppearanceCriminalApprMethodResponse();
            one.setGetAppearanceCriminalApprMethodResponse(resp.getBody());
            out.setGetAppearanceCriminalApprMethodResponse(one);
            return out;

        } catch (Exception ex) {

            log.error(writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_APPEARANCE_APPR_METHOD, getAppearanceCriminalApprMethodSecureRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = "getAppearanceCriminalCount")
    @ResponsePayload
    public GetAppearanceCriminalCountResponse getAppearanceCriminalCount(@RequestPayload GetAppearanceCriminalCount getAppearanceCriminalCount) {
        return null;
    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = "getAppearanceCriminalCountSecure")
    @ResponsePayload
    public GetAppearanceCriminalCountSecureRequest getAppearanceCriminalCountSecure(@RequestPayload GetAppearanceCriminalCountSecure getAppearanceCriminalCountSecure) {
        return null;
    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = "getAppearanceCriminalResource")
    @ResponsePayload
    public GetAppearanceCriminalResourceResponse getAppearanceCriminalResource(@RequestPayload GetAppearanceCriminalResource getAppearanceCriminalResource) {
        return null;
    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = "getAppearanceCriminalSecure")
    @ResponsePayload
    public GetAppearanceCriminalSecureResponse getAppearanceCriminalSecure(@RequestPayload GetAppearanceCriminalSecure getAppearanceCriminalSecure) {
        return null;
    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = "setAppearanceCriminal")
    @ResponsePayload
    public SetAppearanceCriminalResponse setAppearanceCriminal(@RequestPayload SetAppearanceCriminal setAppearanceCriminal) {
        return null;
    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = "setAppearanceMethodCriminal")
    @ResponsePayload
    public SetAppearanceMethodCriminalResponse setAppearanceMethodCriminal(@RequestPayload SetAppearanceMethodCriminal setAppearanceMethodCriminal) {
        return null;
    }

    private String writeLogMessage(String errorMessage, String method, Object requestObject, String exception) throws JsonProcessingException {
        return objectMapper.writeValueAsString(
                new OrdsErrorLog(
                        errorMessage,
                        method,
                        exception,
                        requestObject));
    }

}
