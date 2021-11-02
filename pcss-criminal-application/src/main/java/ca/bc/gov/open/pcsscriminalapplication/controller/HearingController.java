package ca.bc.gov.open.pcsscriminalapplication.controller;

import ca.bc.gov.open.pcsscriminalapplication.Keys;
import ca.bc.gov.open.pcsscriminalapplication.exception.BadDateException;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.two.SetHearingRestrictionCriminal;
import ca.bc.gov.open.wsdl.pcss.one.SetHearingRestrictionCriminalRequest;
import ca.bc.gov.open.wsdl.pcss.two.SetHearingRestrictionCriminalResponse;
import ca.bc.gov.open.wsdl.pcss.two.SetHearingRestrictionCriminalResponse2;
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
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Slf4j
@Endpoint
@EnableConfigurationProperties(PcssProperties.class)
public class HearingController {

    private final RestTemplate restTemplate;
    private final PcssProperties pcssProperties;
    private final LogBuilder logBuilder;

    public HearingController(RestTemplate restTemplate, PcssProperties pcssProperties, LogBuilder logBuilder) {
        this.restTemplate = restTemplate;
        this.pcssProperties = pcssProperties;
        this.logBuilder = logBuilder;
    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_HEARING_RESTRICTION_CRIMINAL)
    @ResponsePayload
    public SetHearingRestrictionCriminalResponse setHearingRestrictionCriminal(SetHearingRestrictionCriminal setHearingRestrictionCriminal) throws JsonProcessingException, BadDateException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_HEARING_RESTRICTION_CRIMINAL);

        SetHearingRestrictionCriminalRequest setHearingRestrictionCriminalRequest = setHearingRestrictionCriminal.getSetHearingRestrictionCriminalRequest() != null
                && setHearingRestrictionCriminal.getSetHearingRestrictionCriminalRequest().getSetHearingRestrictionCriminalRequest() != null
                ? setHearingRestrictionCriminal.getSetHearingRestrictionCriminalRequest().getSetHearingRestrictionCriminalRequest()
                : new SetHearingRestrictionCriminalRequest();

        if(setHearingRestrictionCriminalRequest.getRequestDtm() == null) {
            log.warn(logBuilder.writeLogMessage(Keys.DATE_ERROR_MESSAGE, Keys.SOAP_METHOD_HEARING_RESTRICTION_CRIMINAL, setHearingRestrictionCriminal, ""));
            throw new BadDateException();
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_HEARING);

        HttpEntity<SetHearingRestrictionCriminalRequest> body = new HttpEntity<>(setHearingRestrictionCriminalRequest, new HttpHeaders());

        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_HEARING_RESTRICTION_CRIMINAL);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.SetHearingRestrictionCriminalResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            body,
                            ca.bc.gov.open.wsdl.pcss.one.SetHearingRestrictionCriminalResponse.class);

            SetHearingRestrictionCriminalResponse setHearingRestrictionCriminalResponse = new SetHearingRestrictionCriminalResponse();
            SetHearingRestrictionCriminalResponse2 setHearingRestrictionCriminalResponse2 = new SetHearingRestrictionCriminalResponse2();
            setHearingRestrictionCriminalResponse2.setSetHearingRestrictionCriminalResponse(response.getBody());
            setHearingRestrictionCriminalResponse.setSetHearingRestrictionCriminalResponse(setHearingRestrictionCriminalResponse2);

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_HEARING_RESTRICTION_CRIMINAL);

            return setHearingRestrictionCriminalResponse;

        } catch(Exception ex) {
            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_HEARING_RESTRICTION_CRIMINAL, setHearingRestrictionCriminalRequest, ex.getMessage()));
            throw new ORDSException();
        }
    }
}
