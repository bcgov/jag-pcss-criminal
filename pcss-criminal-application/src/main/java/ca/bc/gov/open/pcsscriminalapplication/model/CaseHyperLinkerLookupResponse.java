package ca.bc.gov.open.pcsscriminalapplication.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CaseHyperLinkerLookupResponse implements Serializable {
    private String message;
    private List<CaseHyperlinks> case_hyperlinks;

    @Data
    public static class CaseHyperlinks {
        private String message;
        private String hyperlink;
        private String rcc_id;
    }
}
