package ca.bc.gov.open.pcsscriminalapplication.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.AuthenticationException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Auth Entry Point Test Suite")
public class MyBasicAuthenticationEntryPointTest {

    @Mock HttpServletRequest mockRequest;

    @Mock HttpServletResponse mockResponse;

    @Mock AuthenticationException authExMock;

    @Mock PrintWriter mockWriter;

    @Mock ObjectMapper mockObjectMapper;

    private MyBasicAuthenticationEntryPoint sut;

    @BeforeAll
    public void beforeAll() {
        MockitoAnnotations.openMocks(this);

        sut = new MyBasicAuthenticationEntryPoint(mockObjectMapper);
    }

    @Test
    @DisplayName("Test commence method")
    public void testCommence() throws IOException {

        Mockito.when(mockRequest.getRequestURI()).thenReturn("request URI");
        Mockito.when(mockResponse.getWriter()).thenReturn(mockWriter);
        Assertions.assertDoesNotThrow(() -> sut.commence(mockRequest, mockResponse, authExMock));
    }
}
