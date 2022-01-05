package ca.bc.gov.open.pcsscriminalapplication.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pcss")
@Getter
@Setter
public class PcssProperties {

    private String host;
    private String namespace;
}
