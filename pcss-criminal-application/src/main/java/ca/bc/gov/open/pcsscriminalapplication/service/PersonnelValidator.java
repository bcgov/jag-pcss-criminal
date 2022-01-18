package ca.bc.gov.open.pcsscriminalapplication.service;

import ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailDetailRequest;
import ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailabilityRequest;
import ca.bc.gov.open.wsdl.pcss.one.GetPersonnelSearchRequest;
import java.util.List;

public interface PersonnelValidator {

    List<String> validateGetPersonnelAvailability(
            GetPersonnelAvailabilityRequest getPersonnelAvailabilityRequest);

    List<String> validateGetPersonnelAvailDetail(
            GetPersonnelAvailDetailRequest getPersonnelAvailDetailRequest);

    List<String> validateGetPersonnelSearch(GetPersonnelSearchRequest getPersonnelSearchRequest);
}
