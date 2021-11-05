package ca.bc.gov.open.pcsscriminalapplication.validation;

import ca.bc.gov.open.pcsscriminalapplication.controller.AppearanceController;
import ca.bc.gov.open.pcsscriminalapplication.exception.InvalidUserException;
import ca.bc.gov.open.pcsscriminalapplication.model.UserValidation;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalapplication.utils.LogBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("LogHelper Test")
public class ValidationServiceTest {

    @Mock
    private RestTemplate restTemplateMock;

    @Mock
    private PcssProperties pcssPropertiesMock;

    private ValidationService sut;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(pcssPropertiesMock.getHost()).thenReturn("http://localhost/");

        sut = new ValidationServiceImpl(restTemplateMock, pcssPropertiesMock);

    }

    @Test
    @DisplayName("Success request is valid")
    public void succesRequestIsValid() {

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenReturn(ResponseEntity.ok(new UserValidation(true, "TEST")));

        Assertions.assertDoesNotThrow(() -> sut.isSecureRequestValid("TEST", "TEST"));

    }

    @Test
    @DisplayName("Success request is not valid")
    public void succesRequestIsNotValid() {

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenReturn(ResponseEntity.ok(new UserValidation(false, "TEST")));

        Assertions.assertThrows(InvalidUserException.class, () -> sut.isSecureRequestValid("TEST", "TEST"));

    }


    @Test
    @DisplayName("Error request exception")
    public void errorRequestFailed() {

        Mockito.when(restTemplateMock.exchange(any(String.class), any(), any(), any(Class.class))).thenReturn(ResponseEntity.badRequest().build());

        Assertions.assertThrows(Exception.class, () -> sut.isSecureRequestValid("TEST", "TEST"));


    }

}
