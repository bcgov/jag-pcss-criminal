package ca.bc.gov.open.pcsscriminalapplication.controller;

import ca.bc.gov.open.pcsscriminalapplication.Keys;
import ca.bc.gov.open.pcsscriminalapplication.exception.BadDateException;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.secure.two.*;
import ca.bc.gov.open.wsdl.pcss.two.*;
import ca.bc.gov.open.wsdl.pcss.two.GetAppearanceCriminalApprMethodResponse;
import ca.bc.gov.open.wsdl.pcss.two.GetAppearanceCriminalCountResponse;
import ca.bc.gov.open.wsdl.pcss.two.GetAppearanceCriminalResponse;
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

@Slf4j
@Endpoint
@EnableConfigurationProperties(PcssProperties.class)
public class AppearanceController {

    private final RestTemplate restTemplate;
    private final PcssProperties pcssProperties;
    private final LogBuilder logBuilder;

    public AppearanceController(RestTemplate restTemplate, PcssProperties pcssProperties, LogBuilder logBuilder) {
        this.restTemplate = restTemplate;
        this.pcssProperties = pcssProperties;
        this.logBuilder = logBuilder;
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

            log.warn(logBuilder.writeLogMessage(Keys.DATE_ERROR_MESSAGE, Keys.SOAP_METHOD_APPEARANCE, getAppearanceCriminal, ""));
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

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResponse.class);

            GetAppearanceCriminalResponse getAppearanceCriminalResponse = new GetAppearanceCriminalResponse();
            GetAppearanceCriminalResponse2 getAppearanceCriminalResponse2 = new GetAppearanceCriminalResponse2();
            getAppearanceCriminalResponse2.setGetAppearanceCriminalResponse(response.getBody());
            getAppearanceCriminalResponse.setGetAppearanceCriminalResponse(getAppearanceCriminalResponse2);

