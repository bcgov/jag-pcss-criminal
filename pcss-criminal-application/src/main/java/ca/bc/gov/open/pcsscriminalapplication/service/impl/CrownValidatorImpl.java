package ca.bc.gov.open.pcsscriminalapplication.service.impl;

import ca.bc.gov.open.wsdl.pcss.one.GetCrownAssignmentRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ca.bc.gov.open.pcsscriminalapplication.utils.ValidationUtils.checkDateTimeTypeForErrors;
import static ca.bc.gov.open.pcsscriminalapplication.utils.ValidationUtils.checkAgencyIdentifierTypeForErrors;
import static ca.bc.gov.open.pcsscriminalapplication.utils.ValidationUtils.checkJustinNoTypeForErrors;
import static ca.bc.gov.open.pcsscriminalapplication.utils.ValidationUtils.checkSystemIdTypeForErrors;

public class CrownValidatorImpl {
    public List<String> validateGetCrownAssignment(GetCrownAssignmentRequest request) {

        if(request == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<String>();

        if(checkAgencyIdentifierTypeForErrors(request.getRequestAgencyIdentifierId())) {
            errors.add("RequestAgencyIdentifierId is not valid");
        }

        if(checkSystemIdTypeForErrors(request.getRequestPartId())) {
            errors.add("RequestPartId is not valid");
        }

        if(checkDateTimeTypeForErrors(request.getRequestDtm().toString())) {
            errors.add("RequestDtm is not valid");
        }

        if(request.getJustinNo() != null && checkJustinNoTypeForErrors(request.getJustinNo())) {
            errors.add("JustinNo is not valid");
        }

        if(request.getSinceDt() != null && checkDateTimeTypeForErrors(request.getSinceDt().toString())) {
            errors.add("SinceDt is not valid");
        }

        return errors;
    }
}
