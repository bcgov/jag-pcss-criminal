package ca.bc.gov.open.pcsscriminalapplication.service.impl;

import ca.bc.gov.open.pcsscriminalapplication.service.AppearanceValidator;
import ca.bc.gov.open.pcsscriminalapplication.utils.ValidationUtils;
import ca.bc.gov.open.wsdl.pcss.one.*;
import ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalApprMethodSecureRequest;
import ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalCountSecureRequest;
import ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalSecureRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AppearanceValidatorImpl implements AppearanceValidator {

    @Override
    public List<String> validateGetAppearanceCriminal(GetAppearanceCriminalRequest getAppearanceCriminalRequest) {

        if (getAppearanceCriminalRequest == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();


        if (ValidationUtils.checkRequestAgencyIdentifierIdForErrors(getAppearanceCriminalRequest.getRequestAgencyIdentifierId())) {
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

        getAppearanceCriminalApprMethodRequest.getRequestDtm();
        getAppearanceCriminalApprMethodRequest.getRequestAgencyIdentifierId();
        getAppearanceCriminalApprMethodRequest.getRequestPartId();
        getAppearanceCriminalApprMethodRequest.getAppearanceId();

        return errors;

    }

    @Override
    public List<String> validateGetAppearanceCriminalApprMethodSecure(GetAppearanceCriminalApprMethodSecureRequest getAppearanceCriminalApprMethodSecureRequest) {

        if (getAppearanceCriminalApprMethodSecureRequest == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        return errors;

    }

    @Override
    public List<String> validateGetAppearanceCriminalCount(GetAppearanceCriminalCountRequest getAppearanceCriminalCountRequest) {

        if (getAppearanceCriminalCountRequest == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        return errors;

    }

    @Override
    public List<String> validateGetAppearanceCriminalCountSecure(GetAppearanceCriminalCountSecureRequest getAppearanceCriminalCountSecureRequest) {

        if (getAppearanceCriminalCountSecureRequest == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        return errors;

    }

    @Override
    public List<String> validateGetAppearanceCriminalResource(GetAppearanceCriminalResourceRequest getAppearanceCriminalResourceRequest) {

        if (getAppearanceCriminalResourceRequest == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        return errors;

    }

    @Override
    public List<String> validateGetAppearanceCriminalSecure(GetAppearanceCriminalSecureRequest getAppearanceCriminalSecureRequest) {

        if (getAppearanceCriminalSecureRequest == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        return errors;

    }

    @Override
    public List<String> validateSetAppearanceCriminal(SetAppearanceCriminalRequest setAppearanceCriminalRequest) {

        if (setAppearanceCriminalRequest == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        return errors;

    }

    @Override
    public List<String> validateSetAppearanceMethodCriminal(SetAppearanceMethodCriminalRequest setAppearanceMethodCriminalRequest) {

        if (setAppearanceMethodCriminalRequest == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        return errors;

    }
}
