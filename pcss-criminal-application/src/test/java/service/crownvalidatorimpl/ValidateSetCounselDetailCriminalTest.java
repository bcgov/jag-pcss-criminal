package service.crownvalidatorimpl;
import ca.bc.gov.open.wsdl.pcss.three.OperationMode2Type;
import ca.bc.gov.open.pcsscriminalcommon.utils.InstantUtils;
import ca.bc.gov.open.wsdl.pcss.one.Detail4;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.open.pcsscriminalapplication.service.impl.CrownValidatorImpl;
import ca.bc.gov.open.wsdl.pcss.one.SetCounselDetailCriminalRequest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ValidateSetCounselDetailCriminal Test")
public class ValidateSetCounselDetailCriminalTest {

    private static final String LONG_STRING = "000000000000000000000000000000000000";
    private static final String VALUE = "0000";

    private CrownValidatorImpl sut;

    @BeforeAll
    public void BeforeAll() {

        sut = new CrownValidatorImpl();

    }

    @Test
    @DisplayName("Success: all validation succeed")
    public void successTestReturns() {

        SetCounselDetailCriminalRequest setCounselDetailCriminalRequest = new SetCounselDetailCriminalRequest();
        setCounselDetailCriminalRequest.setRequestAgencyIdentifierId(VALUE);
        setCounselDetailCriminalRequest.setRequestPartId(VALUE);
        setCounselDetailCriminalRequest.setRequestDtm(InstantUtils.parse("2013-03-25 13:04:22.1"));
        setCounselDetailCriminalRequest.setProfPartId(VALUE);
        setCounselDetailCriminalRequest.setProfSeqNo(VALUE);

        List<Detail4> detailList = new ArrayList<>();
        Detail4 detail = new Detail4();
        detail.setOperationModeCd(OperationMode2Type.ADD);
        detail.setCounselLastNm(VALUE);
        detail.setCounselFirstNm(VALUE);
        detail.setOfficePhoneNoTxt(VALUE);
        detail.setCellPhoneNoTxt(VALUE);
        detail.setEmailAddressTxt(VALUE);
        detail.setOtherDeviceTxt(VALUE);
        detailList.add(detail);

        setCounselDetailCriminalRequest.setDetail(detailList);

        List<String> validationErrors = sut.validateSetCounselDetailCriminal(setCounselDetailCriminalRequest);

        Assertions.assertTrue(validationErrors.isEmpty());

    }

    @Test
    @DisplayName("Success: optional properties set to null")
    public void successNullOptionalPropertiesReturns() {
        SetCounselDetailCriminalRequest setCounselDetailCriminalRequest = new SetCounselDetailCriminalRequest();
        setCounselDetailCriminalRequest.setRequestAgencyIdentifierId(VALUE);
        setCounselDetailCriminalRequest.setRequestPartId(VALUE);
        setCounselDetailCriminalRequest.setRequestDtm(InstantUtils.parse("2013-03-25 13:04:22.1"));
        setCounselDetailCriminalRequest.setProfPartId(VALUE);
        setCounselDetailCriminalRequest.setProfSeqNo(VALUE);

        List<Detail4> detailList = new ArrayList<>();
        Detail4 detail = new Detail4();
        detail.setOperationModeCd(OperationMode2Type.ADD);
        detail.setCounselLastNm(VALUE);
        detail.setCounselFirstNm(VALUE);
        detailList.add(detail);

        setCounselDetailCriminalRequest.setDetail(detailList);

        List<String> validationErrors = sut.validateSetCounselDetailCriminal(setCounselDetailCriminalRequest);

        Assertions.assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Fail: all validations fail, except for having no detail object")
    public void failInvalidFieldValuesReturns() {
        SetCounselDetailCriminalRequest setCounselDetailCriminalRequest = new SetCounselDetailCriminalRequest();
        setCounselDetailCriminalRequest.setRequestAgencyIdentifierId(LONG_STRING);
        setCounselDetailCriminalRequest.setRequestPartId(LONG_STRING);
        setCounselDetailCriminalRequest.setRequestDtm(InstantUtils.parse("2020-SEP-05"));
        setCounselDetailCriminalRequest.setProfPartId(LONG_STRING);
        setCounselDetailCriminalRequest.setProfSeqNo(LONG_STRING);

        List<Detail4> detailList = new ArrayList<>();
        Detail4 detail = new Detail4();
        detail.setOperationModeCd(OperationMode2Type.ADD);
        detail.setCounselLastNm(null);
        detail.setCounselFirstNm(null);
        detail.setOfficePhoneNoTxt(LONG_STRING);
        detail.setCellPhoneNoTxt(LONG_STRING);
        detail.setEmailAddressTxt(LONG_STRING);
        detail.setOtherDeviceTxt(LONG_STRING);
        detailList.add(detail);

        setCounselDetailCriminalRequest.setDetail(detailList);

        List<String> validationErrors = sut.validateSetCounselDetailCriminal(setCounselDetailCriminalRequest);

        Assertions.assertEquals(7, validationErrors.size());
        Assertions.assertEquals("RequestAgencyIdentifierId is not valid,RequestPartId is not valid,RequestDtm is not valid,ProfPartId is not valid,ProfSeqNo is not valid,Details CounselLastNm at index 1 is not valid,Details CounselFirstNm at index 1 is not valid", StringUtils.join(validationErrors, ","));

    }

    @Test
    @DisplayName("Fail: no detail object provided")
    public void failNoDetailObjectReturns() {

        SetCounselDetailCriminalRequest setCounselDetailCriminalRequest = new SetCounselDetailCriminalRequest();
        setCounselDetailCriminalRequest.setRequestAgencyIdentifierId(VALUE);
        setCounselDetailCriminalRequest.setRequestPartId(VALUE);
        setCounselDetailCriminalRequest.setRequestDtm(InstantUtils.parse("2013-03-25 13:04:22.1"));
        setCounselDetailCriminalRequest.setProfPartId(VALUE);
        setCounselDetailCriminalRequest.setProfSeqNo(VALUE);

        List<String> validationErrors = sut.validateSetCounselDetailCriminal(setCounselDetailCriminalRequest);

        Assertions.assertEquals(1, validationErrors.size());
        Assertions.assertEquals("Detail is not valid", StringUtils.join(validationErrors, ","));

    }


}
