package ca.bc.gov.open.pcsscriminalapplication.service.impl;

import ca.bc.gov.open.pcsscriminalapplication.service.AppearanceValidator;
import ca.bc.gov.open.pcsscriminalapplication.utils.ValidationUtils;
import ca.bc.gov.open.wsdl.pcss.one.*;
import ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalApprMethodSecureRequest;
import ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalCountSecureRequest;
import ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalSecureRequest;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AppearanceValidatorImpl implements AppearanceValidator {

    @Override
    public List<String> validateGetAppearanceCriminal(GetAppearanceCriminalRequest getAppearanceCriminalRequest) {

        if (getAppearanceCriminalRequest == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        if (ValidationUtils.checkAgencyIdentifierTypeForErrors(getAppearanceCriminalRequest.getRequestAgencyIdentifierId())) {
            errors.add("RequestAgencyIdentifierId is not valid");
        }

        if (ValidationUtils.checkSystemIdTypeForErrors(getAppearanceCriminalRequest.getRequestPartId())) {
            errors.add("RequestPartId is not valid");
        }

        if (getAppearanceCriminalRequest.getRequestDtm() == null || ValidationUtils.checkDateTimeTypeForErrors(getAppearanceCriminalRequest.getRequestDtm().toString())) {
            errors.add("RequestDtm is not valid");
        }

        if (ValidationUtils.checkSystemIdTypeForErrors(getAppearanceCriminalRequest.getAppearanceId())) {
            errors.add("AppearanceId is not valid");
        }

        if (ValidationUtils.checkJustinNoTypeForErrors(getAppearanceCriminalRequest.getJustinNo())) {
            errors.add("JustinNo is not valid");
        }

        return errors;

    }

    @Override
    public List<String> validateGetAppearanceCriminalApprMethod(GetAppearanceCriminalApprMethodRequest getAppearanceCriminalApprMethodRequest) {

        if (getAppearanceCriminalApprMethodRequest == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        if (ValidationUtils.checkAgencyIdentifierTypeForErrors(getAppearanceCriminalApprMethodRequest.getRequestAgencyIdentifierId())) {
            errors.add("RequestAgencyIdentifierId is not valid");
        }

        if (ValidationUtils.checkSystemIdTypeForErrors(getAppearanceCriminalApprMethodRequest.getRequestPartId())) {
            errors.add("RequestPartId is not valid");
        }

        if (getAppearanceCriminalApprMethodRequest.getRequestDtm() == null || ValidationUtils.checkDateTimeTypeForErrors(getAppearanceCriminalApprMethodRequest.getRequestDtm().toString())) {
            errors.add("RequestDtm is not valid");
        }

        if (ValidationUtils.checkSystemIdTypeForErrors(getAppearanceCriminalApprMethodRequest.getAppearanceId())) {
            errors.add("AppearanceId is not valid");
        }

        return errors;

    }

    @Override
    public List<String> validateGetAppearanceCriminalApprMethodSecure(GetAppearanceCriminalApprMethodSecureRequest getAppearanceCriminalApprMethodSecureRequest) {

        if (getAppearanceCriminalApprMethodSecureRequest == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        if (ValidationUtils.checkAgencyIdentifierTypeForErrors(getAppearanceCriminalApprMethodSecureRequest.getRequestAgencyIdentifierId())) {
            errors.add("RequestAgencyIdentifierId is not valid");
        }

        if (ValidationUtils.checkSystemIdTypeForErrors(getAppearanceCriminalApprMethodSecureRequest.getRequestPartId())) {
            errors.add("RequestPartId is not valid");
        }

        if (getAppearanceCriminalApprMethodSecureRequest.getRequestDtm() == null || ValidationUtils.checkDateTimeTypeForErrors(getAppearanceCriminalApprMethodSecureRequest.getRequestDtm().toString())) {
            errors.add("RequestDtm is not valid");
        }

        if (ValidationUtils.checkSystemIdTypeForErrors(getAppearanceCriminalApprMethodSecureRequest.getAppearanceId())) {
            errors.add("AppearanceId is not valid");
        }

        //TODO: Add application cd validation

        return errors;

    }

    @Override
    public List<String> validateGetAppearanceCriminalCount(GetAppearanceCriminalCountRequest getAppearanceCriminalCountRequest) {

        if (getAppearanceCriminalCountRequest == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        if (ValidationUtils.checkAgencyIdentifierTypeForErrors(getAppearanceCriminalCountRequest.getRequestAgencyIdentifierId())) {
            errors.add("RequestAgencyIdentifierId is not valid");
        }

        if (ValidationUtils.checkSystemIdTypeForErrors(getAppearanceCriminalCountRequest.getRequestPartId())) {
            errors.add("RequestPartId is not valid");
        }

        if (getAppearanceCriminalCountRequest.getRequestDtm() == null || ValidationUtils.checkDateTimeTypeForErrors(getAppearanceCriminalCountRequest.getRequestDtm().toString())) {
            errors.add("RequestDtm is not valid");
        }

        if (ValidationUtils.checkSystemIdTypeForErrors(getAppearanceCriminalCountRequest.getAppearanceId())) {
            errors.add("AppearanceId is not valid");
        }

        return errors;

    }

    @Override
    public List<String> validateGetAppearanceCriminalCountSecure(GetAppearanceCriminalCountSecureRequest getAppearanceCriminalCountSecureRequest) {

        if (getAppearanceCriminalCountSecureRequest == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        if (ValidationUtils.checkAgencyIdentifierTypeForErrors(getAppearanceCriminalCountSecureRequest.getRequestAgencyIdentifierId())) {
            errors.add("RequestAgencyIdentifierId is not valid");
        }

        if (ValidationUtils.checkSystemIdTypeForErrors(getAppearanceCriminalCountSecureRequest.getRequestPartId())) {
            errors.add("RequestPartId is not valid");
        }

        if (getAppearanceCriminalCountSecureRequest.getRequestDtm() == null || ValidationUtils.checkDateTimeTypeForErrors(getAppearanceCriminalCountSecureRequest.getRequestDtm().toString())) {
            errors.add("RequestDtm is not valid");
        }

        if (ValidationUtils.checkSystemIdTypeForErrors(getAppearanceCriminalCountSecureRequest.getAppearanceId())) {
            errors.add("AppearanceId is not valid");
        }

        //TODO: Add application cd validation

        return errors;

    }

    @Override
    public List<String> validateGetAppearanceCriminalResource(GetAppearanceCriminalResourceRequest getAppearanceCriminalResourceRequest) {

        if (getAppearanceCriminalResourceRequest == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        if (ValidationUtils.checkAgencyIdentifierTypeForErrors(getAppearanceCriminalResourceRequest.getRequestAgencyIdentifierId())) {
            errors.add("RequestAgencyIdentifierId is not valid");
        }

        if (ValidationUtils.checkSystemIdTypeForErrors(getAppearanceCriminalResourceRequest.getRequestPartId())) {
            errors.add("RequestPartId is not valid");
        }

        if (getAppearanceCriminalResourceRequest.getRequestDtm() == null || ValidationUtils.checkDateTimeTypeForErrors(getAppearanceCriminalResourceRequest.getRequestDtm().toString())) {
            errors.add("RequestDtm is not valid");
        }

        if (ValidationUtils.checkSystemIdTypeForErrors(getAppearanceCriminalResourceRequest.getAppearanceId())) {
            errors.add("AppearanceId is not valid");
        }

        return errors;

    }

    @Override
    public List<String> validateGetAppearanceCriminalSecure(GetAppearanceCriminalSecureRequest getAppearanceCriminalSecureRequest) {

        if (getAppearanceCriminalSecureRequest == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        if (ValidationUtils.checkAgencyIdentifierTypeForErrors(getAppearanceCriminalSecureRequest.getRequestAgencyIdentifierId())) {
            errors.add("RequestAgencyIdentifierId is not valid");
        }

        if (ValidationUtils.checkSystemIdTypeForErrors(getAppearanceCriminalSecureRequest.getRequestPartId())) {
            errors.add("RequestPartId is not valid");
        }

        if (getAppearanceCriminalSecureRequest.getRequestDtm() == null || ValidationUtils.checkDateTimeTypeForErrors(getAppearanceCriminalSecureRequest.getRequestDtm().toString())) {
            errors.add("RequestDtm is not valid");
        }

        if (ValidationUtils.checkSystemIdTypeForErrors(getAppearanceCriminalSecureRequest.getAppearanceId())) {
            errors.add("AppearanceId is not valid");
        }

        if (ValidationUtils.checkJustinNoTypeForErrors(getAppearanceCriminalSecureRequest.getJustinNo())) {
            errors.add("JustinNo is not valid");
        }

        //TODO: Add application cd validation

        return errors;

    }

    @Override
    public List<String> validateSetAppearanceCriminal(SetAppearanceCriminalRequest setAppearanceCriminalRequest) {

        if (setAppearanceCriminalRequest == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        if (ValidationUtils.checkAgencyIdentifierTypeForErrors(setAppearanceCriminalRequest.getRequestAgencyIdentifierId())) {
            errors.add("RequestAgencyIdentifierId is not valid");
        }

        if (ValidationUtils.checkSystemIdTypeForErrors(setAppearanceCriminalRequest.getRequestPartId())) {
            errors.add("RequestPartId is not valid");
        }

        if (setAppearanceCriminalRequest.getRequestDtm() == null || ValidationUtils.checkDateTimeTypeForErrors(setAppearanceCriminalRequest.getRequestDtm().toString())) {
            errors.add("RequestDtm is not valid");
        }

        for (int i = 0; i < setAppearanceCriminalRequest.getDetail().size(); i++) {

            if (ValidationUtils.checkSystemIdTypeForErrors(setAppearanceCriminalRequest.getDetail().get(0).getPcssAppearanceId())) {
                errors.add(MessageFormat.format("Details PcssAppearanceId at index {0} is not valid", i+1));
            }

            if (ValidationUtils.checkJustinNoTypeForErrors(setAppearanceCriminalRequest.getDetail().get(0).getJustinNo())) {
                errors.add(MessageFormat.format("Details JustinNo at index {0} is not valid", i+1));
            }

            if (ValidationUtils.checkSystemIdTypeForErrors(setAppearanceCriminalRequest.getDetail().get(0).getPartId())) {
                errors.add(MessageFormat.format("Details PartId at index {0} is not valid", i+1));
            }

            if (ValidationUtils.checkSystemSeqNoTypeForErrors(setAppearanceCriminalRequest.getDetail().get(0).getProfSeqNo())) {
                errors.add(MessageFormat.format("Details ProfSeqNo at index {0} is not valid", i+1));
            }

            if (ValidationUtils.checkDateTimeTypeForErrors(setAppearanceCriminalRequest.getDetail().get(0).getAppearanceDt().toString())) {
                errors.add(MessageFormat.format("Details AppearanceDt at index {0} is not valid", i+1));
            }

            if (ValidationUtils.checkDateTimeTypeForErrors(setAppearanceCriminalRequest.getDetail().get(0).getAppearanceTm())) {
                errors.add(MessageFormat.format("Details AppearanceTm at index {0} is not valid", i+1));
            }

            if (ValidationUtils.checkAppearanceReasonCriminalTypeForErrors(setAppearanceCriminalRequest.getDetail().get(0).getAppearanceReasonCd())) {
                errors.add(MessageFormat.format("Details AppearanceReasonCd at index {0} is not valid", i+1));
            }

            if (ValidationUtils.checkAgencyIdentifierTypeForErrors(setAppearanceCriminalRequest.getDetail().get(0).getCourtAgencyId())) {
                errors.add(MessageFormat.format("Details CourtAgencyId at index {0} is not valid", i+1));
            }

            if (ValidationUtils.checkCourtRoomTypeForErrors(setAppearanceCriminalRequest.getDetail().get(0).getCourtRoomCd())) {
                errors.add(MessageFormat.format("Details CourtRoomCd at index {0} is not valid", i+1));
            }

        }

        return errors;

    }

    @Override
    public List<String> validateSetAppearanceMethodCriminal(SetAppearanceMethodCriminalRequest setAppearanceMethodCriminalRequest) {

        if (setAppearanceMethodCriminalRequest == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        if (ValidationUtils.checkAgencyIdentifierTypeForErrors(setAppearanceMethodCriminalRequest.getRequestAgencyIdentifierId())) {
            errors.add("RequestAgencyIdentifierId is not valid");
        }

        if (ValidationUtils.checkSystemIdTypeForErrors(setAppearanceMethodCriminalRequest.getRequestPartId())) {
            errors.add("RequestPartId is not valid");
        }

        if (setAppearanceMethodCriminalRequest.getRequestDtm() == null || ValidationUtils.checkDateTimeTypeForErrors(setAppearanceMethodCriminalRequest.getRequestDtm().toString())) {
            errors.add("RequestDtm is not valid");
        }

        for (int i = 0; i < setAppearanceMethodCriminalRequest.getDetail().size(); i++) {

            if (ValidationUtils.checkSystemIdTypeForErrors(setAppearanceMethodCriminalRequest.getDetail().get(0).getApprId())) {
                errors.add(MessageFormat.format("Details PcssAppearanceId at index {0} is not valid", i + 1));
            }

            if (ValidationUtils.checkSystemSeqNoTypeForErrors(setAppearanceMethodCriminalRequest.getDetail().get(0).getRoleCd())) {
                errors.add(MessageFormat.format("Details PcssAppearanceId at index {0} is not valid", i + 1));
            }

            if (ValidationUtils.checkSystemSeqNoTypeForErrors(setAppearanceMethodCriminalRequest.getDetail().get(0).getAssetUsageSeqNo())) {
                errors.add(MessageFormat.format("Details JustinNo at index {0} is not valid", i + 1));
            }

            if (ValidationUtils.checkSystemSeqNoTypeForErrors(setAppearanceMethodCriminalRequest.getDetail().get(0).getAppearanceMethodCd())) {
                errors.add(MessageFormat.format("Details PartId at index {0} is not valid", i + 1));
            }


            if (ValidationUtils.checkConcurrencyControlTypeForErrors(setAppearanceMethodCriminalRequest.getDetail().get(0).getApprMethodCcn())) {
                errors.add(MessageFormat.format("Details AppearanceReasonCd at index {0} is not valid", i + 1));
            }

        }

        return errors;

    }
}