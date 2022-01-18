package service.crownvalidatorimpl;

import ca.bc.gov.open.pcsscriminalapplication.service.impl.CrownValidatorImpl;
import ca.bc.gov.open.wsdl.pcss.one.CrownAssignment2;
import ca.bc.gov.open.wsdl.pcss.one.SetCrownAssignmentRequest;
import ca.bc.gov.open.wsdl.pcss.three.OperationModeType;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ValidateSetCrownAssignment Test")
public class ValidateSetCrownAssignmentTest {

    private static final String LONG_STRING = "000000000000000000000000000000000000";
    private static final String VALUE = "0000";
    private static final String DATE = "2013-03-25 13:04:22.1";
    private static final String BAD_DATE = "2020-SEP-05";

    private CrownValidatorImpl sut;

    @BeforeAll
    public void BeforeAll() {

        sut = new CrownValidatorImpl();
    }

    @Test
    @DisplayName("Success: all validation succeed")
    public void successTestReturns() {

        SetCrownAssignmentRequest getCrownAssignmentRequest = new SetCrownAssignmentRequest();
        getCrownAssignmentRequest.setRequestAgencyIdentifierId(VALUE);
        getCrownAssignmentRequest.setRequestPartId(VALUE);
        getCrownAssignmentRequest.setRequestDtm(DATE);
        getCrownAssignmentRequest.setJustinNo(VALUE);

        CrownAssignment2 crownAssignment = new CrownAssignment2();
        crownAssignment.setAssigningPaasAgencyId(VALUE);
        crownAssignment.setAssigningPaasPartId(VALUE);
        crownAssignment.setAssigningPaasSeqNo(VALUE);
        crownAssignment.setAssignmentCd(VALUE);
        crownAssignment.setAssignmentDt(DATE);
        crownAssignment.setAssignmentEndDt(DATE);
        crownAssignment.setOperationModeCd(OperationModeType.F_ADD);
        crownAssignment.setPaasAgencyId(VALUE);
        crownAssignment.setPaasPartId(VALUE);
        crownAssignment.setPaasSeqNo(VALUE);
        crownAssignment.setWorkAssignmentCcn(VALUE);
        crownAssignment.setWorkAssignmentId(VALUE);
        getCrownAssignmentRequest.setCrownAssignment(Collections.singletonList(crownAssignment));

        List<String> validationErrors = sut.validateSetCrownAssignment(getCrownAssignmentRequest);

        Assertions.assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Fail: all validation succeed")
    public void failEmptyArrayTestReturns() {

        SetCrownAssignmentRequest getCrownAssignmentRequest = new SetCrownAssignmentRequest();
        getCrownAssignmentRequest.setRequestAgencyIdentifierId(VALUE);
        getCrownAssignmentRequest.setRequestPartId(VALUE);
        getCrownAssignmentRequest.setRequestDtm(DATE);
        getCrownAssignmentRequest.setJustinNo(VALUE);

        List<String> validationErrors = sut.validateSetCrownAssignment(getCrownAssignmentRequest);

        Assertions.assertEquals(1, validationErrors.size());
        Assertions.assertEquals(
                "CrownAssignment is not valid", StringUtils.join(validationErrors, ","));
    }

    @Test
    @DisplayName("Fail: all validation fail")
    public void failsTestReturns() {

        SetCrownAssignmentRequest getCrownAssignmentRequest = new SetCrownAssignmentRequest();
        getCrownAssignmentRequest.setRequestAgencyIdentifierId(LONG_STRING);
        getCrownAssignmentRequest.setRequestPartId(LONG_STRING);
        getCrownAssignmentRequest.setRequestDtm(BAD_DATE);
        getCrownAssignmentRequest.setJustinNo(LONG_STRING);

        CrownAssignment2 crownAssignment = new CrownAssignment2();
        crownAssignment.setAssigningPaasAgencyId(LONG_STRING);
        crownAssignment.setAssigningPaasPartId(LONG_STRING);
        crownAssignment.setAssigningPaasSeqNo(LONG_STRING);
        crownAssignment.setAssignmentCd(null);
        crownAssignment.setAssignmentDt(BAD_DATE);
        crownAssignment.setAssignmentEndDt(BAD_DATE);
        crownAssignment.setOperationModeCd(null);
        crownAssignment.setPaasAgencyId(LONG_STRING);
        crownAssignment.setPaasPartId(LONG_STRING);
        crownAssignment.setPaasSeqNo(LONG_STRING);
        crownAssignment.setWorkAssignmentCcn(LONG_STRING);
        crownAssignment.setWorkAssignmentId(LONG_STRING);
        getCrownAssignmentRequest.setCrownAssignment(Collections.singletonList(crownAssignment));

        List<String> validationErrors = sut.validateSetCrownAssignment(getCrownAssignmentRequest);

        Assertions.assertEquals(15, validationErrors.size());
        Assertions.assertEquals(
                "RequestAgencyIdentifierId is not valid,RequestPartId is not valid,RequestDtm is not valid,JustinNo is not valid,CrownAssignment AssigningPaasAgencyId at index 1 is not valid,CrownAssignment AssigningPaasPartId at index 1 is not valid,CrownAssignment AssigningPaasSeqNo at index 1 is not valid,CrownAssignment AssignmentDt at index 1 is not valid,CrownAssignment AssignmentEndDt at index 1 is not valid,CrownAssignment OperationModeCd at index 1 is not valid,CrownAssignment PaasAgencyId at index 1 is not valid,CrownAssignment PaasPartId at index 1 is not valid,CrownAssignment PaasSeqNo at index 1 is not valid,CrownAssignment WorkAssignmentCcn at index 1 is not valid,CrownAssignment WorkAssignmentId at index 1 is not valid",
                StringUtils.join(validationErrors, ","));
    }
}
