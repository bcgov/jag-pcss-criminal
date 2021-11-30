package ca.bc.gov.open.pcsscriminalapplication.controller;

import ca.bc.gov.open.pcsscriminalapplication.Keys;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.service.AppearanceValidator;
import ca.bc.gov.open.pcsscriminalapplication.utils.DateUtils;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.one.ApprDetail;
import ca.bc.gov.open.wsdl.pcss.one.Resource;
import ca.bc.gov.open.wsdl.pcss.secure.two.*;
import ca.bc.gov.open.wsdl.pcss.two.*;
import ca.bc.gov.open.wsdl.pcss.two.GetAppearanceCriminalApprMethodResponse;
import ca.bc.gov.open.wsdl.pcss.two.GetAppearanceCriminalCountResponse;
import ca.bc.gov.open.wsdl.pcss.two.GetAppearanceCriminalResponse;
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
import java.util.function.Consumer;

@Slf4j
@Endpoint
@EnableConfigurationProperties(PcssProperties.class)
public class AppearanceController {

    private final RestTemplate restTemplate;
    private final PcssProperties pcssProperties;
    private final LogBuilder logBuilder;
    private final AppearanceValidator appearanceValidator;

    public AppearanceController(RestTemplate restTemplate, PcssProperties pcssProperties, LogBuilder logBuilder, AppearanceValidator appearanceValidator) {
        this.restTemplate = restTemplate;
        this.pcssProperties = pcssProperties;
        this.logBuilder = logBuilder;
        this.appearanceValidator = appearanceValidator;
    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_APPEARANCE)
    @ResponsePayload
    public GetAppearanceCriminalResponse getAppearanceCriminal(@RequestPayload GetAppearanceCriminal getAppearanceCriminal) throws JsonProcessingException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_APPEARANCE);

        ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalRequest getAppearanceCriminalRequest =
                getAppearanceCriminal.getGetAppearanceCriminalRequest() != null
                && getAppearanceCriminal.getGetAppearanceCriminalRequest().getGetAppearanceCriminalRequest() != null
                ? getAppearanceCriminal.getGetAppearanceCriminalRequest().getGetAppearanceCriminalRequest()
                : new ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalRequest();

        List<String> validation =  appearanceValidator.validateGetAppearanceCriminal(getAppearanceCriminalRequest);
        if (!validation.isEmpty()) {

            ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResponse getAppearanceCriminalResponseValidation = new ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResponse();

            getAppearanceCriminalResponseValidation.setResponseCd(Keys.FAILED_VALIDATION.toString());
            getAppearanceCriminalResponseValidation.setResponseMessageTxt(StringUtils.join(validation, ","));

            log.info(Keys.LOG_FAILED_VALIDATION, Keys.SOAP_METHOD_APPEARANCE);

            return buildAppearanceResponse(getAppearanceCriminalResponseValidation);

        }


        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_APPEARANCE)
                        .queryParam(Keys.QUERY_APPEARANCE_ID, getAppearanceCriminalRequest.getAppearanceId())
                        .queryParam(Keys.QUERY_JUSTIN_NO, getAppearanceCriminalRequest.getJustinNo())
                        .queryParam(Keys.QUERY_FUTURE_FLAG, getAppearanceCriminalRequest.getFutureYN())
                        .queryParam(Keys.QUERY_HISTORY_FLAG, getAppearanceCriminalRequest.getHistoryYN());

        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_APPEARANCE);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResponse.class);

            GetAppearanceCriminalResponse getAppearanceCriminalResponse = buildAppearanceResponse(response.getBody());

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_APPEARANCE);

            return getAppearanceCriminalResponse;

        } catch (Exception ex) {

            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_APPEARANCE, getAppearanceCriminalRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

    private GetAppearanceCriminalResponse buildAppearanceResponse(ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResponse getAppearanceCriminalResponseInner) {

        if (getAppearanceCriminalResponseInner.getApprDetail() != null) {
            getAppearanceCriminalResponseInner.getApprDetail()
                .forEach(
                    ((Consumer<ApprDetail>) apprDetail -> apprDetail.setAppearanceDt(DateUtils.formatDate(apprDetail.getAppearanceDt())))
                        .andThen(apprDetail -> apprDetail.setAppearanceTm(DateUtils.formatDate(apprDetail.getAppearanceTm())))
                );
        }

        GetAppearanceCriminalResponse getAppearanceCriminalResponse = new GetAppearanceCriminalResponse();
        GetAppearanceCriminalResponse2 getAppearanceCriminalResponse2 = new GetAppearanceCriminalResponse2();
        getAppearanceCriminalResponse2.setGetAppearanceCriminalResponse(getAppearanceCriminalResponseInner);
        getAppearanceCriminalResponse.setGetAppearanceCriminalResponse(getAppearanceCriminalResponse2);

        return getAppearanceCriminalResponse;

    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_APPEARANCE_APPR_METHOD)
    @ResponsePayload
    public GetAppearanceCriminalApprMethodResponse getAppearanceCriminalApprMethod(@RequestPayload GetAppearanceCriminalApprMethod getAppearanceCriminalApprMethod) throws JsonProcessingException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_APPEARANCE_APPR_METHOD);

        ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalApprMethodRequest getAppearanceCriminalApprMethodRequest =
                getAppearanceCriminalApprMethod.getGetAppearanceCriminalApprMethodRequest() != null
                && getAppearanceCriminalApprMethod.getGetAppearanceCriminalApprMethodRequest().getGetAppearanceCriminalApprMethodRequest() != null
                ? getAppearanceCriminalApprMethod.getGetAppearanceCriminalApprMethodRequest().getGetAppearanceCriminalApprMethodRequest()
                : new ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalApprMethodRequest();

        List<String> validation =  appearanceValidator.validateGetAppearanceCriminalApprMethod(getAppearanceCriminalApprMethodRequest);
        if (!validation.isEmpty()) {

            ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalApprMethodResponse getAppearanceCriminalApprMethodResponse = new ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalApprMethodResponse();

            getAppearanceCriminalApprMethodResponse.setResponseCd(Keys.FAILED_VALIDATION.toString());
            getAppearanceCriminalApprMethodResponse.setResponseMessageTxt(StringUtils.join(validation, ","));

            log.info(Keys.LOG_FAILED_VALIDATION, Keys.SOAP_METHOD_APPEARANCE);

            return buildAppearanceCriminalApprMethodResponse(getAppearanceCriminalApprMethodResponse);

        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_APPEARANCE_METHOD)
                        .queryParam(Keys.QUERY_APPEARANCE_ID, getAppearanceCriminalApprMethodRequest.getAppearanceId());

        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_APPEARANCE_APPR_METHOD);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalApprMethodResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalApprMethodResponse.class);

            GetAppearanceCriminalApprMethodResponse getAppearanceCriminalApprMethodResponse = buildAppearanceCriminalApprMethodResponse(response.getBody());

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_APPEARANCE_APPR_METHOD);

            return getAppearanceCriminalApprMethodResponse;

        } catch (Exception ex) {

            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_APPEARANCE_APPR_METHOD, getAppearanceCriminalApprMethodRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

    private GetAppearanceCriminalApprMethodResponse buildAppearanceCriminalApprMethodResponse(ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalApprMethodResponse getAppearanceCriminalApprMethodResponseInner) {

        GetAppearanceCriminalApprMethodResponse getAppearanceCriminalApprMethodResponse = new GetAppearanceCriminalApprMethodResponse();
        GetAppearanceCriminalApprMethodResponse2 getAppearanceCriminalApprMethodResponse2 = new GetAppearanceCriminalApprMethodResponse2();
        getAppearanceCriminalApprMethodResponse2.setGetAppearanceCriminalApprMethodResponse(getAppearanceCriminalApprMethodResponseInner);
        getAppearanceCriminalApprMethodResponse.setGetAppearanceCriminalApprMethodResponse(getAppearanceCriminalApprMethodResponse2);

        return getAppearanceCriminalApprMethodResponse;

    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_APPEARANCE_APPR_METHOD_SECURE)
    @ResponsePayload
    public GetAppearanceCriminalApprMethodSecureResponse getAppearanceCriminalApprMethodSecure(@RequestPayload
                             GetAppearanceCriminalApprMethodSecure getAppearanceCriminalApprMethodSecure) throws JsonProcessingException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_APPEARANCE_APPR_METHOD_SECURE);

        ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalApprMethodSecureRequest getAppearanceCriminalApprMethodSecureRequest =
                getAppearanceCriminalApprMethodSecure.getGetAppearanceCriminalApprMethodSecureRequest() != null
                        && getAppearanceCriminalApprMethodSecure.getGetAppearanceCriminalApprMethodSecureRequest().getGetAppearanceCriminalApprMethodSecureRequest() != null
                        ? getAppearanceCriminalApprMethodSecure.getGetAppearanceCriminalApprMethodSecureRequest().getGetAppearanceCriminalApprMethodSecureRequest()
                        : new  ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalApprMethodSecureRequest();

        List<String> validation =  appearanceValidator.validateGetAppearanceCriminalApprMethodSecure(getAppearanceCriminalApprMethodSecureRequest);
        if (!validation.isEmpty()) {

            ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalApprMethodResponse appearanceCriminalApprMethodResponse = new ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalApprMethodResponse();

            appearanceCriminalApprMethodResponse.setResponseCd(Keys.FAILED_VALIDATION.toString());
            appearanceCriminalApprMethodResponse.setResponseMessageTxt(StringUtils.join(validation, ","));

            log.info(Keys.LOG_FAILED_VALIDATION, Keys.SOAP_METHOD_APPEARANCE);

            return buildAppearanceCriminalApprMethodSecureResponse(appearanceCriminalApprMethodResponse);

        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_APPEARANCE_METHOD_SECURE)
                        .queryParam(Keys.QUERY_AGENT_ID, getAppearanceCriminalApprMethodSecureRequest.getRequestAgencyIdentifierId())
                        .queryParam(Keys.QUERY_PART_ID, getAppearanceCriminalApprMethodSecureRequest.getRequestPartId())
                        .queryParam(Keys.QUERY_REQUEST_DATE, DateUtils.formatORDSDate(getAppearanceCriminalApprMethodSecureRequest.getRequestDtm()))
                        .queryParam(Keys.QUERY_APPLICATION_CD, getAppearanceCriminalApprMethodSecureRequest.getApplicationCd())
                        .queryParam(Keys.QUERY_APPEARANCE_ID, getAppearanceCriminalApprMethodSecureRequest.getAppearanceId());

        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_APPEARANCE_APPR_METHOD_SECURE);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalApprMethodResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalApprMethodResponse.class);

            GetAppearanceCriminalApprMethodSecureResponse getAppearanceCriminalApprMethodSecureResponse = buildAppearanceCriminalApprMethodSecureResponse(response.getBody());

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_APPEARANCE_APPR_METHOD_SECURE);

            return getAppearanceCriminalApprMethodSecureResponse;

        } catch (Exception ex) {

            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_APPEARANCE_APPR_METHOD_SECURE, getAppearanceCriminalApprMethodSecureRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

    private GetAppearanceCriminalApprMethodSecureResponse buildAppearanceCriminalApprMethodSecureResponse(ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalApprMethodResponse getAppearanceCriminalApprMethodResponseInner) {

        GetAppearanceCriminalApprMethodSecureResponse getAppearanceCriminalApprMethodSecureResponse = new GetAppearanceCriminalApprMethodSecureResponse();
        ca.bc.gov.open.wsdl.pcss.secure.two.GetAppearanceCriminalApprMethodResponse getAppearanceCriminalApprMethodResponse2 = new ca.bc.gov.open.wsdl.pcss.secure.two.GetAppearanceCriminalApprMethodResponse();
        getAppearanceCriminalApprMethodResponse2.setGetAppearanceCriminalApprMethodResponse(getAppearanceCriminalApprMethodResponseInner);
        getAppearanceCriminalApprMethodSecureResponse.setGetAppearanceCriminalApprMethodResponse(getAppearanceCriminalApprMethodResponse2);

        return getAppearanceCriminalApprMethodSecureResponse;

    }


    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_APPEARANCE_COUNT)
    @ResponsePayload
    public GetAppearanceCriminalCountResponse getAppearanceCriminalCount(@RequestPayload GetAppearanceCriminalCount getAppearanceCriminalCount) throws JsonProcessingException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_APPEARANCE_COUNT);

        ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalCountRequest getAppearanceCriminalCountRequest =
                getAppearanceCriminalCount.getGetAppearanceCriminalCountRequest() != null
                        && getAppearanceCriminalCount.getGetAppearanceCriminalCountRequest().getGetAppearanceCriminalCountRequest() != null
                        ? getAppearanceCriminalCount.getGetAppearanceCriminalCountRequest().getGetAppearanceCriminalCountRequest()
                        : new  ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalCountRequest();

        List<String> validation =  appearanceValidator.validateGetAppearanceCriminalCount(getAppearanceCriminalCountRequest);
        if (!validation.isEmpty()) {

            ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalCountResponse getAppearanceCriminalCountResponse = new ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalCountResponse();

            getAppearanceCriminalCountResponse.setResponseCd(Keys.FAILED_VALIDATION.toString());
            getAppearanceCriminalCountResponse.setResponseMessageTxt(StringUtils.join(validation, ","));

            log.info(Keys.LOG_FAILED_VALIDATION, Keys.SOAP_METHOD_APPEARANCE);

            return buildAppearanceCriminalCountResponse(getAppearanceCriminalCountResponse);

        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_APPEARANCE_COUNT)
                        .queryParam(Keys.QUERY_APPEARANCE_ID, getAppearanceCriminalCountRequest.getAppearanceId());

        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_APPEARANCE_COUNT);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalCountResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalCountResponse.class);

            GetAppearanceCriminalCountResponse getAppearanceCriminalCountResponse = buildAppearanceCriminalCountResponse(response.getBody());

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_APPEARANCE_COUNT);

            return getAppearanceCriminalCountResponse;

        } catch (Exception ex) {

            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_APPEARANCE_COUNT, getAppearanceCriminalCountRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

    private GetAppearanceCriminalCountResponse buildAppearanceCriminalCountResponse(ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalCountResponse getAppearanceCriminalCountResponseInner) {

        GetAppearanceCriminalCountResponse getAppearanceCriminalCountResponse = new GetAppearanceCriminalCountResponse();
        ca.bc.gov.open.wsdl.pcss.two.GetAppearanceCriminalCountResponse2 getAppearanceCriminalCountResponse2 = new ca.bc.gov.open.wsdl.pcss.two.GetAppearanceCriminalCountResponse2();
        getAppearanceCriminalCountResponse2.setGetAppearanceCriminalCountResponse(getAppearanceCriminalCountResponseInner);
        getAppearanceCriminalCountResponse.setGetAppearanceCriminalCountResponse(getAppearanceCriminalCountResponse2);

        return getAppearanceCriminalCountResponse;

    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_APPEARANCE_COUNT_SECURE)
    @ResponsePayload
    public GetAppearanceCriminalCountSecureResponse getAppearanceCriminalCountSecure(@RequestPayload GetAppearanceCriminalCountSecure getAppearanceCriminalCountSecure) throws JsonProcessingException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_APPEARANCE_COUNT_SECURE);

        ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalCountSecureRequest getAppearanceCriminalCountSecureRequest =
                getAppearanceCriminalCountSecure.getGetAppearanceCriminalCountSecureRequest() != null
                        && getAppearanceCriminalCountSecure.getGetAppearanceCriminalCountSecureRequest().getGetAppearanceCriminalCountSecureRequest() != null
                        ? getAppearanceCriminalCountSecure.getGetAppearanceCriminalCountSecureRequest().getGetAppearanceCriminalCountSecureRequest()
                        : new  ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalCountSecureRequest();

        List<String> validation =  appearanceValidator.validateGetAppearanceCriminalCountSecure(getAppearanceCriminalCountSecureRequest);
        if (!validation.isEmpty()) {

            ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalCountResponse appearanceCriminalCountResponse = new ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalCountResponse();

            appearanceCriminalCountResponse.setResponseCd(Keys.FAILED_VALIDATION.toString());
            appearanceCriminalCountResponse.setResponseMessageTxt(StringUtils.join(validation, ","));

            log.info(Keys.LOG_FAILED_VALIDATION, Keys.SOAP_METHOD_APPEARANCE);

            return buildAppearanceCriminalCountResponse(appearanceCriminalCountResponse);

        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_APPEARANCE_COUNT_SECURE)
                        .queryParam(Keys.QUERY_AGENT_ID, getAppearanceCriminalCountSecureRequest.getRequestAgencyIdentifierId())
                        .queryParam(Keys.QUERY_PART_ID, getAppearanceCriminalCountSecureRequest.getRequestPartId())
                        .queryParam(Keys.QUERY_REQUEST_DATE, DateUtils.formatORDSDate(getAppearanceCriminalCountSecureRequest.getRequestDtm()))
                        .queryParam(Keys.QUERY_APPLICATION_CD, getAppearanceCriminalCountSecureRequest.getApplicationCd())
                        .queryParam(Keys.QUERY_APPEARANCE_ID, getAppearanceCriminalCountSecureRequest.getAppearanceId());

        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_APPEARANCE_COUNT_SECURE);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalCountResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalCountResponse.class);

            GetAppearanceCriminalCountSecureResponse getAppearanceCriminalCountSecureResponse = buildAppearanceCriminalCountResponse(response.getBody());

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_APPEARANCE_COUNT_SECURE);

            return getAppearanceCriminalCountSecureResponse;

        } catch (Exception ex) {

            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_APPEARANCE_COUNT_SECURE, getAppearanceCriminalCountSecureRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

    private GetAppearanceCriminalCountSecureResponse buildAppearanceCriminalCountResponse(ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalCountResponse getAppearanceCriminalCountResponseInner) {

        GetAppearanceCriminalCountSecureResponse getAppearanceCriminalCountSecureResponse = new GetAppearanceCriminalCountSecureResponse();
        ca.bc.gov.open.wsdl.pcss.secure.two.GetAppearanceCriminalCountResponse getAppearanceCriminalCountResponse2 = new ca.bc.gov.open.wsdl.pcss.secure.two.GetAppearanceCriminalCountResponse();
        getAppearanceCriminalCountResponse2.setGetAppearanceCriminalCountResponse(getAppearanceCriminalCountResponseInner);
        getAppearanceCriminalCountSecureResponse.setGetAppearanceCriminalCountResponse(getAppearanceCriminalCountResponse2);

        return getAppearanceCriminalCountSecureResponse;

    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_APPEARANCE_RESOURCE)
    @ResponsePayload
    public GetAppearanceCriminalResourceResponse getAppearanceCriminalResource(@RequestPayload GetAppearanceCriminalResource getAppearanceCriminalResource) throws JsonProcessingException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_APPEARANCE_RESOURCE);

        ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResourceRequest getAppearanceCriminalResourceRequest =
                getAppearanceCriminalResource.getGetAppearanceCriminalResourceRequest() != null
                        && getAppearanceCriminalResource.getGetAppearanceCriminalResourceRequest().getGetAppearanceCriminalResourceRequest() != null
                        ? getAppearanceCriminalResource.getGetAppearanceCriminalResourceRequest().getGetAppearanceCriminalResourceRequest()
                        : new  ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResourceRequest();

        List<String> validation =  appearanceValidator.validateGetAppearanceCriminalResource(getAppearanceCriminalResourceRequest);
        if (!validation.isEmpty()) {

            ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResourceResponse getAppearanceCriminalResourceResponse = new ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResourceResponse();

            getAppearanceCriminalResourceResponse.setResponseCd(Keys.FAILED_VALIDATION.toString());
            getAppearanceCriminalResourceResponse.setResponseMessageTxt(StringUtils.join(validation, ","));

            log.info(Keys.LOG_FAILED_VALIDATION, Keys.SOAP_METHOD_APPEARANCE);

            return buildAppearanceCriminalResourceResponse(getAppearanceCriminalResourceResponse);

        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_APPEARANCE_RESOURCE)
                        .queryParam(Keys.QUERY_APPEARANCE_ID, getAppearanceCriminalResourceRequest.getAppearanceId());

        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_APPEARANCE_RESOURCE);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResourceResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResourceResponse.class);

            GetAppearanceCriminalResourceResponse getAppearanceCriminalResourceResponse = buildAppearanceCriminalResourceResponse(response.getBody());

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_APPEARANCE_RESOURCE);

            return getAppearanceCriminalResourceResponse;

        } catch (Exception ex) {

            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_APPEARANCE_RESOURCE, getAppearanceCriminalResourceRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

    private GetAppearanceCriminalResourceResponse buildAppearanceCriminalResourceResponse(ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResourceResponse getAppearanceCriminalResourceResponseInner) {

        if (getAppearanceCriminalResourceResponseInner.getResource() != null) {
            getAppearanceCriminalResourceResponseInner.getResource()
                .forEach(
                    ((Consumer<Resource>) resource -> resource.setBookedDt(DateUtils.formatDate(resource.getBookedDt())))
                        .andThen(resource -> resource.setBookedFromTm(DateUtils.formatDate(resource.getBookedFromTm())))
                        .andThen(resource -> resource.setBookedToTm(DateUtils.formatDate(resource.getBookedToTm())))
                );
        }


        GetAppearanceCriminalResourceResponse getAppearanceCriminalResourceResponse = new GetAppearanceCriminalResourceResponse();
        ca.bc.gov.open.wsdl.pcss.two.GetAppearanceCriminalResourceResponse2 getAppearanceCriminalResourceResponse2 = new ca.bc.gov.open.wsdl.pcss.two.GetAppearanceCriminalResourceResponse2();
        getAppearanceCriminalResourceResponse2.setGetAppearanceCriminalResourceResponse(getAppearanceCriminalResourceResponseInner);
        getAppearanceCriminalResourceResponse.setGetAppearanceCriminalResourceResponse(getAppearanceCriminalResourceResponse2);

        return getAppearanceCriminalResourceResponse;

    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_APPEARANCE_SECURE)
    @ResponsePayload
    public GetAppearanceCriminalSecureResponse getAppearanceCriminalSecure(@RequestPayload GetAppearanceCriminalSecure getAppearanceCriminalSecure) throws JsonProcessingException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_APPEARANCE_SECURE);

        ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalSecureRequest getAppearanceCriminalSecureRequest =
                getAppearanceCriminalSecure.getGetAppearanceCriminalSecureRequest() != null
                        && getAppearanceCriminalSecure.getGetAppearanceCriminalSecureRequest().getGetAppearanceCriminalSecureRequest() != null
                        ? getAppearanceCriminalSecure.getGetAppearanceCriminalSecureRequest().getGetAppearanceCriminalSecureRequest()
                        : new  ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalSecureRequest();

        List<String> validation =  appearanceValidator.validateGetAppearanceCriminalSecure(getAppearanceCriminalSecureRequest);
        if (!validation.isEmpty()) {

            ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalResponse getAppearanceCriminalResourceResponse = new ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalResponse();

            getAppearanceCriminalResourceResponse.setResponseCd(Keys.FAILED_VALIDATION.toString());
            getAppearanceCriminalResourceResponse.setResponseMessageTxt(StringUtils.join(validation, ","));

            log.info(Keys.LOG_FAILED_VALIDATION, Keys.SOAP_METHOD_APPEARANCE);

            return buildAppearanceCriminalSecureResponse(getAppearanceCriminalResourceResponse);

        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_APPEARANCE_SECURE)
                        .queryParam(Keys.QUERY_AGENT_ID, getAppearanceCriminalSecureRequest.getRequestAgencyIdentifierId())
                        .queryParam(Keys.QUERY_APPLICATION_CD, getAppearanceCriminalSecureRequest.getApplicationCd())
                        .queryParam(Keys.QUERY_PART_ID, getAppearanceCriminalSecureRequest.getRequestPartId())
                        .queryParam(Keys.QUERY_REQUEST_DATE, DateUtils.formatORDSDate(getAppearanceCriminalSecureRequest.getRequestDtm()))
                        .queryParam(Keys.QUERY_APPEARANCE_ID, getAppearanceCriminalSecureRequest.getAppearanceId())
                        .queryParam(Keys.QUERY_JUSTIN_NO, getAppearanceCriminalSecureRequest.getJustinNo())
                        .queryParam(Keys.QUERY_FUTURE_FLAG, getAppearanceCriminalSecureRequest.getFutureYN())
                        .queryParam(Keys.QUERY_HISTORY_FLAG, getAppearanceCriminalSecureRequest.getHistoryYN());

        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_APPEARANCE_SECURE);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalResponse.class);

            GetAppearanceCriminalSecureResponse getAppearanceCriminalSecureResponse = buildAppearanceCriminalSecureResponse(response.getBody());

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_APPEARANCE_SECURE);

            return getAppearanceCriminalSecureResponse;

        } catch (Exception ex) {

            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_APPEARANCE_SECURE, getAppearanceCriminalSecureRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

    private GetAppearanceCriminalSecureResponse buildAppearanceCriminalSecureResponse(ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalResponse getAppearanceCriminalResponseInner) {

        if (getAppearanceCriminalResponseInner.getApprDetail() != null) {
            getAppearanceCriminalResponseInner.getApprDetail()
                .forEach(
                    ((Consumer<ca.bc.gov.open.wsdl.pcss.secure.one.ApprDetail>) apprDetail -> apprDetail.setAppearanceDt(DateUtils.formatDate(apprDetail.getAppearanceDt())))
                        .andThen(apprDetail -> apprDetail.setAppearanceTm(DateUtils.formatDate(apprDetail.getAppearanceTm())))
                );
        }

        GetAppearanceCriminalSecureResponse getAppearanceCriminalSecureResponse = new GetAppearanceCriminalSecureResponse();
        ca.bc.gov.open.wsdl.pcss.secure.two.GetAppearanceCriminalResponse getAppearanceCriminalResponse2 = new ca.bc.gov.open.wsdl.pcss.secure.two.GetAppearanceCriminalResponse();
        getAppearanceCriminalResponse2.setGetAppearanceCriminalResponse(getAppearanceCriminalResponseInner);
        getAppearanceCriminalSecureResponse.setGetAppearanceCriminalResponse(getAppearanceCriminalResponse2);

        return getAppearanceCriminalSecureResponse;

    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_SET_APPEARANCE)
    @ResponsePayload
    public SetAppearanceCriminalResponse setAppearanceCriminal(@RequestPayload SetAppearanceCriminal setAppearanceCriminal) throws JsonProcessingException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_SET_APPEARANCE);

        ca.bc.gov.open.wsdl.pcss.one.SetAppearanceCriminalRequest setAppearanceCriminalRequest = setAppearanceCriminal.getSetAppearanceCriminalRequest() != null
                        && setAppearanceCriminal.getSetAppearanceCriminalRequest().getSetAppearanceCriminalRequest() != null
                        ? setAppearanceCriminal.getSetAppearanceCriminalRequest().getSetAppearanceCriminalRequest()
                        : new ca.bc.gov.open.wsdl.pcss.one.SetAppearanceCriminalRequest();

        List<String> validation =  appearanceValidator.validateSetAppearanceCriminal(setAppearanceCriminalRequest);
        if (!validation.isEmpty()) {

            ca.bc.gov.open.wsdl.pcss.one.SetAppearanceCriminalResponse setAppearanceCriminalResponse = new ca.bc.gov.open.wsdl.pcss.one.SetAppearanceCriminalResponse();

            setAppearanceCriminalResponse.setResponseCd(Keys.FAILED_VALIDATION.toString());
            setAppearanceCriminalResponse.setResponseMessageTxt(StringUtils.join(validation, ","));

            log.info(Keys.LOG_FAILED_VALIDATION, Keys.SOAP_METHOD_APPEARANCE);

            return buildAppearanceCriminalResponse(setAppearanceCriminalResponse);

        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_APPEARANCE);

        HttpEntity<ca.bc.gov.open.wsdl.pcss.one.SetAppearanceCriminalRequest> body =
                new HttpEntity<>(setAppearanceCriminalRequest, new HttpHeaders());
        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_SET_APPEARANCE);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.SetAppearanceCriminalResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            body,
                            ca.bc.gov.open.wsdl.pcss.one.SetAppearanceCriminalResponse.class);

            SetAppearanceCriminalResponse setAppearanceCriminalResponse = buildAppearanceCriminalResponse(response.getBody());

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_SET_APPEARANCE);

            return setAppearanceCriminalResponse;

        } catch (Exception ex) {

            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_SET_APPEARANCE, setAppearanceCriminalRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

    private SetAppearanceCriminalResponse buildAppearanceCriminalResponse(ca.bc.gov.open.wsdl.pcss.one.SetAppearanceCriminalResponse setAppearanceCriminalResponseInner) {

        SetAppearanceCriminalResponse setAppearanceCriminalResponse = new SetAppearanceCriminalResponse();
        SetAppearanceCriminalResponse2 setAppearanceCriminalResponse2 = new SetAppearanceCriminalResponse2();
        setAppearanceCriminalResponse2.setSetAppearanceCriminalResponse(setAppearanceCriminalResponseInner);
        setAppearanceCriminalResponse.setSetAppearanceCriminalResponse(setAppearanceCriminalResponse2);

        return setAppearanceCriminalResponse;

    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_SET_APPEARANCE_METHOD)
    @ResponsePayload
    public SetAppearanceMethodCriminalResponse setAppearanceMethodCriminal(@RequestPayload SetAppearanceMethodCriminal setAppearanceMethodCriminal) throws JsonProcessingException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_SET_APPEARANCE_METHOD);

        ca.bc.gov.open.wsdl.pcss.one.SetAppearanceMethodCriminalRequest setAppearanceMethodCriminalRequest = setAppearanceMethodCriminal.getSetAppearanceMethodCriminalRequest() != null
                && setAppearanceMethodCriminal.getSetAppearanceMethodCriminalRequest().getSetAppearanceMethodCriminalRequest() != null
                ? setAppearanceMethodCriminal.getSetAppearanceMethodCriminalRequest().getSetAppearanceMethodCriminalRequest()
                : new ca.bc.gov.open.wsdl.pcss.one.SetAppearanceMethodCriminalRequest();


        List<String> validation =  appearanceValidator.validateSetAppearanceMethodCriminal(setAppearanceMethodCriminalRequest);
        if (!validation.isEmpty()) {

            ca.bc.gov.open.wsdl.pcss.one.SetAppearanceMethodCriminalResponse setAppearanceMethodCriminalResponse = new ca.bc.gov.open.wsdl.pcss.one.SetAppearanceMethodCriminalResponse();

            setAppearanceMethodCriminalResponse.setResponseCd(Keys.FAILED_VALIDATION.toString());
            setAppearanceMethodCriminalResponse.setResponseMessageTxt(StringUtils.join(validation, ","));

            log.info(Keys.LOG_FAILED_VALIDATION, Keys.SOAP_METHOD_APPEARANCE);

            return buildAppearanceMethodCriminalResponse(setAppearanceMethodCriminalResponse);

        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_APPEARANCE_METHOD);

        HttpEntity<ca.bc.gov.open.wsdl.pcss.one.SetAppearanceMethodCriminalRequest> body =
                new HttpEntity<>(setAppearanceMethodCriminalRequest, new HttpHeaders());
        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_SET_APPEARANCE_METHOD);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.SetAppearanceMethodCriminalResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            body,
                            ca.bc.gov.open.wsdl.pcss.one.SetAppearanceMethodCriminalResponse.class);

            SetAppearanceMethodCriminalResponse setAppearanceMethodCriminalResponse = buildAppearanceMethodCriminalResponse(response.getBody());

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_SET_APPEARANCE_METHOD);

            return setAppearanceMethodCriminalResponse;

        } catch (Exception ex) {

            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_SET_APPEARANCE_METHOD, setAppearanceMethodCriminalRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

    private SetAppearanceMethodCriminalResponse buildAppearanceMethodCriminalResponse(ca.bc.gov.open.wsdl.pcss.one.SetAppearanceMethodCriminalResponse setAppearanceMethodCriminalResponseInner) {

        SetAppearanceMethodCriminalResponse setAppearanceMethodCriminalResponse = new SetAppearanceMethodCriminalResponse();
        SetAppearanceMethodCriminalResponse2 setAppearanceMethodCriminalResponse2 = new SetAppearanceMethodCriminalResponse2();
        setAppearanceMethodCriminalResponse2.setSetAppearanceMethodCriminalResponse(setAppearanceMethodCriminalResponseInner);
        setAppearanceMethodCriminalResponse.setSetAppearanceMethodCriminalResponse(setAppearanceMethodCriminalResponse2);

        return setAppearanceMethodCriminalResponse;

    }

}
