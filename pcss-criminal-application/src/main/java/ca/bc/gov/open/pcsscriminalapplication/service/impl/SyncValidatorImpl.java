package ca.bc.gov.open.pcsscriminalapplication.service.impl;

import ca.bc.gov.open.pcsscriminalapplication.service.SyncValidator;
import ca.bc.gov.open.pcsscriminalapplication.utils.ValidationUtils;
import ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceRequest;
import ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SyncValidatorImpl implements SyncValidator {

    @Override
    public List<String> validateGetSyncCriminalAppearance(GetSyncCriminalAppearanceRequest getSyncCriminalAppearanceRequest) {

        if (getSyncCriminalAppearanceRequest == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        if (ValidationUtils.checkRequestAgencyIdentifierIdForErrors(getSyncCriminalAppearanceRequest.getRequestAgencyIdentifierId())) {
            errors.add("RequestAgencyIdentifierId is not valid");
        }

        if (ValidationUtils.checkSystemIdTypeForErrors(getSyncCriminalAppearanceRequest.getRequestPartId())) {
            errors.add("RequestPartId is not valid");
        }

        if (getSyncCriminalAppearanceRequest.getRequestDtm() == null || ValidationUtils.checkDateTimeTypeForErrors(getSyncCriminalAppearanceRequest.getRequestDtm().toString())) {
            errors.add("RequestDtm is not valid");
        }

        if (getSyncCriminalAppearanceRequest.getProcessUpToDtm() == null || ValidationUtils.checkDateTimeTypeForErrors(getSyncCriminalAppearanceRequest.getProcessUpToDtm().toString())) {
            errors.add("ProcessUpToDtm is not valid");
        }

        return errors;

    }

    @Override
    public List<String> validateGetSyncCriminalHearingRestriction(GetSyncCriminalHearingRestrictionRequest getSyncCriminalHearingRestrictionRequest) {

        if (getSyncCriminalHearingRestrictionRequest == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        if (ValidationUtils.checkRequestAgencyIdentifierIdForErrors(getSyncCriminalHearingRestrictionRequest.getRequestAgencyIdentifierId())) {
            errors.add("RequestAgencyIdentifierId is not valid");
        }

        if (ValidationUtils.checkSystemIdTypeForErrors(getSyncCriminalHearingRestrictionRequest.getRequestPartId())) {
            errors.add("RequestPartId is not valid");
        }

        if (ValidationUtils.checkDateTimeTypeForErrors(getSyncCriminalHearingRestrictionRequest.getRequestDtm().toString())) {
            errors.add("RequestDtm is not valid");
        }

        if (ValidationUtils.checkDateTimeTypeForErrors(getSyncCriminalHearingRestrictionRequest.getProcessUpToDtm().toString())) {
            errors.add("ProcessUpToDtm is not valid");
        }

        return errors;

    }
}
