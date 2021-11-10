package ca.bc.gov.open.pcsscriminalapplication.service.impl;

import ca.bc.gov.open.pcsscriminalapplication.service.HearingValidator;
import ca.bc.gov.open.wsdl.pcss.one.SetHearingRestrictionCriminalRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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

        if (request == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<String>();

        if(checkRequestAgencyIdentifierIdForErrors(request.getRequestAgencyIdentifierId())) {
            errors.add("RequestAgencyIdentifierId is not valid");
        }

        if(checkSystemIdTypeForErrors(request.getRequestPartId())) {
            errors.add("RequestPartId is not valid");
        }

        if(checkDateTimeTypeForErrors(request.getRequestDtm().toString())) {
            errors.add("RequestDtm is not valid");
        }

        if(request.getOperationModeCd() == null) {
            errors.add("OperationModeCd is not valid");
        }

        if(request.getHearingRestrictionId() != null) {
            if(checkSystemIdTypeForErrors(request.getHearingRestrictionId())) {
                errors.add("HearingRestrictionId is not valid");
            }
        }

        if(request.getAdjudicatorPartId() != null) {
            if(checkSystemIdTypeForErrors(request.getAdjudicatorPartId())) {
                errors.add("AdjudicatorPartId is not valid");
            }
        }

        if(request.getJustinNo() != null) {
            if(checkJustinNoTypeForErrors(request.getJustinNo())) {
                errors.add("JustinNo is not valid");
            }
        }

        if(request.getPartId() != null) {
            if(checkSystemIdTypeForErrors(request.getPartId())) {
                errors.add("PartId is not valid");
            }
        }

        if(request.getProfSeqNo() != null) {
            if(checkSystemSeqNoTypeForErrors(request.getProfSeqNo())) {
                errors.add("ProfSeqNo is not valid");
            }
        }

        if(request.getHearingRestrictionCcn() != null) {
            if(checkConcurrencyControlTypeForErrors(request.getHearingRestrictionCcn())) {
                errors.add("HearingRestrictionCcn is not valid");
            }
        }


        // TODO: verify if enums need any validations: OperationModeCd, HearingRestrictionCd

        return errors;
    }
}
