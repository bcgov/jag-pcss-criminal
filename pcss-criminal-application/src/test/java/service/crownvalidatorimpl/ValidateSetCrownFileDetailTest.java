package service.crownvalidatorimpl;

import ca.bc.gov.open.pcsscriminalapplication.service.impl.CrownValidatorImpl;
import ca.bc.gov.open.pcsscriminalcommon.utils.InstantUtils;
import ca.bc.gov.open.wsdl.pcss.one.SetCrownFileDetailRequest;
import ca.bc.gov.open.wsdl.pcss.three.AppearanceDurationType;
import ca.bc.gov.open.wsdl.pcss.three.FileComplexityType;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ValidateSetCrownFileDetail Test")
public class ValidateSetCrownFileDetailTest {

    private static final String LONG_STRING = "000000000000000000000000000000000000";
    private static final String VALUE = "0000";
    private static final String DATE = "2013-03-25 13:04:22.1";
    private static final String BAD_DATE = "2020-SEP-05";

    private CrownValidatorImpl sut;

    @BeforeAll
    public void BeforeAll() {

        sut = new CrownValidatorImpl();

    }

    @Test
    @DisplayName("Success: all validation succeed")
    public void successTestReturns() {

        SetCrownFileDetailRequest setCrownFileDetailRequest = new SetCrownFileDetailRequest();
        setCrownFileDetailRequest.setRequestAgencyIdentifierId(VALUE);
        setCrownFileDetailRequest.setRequestPartId(VALUE);
        setCrownFileDetailRequest.setRequestDtm(InstantUtils.parse(DATE));
        setCrownFileDetailRequest.setJustinNo(VALUE);
        setCrownFileDetailRequest.setMdocCcn(VALUE);
        setCrownFileDetailRequest.setCrownEstimateLenQty(VALUE);
        setCrownFileDetailRequest.setCrownEstimateLenUnit(AppearanceDurationType.DYS);
        setCrownFileDetailRequest.setFileDesignationCd(FileComplexityType.ALL);

        List<String> validationErrors = sut.validateSetCrownFileDetail(setCrownFileDetailRequest);

        Assertions.assertTrue(validationErrors.isEmpty());

    }

    @Test
    @DisplayName("Fail: all validation fail")
    public void failsTestReturns() {

        SetCrownFileDetailRequest setCrownFileDetailRequest = new SetCrownFileDetailRequest();
        setCrownFileDetailRequest.setRequestAgencyIdentifierId(LONG_STRING);
        setCrownFileDetailRequest.setRequestPartId(LONG_STRING);
        setCrownFileDetailRequest.setRequestDtm(InstantUtils.parse(BAD_DATE));
        setCrownFileDetailRequest.setJustinNo(LONG_STRING);
        setCrownFileDetailRequest.setMdocCcn(LONG_STRING);
        setCrownFileDetailRequest.setCrownEstimateLenQty(LONG_STRING);
        setCrownFileDetailRequest.setCrownEstimateLenUnit(AppearanceDurationType.DYS);
        setCrownFileDetailRequest.setFileDesignationCd(FileComplexityType.ALL);

        List<String> validationErrors = sut.validateSetCrownFileDetail(setCrownFileDetailRequest);

        Assertions.assertEquals(5, validationErrors.size());
        Assertions.assertEquals("RequestAgencyIdentifierId is not valid,RequestPartId is not valid,RequestDtm is not valid,JustinNo is not valid,MdocCcn is not valid", StringUtils.join(validationErrors, ","));

    }

}
