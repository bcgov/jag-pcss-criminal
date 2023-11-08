package ca.bc.gov.open.pcsscriminalapplication.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class CaseHyperLinkerLookupRequest implements Serializable {
    private List<String> rcc_ids = new ArrayList<>();

    public void add(String id) {
        rcc_ids.add(id);
    }
}
