package service.filevalidationimpl;

import ca.bc.gov.open.pcsscriminalapplication.service.impl.FileValidatorImpl;
import ca.bc.gov.open.wsdl.pcss.one.GetClosedFileRequest;
import java.time.Instant;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ValidateGetClosedFile Test")
public class ValidateGetClosedFileTest {

    private static final String LONG_STRING = "VERYLONGSTRINGTOTESTTHELENGTHRESTRICTION";
    private static final String VALUE = "TEST";
    private static final Instant DATE = Instant.now();
    private static final Instant BAD_DATE = null;

    FileValidatorImpl sut;

    @BeforeAll
    public void BeforeAll() {

        sut = new FileValidatorImpl();
    }

    @Test
    @DisplayName("Success: null returns empty")
    public void nullTestReturnsEmpty() {

        List<String> result = sut.validateGetClosedFile(null);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Empty request is invalid", result.get(0));
    }

    @Test
    @DisplayName("Success: all validations succeed")
    public void successTestReturns() {

        GetClosedFileRequest getClosedFileRequest = new GetClosedFileRequest();

        getClosedFileRequest.setRequestDtm(DATE);
        getClosedFileRequest.setRequestAgencyIdentifierId(VALUE);
        getClosedFileRequest.setRequestPartId(VALUE);
        getClosedFileRequest.setFromApprDt(DATE);
        getClosedFileRequest.setToApprDt(DATE);
        getClosedFileRequest.setAgencyId(VALUE);

        List<String> result = sut.validateGetClosedFile(getClosedFileRequest);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Fail: all validations fail")
    public void failTestReturns() {

        GetClosedFileRequest getClosedFileRequest = new GetClosedFileRequest();

        getClosedFileRequest.setRequestDtm(BAD_DATE);
        getClosedFileRequest.setRequestAgencyIdentifierId(LONG_STRING);
        getClosedFileRequest.setRequestPartId(LONG_STRING);
        getClosedFileRequest.setFromApprDt(BAD_DATE);
        getClosedFileRequest.setToApprDt(BAD_DATE);
        getClosedFileRequest.setAgencyId(LONG_STRING);

        List<String> result = sut.validateGetClosedFile(getClosedFileRequest);

        Assertions.assertEquals(6, result.size());
        Assertions.assertEquals(
                "RequestAgencyIdentifierId is not valid,RequestPartId is not valid,RequestDtm is not valid,AgencyId is not valid,FromApprDt is not valid,ToApprDt is not valid",
                StringUtils.join(result, ","));
    }
}
