package ca.bc.gov.open.pcsscriminalapplication.exception;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(
        faultCode = FaultCode.CLIENT,
        faultStringOrReason =
                "User provided does not have access to resource.")
public class InvalidUserException extends Exception {
    public InvalidUserException() {
        super();
    }
}
