package ca.bc.gov.open.pcsscriminalapplication.service.impl;

import ca.bc.gov.open.pcsscriminalapplication.service.PersonnelValidator;
import ca.bc.gov.open.pcsscriminalapplication.utils.ValidationUtils;
import ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailDetailRequest;
import ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailabilityRequest;
import ca.bc.gov.open.wsdl.pcss.one.GetPersonnelSearchRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PersonnelValidatorImpl implements PersonnelValidator {

    @Override
    public List<String> validateGetPersonnelAvailability(GetPersonnelAvailabilityRequest getPersonnelAvailabilityRequest) {

        if (getPersonnelAvailabilityRequest == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        if (ValidationUtils.checkAgencyIdentifierTypeForErrors(getPersonnelAvailabilityRequest.getRequestAgencyIdentifierId())) {
            errors.add("RequestAgencyIdentifierId is not valid");
        }

        if (ValidationUtils.checkSystemIdTypeForErrors(getPersonnelAvailabilityRequest.getRequestPartId())) {
            errors.add("RequestPartId is not valid");
        }

        if (getPersonnelAvailabilityRequest.getRequestDtm() == null || ValidationUtils.checkDateTimeTypeForErrors(getPersonnelAvailabilityRequest.getRequestDtm())) {
            errors.add("RequestDtm is not valid");
        }

        if (getPersonnelAvailabilityRequest.getToDt() == null || ValidationUtils.checkDateTimeTypeForErrors(getPersonnelAvailabilityRequest.getToDt())) {
            errors.add("ToDt is not valid");
        }

        if (getPersonnelAvailabilityRequest.getFromDt() == null || ValidationUtils.checkDateTimeTypeForErrors(getPersonnelAvailabilityRequest.getFromDt())) {
            errors.add("FromDt is not valid");
        }

        if (getPersonnelAvailabilityRequest.getPersonTypeCd() == null) {
            errors.add("PersonTypeCd is not valid");
        }

        return errors;

    }

    @Override
    public List<String> validateGetPersonnelAvailDetail(GetPersonnelAvailDetailRequest getPersonnelAvailDetailRequest) {

        if (getPersonnelAvailDetailRequest == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        if (ValidationUtils.checkAgencyIdentifierTypeForErrors(getPersonnelAvailDetailRequest.getRequestAgencyIdentifierId())) {
            errors.add("RequestAgencyIdentifierId is not valid");
        }

        if (ValidationUtils.checkSystemIdTypeForErrors(getPersonnelAvailDetailRequest.getRequestPartId())) {
            errors.add("RequestPartId is not valid");
        }

        if (getPersonnelAvailDetailRequest.getRequestDtm() == null || ValidationUtils.checkDateTimeTypeForErrors(getPersonnelAvailDetailRequest.getRequestDtm())) {
            errors.add("RequestDtm is not valid");
        }

        if (getPersonnelAvailDetailRequest.getAvailabilityDt() == null || ValidationUtils.checkDateTimeTypeForErrors(getPersonnelAvailDetailRequest.getAvailabilityDt())) {
            errors.add("AvailabilityDt is not valid");
        }

        if (getPersonnelAvailDetailRequest.getPersonTypeCd() == null) {
            errors.add("PersonTypeCd is not valid");
        }

        return errors;

    }

    @Override
    public List<String> validateGetPersonnelSearch(GetPersonnelSearchRequest getPersonnelSearchRequest) {

        if (getPersonnelSearchRequest == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        if (ValidationUtils.checkAgencyIdentifierTypeForErrors(getPersonnelSearchRequest.getRequestAgencyIdentifierId())) {
            errors.add("RequestAgencyIdentifierId is not valid");
        }

        if (ValidationUtils.checkSystemIdTypeForErrors(getPersonnelSearchRequest.getRequestPartId())) {
            errors.add("RequestPartId is not valid");
        }

        if (getPersonnelSearchRequest.getRequestDtm() == null || ValidationUtils.checkDateTimeTypeForErrors(getPersonnelSearchRequest.getRequestDtm().toString())) {
            errors.add("RequestDtm is not valid");
        }

        if (ValidationUtils.checkAgencyIdentifierTypeForErrors(getPersonnelSearchRequest.getAgencyId())) {
            errors.add("AgencyId is not valid");
        }

        return errors;

    }
}
