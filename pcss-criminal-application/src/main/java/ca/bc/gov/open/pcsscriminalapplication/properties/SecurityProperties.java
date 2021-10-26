package ca.bc.gov.open.pcsscriminalapplication.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.basic-auth")
@Getter
@Setter
public class SecurityProperties {

    private String username;
    private String password;

}
