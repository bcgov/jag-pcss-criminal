package ca.bc.gov.open.pcsscriminalapplication.exception;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.SERVER)
public class ORDSException extends RuntimeException {
    public ORDSException() {
        super(
                "An error response was received from ORDS please check that your request is of valid form");
    }

    public ORDSException(String message) {
        super(message);
    }
}
