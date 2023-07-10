package ca.bc.gov.open.pcsscriminalapplication.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceFaultException extends RuntimeException {

    private Object error;

    public ServiceFaultException(Object error) {
        super("Fault returned by invoked service");
        this.error = error;
    }

    public ServiceFaultException(Throwable e, Object error) {
        super("Fault returned by invoked service", e);
        this.error = error;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public String getRestError(String msg) {
        var STATUS_MESSAGE = "status_message\":\"";
        var start = msg.indexOf(STATUS_MESSAGE);
        if (start != -1) {
            var end = msg.indexOf("\" }", start + STATUS_MESSAGE.length());
            msg = msg.substring(start + STATUS_MESSAGE.length(), end);
        }

        return msg;
    }

    public static RuntimeException handleError(Exception ex) {
        return handleError(ex, null);
    }

    public static RuntimeException handleError(
            Exception ex, ca.bc.gov.open.wsdl.pcss.demsCaseUrl.Error error) {
        if (ex instanceof org.springframework.web.client.RestClientException) {
            var faultExceExcption = new ServiceFaultException(error);
            String msg = faultExceExcption.getRestError(ex.getMessage());
            error.setReason(msg);
            return faultExceExcption;
        } else {
            return new ORDSException(ex.getMessage());
        }
    }
}
