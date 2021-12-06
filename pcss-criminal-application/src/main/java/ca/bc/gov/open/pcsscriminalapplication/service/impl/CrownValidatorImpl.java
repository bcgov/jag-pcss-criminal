package ca.bc.gov.open.pcsscriminalapplication.service.impl;

import ca.bc.gov.open.pcsscriminalapplication.service.CrownValidator;
import ca.bc.gov.open.wsdl.pcss.one.GetCrownAssignmentRequest;
import ca.bc.gov.open.wsdl.pcss.one.SetCounselDetailCriminalRequest;
import ca.bc.gov.open.wsdl.pcss.one.SetCrownAssignmentRequest;
import ca.bc.gov.open.wsdl.pcss.one.SetCrownFileDetailRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ca.bc.gov.open.pcsscriminalapplication.utils.ValidationUtils.*;

@Service
public class CrownValidatorImpl implements CrownValidator {

    @Override
    public List<String> validateGetCrownAssignment(GetCrownAssignmentRequest request) {

        if(request == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<String>();

        if(checkAgencyIdentifierTypeForErrors(request.getRequestAgencyIdentifierId())) {
            errors.add("RequestAgencyIdentifierId is not valid");
        }

        if(checkSystemIdTypeForErrors(request.getRequestPartId())) {
            errors.add("RequestPartId is not valid");
        }

        if(request.getRequestDtm() == null || checkDateTimeTypeForErrors(request.getRequestDtm())) {
            errors.add("RequestDtm is not valid");
        }

        if(request.getJustinNo() != null && checkJustinNoTypeForErrors(request.getJustinNo())) {
            errors.add("JustinNo is not valid");
        }

        if (!StringUtils.isEmpty(request.getSinceDt()) && checkDateTimeTypeForErrors(request.getSinceDt())) {
            errors.add("SinceDt is not valid");
        }

        return errors;
    }

    @Override
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

                if(request.getDetail().get(i).getOperationModeCd() ==null) {
                    errors.add(MessageFormat.format("Details OperationModeCd at index {0} is not valid", i+1));
                }

                if(StringUtils.isBlank(request.getDetail().get(i).getCounselLastNm())) {
                    errors.add(MessageFormat.format("Details CounselLastNm at index {0} is not valid", i+1));
                }

                if(StringUtils.isBlank(request.getDetail().get(i).getCounselFirstNm())) {
                    errors.add(MessageFormat.format("Details CounselFirstNm at index {0} is not valid", i+1));
                }

            }

        }

        return errors;

    }

    @Override
    public List<String> validateSetCrownAssignment(SetCrownAssignmentRequest request) {

        if (request == null) return Collections.singletonList("Empty request is invalid");

        List<String> errors = new ArrayList<>();

        if(checkAgencyIdentifierTypeForErrors(request.getRequestAgencyIdentifierId())) {
            errors.add("RequestAgencyIdentifierId is not valid");
        }

        if(checkSystemIdTypeForErrors(request.getRequestPartId())) {
            errors.add("RequestPartId is not valid");
        }

        if(request.getRequestDtm() == null || checkDateTimeTypeForErrors(request.getRequestDtm())) {
            errors.add("RequestDtm is not valid");
        }

        if(StringUtils.isBlank(request.getJustinNo()) || checkJustinNoTypeForErrors(request.getJustinNo())) {
            errors.add("JustinNo is not valid");
        }

        if(request.getCrownAssignment().isEmpty()) {
            errors.add("CrownAssignment is not valid");
        } else {

            for (int i = 0; i < request.getCrownAssignment().size(); i++) {

                if (StringUtils.isNoneEmpty(request.getCrownAssignment().get(i).getAssigningPaasAgencyId()) && checkAgencyIdentifierTypeForErrors(request.getCrownAssignment().get(i).getAssigningPaasAgencyId())) {
                    errors.add(MessageFormat.format("CrownAssignment AssigningPaasAgencyId at index {0} is not valid", i+1));
                }

                if (StringUtils.isNoneEmpty(request.getCrownAssignment().get(i).getAssigningPaasPartId()) && checkSystemIdTypeForErrors(request.getCrownAssignment().get(i).getAssigningPaasPartId())) {
                    errors.add(MessageFormat.format("CrownAssignment AssigningPaasPartId at index {0} is not valid", i+1));
                }

                if (StringUtils.isNoneEmpty(request.getCrownAssignment().get(i).getAssigningPaasSeqNo()) && checkSystemSeqNoTypeForErrors(request.getCrownAssignment().get(i).getAssigningPaasSeqNo())) {
                    errors.add(MessageFormat.format("CrownAssignment AssigningPaasSeqNo at index {0} is not valid", i+1));
                }

                if (checkDateTimeTypeForErrors(request.getCrownAssignment().get(i).getAssignmentDt())) {
                    errors.add(MessageFormat.format("CrownAssignment AssignmentDt at index {0} is not valid", i+1));
                }

                if (checkDateTimeTypeForErrors(request.getCrownAssignment().get(i).getAssignmentEndDt())) {
                    errors.add(MessageFormat.format("CrownAssignment AssignmentEndDt at index {0} is not valid", i+1));
                }

                if (request.getCrownAssignment().get(i).getOperationModeCd() == null) {
                    errors.add(MessageFormat.format("CrownAssignment OperationModeCd at index {0} is not valid", i+1));
                }

                if (StringUtils.isNoneEmpty(request.getCrownAssignment().get(i).getPaasAgencyId()) && checkAgencyIdentifierTypeForErrors(request.getCrownAssignment().get(i).getPaasAgencyId())) {
                    errors.add(MessageFormat.format("CrownAssignment PaasAgencyId at index {0} is not valid", i+1));
                }

                if (StringUtils.isNoneEmpty(request.getCrownAssignment().get(i).getPaasPartId()) && checkSystemIdTypeForErrors(request.getCrownAssignment().get(i).getPaasPartId())) {
                    errors.add(MessageFormat.format("CrownAssignment PaasPartId at index {0} is not valid", i+1));
                }

                if (StringUtils.isNoneEmpty(request.getCrownAssignment().get(i).getPaasSeqNo()) && checkSystemSeqNoTypeForErrors(request.getCrownAssignment().get(i).getPaasSeqNo())) {
                    errors.add(MessageFormat.format("CrownAssignment PaasSeqNo at index {0} is not valid", i+1));
                }

                if (StringUtils.isNoneEmpty(request.getCrownAssignment().get(i).getWorkAssignmentCcn()) && checkConcurrencyControlTypeForErrors(request.getCrownAssignment().get(i).getWorkAssignmentCcn())) {
                    errors.add(MessageFormat.format("CrownAssignment WorkAssignmentCcn at index {0} is not valid", i+1));
                }

                if (StringUtils.isNoneEmpty(request.getCrownAssignment().get(i).getWorkAssignmentId()) && checkSystemIdTypeForErrors(request.getCrownAssignment().get(i).getWorkAssignmentId())) {
                    errors.add(MessageFormat.format("CrownAssignment WorkAssignmentId at index {0} is not valid", i+1));
                }

            }

        }

        return errors;

    }

    @Override
    public List<String> validateSetCrownFileDetail(SetCrownFileDetailRequest request) {

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

        if (StringUtils.isBlank(request.getJustinNo()) || checkJustinNoTypeForErrors(request.getJustinNo())) {
            errors.add("JustinNo is not valid");
        }

        if (StringUtils.isBlank(request.getMdocCcn()) || checkConcurrencyControlTypeForErrors(request.getMdocCcn())) {
            errors.add("MdocCcn is not valid");
        }


        return errors;

    }

}
