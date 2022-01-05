package service.appearancevalidatorimpl;

import ca.bc.gov.open.pcsscriminalapplication.service.impl.AppearanceValidatorImpl;
import ca.bc.gov.open.wsdl.pcss.one.Detail;
import ca.bc.gov.open.wsdl.pcss.one.SetAppearanceCriminalRequest;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ValidateSetAppearanceCriminal Test")
public class ValidateSetAppearanceCriminalTest {

    private static final String LONG_STRING = "VERYLONGSTRINGTOTESTTHELENGTHRESTRICTION";
    private static final String VALUE = "TEST";

    AppearanceValidatorImpl sut;

    @BeforeAll
    public void BeforeAll() {

        sut = new AppearanceValidatorImpl();
    }

    @Test
    @DisplayName("Success: null returns empty")
    public void nullTestReturnsEmpty() {

        List<String> result = sut.validateSetAppearanceCriminal(null);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Empty request is invalid", result.get(0));

    }

    @Test
    @DisplayName("Success: all validations succeed")
    public void successTestReturns() {

        SetAppearanceCriminalRequest setAppearanceCriminalRequest =
                new SetAppearanceCriminalRequest();
        Detail detail = new Detail();

        detail.setAppearanceDt("2013-03-25 13:04:22.1");
        detail.setAppearanceReasonCd("AAA");
        detail.setAppearanceTm("2013-03-25 13:04:22.1");
        detail.setCourtAgencyId(VALUE);
        detail.setCourtRoomCd(VALUE);
        detail.setEstimatedTimeHour(VALUE);
        detail.setEstimatedTimeMin(VALUE);
        detail.setJustinNo(VALUE);
        detail.setPartId(VALUE);
        detail.setOutOfTownJudgeTxt(VALUE);
        detail.setPcssAppearanceId(VALUE);
        detail.setProfSeqNo(VALUE);
        detail.setSecurityRestrictionTxt(VALUE);
        detail.setSupplementalEquipmentTxt(VALUE);

        setAppearanceCriminalRequest.setDetail(Collections.singletonList(detail));
        setAppearanceCriminalRequest.setRequestDtm("2013-03-25 13:04:22.1");
        setAppearanceCriminalRequest.setRequestAgencyIdentifierId(VALUE);
        setAppearanceCriminalRequest.setRequestPartId(VALUE);

        List<String> result = sut.validateSetAppearanceCriminal(setAppearanceCriminalRequest);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Fail: all validations fail")
    public void failTestReturns() {

        SetAppearanceCriminalRequest setAppearanceCriminalRequest =
                new SetAppearanceCriminalRequest();
        Detail detail = new Detail();

        detail.setAppearanceDt("2013-03-25");
        detail.setAppearanceReasonCd(LONG_STRING);
        detail.setAppearanceTm(LONG_STRING);
        detail.setCourtAgencyId(LONG_STRING);
        detail.setCourtRoomCd(LONG_STRING);
        detail.setEstimatedTimeHour(LONG_STRING);
        detail.setEstimatedTimeMin(LONG_STRING);
        detail.setJustinNo(LONG_STRING);
        detail.setPartId(LONG_STRING);
        detail.setOutOfTownJudgeTxt(LONG_STRING);
        detail.setPcssAppearanceId(LONG_STRING);
        detail.setProfSeqNo(LONG_STRING);
        detail.setSecurityRestrictionTxt(LONG_STRING);
        detail.setSupplementalEquipmentTxt(LONG_STRING);

        setAppearanceCriminalRequest.setDetail(Collections.singletonList(detail));
        setAppearanceCriminalRequest.setRequestDtm("2013-03-25");
        setAppearanceCriminalRequest.setRequestAgencyIdentifierId(LONG_STRING);
        setAppearanceCriminalRequest.setRequestPartId(LONG_STRING);

        List<String> result = sut.validateSetAppearanceCriminal(setAppearanceCriminalRequest);

        Assertions.assertEquals(12, result.size());
        Assertions.assertEquals(
                "RequestAgencyIdentifierId is not valid,RequestPartId is not valid,RequestDtm is not valid,Details PcssAppearanceId at index 1 is not valid,Details JustinNo at index 1 is not valid,Details PartId at index 1 is not valid,Details ProfSeqNo at index 1 is not valid,Details AppearanceDt at index 1 is not valid,Details AppearanceTm at index 1 is not valid,Details AppearanceReasonCd at index 1 is not valid,Details CourtAgencyId at index 1 is not valid,Details CourtRoomCd at index 1 is not valid",
                StringUtils.join(result, ","));
    }
}
