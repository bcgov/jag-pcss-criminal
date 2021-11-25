package service.appearancevalidatorimpl;

import ca.bc.gov.open.pcsscriminalapplication.service.impl.AppearanceValidatorImpl;
import ca.bc.gov.open.pcsscriminalcommon.utils.InstantUtils;
import ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResourceRequest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ValidateGetAppearanceCriminalResource Test")
public class ValidateGetAppearanceCriminalResourceTest {

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

        GetAppearanceCriminalResourceRequest getAppearanceCriminalResourceRequest = new GetAppearanceCriminalResourceRequest();
        getAppearanceCriminalResourceRequest.setAppearanceId(VALUE);
        getAppearanceCriminalResourceRequest.setRequestDtm("2013-03-25 13:04:22.1");
        getAppearanceCriminalResourceRequest.setRequestAgencyIdentifierId(VALUE);
        getAppearanceCriminalResourceRequest.setRequestPartId(VALUE);

        List<String> result = sut.validateGetAppearanceCriminalResource(getAppearanceCriminalResourceRequest);

        Assertions.assertTrue(result.isEmpty());

    }

    @Test
    @DisplayName("Fail: all validations fail")
    public void failTestReturns() {

        GetAppearanceCriminalResourceRequest getAppearanceCriminalResourceRequest = new GetAppearanceCriminalResourceRequest();
        getAppearanceCriminalResourceRequest.setAppearanceId(LONG_STRING);
        getAppearanceCriminalResourceRequest.setRequestDtm("2013-03-25");
        getAppearanceCriminalResourceRequest.setRequestAgencyIdentifierId(LONG_STRING);
        getAppearanceCriminalResourceRequest.setRequestPartId(LONG_STRING);

        List<String> result = sut.validateGetAppearanceCriminalResource(getAppearanceCriminalResourceRequest);

        Assertions.assertEquals(4, result.size());
        Assertions.assertEquals("RequestAgencyIdentifierId is not valid,RequestPartId is not valid,RequestDtm is not valid,AppearanceId is not valid", StringUtils.join(result, ","));

    }

}
