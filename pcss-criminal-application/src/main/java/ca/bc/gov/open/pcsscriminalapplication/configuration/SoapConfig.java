package ca.bc.gov.open.pcsscriminalapplication.configuration;

import ca.bc.gov.open.pcsscriminalapplication.exception.DetailSoapFaultDefinitionExceptionResolver;
import ca.bc.gov.open.pcsscriminalapplication.exception.ServiceFaultException;
import ca.bc.gov.open.pcsscriminalapplication.properties.CaseLookupProperties;
import ca.bc.gov.open.pcsscriminalapplication.properties.DemsProperties;
import ca.bc.gov.open.pcsscriminalapplication.properties.IslProperties;
import ca.bc.gov.open.pcsscriminalapplication.properties.PcssProperties;
import ca.bc.gov.open.pcsscriminalcommon.serializer.InstantDeserializer;
import ca.bc.gov.open.pcsscriminalcommon.serializer.InstantSerializer;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.xml.soap.SOAPMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
@EnableConfigurationProperties({
    DemsProperties.class,
    PcssProperties.class,
    IslProperties.class,
    CaseLookupProperties.class
})
public class SoapConfig extends WsConfigurerAdapter {

    @Autowired private PcssProperties pcssProperties;
    @Autowired private DemsProperties demsProperties;
    @Autowired private IslProperties islProperties;
    @Autowired private CaseLookupProperties caseLookupProperties;

    @Bean
    public SoapFaultMappingExceptionResolver exceptionResolver() {
        SoapFaultMappingExceptionResolver exceptionResolver =
                new DetailSoapFaultDefinitionExceptionResolver();

        SoapFaultDefinition faultDefinition = new SoapFaultDefinition();
        faultDefinition.setFaultCode(SoapFaultDefinition.SERVER);
        exceptionResolver.setDefaultFault(faultDefinition);

        Properties errorMappings = new Properties();
        errorMappings.setProperty(Exception.class.getName(), SoapFaultDefinition.SERVER.toString());
        errorMappings.setProperty(
                ServiceFaultException.class.getName(), SoapFaultDefinition.SERVER.toString());
        exceptionResolver.setExceptionMappings(errorMappings);
        exceptionResolver.setOrder(1);
        return exceptionResolver;
    }

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(
            ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/criminal/*");
    }

    @Bean
    @Primary
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        var restTemplate =
                restTemplateBuilder
                        .basicAuthentication(
                                pcssProperties.getUserName(), pcssProperties.getPassword())
                        .setReadTimeout(
                                Duration.ofSeconds(
                                        Integer.parseInt(pcssProperties.getOrdsReadTimeout())))
                        .build();
        restTemplate.getMessageConverters().add(0, createMappingJacksonHttpMessageConverter());
        return restTemplate;
    }

    @Bean(name = "restTemplateDEMS")
    public RestTemplate restTemplateDEMS(RestTemplateBuilder restTemplateBuilder) {
        var restTemplate =
                restTemplateBuilder
                        .basicAuthentication(
                                demsProperties.getUserName(), demsProperties.getPassword())
                        .setReadTimeout(
                                Duration.ofSeconds(
                                        Integer.parseInt(demsProperties.getOrdsReadTimeout())))
                        .build();
        restTemplate.getMessageConverters().add(0, createMappingJacksonHttpMessageConverter());
        return restTemplate;
    }

    @Bean(name = "restTemplateCaseLookup")
    public RestTemplate restTemplateCaseLookup(RestTemplateBuilder restTemplateBuilder) {
        var restTemplate =
                restTemplateBuilder
                        .setReadTimeout(
                                Duration.ofSeconds(
                                        Integer.parseInt(
                                                caseLookupProperties.getOrdsReadTimeout())))
                        .build();
        restTemplate.getMessageConverters().add(0, createMappingJacksonHttpMessageConverter());
        restTemplate
                .getInterceptors()
                .add(
                        new ClientHttpRequestInterceptor() {
                            @Override
                            public ClientHttpResponse intercept(
                                    HttpRequest request,
                                    byte[] body,
                                    ClientHttpRequestExecution execution)
                                    throws IOException {
                                request.getHeaders()
                                        .add(
                                                "Authorization",
                                                "Bearer "
                                                        + new String(
                                                                caseLookupProperties.getToken()));
                                return execution.execute(request, body);
                            }
                        });
        return restTemplate;
    }

    @Bean(name = "restTemplateISL")
    public RestTemplate restTemplateISL(RestTemplateBuilder restTemplateBuilder) {
        var restTemplate =
                restTemplateBuilder
                        .setReadTimeout(
                                Duration.ofSeconds(
                                        Integer.parseInt(islProperties.getOrdsReadTimeout())))
                        .build();
        restTemplate.getMessageConverters().add(0, createMappingJacksonHttpMessageConverter());
        restTemplate
                .getInterceptors()
                .add(
                        new ClientHttpRequestInterceptor() {
                            @Override
                            public ClientHttpResponse intercept(
                                    HttpRequest request,
                                    byte[] body,
                                    ClientHttpRequestExecution execution)
                                    throws IOException {
                                request.getHeaders()
                                        .add(
                                                "Authorization",
                                                "Bearer " + new String(islProperties.getToken()));
                                return execution.execute(request, body);
                            }
                        });
        return restTemplate;
    }

    private MappingJackson2HttpMessageConverter createMappingJacksonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        converter.setObjectMapper(objectMapper());
        return converter;
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Instant.class, new InstantDeserializer());
        module.addSerializer(Instant.class, new InstantSerializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }

    @Bean
    public SaajSoapMessageFactory messageFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(SOAPMessage.WRITE_XML_DECLARATION, "true");
        SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
        messageFactory.setMessageProperties(props);
        messageFactory.setSoapVersion(SoapVersion.SOAP_12);
        return messageFactory;
    }

    @Bean(name = "JusticePCSSCriminal.wsProvider:pcssCriminal")
    public Wsdl11Definition JusticePCSSWSDL() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("xsdSchemas/pcssCriminal.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "JusticePCSSCriminal.wsProvider:pcssCriminalSecure")
    public Wsdl11Definition JusticeSecurePCSSWSDL() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("xsdSchemas/pcssCriminalSecure.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "generateDemsCaseWSDLFromXSD")
    public DefaultWsdl11Definition demsCaseWSDL(XsdSchema demsCaseSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("DemsCasePort");
        wsdl11Definition.setLocationUri("/criminal");
        wsdl11Definition.setTargetNamespace("http://courts.gov.bc.ca/xml/ns/pcss/demsCase/v1");
        wsdl11Definition.setCreateSoap12Binding(true);
        wsdl11Definition.setSchema(demsCaseSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema demsCaseSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsdSchemas/demsCase.xsd"));
    }

    @Bean(name = "JusticePCSSCriminal.Integration:demsCase")
    public Wsdl11Definition DemsCaseWSDL() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("xsdSchemas/demsCase.wsdl"));
        return wsdl11Definition;
    }
}
