package service.appearancevalidatorimpl;

import ca.bc.gov.open.pcsscriminalapplication.service.impl.AppearanceValidatorImpl;
import ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalSecureRequest;
import ca.bc.gov.open.wsdl.pcss.secure.three.YesNoType;
import java.time.Instant;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ValidateGetAppearanceCriminalSecure Test")
public class ValidateGetAppearanceCriminalSecureTest {

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

        List<String> result = sut.validateGetAppearanceCriminalSecure(null);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Empty request is invalid", result.get(0));
    }

    @Test
    @DisplayName("Success: all validations succeed")
    public void successTestReturns() {

        GetAppearanceCriminalSecureRequest getAppearanceCriminalSecureRequest =
                new GetAppearanceCriminalSecureRequest();
        getAppearanceCriminalSecureRequest.setAppearanceId(VALUE);
        getAppearanceCriminalSecureRequest.setApplicationCd(VALUE);
        getAppearanceCriminalSecureRequest.setFutureYN(YesNoType.Y);
        getAppearanceCriminalSecureRequest.setHistoryYN(YesNoType.Y);
        getAppearanceCriminalSecureRequest.setJustinNo(VALUE);
        getAppearanceCriminalSecureRequest.setRequestDtm(Instant.now());
        getAppearanceCriminalSecureRequest.setRequestAgencyIdentifierId(VALUE);
        getAppearanceCriminalSecureRequest.setRequestPartId(VALUE);

        List<String> result =
                sut.validateGetAppearanceCriminalSecure(getAppearanceCriminalSecureRequest);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Fail: all validations fail")
    public void failTestReturns() {

        GetAppearanceCriminalSecureRequest getAppearanceCriminalSecureRequest =
                new GetAppearanceCriminalSecureRequest();
        getAppearanceCriminalSecureRequest.setAppearanceId(LONG_STRING);
        getAppearanceCriminalSecureRequest.setApplicationCd(VALUE);
        getAppearanceCriminalSecureRequest.setFutureYN(YesNoType.Y);
        getAppearanceCriminalSecureRequest.setHistoryYN(YesNoType.Y);
        getAppearanceCriminalSecureRequest.setJustinNo(LONG_STRING);
        getAppearanceCriminalSecureRequest.setRequestDtm(null);
        getAppearanceCriminalSecureRequest.setRequestAgencyIdentifierId(LONG_STRING);
        getAppearanceCriminalSecureRequest.setRequestPartId(LONG_STRING);

        List<String> result =
                sut.validateGetAppearanceCriminalSecure(getAppearanceCriminalSecureRequest);

        Assertions.assertEquals(5, result.size());
        Assertions.assertEquals(
                "RequestAgencyIdentifierId is not valid,RequestPartId is not valid,RequestDtm is not valid,AppearanceId is not valid,JustinNo is not valid",
                StringUtils.join(result, ","));
    }
}
