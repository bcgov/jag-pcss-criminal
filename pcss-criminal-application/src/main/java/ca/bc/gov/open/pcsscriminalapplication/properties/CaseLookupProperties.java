package ca.bc.gov.open.pcsscriminalapplication.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "caselookup")
@Getter
@Setter
public class CaseLookupProperties {
    private String host;
    private String token;
    private String ordsReadTimeout;
}
