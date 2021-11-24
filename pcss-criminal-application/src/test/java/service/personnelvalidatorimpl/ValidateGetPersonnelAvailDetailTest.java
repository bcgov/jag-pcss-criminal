package service.personnelvalidatorimpl;

import ca.bc.gov.open.pcsscriminalapplication.service.impl.PersonnelValidatorImpl;
import ca.bc.gov.open.pcsscriminalcommon.utils.InstantUtils;
import ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailDetailRequest;
import ca.bc.gov.open.wsdl.pcss.three.AvailablePersonType;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ValidateGetPersonnelAvailDetail Test")
public class ValidateGetPersonnelAvailDetailTest {

    private static final String LONG_STRING = "VERYLONGSTRINGTOTESTTHELENGTHRESTRICTION";
    private static final String VALUE = "TEST";
    private static final String DATE = "2013-03-25 13:04:22.1";
    private static final String BAD_DATE = "2001-DEC-26";

    PersonnelValidatorImpl sut;

    @BeforeAll
    public void BeforeAll() {

        sut = new PersonnelValidatorImpl();

    }

    @Test
    @DisplayName("Success: all validations succeed")
    public void successTestReturns() {

        GetPersonnelAvailDetailRequest getPersonnelAvailDetailRequest = new GetPersonnelAvailDetailRequest();
        getPersonnelAvailDetailRequest.setRequestDtm(InstantUtils.parse(DATE));
        getPersonnelAvailDetailRequest.setRequestAgencyIdentifierId(VALUE);
        getPersonnelAvailDetailRequest.setRequestPartId(VALUE);
        getPersonnelAvailDetailRequest.setPersonTypeCd(AvailablePersonType.C);
        getPersonnelAvailDetailRequest.setAvailabilityDt(InstantUtils.parse(DATE));
        getPersonnelAvailDetailRequest.setPaasPartId(VALUE);

        List<String> result = sut.validateGetPersonnelAvailDetail(getPersonnelAvailDetailRequest);

        Assertions.assertTrue(result.isEmpty());

    }

    @Test
    @DisplayName("Fail: all validations fail")
    public void failTestReturns() {

        GetPersonnelAvailDetailRequest getPersonnelAvailDetailRequest = new GetPersonnelAvailDetailRequest();
        getPersonnelAvailDetailRequest.setRequestDtm(InstantUtils.parse(BAD_DATE));
        getPersonnelAvailDetailRequest.setRequestAgencyIdentifierId(LONG_STRING);
        getPersonnelAvailDetailRequest.setRequestPartId(LONG_STRING);
        getPersonnelAvailDetailRequest.setPersonTypeCd(AvailablePersonType.C);
        getPersonnelAvailDetailRequest.setAvailabilityDt(InstantUtils.parse(BAD_DATE));
        getPersonnelAvailDetailRequest.setPaasPartId(LONG_STRING);

        List<String> result = sut.validateGetPersonnelAvailDetail(getPersonnelAvailDetailRequest);

        Assertions.assertEquals(4, result.size());
        Assertions.assertEquals("RequestAgencyIdentifierId is not valid,RequestPartId is not valid,RequestDtm is not valid,AvailabilityDt is not valid", StringUtils.join(result, ","));

    }

}