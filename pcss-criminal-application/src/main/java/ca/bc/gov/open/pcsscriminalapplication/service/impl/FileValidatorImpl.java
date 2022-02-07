package ca.bc.gov.open.pcsscriminalapplication.service.impl;

import ca.bc.gov.open.pcsscriminalapplication.service.FileValidator;
import ca.bc.gov.open.pcsscriminalapplication.utils.DateUtils;
import ca.bc.gov.open.pcsscriminalapplication.utils.ValidationUtils;
import ca.bc.gov.open.wsdl.pcss.one.GetClosedFileRequest;
import ca.bc.gov.open.wsdl.pcss.one.GetFileDetailCriminalRequest;
import ca.bc.gov.open.wsdl.pcss.one.SetFileNoteRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FileValidatorImpl implements FileValidator {

    @Override
    public List<String> validateGetClosedFile(GetClosedFileRequest getClosedFileRequest) {

        if (getClosedFileRequest == null)
            return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        if (ValidationUtils.checkAgencyIdentifierTypeForErrors(
                getClosedFileRequest.getRequestAgencyIdentifierId())) {
            errors.add("RequestAgencyIdentifierId is not valid");
        }

        if (ValidationUtils.checkSystemIdTypeForErrors(getClosedFileRequest.getRequestPartId())) {
            errors.add("RequestPartId is not valid");
        }

        if (getClosedFileRequest.getRequestDtm() == null
                || ValidationUtils.checkDateTimeTypeForErrors(
                        DateUtils.formatTo21Length(getClosedFileRequest.getRequestDtm()))) {
            errors.add("RequestDtm is not valid");
        }

        if (ValidationUtils.checkAgencyIdentifierTypeForErrors(
                getClosedFileRequest.getAgencyId())) {
            errors.add("AgencyId is not valid");
        }

        if (getClosedFileRequest.getFromApprDt() == null
                || ValidationUtils.checkDateTimeTypeForErrors(
                        DateUtils.formatTo21Length(getClosedFileRequest.getFromApprDt()))) {
            errors.add("FromApprDt is not valid");
        }

        if (getClosedFileRequest.getToApprDt() == null
                || ValidationUtils.checkDateTimeTypeForErrors(
                        DateUtils.formatTo21Length(getClosedFileRequest.getToApprDt()))) {
            errors.add("ToApprDt is not valid");
        }

        return errors;
    }

    @Override
    public List<String> validateGetFileDetailCriminal(
            GetFileDetailCriminalRequest getFileDetailCriminalRequest) {

        if (getFileDetailCriminalRequest == null)
            return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        if (ValidationUtils.checkAgencyIdentifierTypeForErrors(
                getFileDetailCriminalRequest.getRequestAgencyIdentifierId())) {
            errors.add("RequestAgencyIdentifierId is not valid");
        }

        if (ValidationUtils.checkSystemIdTypeForErrors(
                getFileDetailCriminalRequest.getRequestPartId())) {
            errors.add("RequestPartId is not valid");
        }

        if (getFileDetailCriminalRequest.getRequestDtm() == null
                || ValidationUtils.checkDateTimeTypeForErrors(
                        DateUtils.formatTo21Length(getFileDetailCriminalRequest.getRequestDtm()))) {
            errors.add("RequestDtm is not valid");
        }

        if (ValidationUtils.checkJustinNoTypeForErrors(
                getFileDetailCriminalRequest.getJustinNo())) {
            errors.add("JustinNo is not valid");
        }

        // TODO: Add application cd validation

        return errors;
    }

    @Override
    public List<String> validateGetFileDetailCriminalSecure(
            ca.bc.gov.open.wsdl.pcss.secure.one.GetFileDetailCriminalRequest
                    getFileDetailCriminalRequest) {

        if (getFileDetailCriminalRequest == null)
            return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        if (ValidationUtils.checkAgencyIdentifierTypeForErrors(
                getFileDetailCriminalRequest.getRequestAgencyIdentifierId())) {
            errors.add("RequestAgencyIdentifierId is not valid");
        }

        if (ValidationUtils.checkSystemIdTypeForErrors(
                getFileDetailCriminalRequest.getRequestPartId())) {
            errors.add("RequestPartId is not valid");
        }

        if (getFileDetailCriminalRequest.getRequestDtm() == null
                || ValidationUtils.checkDateTimeTypeForErrors(
                        DateUtils.formatTo21Length(getFileDetailCriminalRequest.getRequestDtm()))) {
            errors.add("RequestDtm is not valid");
        }

        if (ValidationUtils.checkJustinNoTypeForErrors(
                getFileDetailCriminalRequest.getJustinNo())) {
            errors.add("JustinNo is not valid");
        }

        // TODO: Add application cd validation

        return errors;
    }

    @Override
    public List<String> validateSetFileNote(SetFileNoteRequest setFileNoteRequest) {

        if (setFileNoteRequest == null)
            return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        if (ValidationUtils.checkAgencyIdentifierTypeForErrors(
                setFileNoteRequest.getRequestAgencyIdentifierId())) {
            errors.add("RequestAgencyIdentifierId is not valid");
        }

        if (ValidationUtils.checkSystemIdTypeForErrors(setFileNoteRequest.getRequestPartId())) {
            errors.add("RequestPartId is not valid");
        }

        if (setFileNoteRequest.getRequestDtm() == null
                || ValidationUtils.checkDateTimeTypeForErrors(
                        DateUtils.formatTo21Length(setFileNoteRequest.getRequestDtm()))) {
            errors.add("RequestDtm is not valid");
        }

        if (ValidationUtils.checkJustinNoTypeForErrors(setFileNoteRequest.getJustinNo())) {
            errors.add("JustinNo is not valid");
        }

        return errors;
    }
}
