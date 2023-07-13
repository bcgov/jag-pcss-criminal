package ca.bc.gov.open.pcsscriminalapplication.controller;

import static ca.bc.gov.open.pcsscriminalapplication.exception.ServiceFaultException.handleError;

import ca.bc.gov.open.pcsscriminalapplication.Keys;
import ca.bc.gov.open.pcsscriminalapplication.exception.ReThrowException;
import ca.bc.gov.open.pcsscriminalapplication.model.CaseLookup;
import ca.bc.gov.open.pcsscriminalapplication.model.JustinRCCs;
import ca.bc.gov.open.pcsscriminalapplication.model.JustinRcc;
import ca.bc.gov.open.pcsscriminalapplication.model.RccCase;
import ca.bc.gov.open.pcsscriminalapplication.properties.CaseLookupProperties;
import ca.bc.gov.open.pcsscriminalapplication.properties.DemsProperties;
import ca.bc.gov.open.pcsscriminalapplication.properties.IslProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.DateUtils;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import ca.bc.gov.open.wsdl.pcss.demsCaseUrl.DemsCaseType;
import ca.bc.gov.open.wsdl.pcss.demsCaseUrl.GetDemsCasesRequest;
import ca.bc.gov.open.wsdl.pcss.demsCaseUrl.GetDemsCasesResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
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
    private final RestTemplate restTemplateISL;
    private final RestTemplate restTemplateCaseLookup;
    private final DemsProperties demsProperties;
    private final IslProperties islProperties;
    private final CaseLookupProperties caseLookupProperties;
    private final LogBuilder logBuilder;
    private final ObjectMapper objectMapper;

    public DemsCasesController(
            @Qualifier("restTemplateDEMS") RestTemplate restTemplate,
            @Qualifier("restTemplateISL") RestTemplate restTemplateISL,
            @Qualifier("restTemplateCaseLookup") RestTemplate restTemplateCaseLookup,
            DemsProperties demsProperties,
            IslProperties islProperties,
            CaseLookupProperties caseLookupProperties,
            LogBuilder logBuilder,
            ObjectMapper objectMapper)
            throws JsonProcessingException {
        this.restTemplate = restTemplate;
        this.restTemplateISL = restTemplateISL;
        this.restTemplateCaseLookup = restTemplateCaseLookup;
        this.demsProperties = demsProperties;
        this.islProperties = islProperties;
        this.caseLookupProperties = caseLookupProperties;
        this.logBuilder = logBuilder;
        this.objectMapper = objectMapper;
    }

    private HttpEntity<JustinRCCs> getJustinRCCs(GetDemsCasesRequest getDemsCasesRequest)
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
                                    String.join(",", getDemsCasesRequest.getJustinNo()));

            HttpEntity<JustinRCCs> response =
                    restTemplate.exchange(
                            builder.build().toUri(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            JustinRCCs.class);

            log.info("Request success from ORDS: Dems rccid request");

            return response;
        } catch (Exception ex) {

            log.error(
                    logBuilder.writeLogMessage(
                            Keys.ORDS_ERROR_MESSAGE,
                            "dems rccid request",
                            getDemsCasesRequest,
                            ex.getMessage()));

            throw handleError(ex, new ca.bc.gov.open.wsdl.pcss.demsCaseUrl.Error());
        }
    }

    private String getHyperLink(GetDemsCasesRequest getDemsCasesRequest, String rccId)
            throws JsonProcessingException {
        try {
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(caseLookupProperties.getHost() + rccId);
            HttpEntity<CaseLookup> response =
                    restTemplateCaseLookup.exchange(
                            builder.build().toUri(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            CaseLookup.class);

            CaseLookup caseLookup = response.getBody();

            log.info("Request success from ORDS: Case lookup request for rcc_id: " + rccId);

            return caseLookup.getHyperlink();
        } catch (Exception ex) {

            log.error(
                    logBuilder.writeLogMessage(
                            "Error received from ORDS",
                            "Case lookup request",
                            getDemsCasesRequest,
                            ex.getMessage()));
            // do not throw exception for an unexisting rcc_id
            return null;
        }
    }

    private GetDemsCasesResponse getDemsCases(
            GetDemsCasesRequest getDemsCasesRequest, HttpEntity<JustinRCCs> response)
            throws JsonProcessingException {
        try {
            GetDemsCasesResponse ret = new GetDemsCasesResponse();
            ArrayList<DemsCaseType> demsCase = new ArrayList<DemsCaseType>();

            JustinRCCs justinRCCs = response.getBody();

            HashMap<String, String> justinRccs =
                    (HashMap<String, String>)
                            justinRCCs.getJustins().stream()
                                    .collect(
                                            Collectors.toMap(
                                                    JustinRcc::getJustinNo, JustinRcc::getRccId));

            HashMap<String, String> rccIdToDemsURLs = new HashMap<>();
            justinRCCs.getJustins().stream().forEach(id -> rccIdToDemsURLs.put(id.getRccId(), ""));

            for (Map.Entry<String, String> entry : rccIdToDemsURLs.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                UriComponentsBuilder islBuilder = null;
                try {
                    islBuilder =
                            UriComponentsBuilder.fromHttpUrl(
                                    String.format(islProperties.getHost(), key));

                    HttpEntity<List<RccCase>> resp =
                            restTemplateISL.exchange(
                                    islBuilder.build().toUri(),
                                    HttpMethod.GET,
                                    new HttpEntity<>(new HttpHeaders()),
                                    new ParameterizedTypeReference<List<RccCase>>() {});

                    List<RccCase> list = resp.getBody();
                    if (list.size() > 0) {
                        String hyperlink = getHyperLink(getDemsCasesRequest, key);
                        if (hyperlink != null && !hyperlink.trim().isEmpty()) {
                            rccIdToDemsURLs.put(key, hyperlink);
                        }
                    }
                    log.info("Request Success from ORDS ISL request for rcc_id " + key);
                } catch (ReThrowException ex) {
                    throw handleError(ex, new ca.bc.gov.open.wsdl.pcss.demsCaseUrl.Error());
                } catch (Exception ex) {
                    log.error(
                            logBuilder.writeLogMessage(
                                    "Error received from ORDS",
                                    "ISL request",
                                    getDemsCasesRequest,
                                    ex.getMessage()));

                    throw new ReThrowException(
                            ex.getMessage(), "Error received from ORDS: ISL request");
                }
            }

            justinRccs.forEach(
                    (justinNo, rccid) -> {
                        DemsCaseType demsCaseType = new DemsCaseType();
                        demsCaseType.setJustinNo(justinNo);
                        demsCaseType.setDemsUrl(rccIdToDemsURLs.get(rccid));
                        demsCase.add(demsCaseType);
                    });
            ret.setDemsCase(demsCase);
            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_DEMSCASE_REQUEST);
            return ret;
        } catch (ReThrowException ex) {
            throw handleError(ex, new ca.bc.gov.open.wsdl.pcss.demsCaseUrl.Error());
        } catch (Exception ex) {

            log.error(
                    logBuilder.writeLogMessage(
                            Keys.ORDS_ERROR_MESSAGE,
                            Keys.SOAP_METHOD_DEMSCASE_REQUEST,
                            getDemsCasesRequest,
                            ex.getMessage()));

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

        HttpEntity<JustinRCCs> response = getJustinRCCs(getDemsCasesRequest);
        return getDemsCases(getDemsCasesRequest, response);
    }
}
