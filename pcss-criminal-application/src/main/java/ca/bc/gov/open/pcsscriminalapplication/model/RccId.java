package ca.bc.gov.open.pcsscriminalapplication.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "rccId",
        propOrder = {"justinNo", "rccId"})
public class RccId implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "justinNo", required = true)
    protected String justinNo;

    @XmlElement(name = "rccId")
    protected String rccId;

    public String getJustinNo() {
        return justinNo;
    }

    public void setJustinNo(String value) {
        this.justinNo = value;
    }

    public String getRccId() {
        return rccId;
    }

    public void setRccId(String value) {
        this.rccId = value;
    }
}
