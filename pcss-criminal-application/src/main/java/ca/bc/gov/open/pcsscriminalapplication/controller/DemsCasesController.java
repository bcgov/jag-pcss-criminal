package ca.bc.gov.open.pcsscriminalapplication.controller;

import static ca.bc.gov.open.pcsscriminalapplication.exception.ServiceFaultException.handleError;

import ca.bc.gov.open.pcsscriminalapplication.Keys;
import ca.bc.gov.open.pcsscriminalapplication.model.JustinRCCs;
import ca.bc.gov.open.pcsscriminalapplication.model.JustinRcc;
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
@EnableConfigurationProperties({DemsProperties.class, IslProperties.class})
public class DemsCasesController {
    private final RestTemplate restTemplate;
    private final RestTemplate restTemplateISL;
    private final DemsProperties demsProperties;
    private final IslProperties islProperties;
    private final LogBuilder logBuilder;
    private final ObjectMapper objectMapper;

    public DemsCasesController(
            @Qualifier("restTemplateDEMS") RestTemplate restTemplate,
            @Qualifier("restTemplateISL") RestTemplate restTemplateISL,
            DemsProperties demsProperties,
            IslProperties islProperties,
            LogBuilder logBuilder,
            ObjectMapper objectMapper)
            throws JsonProcessingException {
        this.restTemplate = restTemplate;
        this.restTemplateISL = restTemplateISL;
        this.demsProperties = demsProperties;
        this.islProperties = islProperties;
        this.logBuilder = logBuilder;
        this.objectMapper = objectMapper;
    }

    @PayloadRoot(
            namespace = Keys.SOAP_DEMSCASEURL_NAMESPACE,
            localPart = Keys.SOAP_METHOD_DEMSCASE_REQUEST)
    @ResponsePayload
    public GetDemsCasesResponse getDemsCaseMapping(
            @RequestPayload GetDemsCasesRequest getDemsCasesRequest)
            throws JsonProcessingException {

        HttpEntity<JustinRCCs> response = null;

        try {
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(
                                    demsProperties.getHost() + Keys.ORDS_FILE_DEMS_CASE_URL)
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

            response =
                    restTemplate.exchange(
                            builder.build().toUri(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            JustinRCCs.class);

            log.info("Request Success from ORDS", "dems rccid request");
        } catch (Exception ex) {

            log.error(
                    logBuilder.writeLogMessage(
                            Keys.ORDS_ERROR_MESSAGE,
                            "dems rccid request",
                            getDemsCasesRequest,
                            ex.getMessage()));

            throw handleError(ex, new ca.bc.gov.open.wsdl.pcss.demsCaseUrl.Error());
        }

        try {

            GetDemsCasesResponse ret = new GetDemsCasesResponse();
            ArrayList<DemsCaseType> demsCase = new ArrayList<DemsCaseType>();

            JustinRCCs justinRCCs = response.getBody();
            String rccPattern = justinRCCs.getDemsCasePattern();

            HashMap<String, String> rccIdsMap =
                    (HashMap<String, String>)
                            justinRCCs.getJustins().stream()
                                    .collect(
                                            Collectors.toMap(
                                                    JustinRcc::getJustinNo, JustinRcc::getRccId));

            HashMap<String, String> rccIdToDemsURL = new HashMap<>();
            justinRCCs.getJustins().stream().forEach(id -> rccIdToDemsURL.put(id.getRccId(), ""));

            for (Map.Entry<String, String> entry : rccIdToDemsURL.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                UriComponentsBuilder islBuilder = null;
                try {
                    islBuilder =
                            UriComponentsBuilder.fromHttpUrl(
                                    String.format(islProperties.getHost(), key));

                    HttpEntity<List<Map<String, String>>> resp =
                            restTemplateISL.exchange(
                                    islBuilder.build().toUri(),
                                    HttpMethod.GET,
                                    new HttpEntity<>(new HttpHeaders()),
                                    new ParameterizedTypeReference<>() {});

                    List<Map<String, String>> list = resp.getBody();
                    if (list.stream().count() > 0) {
                        rccIdToDemsURL.put(key, rccPattern.replaceAll("<<RCC_ID>>", key));
                    }
                } catch (Exception ex) {
                    log.error(
                            logBuilder.writeLogMessage(
                                    Keys.ORDS_ERROR_MESSAGE, "ISL request", null, ex.getMessage()));

                    throw handleError(ex, new ca.bc.gov.open.wsdl.pcss.demsCaseUrl.Error());
                }
            }

            rccIdsMap.forEach(
                    (justinNo, rccid) -> {
                        DemsCaseType demsCaseType = new DemsCaseType();
                        demsCaseType.setMdocJustinNo(justinNo);
                        demsCaseType.setDemsUrl(rccIdToDemsURL.get(rccid));
                        demsCase.add(demsCaseType);
                    });
            ret.setDemsCase(demsCase);
            log.info(Keys.LOG_SUCCESS, Keys.SOAP_METHOD_DEMSCASE_REQUEST);
            return ret;
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
}
