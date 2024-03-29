package ca.bc.gov.open.pcsscriminalapplication.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("WSDL Filter Test Suite")
public class WSDLFilterTest {

    private WSDLFilter sut;

    @Mock HttpServletRequest requestMock;

    @Mock ServletResponse responseMock;

    @Mock FilterChain chainMock;

    @BeforeAll
    public void beforeAll() {
        MockitoAnnotations.openMocks(this);

        sut = new WSDLFilter();
    }

    @Test
    @DisplayName("Test doFilter with wrapper")
    public void testDoFilterWrapper() throws ServletException, IOException {
        Mockito.when(requestMock.getQueryString()).thenReturn("wsdl");

        Assertions.assertDoesNotThrow(() -> sut.doFilter(requestMock, responseMock, chainMock));
    }

    @Test
    @DisplayName("Test doFilter without wrapper")
    public void testDoFilterNoWrapper() {
        Mockito.when(requestMock.getQueryString()).thenReturn("something else");

        Assertions.assertDoesNotThrow(() -> sut.doFilter(requestMock, responseMock, chainMock));
    }
}
