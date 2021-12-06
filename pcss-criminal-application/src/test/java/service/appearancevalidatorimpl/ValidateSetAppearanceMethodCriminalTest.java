package service.appearancevalidatorimpl;

import ca.bc.gov.open.pcsscriminalapplication.service.impl.AppearanceValidatorImpl;
import ca.bc.gov.open.pcsscriminalcommon.utils.InstantUtils;
import ca.bc.gov.open.wsdl.pcss.one.Detail3;
import ca.bc.gov.open.wsdl.pcss.one.SetAppearanceMethodCriminalRequest;
import ca.bc.gov.open.wsdl.pcss.three.OperationModeType;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ValidateSetAppearanceMethodCriminal Test")
public class ValidateSetAppearanceMethodCriminalTest {

    private static final String LONG_STRING = "VERYLONGSTRINGTOTESTTHELENGTHRESTRICTION";
    private static final String VALUE = "TEST";

    AppearanceValidatorImpl sut;

    @BeforeAll
    public void BeforeAll() {

        sut = new AppearanceValidatorImpl();

    }


    @Test
    @DisplayName("Success: all validations succeed")
    public void successTestReturns() {

        SetAppearanceMethodCriminalRequest setAppearanceMethodCriminalRequest = new SetAppearanceMethodCriminalRequest();

        Detail3 detail = new Detail3();
        detail.setAppearanceMethodCd(VALUE);
        detail.setApprId(VALUE);
        detail.setApprMethodCcn(VALUE);
        detail.setAssetUsageSeqNo(VALUE);
        detail.setInstructionTxt(VALUE);
        detail.setOperationModeCd(OperationModeType.F_ADD);
        detail.setPhoneNoTxt(VALUE);
        detail.setRoleCd(VALUE);

        setAppearanceMethodCriminalRequest.setDetail(Collections.singletonList(detail));
        setAppearanceMethodCriminalRequest.setRequestDtm("2013-03-25 13:04:22.1");
        setAppearanceMethodCriminalRequest.setRequestAgencyIdentifierId(VALUE);
        setAppearanceMethodCriminalRequest.setRequestPartId(VALUE);

        List<String> result = sut.validateSetAppearanceMethodCriminal(setAppearanceMethodCriminalRequest);

        Assertions.assertTrue(result.isEmpty());

    }

    @Test
    @DisplayName("Fail: all validations fail")
    public void failTestReturns() {

        SetAppearanceMethodCriminalRequest setAppearanceMethodCriminalRequest = new SetAppearanceMethodCriminalRequest();

        Detail3 detail = new Detail3();
        detail.setAppearanceMethodCd(LONG_STRING);
        detail.setApprId(LONG_STRING);
        detail.setApprMethodCcn(LONG_STRING);
        detail.setAssetUsageSeqNo(LONG_STRING);
        detail.setInstructionTxt(LONG_STRING);
        detail.setPhoneNoTxt(LONG_STRING);
        detail.setRoleCd(LONG_STRING);

        setAppearanceMethodCriminalRequest.setDetail(Collections.singletonList(detail));
        setAppearanceMethodCriminalRequest.setRequestDtm("2013-03-25");
        setAppearanceMethodCriminalRequest.setRequestAgencyIdentifierId(LONG_STRING);
        setAppearanceMethodCriminalRequest.setRequestPartId(LONG_STRING);

        List<String> result = sut.validateSetAppearanceMethodCriminal(setAppearanceMethodCriminalRequest);

        Assertions.assertEquals(9, result.size());
        Assertions.assertEquals("RequestAgencyIdentifieId is not valid,RequestPartId is not valid,RequestDtm is not valid,Details PcssAppearanceId at index 1 is not valid,Details OperationModeCd at index 1 is not valid,Details PcssAppearanceId at index 1 is not valid,Details JustinNo at index 1 is not valid,Details PartId at index 1 is not valid,Details AppearanceReasonCd at index 1 is not valid", StringUtils.join(result, ","));

    }

}
