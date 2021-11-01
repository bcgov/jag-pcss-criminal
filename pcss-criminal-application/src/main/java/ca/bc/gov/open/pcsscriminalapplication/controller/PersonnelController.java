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
public class PersonnelController {

    private final RestTemplate restTemplate;
    private final PcssProperties pcssProperties;
    private final LogBuilder logBuilder;

    public PersonnelController(RestTemplate restTemplate, PcssProperties pcssProperties, LogBuilder logBuilder) {
        this.restTemplate = restTemplate;
        this.pcssProperties = pcssProperties;
        this.logBuilder = logBuilder;
    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_PERSONNEL_AVAILABILITY)
    @ResponsePayload
    public GetPersonnelAvailabilityResponse getPersonnelAvailability(@RequestPayload GetPersonnelAvailability getPersonnelAvailability) throws JsonProcessingException, BadDateException {

        log.info("Get Personnel Availability");

        ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailabilityRequest getPersonnelAvailabilityRequest =
                getPersonnelAvailability.getGetPersonnelAvailabilityRequest() != null
                        && getPersonnelAvailability.getGetPersonnelAvailabilityRequest().getGetPersonnelAvailabilityRequest() != null
                        ? getPersonnelAvailability.getGetPersonnelAvailabilityRequest().getGetPersonnelAvailabilityRequest()
                        : new ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailabilityRequest();

        if (getPersonnelAvailabilityRequest.getRequestDtm() == null) {

            log.warn(logBuilder.writeLogMessage(Keys.DATE_ERROR_MESSAGE, Keys.SOAP_METHOD_PERSONNEL_AVAILABILITY, getPersonnelAvailability, ""));
            throw new BadDateException();

        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_PERSONNEL_AVAILABILITY)
                        .queryParam(Keys.QUERY_AGENCY_IDENTIFIER, getPersonnelAvailabilityRequest.getRequestAgencyIdentifierId())
                        .queryParam(Keys.QUERY_PART_ID, getPersonnelAvailabilityRequest.getRequestPartId())
                        .queryParam(Keys.QUERY_REQUEST_DATE, getPersonnelAvailabilityRequest.getRequestDtm())
                        .queryParam(Keys.QUERY_PERSON_CD, getPersonnelAvailabilityRequest.getPersonTypeCd().value())
                        .queryParam(Keys.QUERY_PART_ID, getPersonnelAvailabilityRequest.getPartIdList())
                        .queryParam(Keys.QUERY_FROM_DATE, getPersonnelAvailabilityRequest.getFromDt())
                        .queryParam(Keys.QUERY_TO_DATE, getPersonnelAvailabilityRequest.getToDt());

        try {

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailabilityResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailabilityResponse.class);

            GetPersonnelAvailabilityResponse getPersonnelAvailabilityResponse = new GetPersonnelAvailabilityResponse();
            GetPersonnelAvailabilityResponse2 getPersonnelAvailabilityResponse2 = new GetPersonnelAvailabilityResponse2();
            getPersonnelAvailabilityResponse2.setGetPersonnelAvailabilityResponse(response.getBody());
            getPersonnelAvailabilityResponse.setGetPersonnelAvailabilityResponse(getPersonnelAvailabilityResponse2);

            return getPersonnelAvailabilityResponse;

        } catch (Exception ex) {

            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_PERSONNEL_AVAILABILITY, getPersonnelAvailabilityRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_PERSONNEL_DETAIL)
    @ResponsePayload
    public GetPersonnelAvailDetailResponse getPersonnelAvailDetail(@RequestPayload GetPersonnelAvailDetail getPersonnelAvailDetail) throws JsonProcessingException, BadDateException {

        log.info("Get Appearance Criminal Request Received");

        ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailDetailRequest getPersonnelAvailDetailRequest =
                getPersonnelAvailDetail.getGetPersonnelAvailDetailRequest() != null
                        && getPersonnelAvailDetail.getGetPersonnelAvailDetailRequest().getGetPersonnelAvailDetailRequest() != null
                        ? getPersonnelAvailDetail.getGetPersonnelAvailDetailRequest().getGetPersonnelAvailDetailRequest()
                        : new ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailDetailRequest();

        if (getPersonnelAvailDetailRequest.getRequestDtm() == null) {

            log.warn(logBuilder.writeLogMessage(Keys.DATE_ERROR_MESSAGE, Keys.SOAP_METHOD_PERSONNEL_DETAIL, getPersonnelAvailDetail, ""));
            throw new BadDateException();

        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_PERSONNEL_DETAIL)
                        .queryParam(Keys.QUERY_AGENCY_IDENTIFIER, getPersonnelAvailDetailRequest.getRequestAgencyIdentifierId())
                        .queryParam(Keys.QUERY_PART_ID, getPersonnelAvailDetailRequest.getRequestPartId())
                        .queryParam(Keys.QUERY_REQUEST_DATE, getPersonnelAvailDetailRequest.getRequestDtm())
                        .queryParam(Keys.QUERY_PERSON_CD, getPersonnelAvailDetailRequest.getPersonTypeCd().value())
                        .queryParam(Keys.QUERY_AVAILABILITY_DATE, getPersonnelAvailDetailRequest.getAvailabilityDt())
                        .queryParam(Keys.QUERY_PAAS_PART_ID, getPersonnelAvailDetailRequest.getPaasPartId());

        try {

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailDetailResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailDetailResponse.class);

            GetPersonnelAvailDetailResponse getPersonnelAvailDetailResponse = new GetPersonnelAvailDetailResponse();
            GetPersonnelAvailDetailResponse2 getPersonnelAvailDetailResponse2 = new GetPersonnelAvailDetailResponse2();
            getPersonnelAvailDetailResponse2.setGetPersonnelAvailDetailResponse(response.getBody());
            getPersonnelAvailDetailResponse.setGetPersonnelAvailDetailResponse(getPersonnelAvailDetailResponse2);

            return getPersonnelAvailDetailResponse;

        } catch (Exception ex) {

            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_PERSONNEL_DETAIL, getPersonnelAvailDetailRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_PERSONNEL_SEARCH)
    @ResponsePayload
    public GetPersonnelSearchResponse getPersonnelSearch(GetPersonnelSearch getPersonnelSearch) throws JsonProcessingException, BadDateException {

        log.info("Get Personnel Search");

        ca.bc.gov.open.wsdl.pcss.one.GetPersonnelSearchRequest getAppearanceCriminalRequest =
                getPersonnelSearch.getGetPersonnelSearchRequest() != null
                        && getPersonnelSearch.getGetPersonnelSearchRequest().getGetPersonnelSearchRequest() != null
                        ? getPersonnelSearch.getGetPersonnelSearchRequest().getGetPersonnelSearchRequest()
                        : new ca.bc.gov.open.wsdl.pcss.one.GetPersonnelSearchRequest();

        if (getAppearanceCriminalRequest.getRequestDtm() == null) {

            log.warn(logBuilder.writeLogMessage(Keys.DATE_ERROR_MESSAGE, Keys.SOAP_METHOD_PERSONNEL_SEARCH, getPersonnelSearch, ""));
            throw new BadDateException();

        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_PERSONNEL_SEARCH)
                        .queryParam(Keys.QUERY_AGENCY_IDENTIFIER, getAppearanceCriminalRequest.getRequestAgencyIdentifierId())
                        .queryParam(Keys.QUERY_PART_ID, getAppearanceCriminalRequest.getRequestPartId())
                        .queryParam(Keys.QUERY_REQUEST_DATE, getAppearanceCriminalRequest.getRequestDtm())
                        .queryParam(Keys.QUERY_AGENT_ID, getAppearanceCriminalRequest.getAgencyId())
                        .queryParam(Keys.QUERY_SEARCH_TEXT, getAppearanceCriminalRequest.getSearchTxt())
                        .queryParam(Keys.QUERY_SEARCH_TYPE_CD, getAppearanceCriminalRequest.getSearchTypeCd().value());

        try {

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.GetPersonnelSearchResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.one.GetPersonnelSearchResponse.class);

            GetPersonnelSearchResponse getPersonnelSearchResponse = new GetPersonnelSearchResponse();
            GetPersonnelSearchResponse2 getPersonnelSearchResponse2 = new GetPersonnelSearchResponse2();
            getPersonnelSearchResponse2.setGetPersonnelSearchResponse(response.getBody());
            getPersonnelSearchResponse.setGetPersonnelSearchResponse(getPersonnelSearchResponse2);

            return getPersonnelSearchResponse;

        } catch (Exception ex) {

            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_PERSONNEL_SEARCH, getAppearanceCriminalRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

}
