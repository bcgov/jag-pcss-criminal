package ca.bc.gov.open.pcsscriminalapplication.model;

import jakarta.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"JustinRccs"})
@XmlRootElement(name = "rccIdsResponse")
public class JustinRCCs implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "justins")
    protected List<JustinRcc> justins;

    public List<JustinRcc> getJustins() {
        if (justins == null) {
            justins = new ArrayList<JustinRcc>();
        }
        return this.justins;
    }

    public void setDemsCase(List<JustinRcc> value) {
        this.justins = null;
        if (value != null) {
            List<JustinRcc> draftl = this.getJustins();
            draftl.addAll(value);
        }
    }
}
