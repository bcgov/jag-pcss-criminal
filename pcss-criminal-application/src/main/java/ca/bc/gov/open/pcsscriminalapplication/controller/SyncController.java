package ca.bc.gov.open.pcsscriminalapplication.controller;

import ca.bc.gov.open.pcsscriminalapplication.Keys;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.service.SyncValidator;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.two.*;
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
public class SyncController {

    private final RestTemplate restTemplate;
    private final PcssProperties pcssProperties;
    private final LogBuilder logBuilder;
    private final SyncValidator syncValidator;

    public SyncController(RestTemplate restTemplate, PcssProperties pcssProperties, LogBuilder logBuilder, SyncValidator syncValidator) {
        this.restTemplate = restTemplate;
        this.pcssProperties = pcssProperties;
        this.logBuilder = logBuilder;
        this.syncValidator = syncValidator;
    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_SYNC_APPEARANCE)
    @ResponsePayload
    public GetSyncCriminalAppearanceResponse getSyncCriminalAppearance(@RequestPayload GetSyncCriminalAppearance getSyncCriminalAppearance) throws JsonProcessingException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_SYNC_APPEARANCE);

        ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceRequest getSyncCriminalAppearanceRequest =
                getSyncCriminalAppearance.getGetSyncCriminalAppearanceRequest() != null
                        && getSyncCriminalAppearance.getGetSyncCriminalAppearanceRequest().getGetSyncCriminalAppearanceRequest() != null
                        ? getSyncCriminalAppearance.getGetSyncCriminalAppearanceRequest().getGetSyncCriminalAppearanceRequest()
                        : new ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceRequest();

        List<String> validation = syncValidator.validateGetSyncCriminalAppearance(getSyncCriminalAppearanceRequest);
        if (!validation.isEmpty()) {

            ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceResponse getSyncCriminalAppearanceResponseFailed = new ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceResponse();
            getSyncCriminalAppearanceResponseFailed.setResponseCd(Keys.FAILED_VALIDATION.toString());
            getSyncCriminalAppearanceResponseFailed.setResponseMessageTxt(StringUtils.join(validation, ","));

            log.info(Keys.LOG_FAILED_VALIDATION, Keys.SOAP_METHOD_SYNC_APPEARANCE);

            return buildAppearanceResponse(getSyncCriminalAppearanceResponseFailed);

        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_SYNC_APPEARANCE)
                        .queryParam(Keys.QUERY_AGENT_ID, getSyncCriminalAppearanceRequest.getRequestAgencyIdentifierId())
                        .queryParam(Keys.QUERY_PART_ID, getSyncCriminalAppearanceRequest.getRequestAgencyIdentifierId())
                        .queryParam(Keys.QUERY_REQUEST_DATE, getSyncCriminalAppearanceRequest.getRequestDtm())
                        .queryParam(Keys.QUERY_SYNC_TO_DATE, getSyncCriminalAppearanceRequest.getProcessUpToDtm());

        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_SYNC_APPEARANCE);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceResponse.class);

            ca.bc.gov.open.wsdl.pcss.two.GetSyncCriminalAppearanceResponse getSyncCriminalAppearanceResponse = buildAppearanceResponse(response.getBody());

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_SYNC_APPEARANCE);

            return getSyncCriminalAppearanceResponse;

        } catch (Exception ex) {
            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_SYNC_APPEARANCE, getSyncCriminalAppearanceRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

    private  ca.bc.gov.open.wsdl.pcss.two.GetSyncCriminalAppearanceResponse buildAppearanceResponse(ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceResponse getSyncCriminalAppearanceResponseInner) {

        ca.bc.gov.open.wsdl.pcss.two.GetSyncCriminalAppearanceResponse getSyncCriminalAppearanceResponse = new ca.bc.gov.open.wsdl.pcss.two.GetSyncCriminalAppearanceResponse();
        GetSyncCriminalAppearanceResponse2 getSyncCriminalAppearanceResponse2 = new GetSyncCriminalAppearanceResponse2();
        getSyncCriminalAppearanceResponse2.setGetSyncCriminalAppearanceResponse(getSyncCriminalAppearanceResponseInner);
        getSyncCriminalAppearanceResponse.setGetSyncCriminalAppearanceResponse(getSyncCriminalAppearanceResponse2);

        return getSyncCriminalAppearanceResponse;

    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_SYNC_HEARING)
    @ResponsePayload
    public GetSyncCriminalHearingRestrictionResponse getSyncCriminalHearingRestriction(@RequestPayload GetSyncCriminalHearingRestriction getSyncCriminalHearingRestriction) throws JsonProcessingException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_SYNC_HEARING);

        ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionRequest getSyncCriminalHearingRestrictionRequest =
                getSyncCriminalHearingRestriction.getGetSyncCriminalHearingRestrictionRequest() != null
                        && getSyncCriminalHearingRestriction.getGetSyncCriminalHearingRestrictionRequest().getGetSyncCriminalHearingRestrictionRequest() != null
                        ? getSyncCriminalHearingRestriction.getGetSyncCriminalHearingRestrictionRequest().getGetSyncCriminalHearingRestrictionRequest()
                        : new ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionRequest();

        List<String> validation = syncValidator.validateGetSyncCriminalHearingRestriction(getSyncCriminalHearingRestrictionRequest);
        if (!validation.isEmpty()) {

            ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionResponse getSyncCriminalAppearanceResponseFailed = new ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionResponse();
            getSyncCriminalAppearanceResponseFailed.setResponseCd(Keys.FAILED_VALIDATION.toString());
            getSyncCriminalAppearanceResponseFailed.setResponseMessageTxt(StringUtils.join(validation, ","));

            log.info(Keys.LOG_FAILED_VALIDATION, Keys.SOAP_METHOD_SYNC_HEARING);

            return buildHearingResponse(getSyncCriminalAppearanceResponseFailed);

        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_SYNC_HEARING)
                        .queryParam(Keys.QUERY_AGENT_ID, getSyncCriminalHearingRestrictionRequest.getRequestAgencyIdentifierId())
                        .queryParam(Keys.QUERY_PART_ID, getSyncCriminalHearingRestrictionRequest.getRequestAgencyIdentifierId())
                        .queryParam(Keys.QUERY_REQUEST_DATE, getSyncCriminalHearingRestrictionRequest.getRequestDtm())
                        .queryParam(Keys.QUERY_SYNC_TO_DATE, getSyncCriminalHearingRestrictionRequest.getProcessUpToDtm());

        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_SYNC_HEARING);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionResponse.class);

            ca.bc.gov.open.wsdl.pcss.two.GetSyncCriminalHearingRestrictionResponse getSyncCriminalHearingRestrictionResponse = buildHearingResponse(response.getBody());

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_SYNC_HEARING);

            return getSyncCriminalHearingRestrictionResponse;

        } catch (Exception ex) {
            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_SYNC_HEARING, getSyncCriminalHearingRestrictionRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

    private ca.bc.gov.open.wsdl.pcss.two.GetSyncCriminalHearingRestrictionResponse buildHearingResponse(ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionResponse getSyncCriminalHearingRestrictionResponseInner) {

        ca.bc.gov.open.wsdl.pcss.two.GetSyncCriminalHearingRestrictionResponse getSyncCriminalHearingRestrictionResponse = new ca.bc.gov.open.wsdl.pcss.two.GetSyncCriminalHearingRestrictionResponse();
        GetSyncCriminalHearingRestrictionResponse2 getSyncCriminalHearingRestrictionResponse2 = new GetSyncCriminalHearingRestrictionResponse2();
        getSyncCriminalHearingRestrictionResponse2.setGetSyncCriminalHearingRestrictionResponse(getSyncCriminalHearingRestrictionResponseInner);
        getSyncCriminalHearingRestrictionResponse.setGetSyncCriminalHearingRestrictionResponse(getSyncCriminalHearingRestrictionResponse2);

        return getSyncCriminalHearingRestrictionResponse;

    }

}
