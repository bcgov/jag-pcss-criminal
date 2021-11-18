package ca.bc.gov.open.pcsscriminalapplication.service;

import ca.bc.gov.open.wsdl.pcss.one.GetCrownAssignmentRequest;

import java.util.List;

public interface CrownValidator {
    public List<String> validateGetCrownAssignment(GetCrownAssignmentRequest request);
}
