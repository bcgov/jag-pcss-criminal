package ca.bc.gov.open.pcsscriminalapplication.controller;

import ca.bc.gov.open.pcsscriminalapplication.Keys;
import ca.bc.gov.open.pcsscriminalapplication.exception.BadDateException;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.two.*;
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
public class SyncController {

    private final RestTemplate restTemplate;
    private final PcssProperties pcssProperties;
    private final LogBuilder logBuilder;

    public SyncController(RestTemplate restTemplate, PcssProperties pcssProperties, LogBuilder logBuilder) {
        this.restTemplate = restTemplate;
        this.pcssProperties = pcssProperties;
        this.logBuilder = logBuilder;
    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_SYNC_APPEARANCE)
    @ResponsePayload
    public GetSyncCriminalAppearanceResponse getSyncCriminalAppearance(@RequestPayload GetSyncCriminalAppearance getSyncCriminalAppearance) throws JsonProcessingException, BadDateException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_SYNC_APPEARANCE);

        ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceRequest getSyncCriminalAppearanceRequest =
                getSyncCriminalAppearance.getGetSyncCriminalAppearanceRequest() != null
                        && getSyncCriminalAppearance.getGetSyncCriminalAppearanceRequest().getGetSyncCriminalAppearanceRequest() != null
                        ? getSyncCriminalAppearance.getGetSyncCriminalAppearanceRequest().getGetSyncCriminalAppearanceRequest()
                        : new ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceRequest();

        if (getSyncCriminalAppearanceRequest.getRequestDtm() == null) {

            log.warn(logBuilder.writeLogMessage(Keys.DATE_ERROR_MESSAGE, Keys.SOAP_METHOD_SYNC_APPEARANCE, getSyncCriminalAppearance, ""));
            throw new BadDateException();

        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_SYNC_APPEARANCE)
                        .queryParam(Keys.QUERY_AGENCY_IDENTIFIER, getSyncCriminalAppearanceRequest.getRequestAgencyIdentifierId())
                        .queryParam(Keys.QUERY_PART_ID, getSyncCriminalAppearanceRequest.getRequestAgencyIdentifierId())
                        .queryParam(Keys.QUERY_REQUEST_DATE, getSyncCriminalAppearanceRequest.getRequestDtm())
                        .queryParam(Keys.QUERY_TO_DATE, getSyncCriminalAppearanceRequest.getProcessUpToDtm());

        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_SYNC_APPEARANCE);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceResponse.class);

            ca.bc.gov.open.wsdl.pcss.two.GetSyncCriminalAppearanceResponse getSyncCriminalAppearanceResponse = new ca.bc.gov.open.wsdl.pcss.two.GetSyncCriminalAppearanceResponse();
            GetSyncCriminalAppearanceResponse2 getSyncCriminalAppearanceResponse2 = new GetSyncCriminalAppearanceResponse2();
            getSyncCriminalAppearanceResponse2.setGetSyncCriminalAppearanceResponse(response.getBody());
            getSyncCriminalAppearanceResponse.setGetSyncCriminalAppearanceResponse(getSyncCriminalAppearanceResponse2);

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_SYNC_APPEARANCE);

            return getSyncCriminalAppearanceResponse;

        } catch (Exception ex) {
            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_SYNC_APPEARANCE, getSyncCriminalAppearanceRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_SYNC_HEARING)
    @ResponsePayload
    public GetSyncCriminalHearingRestrictionResponse getSyncCriminalHearingRestriction(@RequestPayload GetSyncCriminalHearingRestriction getSyncCriminalHearingRestriction) throws JsonProcessingException, BadDateException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_SYNC_HEARING);

        ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionRequest getSyncCriminalHearingRestrictionRequest =
                getSyncCriminalHearingRestriction.getGetSyncCriminalHearingRestrictionRequest() != null
                        && getSyncCriminalHearingRestriction.getGetSyncCriminalHearingRestrictionRequest().getGetSyncCriminalHearingRestrictionRequest() != null
                        ? getSyncCriminalHearingRestriction.getGetSyncCriminalHearingRestrictionRequest().getGetSyncCriminalHearingRestrictionRequest()
                        : new ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionRequest();

        if (getSyncCriminalHearingRestrictionRequest.getRequestDtm() == null) {

            log.warn(logBuilder.writeLogMessage(Keys.DATE_ERROR_MESSAGE, Keys.SOAP_METHOD_SYNC_HEARING, getSyncCriminalHearingRestriction, ""));
            throw new BadDateException();

        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_SYNC_HEARING)
                        .queryParam(Keys.QUERY_AGENCY_IDENTIFIER, getSyncCriminalHearingRestrictionRequest.getRequestAgencyIdentifierId())
                        .queryParam(Keys.QUERY_PART_ID, getSyncCriminalHearingRestrictionRequest.getRequestAgencyIdentifierId())
                        .queryParam(Keys.QUERY_REQUEST_DATE, getSyncCriminalHearingRestrictionRequest.getRequestDtm())
                        .queryParam(Keys.QUERY_TO_DATE, getSyncCriminalHearingRestrictionRequest.getProcessUpToDtm());

        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_SYNC_HEARING);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionResponse.class);

            ca.bc.gov.open.wsdl.pcss.two.GetSyncCriminalHearingRestrictionResponse getSyncCriminalHearingRestrictionResponse = new ca.bc.gov.open.wsdl.pcss.two.GetSyncCriminalHearingRestrictionResponse();
            GetSyncCriminalHearingRestrictionResponse2 getSyncCriminalHearingRestrictionResponse2 = new GetSyncCriminalHearingRestrictionResponse2();
            getSyncCriminalHearingRestrictionResponse2.setGetSyncCriminalHearingRestrictionResponse(response.getBody());
            getSyncCriminalHearingRestrictionResponse.setGetSyncCriminalHearingRestrictionResponse(getSyncCriminalHearingRestrictionResponse2);

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_SYNC_HEARING);

            return getSyncCriminalHearingRestrictionResponse;

        } catch (Exception ex) {
            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_SYNC_HEARING, getSyncCriminalHearingRestrictionRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

}
