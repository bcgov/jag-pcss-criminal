package service.appearancevalidatorimpl;

import ca.bc.gov.open.pcsscriminalapplication.service.impl.AppearanceValidatorImpl;
import ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalRequest;
import ca.bc.gov.open.wsdl.pcss.three.YesNoType;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ValidateGetAppearanceCriminal Test")
public class ValidateGetAppearanceCriminalTest {

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

        GetAppearanceCriminalRequest getAppearanceCriminalRequest =
                new GetAppearanceCriminalRequest();
        getAppearanceCriminalRequest.setAppearanceId(VALUE);
        getAppearanceCriminalRequest.setFutureYN(YesNoType.Y);
        getAppearanceCriminalRequest.setHistoryYN(YesNoType.Y);
        getAppearanceCriminalRequest.setJustinNo(VALUE);
        getAppearanceCriminalRequest.setRequestDtm("2013-03-25 13:04:22.1");
        getAppearanceCriminalRequest.setRequestAgencyIdentifierId(VALUE);
        getAppearanceCriminalRequest.setRequestPartId(VALUE);

        List<String> result = sut.validateGetAppearanceCriminal(getAppearanceCriminalRequest);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Fail: all validations fail")
    public void failTestReturns() {

        GetAppearanceCriminalRequest getAppearanceCriminalRequest =
                new GetAppearanceCriminalRequest();
        getAppearanceCriminalRequest.setAppearanceId(LONG_STRING);
        getAppearanceCriminalRequest.setFutureYN(YesNoType.Y);
        getAppearanceCriminalRequest.setHistoryYN(YesNoType.Y);
        getAppearanceCriminalRequest.setJustinNo(LONG_STRING);
        getAppearanceCriminalRequest.setRequestDtm("2013-03-25");
        getAppearanceCriminalRequest.setRequestAgencyIdentifierId(LONG_STRING);
        getAppearanceCriminalRequest.setRequestPartId(LONG_STRING);

        List<String> result = sut.validateGetAppearanceCriminal(getAppearanceCriminalRequest);

        Assertions.assertEquals(5, result.size());
        Assertions.assertEquals(
                "RequestAgencyIdentifierId is not valid,RequestPartId is not valid,RequestDtm is not valid,AppearanceId is not valid,JustinNo is not valid",
                StringUtils.join(result, ","));
    }
}
