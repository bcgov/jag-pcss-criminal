package service.appearancevalidatorimpl;

import ca.bc.gov.open.pcsscriminalapplication.service.impl.AppearanceValidatorImpl;
import ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalCountSecureRequest;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ValidateGetAppearanceCriminalCountSecure Test")
public class ValidateGetAppearanceCriminalCountSecureTest {

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

        List<String> result = sut.validateGetAppearanceCriminalCountSecure(null);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Empty request is invalid", result.get(0));
    }

    @Test
    @DisplayName("Success: all validations succeed")
    public void successTestReturns() {

        GetAppearanceCriminalCountSecureRequest getAppearanceCriminalCountSecureRequest =
                new GetAppearanceCriminalCountSecureRequest();
        getAppearanceCriminalCountSecureRequest.setAppearanceId(VALUE);
        getAppearanceCriminalCountSecureRequest.setRequestDtm("2013-03-25 13:04:22.1");
        getAppearanceCriminalCountSecureRequest.setRequestAgencyIdentifierId(VALUE);
        getAppearanceCriminalCountSecureRequest.setRequestPartId(VALUE);

        List<String> result =
                sut.validateGetAppearanceCriminalCountSecure(
                        getAppearanceCriminalCountSecureRequest);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Fail: all validations fail")
    public void failTestReturns() {

        GetAppearanceCriminalCountSecureRequest getAppearanceCriminalCountSecureRequest =
                new GetAppearanceCriminalCountSecureRequest();
        getAppearanceCriminalCountSecureRequest.setAppearanceId(LONG_STRING);
        getAppearanceCriminalCountSecureRequest.setRequestDtm("2001-DEC-26");
        getAppearanceCriminalCountSecureRequest.setRequestAgencyIdentifierId(LONG_STRING);
        getAppearanceCriminalCountSecureRequest.setRequestPartId(LONG_STRING);

        List<String> result =
                sut.validateGetAppearanceCriminalCountSecure(
                        getAppearanceCriminalCountSecureRequest);

        Assertions.assertEquals(4, result.size());
        Assertions.assertEquals(
                "RequestAgencyIdentifierId is not valid,RequestPartId is not valid,RequestDtm is not valid,AppearanceId is not valid",
                StringUtils.join(result, ","));
    }
}
