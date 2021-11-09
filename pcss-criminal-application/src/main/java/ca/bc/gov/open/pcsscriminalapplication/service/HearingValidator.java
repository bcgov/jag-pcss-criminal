package ca.bc.gov.open.pcsscriminalapplication.service;

import ca.bc.gov.open.wsdl.pcss.one.SetHearingRestrictionCriminalRequest;

import java.util.List;

public interface HearingValidator {
    public List<String> validateSetHearingRestrictionCriminal(SetHearingRestrictionCriminalRequest request);
}