            return getAppearanceCriminalResponse;

        } catch (Exception ex) {
            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_APPEARANCE, getAppearanceCriminalRequest, ex.getMessage()));
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

            log.warn(logBuilder.writeLogMessage(Keys.DATE_ERROR_MESSAGE, Keys.SOAP_METHOD_APPEARANCE_APPR_METHOD, getAppearanceCriminalApprMethod, ""));
            throw new BadDateException();

        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_APPEARANCE_METHOD)
                        .queryParam(Keys.QUERY_AGENT_ID, getAppearanceCriminalApprMethodRequest.getRequestAgencyIdentifierId())
                        .queryParam(Keys.QUERY_PART_ID, getAppearanceCriminalApprMethodRequest.getRequestPartId())
                        .queryParam(Keys.QUERY_REQUEST_DATE, getAppearanceCriminalApprMethodRequest.getRequestDtm())
                        .queryParam(Keys.QUERY_APPEARANCE_ID, getAppearanceCriminalApprMethodRequest.getAppearanceId());

        try {

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalApprMethodResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalApprMethodResponse.class);

            GetAppearanceCriminalApprMethodResponse getAppearanceCriminalApprMethodResponse = new GetAppearanceCriminalApprMethodResponse();
            GetAppearanceCriminalApprMethodResponse2 getAppearanceCriminalApprMethodResponse2 = new GetAppearanceCriminalApprMethodResponse2();
            getAppearanceCriminalApprMethodResponse2.setGetAppearanceCriminalApprMethodResponse(response.getBody());
            getAppearanceCriminalApprMethodResponse.setGetAppearanceCriminalApprMethodResponse(getAppearanceCriminalApprMethodResponse2);

            return getAppearanceCriminalApprMethodResponse;

        } catch (Exception ex) {

            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_APPEARANCE_APPR_METHOD, getAppearanceCriminalApprMethodRequest, ex.getMessage()));
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

            log.warn(logBuilder.writeLogMessage(Keys.DATE_ERROR_MESSAGE, Keys.SOAP_METHOD_APPEARANCE_APPR_METHOD_SECURE, getAppearanceCriminalApprMethodSecure, ""));
            throw new BadDateException();

        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_APPEARANCE_METHOD_SECURE)
                        .queryParam(Keys.QUERY_AGENT_ID, getAppearanceCriminalApprMethodSecureRequest.getRequestAgencyIdentifierId())
                        .queryParam(Keys.QUERY_PART_ID, getAppearanceCriminalApprMethodSecureRequest.getRequestPartId())
                        .queryParam(Keys.QUERY_REQUEST_DATE, getAppearanceCriminalApprMethodSecureRequest.getRequestDtm())
                        .queryParam(Keys.QUERY_APPEARANCE_CD, getAppearanceCriminalApprMethodSecureRequest.getApplicationCd())
                        .queryParam(Keys.QUERY_APPEARANCE_ID, getAppearanceCriminalApprMethodSecureRequest.getAppearanceId());

        try {

            HttpEntity<ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalApprMethodResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalApprMethodResponse.class);

            GetAppearanceCriminalApprMethodSecureResponse getAppearanceCriminalApprMethodSecureResponse = new GetAppearanceCriminalApprMethodSecureResponse();
            ca.bc.gov.open.wsdl.pcss.secure.two.GetAppearanceCriminalApprMethodResponse getAppearanceCriminalApprMethodResponse2 = new ca.bc.gov.open.wsdl.pcss.secure.two.GetAppearanceCriminalApprMethodResponse();
            getAppearanceCriminalApprMethodResponse2.setGetAppearanceCriminalApprMethodResponse(response.getBody());
            getAppearanceCriminalApprMethodSecureResponse.setGetAppearanceCriminalApprMethodResponse(getAppearanceCriminalApprMethodResponse2);

            return getAppearanceCriminalApprMethodSecureResponse;

        } catch (Exception ex) {

            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_APPEARANCE_APPR_METHOD_SECURE, getAppearanceCriminalApprMethodSecureRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_APPEARANCE_COUNT)
    @ResponsePayload
    public GetAppearanceCriminalCountResponse getAppearanceCriminalCount(@RequestPayload GetAppearanceCriminalCount getAppearanceCriminalCount) throws JsonProcessingException, BadDateException {

        log.info("Get Appearance Criminal Count");

        ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalCountRequest getAppearanceCriminalCountRequest =
                getAppearanceCriminalCount.getGetAppearanceCriminalCountRequest() != null
                        && getAppearanceCriminalCount.getGetAppearanceCriminalCountRequest().getGetAppearanceCriminalCountRequest() != null
                        ? getAppearanceCriminalCount.getGetAppearanceCriminalCountRequest().getGetAppearanceCriminalCountRequest()
                        : new  ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalCountRequest();

        if (getAppearanceCriminalCountRequest.getRequestDtm() == null) {

            log.warn(logBuilder.writeLogMessage(Keys.DATE_ERROR_MESSAGE, Keys.SOAP_METHOD_APPEARANCE_COUNT, getAppearanceCriminalCount, ""));
            throw new BadDateException();

        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_APPEARANCE_COUNT)
                        .queryParam(Keys.QUERY_AGENT_ID, getAppearanceCriminalCountRequest.getRequestAgencyIdentifierId())
                        .queryParam(Keys.QUERY_PART_ID, getAppearanceCriminalCountRequest.getRequestPartId())
                        .queryParam(Keys.QUERY_REQUEST_DATE, getAppearanceCriminalCountRequest.getRequestDtm())
                        .queryParam(Keys.QUERY_APPEARANCE_ID, getAppearanceCriminalCountRequest.getAppearanceId());

        try {

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalCountResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalCountResponse.class);

            GetAppearanceCriminalCountResponse getAppearanceCriminalCountResponse = new GetAppearanceCriminalCountResponse();
            ca.bc.gov.open.wsdl.pcss.two.GetAppearanceCriminalCountResponse2 getAppearanceCriminalCountResponse2 = new ca.bc.gov.open.wsdl.pcss.two.GetAppearanceCriminalCountResponse2();
            getAppearanceCriminalCountResponse2.setGetAppearanceCriminalCountResponse(response.getBody());
            getAppearanceCriminalCountResponse.setGetAppearanceCriminalCountResponse(getAppearanceCriminalCountResponse2);

            return getAppearanceCriminalCountResponse;

        } catch (Exception ex) {

            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_APPEARANCE_COUNT, getAppearanceCriminalCountRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_APPEARANCE_COUNT_SECURE)
    @ResponsePayload
    public GetAppearanceCriminalCountSecureResponse getAppearanceCriminalCountSecure(@RequestPayload GetAppearanceCriminalCountSecure getAppearanceCriminalCountSecure) throws JsonProcessingException, BadDateException {

        log.info("Get Appearance Criminal Count Secure");

        ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalCountSecureRequest getAppearanceCriminalCountSecureRequest =
                getAppearanceCriminalCountSecure.getGetAppearanceCriminalCountSecureRequest() != null
                        && getAppearanceCriminalCountSecure.getGetAppearanceCriminalCountSecureRequest().getGetAppearanceCriminalCountSecureRequest() != null
                        ? getAppearanceCriminalCountSecure.getGetAppearanceCriminalCountSecureRequest().getGetAppearanceCriminalCountSecureRequest()
                        : new  ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalCountSecureRequest();

        if (getAppearanceCriminalCountSecureRequest.getRequestDtm() == null) {

            log.warn(logBuilder.writeLogMessage(Keys.DATE_ERROR_MESSAGE, Keys.SOAP_METHOD_APPEARANCE_COUNT_SECURE, getAppearanceCriminalCountSecure, ""));
            throw new BadDateException();

        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_APPEARANCE_COUNT_SECURE)
                        .queryParam(Keys.QUERY_AGENT_ID, getAppearanceCriminalCountSecureRequest.getRequestAgencyIdentifierId())
                        .queryParam(Keys.QUERY_PART_ID, getAppearanceCriminalCountSecureRequest.getRequestPartId())
                        .queryParam(Keys.QUERY_REQUEST_DATE, getAppearanceCriminalCountSecureRequest.getRequestDtm())
                        .queryParam(Keys.QUERY_APPEARANCE_CD, getAppearanceCriminalCountSecureRequest.getApplicationCd())
                        .queryParam(Keys.QUERY_APPEARANCE_ID, getAppearanceCriminalCountSecureRequest.getAppearanceId());

        try {

            HttpEntity<ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalCountResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalCountResponse.class);

            GetAppearanceCriminalCountSecureResponse getAppearanceCriminalCountSecureResponse = new GetAppearanceCriminalCountSecureResponse();
            ca.bc.gov.open.wsdl.pcss.secure.two.GetAppearanceCriminalCountResponse getAppearanceCriminalCountResponse2 = new ca.bc.gov.open.wsdl.pcss.secure.two.GetAppearanceCriminalCountResponse();
            getAppearanceCriminalCountResponse2.setGetAppearanceCriminalCountResponse(response.getBody());
            getAppearanceCriminalCountSecureResponse.setGetAppearanceCriminalCountResponse(getAppearanceCriminalCountResponse2);

            return getAppearanceCriminalCountSecureResponse;

        } catch (Exception ex) {

            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_APPEARANCE_COUNT_SECURE, getAppearanceCriminalCountSecureRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_APPEARANCE_RESOURCE)
    @ResponsePayload
    public GetAppearanceCriminalResourceResponse getAppearanceCriminalResource(@RequestPayload GetAppearanceCriminalResource getAppearanceCriminalResource) throws JsonProcessingException, BadDateException {

        log.info("Get Appearance Criminal Resource");

        ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResourceRequest getAppearanceCriminalResourceRequest =
                getAppearanceCriminalResource.getGetAppearanceCriminalResourceRequest() != null
                        && getAppearanceCriminalResource.getGetAppearanceCriminalResourceRequest().getGetAppearanceCriminalResourceRequest() != null
                        ? getAppearanceCriminalResource.getGetAppearanceCriminalResourceRequest().getGetAppearanceCriminalResourceRequest()
                        : new  ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResourceRequest();

        if (getAppearanceCriminalResourceRequest.getRequestDtm() == null) {

            log.warn(logBuilder.writeLogMessage(Keys.DATE_ERROR_MESSAGE, Keys.SOAP_METHOD_APPEARANCE_RESOURCE, getAppearanceCriminalResource, ""));
            throw new BadDateException();

        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_APPEARANCE_RESOURCE)
                        .queryParam(Keys.QUERY_AGENT_ID, getAppearanceCriminalResourceRequest.getRequestAgencyIdentifierId())
                        .queryParam(Keys.QUERY_PART_ID, getAppearanceCriminalResourceRequest.getRequestPartId())
                        .queryParam(Keys.QUERY_REQUEST_DATE, getAppearanceCriminalResourceRequest.getRequestDtm())
                        .queryParam(Keys.QUERY_APPEARANCE_ID, getAppearanceCriminalResourceRequest.getAppearanceId());

        try {

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResourceResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResourceResponse.class);

            GetAppearanceCriminalResourceResponse getAppearanceCriminalResourceResponse = new GetAppearanceCriminalResourceResponse();
            ca.bc.gov.open.wsdl.pcss.two.GetAppearanceCriminalResourceResponse2 getAppearanceCriminalResourceResponse2 = new ca.bc.gov.open.wsdl.pcss.two.GetAppearanceCriminalResourceResponse2();
            getAppearanceCriminalResourceResponse2.setGetAppearanceCriminalResourceResponse(response.getBody());
            getAppearanceCriminalResourceResponse.setGetAppearanceCriminalResourceResponse(getAppearanceCriminalResourceResponse2);

            return getAppearanceCriminalResourceResponse;

        } catch (Exception ex) {

            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_APPEARANCE_RESOURCE, getAppearanceCriminalResourceRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_APPEARANCE_SECURE)
    @ResponsePayload
    public GetAppearanceCriminalSecureResponse getAppearanceCriminalSecure(@RequestPayload GetAppearanceCriminalSecure getAppearanceCriminalSecure) throws JsonProcessingException, BadDateException {

        log.info("Get Appearance Secure");

        ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalSecureRequest getAppearanceCriminalSecureRequest =
                getAppearanceCriminalSecure.getGetAppearanceCriminalSecureRequest() != null
                        && getAppearanceCriminalSecure.getGetAppearanceCriminalSecureRequest().getGetAppearanceCriminalSecureRequest() != null
                        ? getAppearanceCriminalSecure.getGetAppearanceCriminalSecureRequest().getGetAppearanceCriminalSecureRequest()
                        : new  ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalSecureRequest();

        if (getAppearanceCriminalSecureRequest.getRequestDtm() == null) {

            log.warn(logBuilder.writeLogMessage(Keys.DATE_ERROR_MESSAGE, Keys.SOAP_METHOD_APPEARANCE_SECURE, getAppearanceCriminalSecure, ""));
            throw new BadDateException();

        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_APPEARANCE_SECURE)
                        .queryParam(Keys.QUERY_AGENT_ID, getAppearanceCriminalSecureRequest.getRequestAgencyIdentifierId())
                        .queryParam(Keys.QUERY_PART_ID, getAppearanceCriminalSecureRequest.getRequestPartId())
                        .queryParam(Keys.QUERY_REQUEST_DATE, getAppearanceCriminalSecureRequest.getRequestDtm())
                        .queryParam(Keys.QUERY_JUSTIN_NO, getAppearanceCriminalSecureRequest.getJustinNo())
                        .queryParam(Keys.QUERY_FUTURE_FLAG, getAppearanceCriminalSecureRequest.getFutureYN())
                        .queryParam(Keys.QUERY_HISTORY_FLAG, getAppearanceCriminalSecureRequest.getHistoryYN());

        try {

            HttpEntity<ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalResponse.class);

            GetAppearanceCriminalSecureResponse getAppearanceCriminalSecureResponse = new GetAppearanceCriminalSecureResponse();
            ca.bc.gov.open.wsdl.pcss.secure.two.GetAppearanceCriminalResponse getAppearanceCriminalResponse2 = new ca.bc.gov.open.wsdl.pcss.secure.two.GetAppearanceCriminalResponse();
            getAppearanceCriminalResponse2.setGetAppearanceCriminalResponse(response.getBody());
            getAppearanceCriminalSecureResponse.setGetAppearanceCriminalResponse(getAppearanceCriminalResponse2);

            return getAppearanceCriminalSecureResponse;

        } catch (Exception ex) {

            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_APPEARANCE_SECURE, getAppearanceCriminalSecureRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_SET_APPEARANCE)
    @ResponsePayload
    public SetAppearanceCriminalResponse setAppearanceCriminal(@RequestPayload SetAppearanceCriminal setAppearanceCriminal) throws JsonProcessingException {

        ca.bc.gov.open.wsdl.pcss.one.SetAppearanceCriminalRequest setAppearanceCriminalRequest = setAppearanceCriminal.getSetAppearanceCriminalRequest() != null
                        && setAppearanceCriminal.getSetAppearanceCriminalRequest().getSetAppearanceCriminalRequest() != null
                        ? setAppearanceCriminal.getSetAppearanceCriminalRequest().getSetAppearanceCriminalRequest()
                        : new ca.bc.gov.open.wsdl.pcss.one.SetAppearanceCriminalRequest();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_APPEARANCE);

        HttpEntity<ca.bc.gov.open.wsdl.pcss.one.SetAppearanceCriminalRequest> body =
                new HttpEntity<>(setAppearanceCriminalRequest, new HttpHeaders());
        try {

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.SetAppearanceCriminalResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            body,
                            ca.bc.gov.open.wsdl.pcss.one.SetAppearanceCriminalResponse.class);

            SetAppearanceCriminalResponse setAppearanceCriminalResponse = new SetAppearanceCriminalResponse();
            SetAppearanceCriminalResponse2 setAppearanceCriminalResponse2 = new SetAppearanceCriminalResponse2();
            setAppearanceCriminalResponse2.setSetAppearanceCriminalResponse(response.getBody());
            setAppearanceCriminalResponse.setSetAppearanceCriminalResponse(setAppearanceCriminalResponse2);

            return setAppearanceCriminalResponse;

        } catch (Exception ex) {

            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_SET_APPEARANCE, setAppearanceCriminalRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_SET_APPEARANCE_METHOD)
    @ResponsePayload
    public SetAppearanceMethodCriminalResponse setAppearanceMethodCriminal(@RequestPayload SetAppearanceMethodCriminal setAppearanceMethodCriminal) throws JsonProcessingException {

        ca.bc.gov.open.wsdl.pcss.one.SetAppearanceMethodCriminalRequest setAppearanceMethodCriminalRequest = setAppearanceMethodCriminal.getSetAppearanceMethodCriminalRequest() != null
                && setAppearanceMethodCriminal.getSetAppearanceMethodCriminalRequest().getSetAppearanceMethodCriminalRequest() != null
                ? setAppearanceMethodCriminal.getSetAppearanceMethodCriminalRequest().getSetAppearanceMethodCriminalRequest()
                : new ca.bc.gov.open.wsdl.pcss.one.SetAppearanceMethodCriminalRequest();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_APPEARANCE_METHOD);

        HttpEntity<ca.bc.gov.open.wsdl.pcss.one.SetAppearanceMethodCriminalRequest> body =
                new HttpEntity<>(setAppearanceMethodCriminalRequest, new HttpHeaders());
        try {

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.SetAppearanceMethodCriminalResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            body,
                            ca.bc.gov.open.wsdl.pcss.one.SetAppearanceMethodCriminalResponse.class);

            SetAppearanceMethodCriminalResponse setAppearanceMethodCriminalResponse = new SetAppearanceMethodCriminalResponse();
            SetAppearanceMethodCriminalResponse2 setAppearanceMethodCriminalResponse2 = new SetAppearanceMethodCriminalResponse2();
            setAppearanceMethodCriminalResponse2.setSetAppearanceMethodCriminalResponse(response.getBody());
            setAppearanceMethodCriminalResponse.setSetAppearanceMethodCriminalResponse(setAppearanceMethodCriminalResponse2);

            return setAppearanceMethodCriminalResponse;

        } catch (Exception ex) {

            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_SET_APPEARANCE_METHOD, setAppearanceMethodCriminalRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

}
