package ca.bc.gov.open.pcsscriminalapplication.controller;

import ca.bc.gov.open.pcsscriminalapplication.Keys;
import ca.bc.gov.open.pcsscriminalapplication.exception.BadDateException;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.one.SetCrownFileDetailRequest;
import ca.bc.gov.open.wsdl.pcss.one.SetHearingRestrictionCriminalResponse;
import ca.bc.gov.open.wsdl.pcss.two.SetCrownFileDetailResponse;
import ca.bc.gov.open.wsdl.pcss.two.SetCrownFileDetail;
import ca.bc.gov.open.wsdl.pcss.two.SetCrownFileDetailResponse2;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Endpoint
@EnableConfigurationProperties(PcssProperties.class)
public class CrownController {

    private final RestTemplate restTemplate;
    private final PcssProperties pcssProperties;
    private final LogBuilder logBuilder;

    public CrownController(RestTemplate restTemplate, PcssProperties pcssProperties, LogBuilder logBuilder) {
        this.restTemplate = restTemplate;
        this.pcssProperties = pcssProperties;
        this.logBuilder = logBuilder;
    }

    public void getCrownAssignment() {

    }

    public void setCounselDetailCriminal() {

    }

    public void setCrownAssignment() {

    }

    public SetCrownFileDetailResponse setCrownFileDetail(@RequestBody SetCrownFileDetail setCrownFileDetail) throws BadDateException, JsonProcessingException {

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
