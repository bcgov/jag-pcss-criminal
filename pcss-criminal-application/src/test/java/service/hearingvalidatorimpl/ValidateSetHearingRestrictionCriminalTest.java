package service.hearingvalidatorimpl;
import ca.bc.gov.open.pcsscriminalcommon.utils.InstantUtils;
import ca.bc.gov.open.wsdl.pcss.three.HearingRestrictionType;
import java.util.List;

import ca.bc.gov.open.wsdl.pcss.three.OperationModeType;

import ca.bc.gov.open.pcsscriminalapplication.service.impl.HearingValidatorImpl;
import ca.bc.gov.open.wsdl.pcss.one.SetHearingRestrictionCriminalRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ValidateSetHearingRestrictionCriminal Test")
public class ValidateSetHearingRestrictionCriminalTest {

    HearingValidatorImpl sut;

    @BeforeAll
    public void BeforeAll() {

        sut = new HearingValidatorImpl();

    }

    @Test
    @DisplayName("Success: all validations succeed")
    public void successTestReturns() {

        SetHearingRestrictionCriminalRequest setHearingRestrictionCriminalRequest = new SetHearingRestrictionCriminalRequest();
        setHearingRestrictionCriminalRequest.setRequestAgencyIdentifierId("0000");
        setHearingRestrictionCriminalRequest.setRequestPartId("0000");
        setHearingRestrictionCriminalRequest.setRequestDtm(InstantUtils.parse("2013-03-25 13:04:22.1"));
        setHearingRestrictionCriminalRequest.setOperationModeCd(OperationModeType.ADD);
        setHearingRestrictionCriminalRequest.setHearingRestrictionId("000");
        setHearingRestrictionCriminalRequest.setAdjudicatorPartId("000");
        setHearingRestrictionCriminalRequest.setHearingRestrictionCd(HearingRestrictionType.S);
        setHearingRestrictionCriminalRequest.setJustinNo("000");
        setHearingRestrictionCriminalRequest.setPartId("000");
        setHearingRestrictionCriminalRequest.setProfSeqNo("000");
        setHearingRestrictionCriminalRequest.setHearingRestrictionCcn("000");

        List<String> validationErrors = sut.validateSetHearingRestrictionCriminal(setHearingRestrictionCriminalRequest);

        Assertions.assertTrue(validationErrors.isEmpty());

    }

    @Test
    @DisplayName("Fail: agency identifier id failed")
    public void failAgencyIdentifierIdFailedReturns() {

        SetHearingRestrictionCriminalRequest setHearingRestrictionCriminalRequest = new SetHearingRestrictionCriminalRequest();
        setHearingRestrictionCriminalRequest.setRequestAgencyIdentifierId("000000000000000000000000");
        setHearingRestrictionCriminalRequest.setRequestPartId("0000");
        setHearingRestrictionCriminalRequest.setRequestDtm(InstantUtils.parse("2013-03-25 13:04:22.1"));
        setHearingRestrictionCriminalRequest.setOperationModeCd(OperationModeType.ADD);
        setHearingRestrictionCriminalRequest.setHearingRestrictionId("000");
        setHearingRestrictionCriminalRequest.setAdjudicatorPartId("000");
        setHearingRestrictionCriminalRequest.setHearingRestrictionCd(HearingRestrictionType.S);
        setHearingRestrictionCriminalRequest.setJustinNo("000");
        setHearingRestrictionCriminalRequest.setPartId("000");
        setHearingRestrictionCriminalRequest.setProfSeqNo("000");
        setHearingRestrictionCriminalRequest.setHearingRestrictionCcn("000");

        List<String> validationErrors = sut.validateSetHearingRestrictionCriminal(setHearingRestrictionCriminalRequest);

        Assertions.assertEquals(1, validationErrors.size());
        Assertions.assertEquals("RequestAgencyIdentifierId is not valid", validationErrors.get(0));

    }

