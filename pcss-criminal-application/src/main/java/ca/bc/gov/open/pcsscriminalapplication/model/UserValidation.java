package ca.bc.gov.open.pcsscriminalapplication.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserValidation {

    public Boolean isValid;
    public String reason;

}
