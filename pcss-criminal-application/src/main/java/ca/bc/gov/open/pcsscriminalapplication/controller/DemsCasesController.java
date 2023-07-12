package ca.bc.gov.open.pcsscriminalapplication.controller;

import static ca.bc.gov.open.pcsscriminalapplication.exception.ServiceFaultException.handleError;

import ca.bc.gov.open.pcsscriminalapplication.Keys;
import ca.bc.gov.open.pcsscriminalapplication.model.RccId;
import ca.bc.gov.open.pcsscriminalapplication.model.RccIds;
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

        log.info(Keys.LOG_RECEIVED, Keys.SOAP_METHOD_DEMSCASE_REQUEST);

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
                                "justinNo", String.join(",", getDemsCasesRequest.getJustinNo()));

        try {

            log.debug(Keys.LOG_ORDS, Keys.SOAP_METHOD_DEMSCASE_REQUEST);

            HttpEntity<RccIds> response =
                    restTemplate.exchange(
                            builder.build().toUri(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            RccIds.class);

            RccIds rccIds = response.getBody();
            String rccPattern = rccIds.getDemsCasePattern();

            HashMap<String, String> rccIdsMap =
                    (HashMap<String, String>)
                            rccIds.getJustins().stream()
                                    .collect(Collectors.toMap(RccId::getJustinNo, RccId::getRccId));

            HashMap<String, String> rccIdToDemsURL = new HashMap<>();
            rccIds.getJustins().stream().forEach(id -> rccIdToDemsURL.put(id.getRccId(), ""));

            rccIdToDemsURL.forEach(
                    (rccid, value) -> {
                        UriComponentsBuilder islBuilder =
                                UriComponentsBuilder.fromHttpUrl(
                                        String.format(islProperties.getHost(), rccid));

                        HttpEntity<List<Map<String, String>>> resp =
                                restTemplateISL.exchange(
                                        islBuilder.build().toUri(),
                                        HttpMethod.GET,
                                        new HttpEntity<>(new HttpHeaders()),
                                        new ParameterizedTypeReference<>() {});

                        List<Map<String, String>> list = resp.getBody();
                        if (list.stream().count() == 0) {
                            rccIdToDemsURL.put(rccid, rccPattern.replaceAll("<<RCC_ID>>", rccid));
                        }
                    });

            GetDemsCasesResponse ret = new GetDemsCasesResponse();
            ArrayList<DemsCaseType> demsCase = new ArrayList<DemsCaseType>();
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
