package ca.bc.gov.open.pcsscriminalapplication.controller;

import ca.bc.gov.open.pcsscriminalapplication.Keys;
import ca.bc.gov.open.pcsscriminalapplication.exception.BadDateException;
import ca.bc.gov.open.pcsscriminalapplication.exception.ORDSException;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.secure.two.GetFileDetailCriminalSecure;
import ca.bc.gov.open.wsdl.pcss.secure.two.GetFileDetailCriminalSecureResponse;
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
public class FileController {

    private final RestTemplate restTemplate;
    private final PcssProperties pcssProperties;
    private final LogBuilder logBuilder;

    public FileController(RestTemplate restTemplate, PcssProperties pcssProperties, LogBuilder logBuilder) {
        this.restTemplate = restTemplate;
        this.pcssProperties = pcssProperties;
        this.logBuilder = logBuilder;
    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_FILE_CLOSED)
    @ResponsePayload
    public GetClosedFileResponse getClosedFile(@RequestPayload GetClosedFile getClosedFile) throws JsonProcessingException, BadDateException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_FILE_CLOSED);

        ca.bc.gov.open.wsdl.pcss.one.GetClosedFileRequest getClosedFileRequest =
                getClosedFile.getGetClosedFileRequest() != null
                        && getClosedFile.getGetClosedFileRequest().getGetClosedFileRequest() != null
                        ? getClosedFile.getGetClosedFileRequest().getGetClosedFileRequest()
                        : new ca.bc.gov.open.wsdl.pcss.one.GetClosedFileRequest();

        if (getClosedFileRequest.getRequestDtm() == null) {

            log.warn(logBuilder.writeLogMessage(Keys.DATE_ERROR_MESSAGE, Keys.SOAP_METHOD_FILE_CLOSED, getClosedFile, ""));
            throw new BadDateException();

        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_APPEARANCE)
                        .queryParam(Keys.QUERY_AGENCY_IDENTIFIER, getClosedFileRequest.getRequestAgencyIdentifierId())
                        .queryParam(Keys.QUERY_AGENT_ID, getClosedFileRequest.getAgencyId())
                        .queryParam(Keys.QUERY_PART_ID, getClosedFileRequest.getRequestPartId())
                        .queryParam(Keys.QUERY_REQUEST_DATE, getClosedFileRequest.getRequestDtm())
                        .queryParam(Keys.QUERY_FROM_DATE, getClosedFileRequest.getFromApprDt())
                        .queryParam(Keys.QUERY_TO_DATE, getClosedFileRequest.getToApprDt());

        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_FILE_CLOSED);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.GetClosedFileResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.one.GetClosedFileResponse.class);

            GetClosedFileResponse getClosedFileResponse = new GetClosedFileResponse();
            GetClosedFileResponce getClosedFileResponce = new GetClosedFileResponce();
            getClosedFileResponce.setGetClosedFileResponse(response.getBody());
            getClosedFileResponse.setGetClosedFileResponce(getClosedFileResponce);

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_FILE_CLOSED);

            return getClosedFileResponse;

        } catch (Exception ex) {

            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_FILE_CLOSED, getClosedFileRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_FILE_DETAIL)
    @ResponsePayload
    public GetFileDetailCriminalResponse getFileDetailCriminal(@RequestPayload GetFileDetailCriminal getFileDetailCriminal) throws JsonProcessingException, BadDateException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_FILE_DETAIL);

        ca.bc.gov.open.wsdl.pcss.one.GetFileDetailCriminalRequest getFileDetailCriminalRequest =
                getFileDetailCriminal.getGetFileDetailCriminalRequest() != null
                        && getFileDetailCriminal.getGetFileDetailCriminalRequest().getGetFileDetailCriminalRequest() != null
                        ? getFileDetailCriminal.getGetFileDetailCriminalRequest().getGetFileDetailCriminalRequest()
                        : new ca.bc.gov.open.wsdl.pcss.one.GetFileDetailCriminalRequest();

        if (getFileDetailCriminalRequest.getRequestDtm() == null) {

            log.warn(logBuilder.writeLogMessage(Keys.DATE_ERROR_MESSAGE, Keys.SOAP_METHOD_FILE_DETAIL, getFileDetailCriminal, ""));
            throw new BadDateException();

        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_APPEARANCE)
                        .queryParam(Keys.QUERY_AGENCY_IDENTIFIER, getFileDetailCriminalRequest.getRequestAgencyIdentifierId())
                        .queryParam(Keys.QUERY_PART_ID, getFileDetailCriminalRequest.getRequestPartId())
                        .queryParam(Keys.QUERY_REQUEST_DATE, getFileDetailCriminalRequest.getRequestDtm())
                        .queryParam(Keys.QUERY_JUSTIN_NO, getFileDetailCriminalRequest.getJustinNo())
                        .queryParam(Keys.QUERY_APPLICATION_CD, getFileDetailCriminalRequest.getApplicationCd());

        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_FILE_DETAIL);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.GetFileDetailCriminalResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.one.GetFileDetailCriminalResponse.class);

            GetFileDetailCriminalResponse getFileDetailCriminalResponse = new GetFileDetailCriminalResponse();
            GetFileDetailCriminalResponse2 getFileDetailCriminalResponse2 = new GetFileDetailCriminalResponse2();
            getFileDetailCriminalResponse2.setGetFileDetailCriminalResponse(response.getBody());
            getFileDetailCriminalResponse.setGetFileDetailCriminalResponse(getFileDetailCriminalResponse2);

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_FILE_DETAIL);

            return getFileDetailCriminalResponse;

        } catch (Exception ex) {
            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_FILE_DETAIL, getFileDetailCriminalRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_FILE_DETAIL_SECURE)
    @ResponsePayload
    public GetFileDetailCriminalSecureResponse getFileDetailCriminalSecure(@RequestPayload GetFileDetailCriminalSecure getFileDetailCriminalSecure) throws BadDateException, JsonProcessingException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_FILE_DETAIL_SECURE);

        ca.bc.gov.open.wsdl.pcss.secure.one.GetFileDetailCriminalRequest getFileDetailCriminalRequest =
                getFileDetailCriminalSecure.getGetFileDetailCriminalSecureRequest() != null
                        && getFileDetailCriminalSecure.getGetFileDetailCriminalSecureRequest().getGetFileDetailCriminalRequest() != null
                        ? getFileDetailCriminalSecure.getGetFileDetailCriminalSecureRequest().getGetFileDetailCriminalRequest()
                        : new ca.bc.gov.open.wsdl.pcss.secure.one.GetFileDetailCriminalRequest();

        if (getFileDetailCriminalRequest.getRequestDtm() == null) {

            log.warn(logBuilder.writeLogMessage(Keys.DATE_ERROR_MESSAGE, Keys.SOAP_METHOD_FILE_DETAIL_SECURE, getFileDetailCriminalSecure, ""));
            throw new BadDateException();

        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_APPEARANCE)
                        .queryParam(Keys.QUERY_AGENCY_IDENTIFIER, getFileDetailCriminalRequest.getRequestAgencyIdentifierId())
                        .queryParam(Keys.QUERY_PART_ID, getFileDetailCriminalRequest.getRequestPartId())
                        .queryParam(Keys.QUERY_REQUEST_DATE, getFileDetailCriminalRequest.getRequestDtm())
                        .queryParam(Keys.QUERY_JUSTIN_NO, getFileDetailCriminalRequest.getJustinNo())
                        .queryParam(Keys.QUERY_APPLICATION_CD, getFileDetailCriminalRequest.getApplicationCd());

        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_FILE_DETAIL_SECURE);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.secure.one.GetFileDetailCriminalResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.wsdl.pcss.secure.one.GetFileDetailCriminalResponse.class);

            GetFileDetailCriminalSecureResponse getFileDetailCriminalSecureResponse = new GetFileDetailCriminalSecureResponse();
            ca.bc.gov.open.wsdl.pcss.secure.two.GetFileDetailCriminalResponse getFileDetailCriminalResponse2 = new ca.bc.gov.open.wsdl.pcss.secure.two.GetFileDetailCriminalResponse();
            getFileDetailCriminalResponse2.setGetFileDetailCriminalResponse(response.getBody());
            getFileDetailCriminalSecureResponse.setGetFileDetailCriminalResponse(getFileDetailCriminalResponse2);

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_FILE_DETAIL_SECURE);

            return getFileDetailCriminalSecureResponse;

        } catch (Exception ex) {
            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_FILE_DETAIL_SECURE, getFileDetailCriminalRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

    @PayloadRoot(namespace = Keys.SOAP_NAMESPACE, localPart = Keys.SOAP_METHOD_SET_FILE_NOTE)
    @ResponsePayload
    public SetFileNoteResponse setFileNote(@RequestPayload SetFileNote setFileNote) throws JsonProcessingException {

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_SET_FILE_NOTE);

        ca.bc.gov.open.wsdl.pcss.one.SetFileNoteRequest setFileNoteRequest =
                  setFileNote.getSetFileNoteRequest() != null &&
                  setFileNote.getSetFileNoteRequest().getSetFileNoteRequest() != null ?
                  setFileNote.getSetFileNoteRequest().getSetFileNoteRequest() :
                  new ca.bc.gov.open.wsdl.pcss.one.SetFileNoteRequest();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_APPEARANCE_METHOD);

        HttpEntity<ca.bc.gov.open.wsdl.pcss.one.SetFileNoteRequest> body =
                new HttpEntity<>(setFileNoteRequest, new HttpHeaders());
        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_SET_FILE_NOTE);

            HttpEntity<ca.bc.gov.open.wsdl.pcss.one.SetFileNoteResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            body,
                            ca.bc.gov.open.wsdl.pcss.one.SetFileNoteResponse.class);

            SetFileNoteResponse setFileNoteResponse = new SetFileNoteResponse();
            SetFileNoteResponse2 setFileNoteResponse2 = new SetFileNoteResponse2();
            setFileNoteResponse2.setSetFileNoteResponse(response.getBody());
            setFileNoteResponse.setSetFileNoteResponse(setFileNoteResponse2);

            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_SET_FILE_NOTE);

            return setFileNoteResponse;

        } catch (Exception ex) {

            log.error(logBuilder.writeLogMessage(Keys.ORDS_ERROR_MESSAGE, Keys.SOAP_METHOD_SET_FILE_NOTE, setFileNoteRequest, ex.getMessage()));
            throw new ORDSException();

        }

    }

}
