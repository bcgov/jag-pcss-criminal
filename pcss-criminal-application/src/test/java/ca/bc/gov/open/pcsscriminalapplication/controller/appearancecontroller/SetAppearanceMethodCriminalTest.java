package ca.bc.gov.open.pcsscriminalapplication.controller.appearancecontroller;

import ca.bc.gov.open.pcsscriminalapplication.controller.AppearanceController;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SetAppearanceMethodCriminalTest {

    @Mock
    private RestTemplate restTemplateMock;

    @Mock
    private PcssProperties pcssPropertiesMock;

    @Mock
    private ObjectMapper objectMapperMock;

    private AppearanceController sut;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        sut = new AppearanceController(restTemplateMock, pcssPropertiesMock, objectMapperMock);

    }

    @Test
    @DisplayName("Success: get returns expected object")
    public void successTestReturns() {

        Assertions.assertNull(sut.setAppearanceMethodCriminal());

    }

}
