package ca.bc.gov.open.pcsscriminalapplication.validation;

import ca.bc.gov.open.pcsscriminalapplication.exception.InvalidUserException;

public interface ValidationService {

    void isSecureRequestValid(String agentIdentifier, String justinNo) throws InvalidUserException;

}
