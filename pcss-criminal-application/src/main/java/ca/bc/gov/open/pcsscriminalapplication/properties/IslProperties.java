package ca.bc.gov.open.pcsscriminalapplication.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "isl")
@Getter
@Setter
public class IslProperties {

    private String host;
    private String token;
    private String ordsReadTimeout;
}
