package service.filevalidationimpl;

import ca.bc.gov.open.pcsscriminalapplication.service.impl.FileValidatorImpl;
import ca.bc.gov.open.wsdl.pcss.secure.one.GetFileDetailCriminalRequest;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ValidateGetFileDetailCriminalSecure Test")
public class ValidateGetFileDetailCriminalSecureTest {

    private static final String LONG_STRING = "VERYLONGSTRINGTOTESTTHELENGTHRESTRICTION";
    private static final String VALUE = "TEST";
    private static final String DATE = "2013-03-25 13:04:22.1";
    private static final String BAD_DATE = "2001-DEC-26";

    FileValidatorImpl sut;

    @BeforeAll
    public void BeforeAll() {

        sut = new FileValidatorImpl();
    }

    @Test
    @DisplayName("Success: null returns empty")
    public void nullTestReturnsEmpty() {

        List<String> result = sut.validateGetFileDetailCriminalSecure(null);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Empty request is invalid", result.get(0));
    }

    @Test
    @DisplayName("Success: all validations succeed")
    public void successTestReturns() {

        GetFileDetailCriminalRequest getFileDetailCriminalRequest =
                new GetFileDetailCriminalRequest();

        getFileDetailCriminalRequest.setRequestDtm(DATE);
        getFileDetailCriminalRequest.setRequestAgencyIdentifierId(VALUE);
        getFileDetailCriminalRequest.setRequestPartId(VALUE);
        getFileDetailCriminalRequest.setApplicationCd(VALUE);
        getFileDetailCriminalRequest.setJustinNo(VALUE);

        List<String> result = sut.validateGetFileDetailCriminalSecure(getFileDetailCriminalRequest);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Fail: all validations fail")
    public void failTestReturns() {

        GetFileDetailCriminalRequest getFileDetailCriminalRequest =
                new GetFileDetailCriminalRequest();

        getFileDetailCriminalRequest.setRequestDtm(BAD_DATE);
        getFileDetailCriminalRequest.setRequestAgencyIdentifierId(LONG_STRING);
        getFileDetailCriminalRequest.setRequestPartId(LONG_STRING);
        getFileDetailCriminalRequest.setApplicationCd(LONG_STRING);
        getFileDetailCriminalRequest.setJustinNo(LONG_STRING);

        List<String> result = sut.validateGetFileDetailCriminalSecure(getFileDetailCriminalRequest);

        Assertions.assertEquals(4, result.size());
        Assertions.assertEquals(
                "RequestAgencyIdentifierId is not valid,RequestPartId is not valid,RequestDtm is not valid,JustinNo is not valid",
                StringUtils.join(result, ","));
    }
}
