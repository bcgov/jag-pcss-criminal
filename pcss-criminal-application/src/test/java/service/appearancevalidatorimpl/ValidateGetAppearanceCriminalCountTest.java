package service.appearancevalidatorimpl;

import ca.bc.gov.open.pcsscriminalapplication.service.impl.AppearanceValidatorImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ValidateGetAppearanceCriminalCount Test")
public class ValidateGetAppearanceCriminalCountTest {

    AppearanceValidatorImpl sut;

    @BeforeAll
    public void BeforeAll() {

        sut = new AppearanceValidatorImpl();

    }


    @Test
    @DisplayName("Success: all validations succeed")
    public void successTestReturns() {

    }

    @Test
    @DisplayName("Fail: all validations fail")
    public void failTestReturns() {

    }

}
