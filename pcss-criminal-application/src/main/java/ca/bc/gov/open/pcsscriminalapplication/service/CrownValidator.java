package ca.bc.gov.open.pcsscriminalapplication.service;

import ca.bc.gov.open.wsdl.pcss.one.GetCrownAssignmentRequest;
import ca.bc.gov.open.wsdl.pcss.one.SetCounselDetailCriminalRequest;
import ca.bc.gov.open.wsdl.pcss.one.SetCrownAssignmentRequest;
import ca.bc.gov.open.wsdl.pcss.one.SetCrownFileDetailRequest;
import java.util.List;

public interface CrownValidator {

    List<String> validateGetCrownAssignment(GetCrownAssignmentRequest request);

    List<String> validateSetCounselDetailCriminal(
            SetCounselDetailCriminalRequest setCounselDetailCriminalRequest);

    List<String> validateSetCrownAssignment(SetCrownAssignmentRequest request);

    List<String> validateSetCrownFileDetail(SetCrownFileDetailRequest request);
}
