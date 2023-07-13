package ca.bc.gov.open.pcsscriminalapplication.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode
@NoArgsConstructor
public class CaseLookup implements Serializable {
    @JsonProperty("message")
    private String message;

    @JsonProperty("hyperlink")
    private String hyperlink;
}
