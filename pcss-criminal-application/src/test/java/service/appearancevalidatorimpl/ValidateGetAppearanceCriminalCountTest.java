package service.appearancevalidatorimpl;

import ca.bc.gov.open.pcsscriminalapplication.service.impl.AppearanceValidatorImpl;
import ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalCountRequest;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ValidateGetAppearanceCriminalCount Test")
public class ValidateGetAppearanceCriminalCountTest {

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

        GetAppearanceCriminalCountRequest getAppearanceCriminalCountRequest =
                new GetAppearanceCriminalCountRequest();
        getAppearanceCriminalCountRequest.setAppearanceId(VALUE);
        getAppearanceCriminalCountRequest.setRequestDtm("2013-03-25 13:04:22.1");
        getAppearanceCriminalCountRequest.setRequestAgencyIdentifierId(VALUE);
        getAppearanceCriminalCountRequest.setRequestPartId(VALUE);

        List<String> result =
                sut.validateGetAppearanceCriminalCount(getAppearanceCriminalCountRequest);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Fail: all validations fail")
    public void failTestReturns() {

        GetAppearanceCriminalCountRequest getAppearanceCriminalCountRequest =
                new GetAppearanceCriminalCountRequest();
        getAppearanceCriminalCountRequest.setAppearanceId(LONG_STRING);
        getAppearanceCriminalCountRequest.setRequestDtm("2013-03-25");
        getAppearanceCriminalCountRequest.setRequestAgencyIdentifierId(LONG_STRING);
        getAppearanceCriminalCountRequest.setRequestPartId(LONG_STRING);

        List<String> result =
                sut.validateGetAppearanceCriminalCount(getAppearanceCriminalCountRequest);

        Assertions.assertEquals(4, result.size());
        Assertions.assertEquals(
                "RequestAgencyIdentifierId is not valid,RequestPartId is not valid,RequestDtm is not valid,AppearanceId is not valid",
                StringUtils.join(result, ","));
    }
}
