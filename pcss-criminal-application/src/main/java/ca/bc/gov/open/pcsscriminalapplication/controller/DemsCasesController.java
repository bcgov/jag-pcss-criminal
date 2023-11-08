package ca.bc.gov.open.pcsscriminalapplication.controller;

import static ca.bc.gov.open.pcsscriminalapplication.exception.ServiceFaultException.handleError;

import ca.bc.gov.open.pcsscriminalapplication.Keys;
import ca.bc.gov.open.pcsscriminalapplication.model.*;
import ca.bc.gov.open.pcsscriminalapplication.properties.CaseLookupProperties;
import ca.bc.gov.open.pcsscriminalapplication.properties.DemsProperties;
import ca.bc.gov.open.pcsscriminalapplication.properties.IslProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.DateUtils;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.demsCaseUrl.DemsCaseType;
import ca.bc.gov.open.wsdl.pcss.demsCaseUrl.GetDemsCasesRequest;
import ca.bc.gov.open.wsdl.pcss.demsCaseUrl.GetDemsCasesResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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

@Endpoint
@Slf4j
@EnableConfigurationProperties({
    DemsProperties.class,
    IslProperties.class,
    CaseLookupProperties.class
})
public class DemsCasesController {
    private final RestTemplate restTemplate;
    private final RestTemplate restTemplateCaseLookup;
    private final DemsProperties demsProperties;
    private final CaseLookupProperties caseLookupProperties;
    private final LogBuilder logBuilder;
    public static final String INVALID_RCC_ID = "0";

    public DemsCasesController(
            @Qualifier("restTemplateDEMS") RestTemplate restTemplate,
            @Qualifier("restTemplateCaseLookup") RestTemplate restTemplateCaseLookup,
            DemsProperties demsProperties,
            CaseLookupProperties caseLookupProperties,
            LogBuilder logBuilder)
            throws JsonProcessingException {
        this.restTemplate = restTemplate;
        this.restTemplateCaseLookup = restTemplateCaseLookup;
        this.demsProperties = demsProperties;
        this.caseLookupProperties = caseLookupProperties;
        this.logBuilder = logBuilder;
    }