    @Test
    @DisplayName("Fail: request part id failed")
    public void failRequestPartIdFailedReturns() {

        SetHearingRestrictionCriminalRequest setHearingRestrictionCriminalRequest = new SetHearingRestrictionCriminalRequest();
        setHearingRestrictionCriminalRequest.setRequestAgencyIdentifierId("0000");
        setHearingRestrictionCriminalRequest.setRequestPartId("0000000000000000000");
        setHearingRestrictionCriminalRequest.setRequestDtm(InstantUtils.parse("2013-03-25 13:04:22.1"));
        setHearingRestrictionCriminalRequest.setOperationModeCd(OperationModeType.ADD);
        setHearingRestrictionCriminalRequest.setHearingRestrictionId("000");
        setHearingRestrictionCriminalRequest.setAdjudicatorPartId("000");
        setHearingRestrictionCriminalRequest.setHearingRestrictionCd(HearingRestrictionType.S);
        setHearingRestrictionCriminalRequest.setJustinNo("000");
        setHearingRestrictionCriminalRequest.setPartId("000");
        setHearingRestrictionCriminalRequest.setProfSeqNo("000");
        setHearingRestrictionCriminalRequest.setHearingRestrictionCcn("000");

        List<String> validationErrors = sut.validateSetHearingRestrictionCriminal(setHearingRestrictionCriminalRequest);

        Assertions.assertEquals(1, validationErrors.size());
        Assertions.assertEquals("RequestPartId is not valid", validationErrors.get(0));

    }

    @Test
    @DisplayName("Fail: request dtm failed")
    public void failRequestDtmFailedReturns() {

        SetHearingRestrictionCriminalRequest setHearingRestrictionCriminalRequest = new SetHearingRestrictionCriminalRequest();
        setHearingRestrictionCriminalRequest.setRequestAgencyIdentifierId("0000");
        setHearingRestrictionCriminalRequest.setRequestPartId("0000");
        setHearingRestrictionCriminalRequest.setRequestDtm(InstantUtils.parse("2001-DEC-26"));
        setHearingRestrictionCriminalRequest.setOperationModeCd(OperationModeType.ADD);
        setHearingRestrictionCriminalRequest.setHearingRestrictionId("000");
        setHearingRestrictionCriminalRequest.setAdjudicatorPartId("000");
        setHearingRestrictionCriminalRequest.setHearingRestrictionCd(HearingRestrictionType.S);
        setHearingRestrictionCriminalRequest.setJustinNo("000");
        setHearingRestrictionCriminalRequest.setPartId("000");
        setHearingRestrictionCriminalRequest.setProfSeqNo("000");
        setHearingRestrictionCriminalRequest.setHearingRestrictionCcn("000");

        List<String> validationErrors = sut.validateSetHearingRestrictionCriminal(setHearingRestrictionCriminalRequest);

        Assertions.assertEquals(1, validationErrors.size());
        Assertions.assertEquals("RequestDtm is not valid", validationErrors.get(0));

    }

    @Test
    @DisplayName("Fail: hearing restriction id failed")
    public void failHearingRestrictionIdFailedReturns() {

        SetHearingRestrictionCriminalRequest setHearingRestrictionCriminalRequest = new SetHearingRestrictionCriminalRequest();
        setHearingRestrictionCriminalRequest.setRequestAgencyIdentifierId("0000");
        setHearingRestrictionCriminalRequest.setRequestPartId("0000");
        setHearingRestrictionCriminalRequest.setRequestDtm(InstantUtils.parse("2013-03-25 13:04:22.1"));
        setHearingRestrictionCriminalRequest.setOperationModeCd(OperationModeType.ADD);
        setHearingRestrictionCriminalRequest.setHearingRestrictionId("00000000000000000");
        setHearingRestrictionCriminalRequest.setAdjudicatorPartId("000");
        setHearingRestrictionCriminalRequest.setHearingRestrictionCd(HearingRestrictionType.S);
        setHearingRestrictionCriminalRequest.setJustinNo("000");
        setHearingRestrictionCriminalRequest.setPartId("000");
        setHearingRestrictionCriminalRequest.setProfSeqNo("000");
        setHearingRestrictionCriminalRequest.setHearingRestrictionCcn("000");

        List<String> validationErrors = sut.validateSetHearingRestrictionCriminal(setHearingRestrictionCriminalRequest);

        Assertions.assertEquals(1, validationErrors.size());
        Assertions.assertEquals("HearingRestrictionId is not valid", validationErrors.get(0));

    }

