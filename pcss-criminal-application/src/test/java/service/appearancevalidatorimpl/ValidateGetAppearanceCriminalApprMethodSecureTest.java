package service.appearancevalidatorimpl;

import ca.bc.gov.open.pcsscriminalapplication.service.impl.AppearanceValidatorImpl;
import ca.bc.gov.open.pcsscriminalcommon.utils.InstantUtils;
import ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalApprMethodSecureRequest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ValidateGetAppearanceCriminalApprMethodSecure Test")
public class ValidateGetAppearanceCriminalApprMethodSecureTest {

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

        GetAppearanceCriminalApprMethodSecureRequest getAppearanceCriminalApprMethodSecureRequest = new GetAppearanceCriminalApprMethodSecureRequest();
        getAppearanceCriminalApprMethodSecureRequest.setApplicationCd(VALUE);
        getAppearanceCriminalApprMethodSecureRequest.setAppearanceId(VALUE);
        getAppearanceCriminalApprMethodSecureRequest.setRequestDtm(InstantUtils.parse("2013-03-25 13:04:22.1"));
        getAppearanceCriminalApprMethodSecureRequest.setRequestAgencyIdentifierId(VALUE);
        getAppearanceCriminalApprMethodSecureRequest.setRequestPartId(VALUE);

        List<String> result = sut.validateGetAppearanceCriminalApprMethodSecure(getAppearanceCriminalApprMethodSecureRequest);

        Assertions.assertTrue(result.isEmpty());

    }

    @Test
    @DisplayName("Fail: all validations fail")
    public void failTestReturns() {

        GetAppearanceCriminalApprMethodSecureRequest getAppearanceCriminalApprMethodSecureRequest = new GetAppearanceCriminalApprMethodSecureRequest();
        getAppearanceCriminalApprMethodSecureRequest.setApplicationCd("TEST");
        getAppearanceCriminalApprMethodSecureRequest.setAppearanceId(LONG_STRING);
        getAppearanceCriminalApprMethodSecureRequest.setRequestDtm(InstantUtils.parse("2001-DEC-26"));
        getAppearanceCriminalApprMethodSecureRequest.setRequestAgencyIdentifierId(LONG_STRING);
        getAppearanceCriminalApprMethodSecureRequest.setRequestPartId(LONG_STRING);

        List<String> result = sut.validateGetAppearanceCriminalApprMethodSecure(getAppearanceCriminalApprMethodSecureRequest);

        Assertions.assertEquals(4, result.size());
        Assertions.assertEquals("RequestAgencyIdentifierId is not valid,RequestPartId is not valid,RequestDtm is not valid,AppearanceId is not valid", StringUtils.join(result, ","));

    }

}