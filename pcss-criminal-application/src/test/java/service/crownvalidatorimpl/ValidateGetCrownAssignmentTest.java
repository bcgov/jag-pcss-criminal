package service.crownvalidatorimpl;
import java.util.List;

import ca.bc.gov.open.pcsscriminalapplication.service.impl.CrownValidatorImpl;
import ca.bc.gov.open.pcsscriminalcommon.utils.InstantUtils;
import ca.bc.gov.open.wsdl.pcss.one.GetCrownAssignmentRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ValidateGetCrownAssignment Test")
public class ValidateGetCrownAssignmentTest {

    private CrownValidatorImpl sut;

    @BeforeAll
    public void BeforeAll() {

        sut = new CrownValidatorImpl();

    }

    @Test
    @DisplayName("Success: all validation succeed")
    public void successTestReturns() {

        GetCrownAssignmentRequest getCrownAssignmentRequest = new GetCrownAssignmentRequest();
        getCrownAssignmentRequest.setRequestAgencyIdentifierId("0000");
        getCrownAssignmentRequest.setRequestPartId("0000");
        getCrownAssignmentRequest.setRequestDtm(InstantUtils.parse("2013-03-25 13:04:22.1"));
        getCrownAssignmentRequest.setJustinNo("0000");
        getCrownAssignmentRequest.setSinceDt(InstantUtils.parse("2013-03-25 13:04:22.1"));

        List<String> validationErrors = sut.validateGetCrownAssignment(getCrownAssignmentRequest);

        Assertions.assertTrue(validationErrors.isEmpty());

    }

    @Test
    @DisplayName("Success: optional properties set to null")
    public void successNullOptionalPropertiesReturns() {

        GetCrownAssignmentRequest getCrownAssignmentRequest = new GetCrownAssignmentRequest();
        getCrownAssignmentRequest.setRequestAgencyIdentifierId("0000");
        getCrownAssignmentRequest.setRequestPartId("0000");
        getCrownAssignmentRequest.setRequestDtm(InstantUtils.parse("2013-03-25 13:04:22.1"));

        List<String> validationErrors = sut.validateGetCrownAssignment(getCrownAssignmentRequest);

        Assertions.assertTrue(validationErrors.isEmpty());

    }

    @Test
    @DisplayName("Fail: agency identifier id failed")
    public void failAgencyIdentifierIdFailedReturns() {
        GetCrownAssignmentRequest getCrownAssignmentRequest = new GetCrownAssignmentRequest();
        getCrownAssignmentRequest.setRequestAgencyIdentifierId("0000000000000000000");
        getCrownAssignmentRequest.setRequestPartId("0000");
        getCrownAssignmentRequest.setRequestDtm(InstantUtils.parse("2013-03-25 13:04:22.1"));
        getCrownAssignmentRequest.setJustinNo("0000");
        getCrownAssignmentRequest.setSinceDt(InstantUtils.parse("2013-03-25 13:04:22.1"));

        List<String> validationErrors = sut.validateGetCrownAssignment(getCrownAssignmentRequest);

        Assertions.assertEquals(1, validationErrors.size());
        Assertions.assertEquals("RequestAgencyIdentifierId is not valid", validationErrors.get(0));
    }

    @Test
    @DisplayName("Fail: part id failed")
    public void failPartIdFailedReturns() {
        GetCrownAssignmentRequest getCrownAssignmentRequest = new GetCrownAssignmentRequest();
        getCrownAssignmentRequest.setRequestAgencyIdentifierId("0000");
        getCrownAssignmentRequest.setRequestPartId("00000000000000000");
        getCrownAssignmentRequest.setRequestDtm(InstantUtils.parse("2013-03-25 13:04:22.1"));
        getCrownAssignmentRequest.setJustinNo("0000");
        getCrownAssignmentRequest.setSinceDt(InstantUtils.parse("2013-03-25 13:04:22.1"));

        List<String> validationErrors = sut.validateGetCrownAssignment(getCrownAssignmentRequest);

        Assertions.assertEquals(1, validationErrors.size());
        Assertions.assertEquals("RequestPartId is not valid", validationErrors.get(0));
    }

    @Test
    @DisplayName("Fail: request dtm failed")
    public void failRequestDtmFailedReturns() {
        GetCrownAssignmentRequest getCrownAssignmentRequest = new GetCrownAssignmentRequest();
        getCrownAssignmentRequest.setRequestAgencyIdentifierId("0000");
        getCrownAssignmentRequest.setRequestPartId("0000");
        getCrownAssignmentRequest.setRequestDtm(InstantUtils.parse("2020-SEP-5"));
        getCrownAssignmentRequest.setJustinNo("0000");
        getCrownAssignmentRequest.setSinceDt(InstantUtils.parse("2013-03-25 13:04:22.1"));

        List<String> validationErrors = sut.validateGetCrownAssignment(getCrownAssignmentRequest);

        Assertions.assertEquals(1, validationErrors.size());
        Assertions.assertEquals("RequestDtm is not valid", validationErrors.get(0));
    }

    @Test
    @DisplayName("Fail: justin no failed")
    public void failJustinNoFailedReturns() {
        GetCrownAssignmentRequest getCrownAssignmentRequest = new GetCrownAssignmentRequest();
        getCrownAssignmentRequest.setRequestAgencyIdentifierId("0000");
        getCrownAssignmentRequest.setRequestPartId("0000");
        getCrownAssignmentRequest.setRequestDtm(InstantUtils.parse("2013-03-25 13:04:22.1"));
        getCrownAssignmentRequest.setJustinNo("000000000000000000000000");
        getCrownAssignmentRequest.setSinceDt(InstantUtils.parse("2013-03-25 13:04:22.1"));

        List<String> validationErrors = sut.validateGetCrownAssignment(getCrownAssignmentRequest);

        Assertions.assertEquals(1, validationErrors.size());
        Assertions.assertEquals("JustinNo is not valid", validationErrors.get(0));
    }

    @Test
    @DisplayName("Fail: since dt failed")
    public void failSinceDtFailedReturns() {
        GetCrownAssignmentRequest getCrownAssignmentRequest = new GetCrownAssignmentRequest();
        getCrownAssignmentRequest.setRequestAgencyIdentifierId("0000");
        getCrownAssignmentRequest.setRequestPartId("0000");
        getCrownAssignmentRequest.setRequestDtm(InstantUtils.parse("2013-03-25 13:04:22.1"));
        getCrownAssignmentRequest.setJustinNo("0000");
        getCrownAssignmentRequest.setSinceDt(InstantUtils.parse("2020-SEP-5"));

        List<String> validationErrors = sut.validateGetCrownAssignment(getCrownAssignmentRequest);

        Assertions.assertEquals(1, validationErrors.size());
        Assertions.assertEquals("SinceDt is not valid", validationErrors.get(0));
    }
}
