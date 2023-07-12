package ca.bc.gov.open.pcsscriminalapplication.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"RccIds"})
@XmlRootElement(name = "rccIdsResponse")
public class RccIds implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "demsCasePattern", required = true)
    protected String demsCasePattern;

    @XmlElement(name = "justins")
    protected List<RccId> justins;

    public String getDemsCasePattern() {
        return demsCasePattern;
    }

    public void setDemsCasePattern(String value) {
        this.demsCasePattern = value;
    }

    public List<RccId> getJustins() {
        if (justins == null) {
            justins = new ArrayList<RccId>();
        }
        return this.justins;
    }

    public void setDemsCase(List<RccId> value) {
        this.justins = null;
        if (value != null) {
            List<RccId> draftl = this.getJustins();
            draftl.addAll(value);
        }
    }
}
