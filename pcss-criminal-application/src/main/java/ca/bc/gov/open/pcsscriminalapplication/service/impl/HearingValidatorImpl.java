package ca.bc.gov.open.pcsscriminalapplication.service.impl;

import ca.bc.gov.open.pcsscriminalapplication.service.HearingValidator;
import ca.bc.gov.open.wsdl.pcss.one.SetHearingRestrictionCriminalRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ca.bc.gov.open.pcsscriminalapplication.utils.ValidationUtils.checkConcurrencyControlTypeForErrors;
import static ca.bc.gov.open.pcsscriminalapplication.utils.ValidationUtils.checkDateTimeTypeForErrors;
import static ca.bc.gov.open.pcsscriminalapplication.utils.ValidationUtils.checkJustinNoTypeForErrors;
import static ca.bc.gov.open.pcsscriminalapplication.utils.ValidationUtils.checkAgencyIdentifierTypeForErrors;
import static ca.bc.gov.open.pcsscriminalapplication.utils.ValidationUtils.checkSystemIdTypeForErrors;
import static ca.bc.gov.open.pcsscriminalapplication.utils.ValidationUtils.checkSystemSeqNoTypeForErrors;

@Service
public class HearingValidatorImpl implements HearingValidator {

    public List<String> validateSetHearingRestrictionCriminal(SetHearingRestrictionCriminalRequest request) {

        if (request == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        if (checkAgencyIdentifierTypeForErrors(request.getRequestAgencyIdentifierId())) {
            errors.add("RequestAgencyIdentifierId is not valid");
        }

        if (checkSystemIdTypeForErrors(request.getRequestPartId())) {
            errors.add("RequestPartId is not valid");
        }

        if (StringUtils.isBlank(request.getRequestDtm()) || checkDateTimeTypeForErrors(request.getRequestDtm())) {
            errors.add("RequestDtm is not valid");
        }

        if (request.getOperationModeCd() == null) {
            errors.add("OperationModeCd is not valid");
        }

        if (StringUtils.isNoneEmpty(request.getHearingRestrictionId()) && checkSystemIdTypeForErrors(request.getHearingRestrictionId())) {
            errors.add("HearingRestrictionId is not valid");
        }

        if (StringUtils.isNoneEmpty(request.getAdjudicatorPartId()) && checkSystemIdTypeForErrors(request.getAdjudicatorPartId())) {
            errors.add("AdjudicatorPartId is not valid");
        }

        if (StringUtils.isNoneEmpty(request.getJustinNo()) && checkJustinNoTypeForErrors(request.getJustinNo())) {
            errors.add("JustinNo is not valid");
        }

        if (StringUtils.isNoneEmpty(request.getPartId()) && checkSystemIdTypeForErrors(request.getPartId())) {
            errors.add("PartId is not valid");
        }

        if (StringUtils.isNoneEmpty(request.getProfSeqNo()) && checkSystemSeqNoTypeForErrors(request.getProfSeqNo())) {
            errors.add("ProfSeqNo is not valid");
        }

        if (StringUtils.isNoneEmpty(request.getHearingRestrictionCcn()) && checkConcurrencyControlTypeForErrors(request.getHearingRestrictionCcn())) {
            errors.add("HearingRestrictionCcn is not valid");
        }
        
        return errors;

    }

}