    @Test
    @DisplayName("Fail: adjudicator part id failed")
    public void failAdjudicatorPartIdFailedReturns() {

        SetHearingRestrictionCriminalRequest setHearingRestrictionCriminalRequest = new SetHearingRestrictionCriminalRequest();
        setHearingRestrictionCriminalRequest.setRequestAgencyIdentifierId("0000");
        setHearingRestrictionCriminalRequest.setRequestPartId("0000");
        setHearingRestrictionCriminalRequest.setRequestDtm(InstantUtils.parse("2013-03-25 13:04:22.1"));
        setHearingRestrictionCriminalRequest.setOperationModeCd(OperationModeType.ADD);
        setHearingRestrictionCriminalRequest.setHearingRestrictionId("000");
        setHearingRestrictionCriminalRequest.setAdjudicatorPartId("000000000000000000");
        setHearingRestrictionCriminalRequest.setHearingRestrictionCd(HearingRestrictionType.S);
        setHearingRestrictionCriminalRequest.setJustinNo("000");
        setHearingRestrictionCriminalRequest.setPartId("000");
        setHearingRestrictionCriminalRequest.setProfSeqNo("000");
        setHearingRestrictionCriminalRequest.setHearingRestrictionCcn("000");

        List<String> validationErrors = sut.validateSetHearingRestrictionCriminal(setHearingRestrictionCriminalRequest);

        Assertions.assertEquals(1, validationErrors.size());
        Assertions.assertEquals("AdjudicatorPartId is not valid", validationErrors.get(0));
    }

    @Test
    @DisplayName("Fail: justin no failed")
    public void failJustinNoFailedReturns() {

        SetHearingRestrictionCriminalRequest setHearingRestrictionCriminalRequest = new SetHearingRestrictionCriminalRequest();
        setHearingRestrictionCriminalRequest.setRequestAgencyIdentifierId("0000");
        setHearingRestrictionCriminalRequest.setRequestPartId("0000");
        setHearingRestrictionCriminalRequest.setRequestDtm(InstantUtils.parse("2013-03-25 13:04:22.1"));
        setHearingRestrictionCriminalRequest.setOperationModeCd(OperationModeType.ADD);
        setHearingRestrictionCriminalRequest.setHearingRestrictionId("000");
        setHearingRestrictionCriminalRequest.setAdjudicatorPartId("000");
        setHearingRestrictionCriminalRequest.setHearingRestrictionCd(HearingRestrictionType.S);
        setHearingRestrictionCriminalRequest.setJustinNo("00000000000000000");
        setHearingRestrictionCriminalRequest.setPartId("000");
        setHearingRestrictionCriminalRequest.setProfSeqNo("000");
        setHearingRestrictionCriminalRequest.setHearingRestrictionCcn("000");

        List<String> validationErrors = sut.validateSetHearingRestrictionCriminal(setHearingRestrictionCriminalRequest);

        Assertions.assertEquals(1, validationErrors.size());
        Assertions.assertEquals("JustinNo is not valid", validationErrors.get(0));
    }

