package ca.bc.gov.open.pcsscriminalapplication.service.impl;

import ca.bc.gov.open.pcsscriminalapplication.service.HearingValidator;
import ca.bc.gov.open.wsdl.pcss.one.SetHearingRestrictionCriminalRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static ca.bc.gov.open.pcsscriminalapplication.utils.ValidationUtils.checkConcurrencyControlTypeForErrors;
import static ca.bc.gov.open.pcsscriminalapplication.utils.ValidationUtils.checkDateTimeTypeForErrors;
import static ca.bc.gov.open.pcsscriminalapplication.utils.ValidationUtils.checkJustinNoTypeForErrors;
import static ca.bc.gov.open.pcsscriminalapplication.utils.ValidationUtils.checkRequestAgencyIdentifierIdForErrors;
import static ca.bc.gov.open.pcsscriminalapplication.utils.ValidationUtils.checkSystemIdTypeForErrors;
import static ca.bc.gov.open.pcsscriminalapplication.utils.ValidationUtils.checkSystemSeqNoTypeForErrors;

@Service
public class HearingValidatorImpl implements HearingValidator {

    public List<String> validateSetHearingRestrictionCriminal(SetHearingRestrictionCriminalRequest request) {
        List<String> errors = new ArrayList<String>();

        if(checkRequestAgencyIdentifierIdForErrors(request.getRequestAgencyIdentifierId())) {
            errors.add("requestAgencyIdentifierId exceeds max length of 16");
        }

        if(checkSystemIdTypeForErrors(request.getRequestPartId())) {
            errors.add("requestPartId exceeds max length of 16");
        }

        if(checkDateTimeTypeForErrors(request.getRequestDtm().toString())) {
            errors.add("requestDtm is not the correct length of 21");
        }

        if(checkSystemIdTypeForErrors(request.getHearingRestrictionId())) {
            errors.add("hearingRestrictionId exceeds max length of 16");
        }

        if(checkSystemIdTypeForErrors(request.getAdjudicatorPartId())) {
            errors.add("adjudicatorPartId exceeds max length of 16");
        }

        if(checkJustinNoTypeForErrors(request.getJustinNo())) {
            errors.add("justinNo exceeds max length of 12");
        }

        if(checkSystemIdTypeForErrors(request.getPartId())) {
            errors.add("partId exceeds max length of 16");
        }

        if(checkSystemSeqNoTypeForErrors(request.getProfSeqNo())) {
            errors.add("profSeqNo exceeds max length of 4");
        }

        if(checkConcurrencyControlTypeForErrors(request.getHearingRestrictionCcn())) {
            errors.add("hearingRestrictionCcn exceeds max length of 12");
        }

        // TODO: verify if enums need any validations: OperationModeCd, HearingRestrictionCd

        return errors;
    }
}
