package ca.bc.gov.open.pcsscriminalapplication.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "dems")
@Getter
@Setter
public class DemsProperties {

    private String host;
    private String namespace;
    private String userName;
    private String password;
    private String ordsReadTimeout;
}