    @Test
    @DisplayName("Fail: part id failed")
    public void failPartIdFailedReturns() {

        SetHearingRestrictionCriminalRequest setHearingRestrictionCriminalRequest = new SetHearingRestrictionCriminalRequest();
        setHearingRestrictionCriminalRequest.setRequestAgencyIdentifierId("0000");
        setHearingRestrictionCriminalRequest.setRequestPartId("0000");
        setHearingRestrictionCriminalRequest.setRequestDtm(InstantUtils.parse("2013-03-25 13:04:22.1"));
        setHearingRestrictionCriminalRequest.setOperationModeCd(OperationModeType.ADD);
        setHearingRestrictionCriminalRequest.setHearingRestrictionId("000");
        setHearingRestrictionCriminalRequest.setAdjudicatorPartId("000");
        setHearingRestrictionCriminalRequest.setHearingRestrictionCd(HearingRestrictionType.S);
        setHearingRestrictionCriminalRequest.setJustinNo("000");
        setHearingRestrictionCriminalRequest.setPartId("00000000000000000000000000000");
        setHearingRestrictionCriminalRequest.setProfSeqNo("000");
        setHearingRestrictionCriminalRequest.setHearingRestrictionCcn("000");

        List<String> validationErrors = sut.validateSetHearingRestrictionCriminal(setHearingRestrictionCriminalRequest);

        Assertions.assertEquals(1, validationErrors.size());
        Assertions.assertEquals("PartId is not valid", validationErrors.get(0));
    }

    @Test
    @DisplayName("Fail: prof seq no failed")
    public void failProfSeqNoFailedReturns() {

        SetHearingRestrictionCriminalRequest setHearingRestrictionCriminalRequest = new SetHearingRestrictionCriminalRequest();
        setHearingRestrictionCriminalRequest.setRequestAgencyIdentifierId("0000");
        setHearingRestrictionCriminalRequest.setRequestPartId("0000");
        setHearingRestrictionCriminalRequest.setRequestDtm(InstantUtils.parse("2013-03-25 13:04:22.1"));
        setHearingRestrictionCriminalRequest.setOperationModeCd(OperationModeType.ADD);
        setHearingRestrictionCriminalRequest.setHearingRestrictionId("000");
        setHearingRestrictionCriminalRequest.setAdjudicatorPartId("000");
        setHearingRestrictionCriminalRequest.setHearingRestrictionCd(HearingRestrictionType.S);
        setHearingRestrictionCriminalRequest.setJustinNo("000");
        setHearingRestrictionCriminalRequest.setPartId("000");
        setHearingRestrictionCriminalRequest.setProfSeqNo("00000000000000000000");
        setHearingRestrictionCriminalRequest.setHearingRestrictionCcn("000");

        List<String> validationErrors = sut.validateSetHearingRestrictionCriminal(setHearingRestrictionCriminalRequest);

        Assertions.assertEquals(1, validationErrors.size());
        Assertions.assertEquals("ProfSeqNo is not valid", validationErrors.get(0));
    }

    @Test
    @DisplayName("Fail: hearing restriction ccn failed")
    public void failHearingRestrictionCcnFailedReturns() {

        SetHearingRestrictionCriminalRequest setHearingRestrictionCriminalRequest = new SetHearingRestrictionCriminalRequest();
        setHearingRestrictionCriminalRequest.setRequestAgencyIdentifierId("0000");
        setHearingRestrictionCriminalRequest.setRequestPartId("0000");
        setHearingRestrictionCriminalRequest.setRequestDtm(InstantUtils.parse("2013-03-25 13:04:22.1"));
        setHearingRestrictionCriminalRequest.setOperationModeCd(OperationModeType.ADD);
        setHearingRestrictionCriminalRequest.setHearingRestrictionId("000");
        setHearingRestrictionCriminalRequest.setAdjudicatorPartId("000");
        setHearingRestrictionCriminalRequest.setHearingRestrictionCd(HearingRestrictionType.S);
        setHearingRestrictionCriminalRequest.setJustinNo("000");
        setHearingRestrictionCriminalRequest.setPartId("000");
        setHearingRestrictionCriminalRequest.setProfSeqNo("000");
        setHearingRestrictionCriminalRequest.setHearingRestrictionCcn("000000000000000");

        List<String> validationErrors = sut.validateSetHearingRestrictionCriminal(setHearingRestrictionCriminalRequest);

        Assertions.assertEquals(1, validationErrors.size());
        Assertions.assertEquals("HearingRestrictionCcn is not valid", validationErrors.get(0));
    }

}
