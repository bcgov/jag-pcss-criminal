package ca.bc.gov.open.pcsscriminalapplication.service.impl;

import ca.bc.gov.open.pcsscriminalapplication.service.CrownValidator;
import ca.bc.gov.open.wsdl.pcss.one.GetCrownAssignmentRequest;
import ca.bc.gov.open.wsdl.pcss.one.SetCounselDetailCriminalRequest;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ca.bc.gov.open.pcsscriminalapplication.utils.ValidationUtils.checkDateTimeTypeForErrors;
import static ca.bc.gov.open.pcsscriminalapplication.utils.ValidationUtils.checkAgencyIdentifierTypeForErrors;
import static ca.bc.gov.open.pcsscriminalapplication.utils.ValidationUtils.checkJustinNoTypeForErrors;
import static ca.bc.gov.open.pcsscriminalapplication.utils.ValidationUtils.checkSystemIdTypeForErrors;
import static ca.bc.gov.open.pcsscriminalapplication.utils.ValidationUtils.checkSystemSeqNoTypeForErrors;

@Service
public class CrownValidatorImpl implements CrownValidator {
    public List<String> validateGetCrownAssignment(GetCrownAssignmentRequest request) {

        if(request == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<String>();

        if(checkAgencyIdentifierTypeForErrors(request.getRequestAgencyIdentifierId())) {
            errors.add("RequestAgencyIdentifierId is not valid");
        }

        if(checkSystemIdTypeForErrors(request.getRequestPartId())) {
            errors.add("RequestPartId is not valid");
        }

        if(request.getRequestDtm() == null || checkDateTimeTypeForErrors(request.getRequestDtm().toString())) {
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


    public List<String> validateSetCounselDetailCriminal(SetCounselDetailCriminalRequest request) {

        if (request == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        if(checkAgencyIdentifierTypeForErrors(request.getRequestAgencyIdentifierId())) {
            errors.add("RequestAgencyIdentifierId is not valid");
        }

        if(checkSystemIdTypeForErrors(request.getRequestPartId())) {
            errors.add("RequestPartId is not valid");
        }

        if(request.getRequestDtm() == null || checkDateTimeTypeForErrors(request.getRequestDtm().toString())) {
            errors.add("RequestDtm is not valid");
        }

        if(checkSystemIdTypeForErrors(request.getProfPartId())) {
            errors.add("ProfPartId is not valid");
        }

        if(checkSystemSeqNoTypeForErrors(request.getProfSeqNo())) {
            errors.add("ProfSeqNo is not valid");
        }

        if(request.getDetail().isEmpty()) {
            errors.add("Detail is not valid");
        } else {

            for(int i = 0; i < request.getDetail().size(); i++) {

                // TODO: Add operation mode cd validation

                if(request.getDetail().get(0).getCounselLastNm() == null) {
                    errors.add(MessageFormat.format("Details CounselLastNm at index {0} is not valid", i+1));
                }

                if(request.getDetail().get(0).getCounselFirstNm() == null) {
                    errors.add(MessageFormat.format("Details CounselFirstNm at index {0} is not valid", i+1));
                }

            }

        }

        return errors;

    }
}
