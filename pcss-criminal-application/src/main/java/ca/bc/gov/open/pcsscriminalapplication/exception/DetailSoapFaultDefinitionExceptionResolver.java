package ca.bc.gov.open.pcsscriminalapplication.exception;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

@Slf4j
public class DetailSoapFaultDefinitionExceptionResolver extends SoapFaultMappingExceptionResolver {

    @Override
    protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {

        SoapFaultDefinition soapFaultDefinition = new SoapFaultDefinition();
        String ENVELOPE_NAMESPACE_URI = "http://schemas.xmlsoap.org/soap/envelope/";

        QName CLIENT_FAULT_NAME = new QName(ENVELOPE_NAMESPACE_URI);
        soapFaultDefinition.setFaultCode(CLIENT_FAULT_NAME);
        setDefaultFault(soapFaultDefinition);
        Result result = fault.addFaultDetail().getResult();

        // marshal
        try {
            var error = ((ServiceFaultException) ex).getError();
            JAXBContext.newInstance(error.getClass()).createMarshaller().marshal(error, result);
        } catch (JAXBException e) {
            log.warn("SoapFault exception: " + e.getMessage());
        }
    }
}
