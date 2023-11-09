package ca.bc.gov.open.pcsscriminalapplication.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "rccId",
        propOrder = {"justinNo", "rccId"})
public class JustinRcc implements Serializable {
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
