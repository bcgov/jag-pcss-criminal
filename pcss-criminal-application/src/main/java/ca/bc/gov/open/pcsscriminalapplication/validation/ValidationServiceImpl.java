package ca.bc.gov.open.pcsscriminalapplication.validation;

import ca.bc.gov.open.pcsscriminalapplication.Keys;
import ca.bc.gov.open.pcsscriminalapplication.exception.InvalidUserException;
import ca.bc.gov.open.pcsscriminalapplication.model.UserValidation;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.wsdl.pcss.one.GetPersonnelSearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
@EnableConfigurationProperties(PcssProperties.class)
public class ValidationServiceImpl implements ValidationService {

    private final RestTemplate restTemplate;
    private final PcssProperties pcssProperties;

    public ValidationServiceImpl(RestTemplate restTemplate, PcssProperties pcssProperties) {
        this.restTemplate = restTemplate;
        this.pcssProperties = pcssProperties;
    }


    @Override
    public void isSecureRequestValid(String agentIdentifier, String justinNo) throws InvalidUserException {

        HttpEntity<UserValidation> response = null;

        try {
            response =
                    restTemplate.exchange(
                            UriComponentsBuilder.fromHttpUrl(pcssProperties.getHost() + Keys.ORDS_APPEARANCE_SECURE)
                                    .queryParam(Keys.QUERY_AGENT_ID, agentIdentifier)
                                    .queryParam(Keys.QUERY_JUSTIN_NO, justinNo).toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            UserValidation.class);

        } catch (Exception e) {

            log.error(e.getMessage());
            throw(e);

        }

        if (!response.getBody().isValid) {
            throw new InvalidUserException();
        }
    }
}
