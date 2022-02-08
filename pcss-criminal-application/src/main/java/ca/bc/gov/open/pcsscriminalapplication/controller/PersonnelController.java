package ca.bc.gov.open.pcsscriminalapplication.controller;

import ca.bc.gov.open.pcsscriminalapplication.Keys;
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

    public PersonnelController(
            RestTemplate restTemplate, PcssProperties pcssProperties, LogBuilder logBuilder) {
        this.restTemplate = restTemplate;
        this.pcssProperties = pcssProperties;
        this.logBuilder = logBuilder;
    }

    @PayloadRoot(
            namespace = Keys.SOAP_NAMESPACE,
            localPart = Keys.SOAP_METHOD_PERSONNEL_AVAILABILITY)
    @ResponsePayload
    public GetPersonnelAvailabilityResponse getPersonnelAvailability(
            @RequestPayload GetPersonnelAvailability getPersonnelAvailability)
            throws JsonProcessingException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_PERSONNEL_AVAILABILITY);

        ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailabilityRequest
                getPersonnelAvailabilityRequest =
                        getPersonnelAvailability.getGetPersonnelAvailabilityRequest() != null
                                        && getPersonnelAvailability
                                                        .getGetPersonnelAvailabilityRequest()
                                                        .getGetPersonnelAvailabilityRequest()
                                                != null
                                ? getPersonnelAvailability
                                        .getGetPersonnelAvailabilityRequest()
                                        .getGetPersonnelAvailabilityRequest()
                                : new ca.bc.gov.open.wsdl.pcss.one
                                        .GetPersonnelAvailabilityRequest();

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(
                                pcssProperties.getHost() + Keys.ORDS_PERSONNEL_AVAILABILITY)
                        .queryParam(
                                Keys.QUERY_AGENCY_IDENTIFIER,
                                getPersonnelAvailabilityRequest.getRequestAgencyIdentifierId())
                        .queryParam(
                                Keys.QUERY_PART_ID_SIMPLE,
                                getPersonnelAvailabilityRequest.getRequestPartId())
                        .queryParam(
                                Keys.QUERY_REQUEST_DATE,
                                getPersonnelAvailabilityRequest.getRequestDtm())
                        .queryParam(
                                Keys.QUERY_PERSON_CD,
                                getPersonnelAvailabilityRequest.getPersonTypeCd().value())
                        .queryParam(
                                Keys.QUERY_PART_ID_LIST,
                                getPersonnelAvailabilityRequest.getPartIdList())
                        .queryParam(
                                Keys.QUERY_FROM_DATE, getPersonnelAvailabilityRequest.getFromDt())
                        .queryParam(Keys.QUERY_TO_DATE, getPersonnelAvailabilityRequest.getToDt());

        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_PERSONNEL_AVAILABILITY);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailabilityResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailabilityResponse.class);

            GetPersonnelAvailabilityResponse getPersonnelAvailabilityResponse =
                    buildPersonnelAvailabilityResponse(response.getBody());

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_PERSONNEL_AVAILABILITY);

            return getPersonnelAvailabilityResponse;

        } catch (Exception ex) {

            log.error(
                    logBuilder.writeLogMessage(
                            Keys.ORDS_ERROR_MESSAGE,
                            Keys.SOAP_METHOD_PERSONNEL_AVAILABILITY,
                            getPersonnelAvailabilityRequest,
                            ex.getMessage()));
            throw new ORDSException();
        }
    }

    private GetPersonnelAvailabilityResponse buildPersonnelAvailabilityResponse(
            ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailabilityResponse
                    getPersonnelAvailabilityResponseInner) {

        GetPersonnelAvailabilityResponse getPersonnelAvailabilityResponse =
                new GetPersonnelAvailabilityResponse();
        GetPersonnelAvailabilityResponse2 getPersonnelAvailabilityResponse2 =
                new GetPersonnelAvailabilityResponse2();
        getPersonnelAvailabilityResponse2.setGetPersonnelAvailabilityResponse(
                getPersonnelAvailabilityResponseInner);
        getPersonnelAvailabilityResponse.setGetPersonnelAvailabilityResponse(
                getPersonnelAvailabilityResponse2);

        return getPersonnelAvailabilityResponse;
    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_PERSONNEL_DETAIL)
    @ResponsePayload
    public GetPersonnelAvailDetailResponse getPersonnelAvailDetail(
            @RequestPayload GetPersonnelAvailDetail getPersonnelAvailDetail)
            throws JsonProcessingException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_PERSONNEL_DETAIL);

        ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailDetailRequest getPersonnelAvailDetailRequest =
                getPersonnelAvailDetail.getGetPersonnelAvailDetailRequest() != null
                                && getPersonnelAvailDetail
                                                .getGetPersonnelAvailDetailRequest()
                                                .getGetPersonnelAvailDetailRequest()
                                        != null
                        ? getPersonnelAvailDetail
                                .getGetPersonnelAvailDetailRequest()
                                .getGetPersonnelAvailDetailRequest()
                        : new ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailDetailRequest();

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(
                                pcssProperties.getHost() + Keys.ORDS_PERSONNEL_DETAIL)
                        .queryParam(
                                Keys.QUERY_AGENT_ID,
                                getPersonnelAvailDetailRequest.getRequestAgencyIdentifierId())
                        .queryParam(
                                Keys.QUERY_PART_ID,
                                getPersonnelAvailDetailRequest.getRequestPartId())
                        .queryParam(
                                Keys.QUERY_REQUEST_DATE,
                                getPersonnelAvailDetailRequest.getRequestDtm())
                        .queryParam(
                                Keys.QUERY_PERSON_CD,
                                getPersonnelAvailDetailRequest.getPersonTypeCd().value())
                        .queryParam(
                                Keys.QUERY_AVAILABILITY_DATE,
                                getPersonnelAvailDetailRequest.getAvailabilityDt())
                        .queryParam(
                                Keys.QUERY_PAAS_PART_ID,
                                getPersonnelAvailDetailRequest.getPaasPartId());

        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_PERSONNEL_DETAIL);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailDetailResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailDetailResponse.class);

            GetPersonnelAvailDetailResponse getPersonnelAvailDetailResponse =
                    buildPersonnelAvailDetail(response.getBody());

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_PERSONNEL_DETAIL);

            return getPersonnelAvailDetailResponse;

        } catch (Exception ex) {

            log.error(
                    logBuilder.writeLogMessage(
                            Keys.ORDS_ERROR_MESSAGE,
                            Keys.SOAP_METHOD_PERSONNEL_DETAIL,
                            getPersonnelAvailDetailRequest,
                            ex.getMessage()));
            throw new ORDSException();
        }
    }

    private GetPersonnelAvailDetailResponse buildPersonnelAvailDetail(
            ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailDetailResponse
                    getPersonnelAvailDetailResponseInner) {

        GetPersonnelAvailDetailResponse getPersonnelAvailDetailResponse =
                new GetPersonnelAvailDetailResponse();
        GetPersonnelAvailDetailResponse2 getPersonnelAvailDetailResponse2 =
                new GetPersonnelAvailDetailResponse2();
        getPersonnelAvailDetailResponse2.setGetPersonnelAvailDetailResponse(
                getPersonnelAvailDetailResponseInner);
        getPersonnelAvailDetailResponse.setGetPersonnelAvailDetailResponse(
                getPersonnelAvailDetailResponse2);

        return getPersonnelAvailDetailResponse;
    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_PERSONNEL_SEARCH)
    @ResponsePayload
    public GetPersonnelSearchResponse getPersonnelSearch(
            @RequestPayload GetPersonnelSearch getPersonnelSearch) throws JsonProcessingException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_PERSONNEL_SEARCH);

        ca.bc.gov.open.wsdl.pcss.one.GetPersonnelSearchRequest getPersonnelSearchRequest =
                getPersonnelSearch.getGetPersonnelSearchRequest() != null
                                && getPersonnelSearch
                                                .getGetPersonnelSearchRequest()
                                                .getGetPersonnelSearchRequest()
                                        != null
                        ? getPersonnelSearch
                                .getGetPersonnelSearchRequest()
                                .getGetPersonnelSearchRequest()
                        : new ca.bc.gov.open.wsdl.pcss.one.GetPersonnelSearchRequest();

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(
                                pcssProperties.getHost() + Keys.ORDS_PERSONNEL_SEARCH)
                        .queryParam(
                                Keys.QUERY_AGENT_ID,
                                getPersonnelSearchRequest.getRequestAgencyIdentifierId())
                        .queryParam(
                                Keys.QUERY_PART_ID, getPersonnelSearchRequest.getRequestPartId())
                        .queryParam(
                                Keys.QUERY_REQUEST_DATE, getPersonnelSearchRequest.getRequestDtm())
                        .queryParam(
                                Keys.QUERY_AGENCY_IDENTIFIER,
                                getPersonnelSearchRequest.getAgencyId())
                        .queryParam(
                                Keys.QUERY_SEARCH_TEXT, getPersonnelSearchRequest.getSearchTxt())
                        .queryParam(
                                Keys.QUERY_SEARCH_TYPE_CD,
                                getPersonnelSearchRequest.getSearchTypeCd().value());

        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_PERSONNEL_SEARCH);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.GetPersonnelSearchResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.one.GetPersonnelSearchResponse.class);

            GetPersonnelSearchResponse getPersonnelSearchResponse =
                    buildPersonnelSearch(response.getBody());

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_PERSONNEL_SEARCH);

            return getPersonnelSearchResponse;

        } catch (Exception ex) {

            log.error(
                    logBuilder.writeLogMessage(
                            Keys.ORDS_ERROR_MESSAGE,
                            Keys.SOAP_METHOD_PERSONNEL_SEARCH,
                            getPersonnelSearchRequest,
                            ex.getMessage()));
            throw new ORDSException();
        }
    }

    private GetPersonnelSearchResponse buildPersonnelSearch(
            ca.bc.gov.open.wsdl.pcss.one.GetPersonnelSearchResponse
                    getPersonnelSearchResponseInner) {

        GetPersonnelSearchResponse getPersonnelSearchResponse = new GetPersonnelSearchResponse();
        GetPersonnelSearchResponse2 getPersonnelSearchResponse2 = new GetPersonnelSearchResponse2();
        getPersonnelSearchResponse2.setGetPersonnelSearchResponse(getPersonnelSearchResponseInner);
        getPersonnelSearchResponse.setGetPersonnelSearchResponse(getPersonnelSearchResponse2);

        return getPersonnelSearchResponse;
    }
}