    private JustinRCCs getJustinRCCs(GetDemsCasesRequest getDemsCasesRequest)
            throws JsonProcessingException {
        try {
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(demsProperties.getHost() + "rccids")
                            .queryParam(
                                    "requestAgencyIdentifierId",
                                    getDemsCasesRequest.getRequestAgencyIdentifierId())
                            .queryParam("requestPartId", getDemsCasesRequest.getRequestPartId())
                            .queryParam(
                                    "requestDtm",
                                    DateUtils.formatORDSDate(getDemsCasesRequest.getRequestDtm()))
                            .queryParam("applicationCd", getDemsCasesRequest.getApplicationCd())
                            .queryParam(
                                    "justinNo",
                                    String.join(
                                            ",",
                                            getDemsCasesRequest.getJustinNo().stream()
                                                    .distinct()
                                                    .collect(Collectors.toList())));

            HttpEntity<JustinRCCs> response =
                    restTemplate.exchange(
                            builder.build().toUri(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            JustinRCCs.class);

            log.info("Request success from ORDS: Dems rccid");

            return response.getBody();
        } catch (Exception ex) {

            log.error(
                    logBuilder.writeLogMessage(
                            "Error occurred while receiving from ORDS",
                            "getJustinRCCs",
                            getDemsCasesRequest,
                            ex.getMessage()));

            throw handleError(ex, new ca.bc.gov.open.wsdl.pcss.demsCaseUrl.Error());
        }
    }

    private GetDemsCasesResponse getCaseListHyperlink(
            GetDemsCasesRequest getDemsCasesRequest, JustinRCCs justinRccs)
            throws JsonProcessingException {

        HttpEntity<CaseHyperLinkerLookupRequest> body = null;
        try {
            GetDemsCasesResponse response = new GetDemsCasesResponse();

            // create <JustinNo, RCCId> pairs - justinRccIdMap
            HashMap<String, String> justinRccIdMap =
                    (HashMap<String, String>)
                            justinRccs.getJustins().stream()
                                    .collect(
                                            Collectors.toMap(
                                                    JustinRcc::getJustinNo, JustinRcc::getRccId));

            // create <RCCId, hyperlink> pairs - rccIdToDemsURLMap
            HashMap<String, String> rccIdToDemsURLMap = new HashMap<>();
            justinRccs.getJustins().stream()
                    .forEach(
                            justinRCC -> {
                                if (!justinRCC.getRccId().equals(INVALID_RCC_ID))
                                    rccIdToDemsURLMap.put(justinRCC.getRccId(), "");
                            });

            // create all rccids' hpyerlink lookup request:  { "rcc_ids": [  "1xxxx2.xxxx",
            // "1xxxx3.xxxx"] }
            CaseHyperLinkerLookupRequest rccIds = new CaseHyperLinkerLookupRequest();
            rccIdToDemsURLMap.forEach((rccId, url) -> rccIds.add(rccId));

            if (rccIds.getRcc_ids().size() > 0) {
                body = new HttpEntity<>(rccIds, new HttpHeaders());

                UriComponentsBuilder islBuilder =
                        UriComponentsBuilder.fromHttpUrl(caseLookupProperties.getHost());

                HttpEntity<CaseHyperLinkerLookupResponse> resp =
                        restTemplateCaseLookup.exchange(
                                islBuilder.build().toUri(),
                                HttpMethod.POST,
                                body,
                                CaseHyperLinkerLookupResponse.class);

                // fill <RCCId, hyperlink> mapping
                CaseHyperLinkerLookupResponse list = resp.getBody();
                if (list != null && list.getCase_hyperlinks() != null) {
                    list.getCase_hyperlinks()
                            .forEach(
                                    link -> {
                                        rccIdToDemsURLMap.put(
                                                link.getRcc_id(), link.getHyperlink());
                                    });
                }
            }
            // iterate <JustinNo, RCCId> map, find RCCID's hyperlink and then add <JustinNo,
            // hyperlink> pairs to demsCase
            justinRccIdMap.forEach(
                    (justinNo, rccid) -> {
                        DemsCaseType demsCaseType = new DemsCaseType();
                        demsCaseType.setJustinNo(justinNo);
                        demsCaseType.setDemsUrl(
                                rccIdToDemsURLMap.get(rccid) != null
                                        ? rccIdToDemsURLMap.get(rccid)
                                        : "");
                        response.getDemsCase().add(demsCaseType);
                    });
            log.info("Request success from the ISL caseHyperlink web service");
            return response;
        } catch (Exception ex) {
            if (body != null && body.getBody() != null) {
                log.error(
                        logBuilder.writeLogMessage(
                                "Error occurred while fetching data from the ISL caseHyperlink web service",
                                "getCaseListHyperlink",
                                body.getBody(),
                                ex.getMessage()));
            } else {
                log.error(
                        logBuilder.writeLogMessage(
                                "Error occurred while fetching data from the ISL caseHyperlink web service",
                                "getCaseListHyperlink",
                                getDemsCasesRequest,
                                ex.getMessage()));
            }

            throw handleError(ex, new ca.bc.gov.open.wsdl.pcss.demsCaseUrl.Error());
        }
    }

    @PayloadRoot(
            namespace = Keys.SOAP_DEMSCASEURL_NAMESPACE,
            localPart = Keys.SOAP_METHOD_DEMSCASE_REQUEST)
    @ResponsePayload
    public GetDemsCasesResponse getDemsCaseMapping(
            @RequestPayload GetDemsCasesRequest getDemsCasesRequest)
            throws JsonProcessingException {

        JustinRCCs justinRccs = getJustinRCCs(getDemsCasesRequest);
        return getCaseListHyperlink(getDemsCasesRequest, justinRccs);
    }
}
