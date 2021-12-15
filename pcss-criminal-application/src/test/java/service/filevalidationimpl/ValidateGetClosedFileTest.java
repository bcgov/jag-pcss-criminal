package service.filevalidationimpl;


import ca.bc.gov.open.pcsscriminalapplication.service.impl.FileValidatorImpl;
import ca.bc.gov.open.pcsscriminalcommon.utils.InstantUtils;
import ca.bc.gov.open.wsdl.pcss.one.Detail3;
import ca.bc.gov.open.wsdl.pcss.one.SetAppearanceMethodCriminalRequest;
import ca.bc.gov.open.wsdl.pcss.three.OperationModeType;
import ca.bc.gov.open.wsdl.pcss.one.GetClosedFileRequest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ValidateGetClosedFile Test")
public class ValidateGetClosedFileTest {

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
        Assertions.assertEquals("RequestAgencyIdentifierId is not valid,RequestPartId is not valid,RequestDtm is not valid,AgencyId is not valid,FromApprDt is not valid,ToApprDt is not valid", StringUtils.join(result, ","));

    }

}
