package service.filevalidationimpl;

import ca.bc.gov.open.pcsscriminalapplication.service.impl.FileValidatorImpl;
import ca.bc.gov.open.pcsscriminalcommon.utils.InstantUtils;
import ca.bc.gov.open.wsdl.pcss.one.SetFileNoteRequest;
import ca.bc.gov.open.wsdl.pcss.three.FileNoteType;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ValidateSetFileNoteTest Test")
public class ValidateSetFileNoteTest {

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

        SetFileNoteRequest setFileNoteRequest = new SetFileNoteRequest();

        setFileNoteRequest.setRequestDtm(InstantUtils.parse(DATE));
        setFileNoteRequest.setRequestAgencyIdentifierId(VALUE);
        setFileNoteRequest.setRequestPartId(VALUE);
        setFileNoteRequest.setFileNoteTypeCd(FileNoteType.FILE);
        setFileNoteRequest.setNoteTxt(VALUE);
        setFileNoteRequest.setJustinNo(VALUE);

        List<String> result = sut.validateSetFileNote(setFileNoteRequest);

        Assertions.assertTrue(result.isEmpty());

    }

    @Test
    @DisplayName("Fail: all validations fail")
    public void failTestReturns() {

        SetFileNoteRequest setFileNoteRequest = new SetFileNoteRequest();

        setFileNoteRequest.setRequestDtm(InstantUtils.parse(BAD_DATE));
        setFileNoteRequest.setRequestAgencyIdentifierId(LONG_STRING);
        setFileNoteRequest.setRequestPartId(LONG_STRING);
        setFileNoteRequest.setFileNoteTypeCd(FileNoteType.FILE);
        setFileNoteRequest.setNoteTxt(LONG_STRING);
        setFileNoteRequest.setJustinNo(LONG_STRING);

        List<String> result = sut.validateSetFileNote(setFileNoteRequest);

        Assertions.assertEquals(4, result.size());
        Assertions.assertEquals("RequestAgencyIdentifierId is not valid,RequestPartId is not valid,RequestDtm is not valid,JustinNo is not valid", StringUtils.join(result, ","));

    }

}