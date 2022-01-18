package service.syncvalidatorimpl;

import ca.bc.gov.open.pcsscriminalapplication.service.impl.SyncValidatorImpl;
import java.util.List;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("validateGetSyncCriminalAppearance Test")
public class validateGetSyncCriminalAppearanceTest {

    SyncValidatorImpl sut;

    @BeforeAll
    public void beforeAll() {

        sut = new SyncValidatorImpl();
    }

    @Test
    @DisplayName("Success: all validations succeed")
    public void successTestReturns() {

        ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceRequest
                getSyncCriminalAppearanceRequest =
                        new ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceRequest();
        getSyncCriminalAppearanceRequest.setProcessUpToDtm("2013-03-25 13:04:22.1");
        getSyncCriminalAppearanceRequest.setRequestDtm("2013-03-25 13:04:22.1");
        getSyncCriminalAppearanceRequest.setRequestAgencyIdentifierId("0");
        getSyncCriminalAppearanceRequest.setRequestPartId("0");

        List<String> result =
                sut.validateGetSyncCriminalAppearance(getSyncCriminalAppearanceRequest);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Fail: agency failed")
    public void successAgencyFailedReturns() {

        ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceRequest
                getSyncCriminalAppearanceRequest =
                        new ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceRequest();
        getSyncCriminalAppearanceRequest.setProcessUpToDtm("2013-03-25 13:04:22.1");
        getSyncCriminalAppearanceRequest.setRequestDtm("2013-03-25 13:04:22.1");
        getSyncCriminalAppearanceRequest.setRequestAgencyIdentifierId("000000000000000000");
        getSyncCriminalAppearanceRequest.setRequestPartId("0");

        List<String> result =
                sut.validateGetSyncCriminalAppearance(getSyncCriminalAppearanceRequest);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("RequestAgencyIdentifierId is not valid", result.get(0));
    }

    @Test
    @DisplayName("Fail: part id failed ")
    public void successPartIdFailedReturns() {

        ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceRequest
                getSyncCriminalAppearanceRequest =
                        new ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceRequest();
        getSyncCriminalAppearanceRequest.setProcessUpToDtm("2013-03-25 13:04:22.1");
        getSyncCriminalAppearanceRequest.setRequestDtm("2013-03-25 13:04:22.1");
        getSyncCriminalAppearanceRequest.setRequestAgencyIdentifierId("0");
        getSyncCriminalAppearanceRequest.setRequestPartId("000000000000000000");

        List<String> result =
                sut.validateGetSyncCriminalAppearance(getSyncCriminalAppearanceRequest);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("RequestPartId is not valid", result.get(0));
    }

    @Test
    @DisplayName("Fail: request dtm failed ")
    public void successRequestDtmFailedReturns() {

        ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceRequest
                getSyncCriminalAppearanceRequest =
                        new ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceRequest();
        getSyncCriminalAppearanceRequest.setProcessUpToDtm("2013-03-25 13:04:22.1");
        getSyncCriminalAppearanceRequest.setRequestDtm("2013-03-25");
        getSyncCriminalAppearanceRequest.setRequestAgencyIdentifierId("0");
        getSyncCriminalAppearanceRequest.setRequestPartId("0");

        List<String> result =
                sut.validateGetSyncCriminalAppearance(getSyncCriminalAppearanceRequest);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("RequestDtm is not valid", result.get(0));
    }

    @Test
    @DisplayName("Fail: process dtm failed ")
    public void successProcessDtmFailedReturns() {

        ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceRequest
                getSyncCriminalAppearanceRequest =
                        new ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceRequest();
        getSyncCriminalAppearanceRequest.setProcessUpToDtm("2013-03-25");
        getSyncCriminalAppearanceRequest.setRequestDtm("2013-03-25 13:04:22.1");
        getSyncCriminalAppearanceRequest.setRequestAgencyIdentifierId("0");
        getSyncCriminalAppearanceRequest.setRequestPartId("0");

        List<String> result =
                sut.validateGetSyncCriminalAppearance(getSyncCriminalAppearanceRequest);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("ProcessUpToDtm is not valid", result.get(0));
    }

    @Test
    @DisplayName("Fail: object null failed ")
    public void successObjectFailedReturns() {

        List<String> result = sut.validateGetSyncCriminalAppearance(null);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Empty request is invalid", result.get(0));
    }
}
