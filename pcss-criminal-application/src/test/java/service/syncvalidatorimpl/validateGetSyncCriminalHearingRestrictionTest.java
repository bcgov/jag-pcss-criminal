package service.syncvalidatorimpl;

import ca.bc.gov.open.pcsscriminalapplication.service.impl.SyncValidatorImpl;
import ca.bc.gov.open.pcsscriminalcommon.utils.InstantUtils;
import org.junit.jupiter.api.*;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("validateGetSyncCriminalHearingRestriction Test")
public class validateGetSyncCriminalHearingRestrictionTest {

    SyncValidatorImpl sut;

    @BeforeAll
    public void beforeAll() {

        sut = new SyncValidatorImpl();

    }

    @Test
    @DisplayName("Success: all validations succeed")
    public void successTestReturns() {

        ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionRequest getSyncCriminalHearingRestrictionRequest = new ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionRequest();
        getSyncCriminalHearingRestrictionRequest.setProcessUpToDtm(InstantUtils.parse("2013-03-25 13:04:22.1"));
        getSyncCriminalHearingRestrictionRequest.setRequestDtm(InstantUtils.parse("2013-03-25 13:04:22.1"));
        getSyncCriminalHearingRestrictionRequest.setRequestAgencyIdentifierId("0");
        getSyncCriminalHearingRestrictionRequest.setRequestPartId("0");

        List<String> result = sut.validateGetSyncCriminalHearingRestriction(getSyncCriminalHearingRestrictionRequest);

        Assertions.assertTrue(result.isEmpty());

    }

    @Test
    @DisplayName("Fail: agency failed")
    public void successAgencyFailedReturns() {

        ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionRequest getSyncCriminalHearingRestrictionRequest = new ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionRequest();
        getSyncCriminalHearingRestrictionRequest.setProcessUpToDtm(InstantUtils.parse("2013-03-25 13:04:22.1"));
        getSyncCriminalHearingRestrictionRequest.setRequestDtm(InstantUtils.parse("2013-03-25 13:04:22.1"));
        getSyncCriminalHearingRestrictionRequest.setRequestAgencyIdentifierId("000000000000000000");
        getSyncCriminalHearingRestrictionRequest.setRequestPartId("0");

        List<String> result = sut.validateGetSyncCriminalHearingRestriction(getSyncCriminalHearingRestrictionRequest);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("RequestAgencyIdentifierId is not valid", result.get(0));

    }

    @Test
    @DisplayName("Fail: part id failed ")
    public void successPartIdFailedReturns() {

        ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionRequest getSyncCriminalHearingRestrictionRequest = new ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionRequest();
        getSyncCriminalHearingRestrictionRequest.setProcessUpToDtm(InstantUtils.parse("2013-03-25 13:04:22.1"));
        getSyncCriminalHearingRestrictionRequest.setRequestDtm(InstantUtils.parse("2013-03-25 13:04:22.1"));
        getSyncCriminalHearingRestrictionRequest.setRequestAgencyIdentifierId("0");
        getSyncCriminalHearingRestrictionRequest.setRequestPartId("000000000000000000");

        List<String> result = sut.validateGetSyncCriminalHearingRestriction(getSyncCriminalHearingRestrictionRequest);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("RequestPartId is not valid", result.get(0));

    }

    @Test
    @DisplayName("Fail: request dtm failed ")
    public void successRequestDtmFailedReturns() {

        ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionRequest getSyncCriminalHearingRestrictionRequest = new ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionRequest();
        getSyncCriminalHearingRestrictionRequest.setProcessUpToDtm(InstantUtils.parse("2013-03-25 13:04:22.1"));
        getSyncCriminalHearingRestrictionRequest.setRequestDtm(InstantUtils.parse("2001-DEC-26"));
        getSyncCriminalHearingRestrictionRequest.setRequestAgencyIdentifierId("0");
        getSyncCriminalHearingRestrictionRequest.setRequestPartId("0");

        List<String> result = sut.validateGetSyncCriminalHearingRestriction(getSyncCriminalHearingRestrictionRequest);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("RequestDtm is not valid", result.get(0));

    }

    @Test
    @DisplayName("Fail: process dtm failed ")
    public void successProcessDtmFailedReturns() {

        ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionRequest getSyncCriminalHearingRestrictionRequest = new ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionRequest();
        getSyncCriminalHearingRestrictionRequest.setProcessUpToDtm(InstantUtils.parse("2001-DEC-26"));
        getSyncCriminalHearingRestrictionRequest.setRequestDtm(InstantUtils.parse("2013-03-25 13:04:22.1"));
        getSyncCriminalHearingRestrictionRequest.setRequestAgencyIdentifierId("0");
        getSyncCriminalHearingRestrictionRequest.setRequestPartId("0");

        List<String> result = sut.validateGetSyncCriminalHearingRestriction(getSyncCriminalHearingRestrictionRequest);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("ProcessUpToDtm is not valid", result.get(0));

    }

    @Test
    @DisplayName("Fail: object null failed ")
    public void successObjectFailedReturns() {

        List<String> result = sut.validateGetSyncCriminalHearingRestriction(null);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Empty request is invalid", result.get(0));

    }


}
