package ca.bc.gov.open.pcsscriminalapplication.service;

import ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceRequest;
import ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionRequest;
import java.util.List;

public interface SyncValidator {

    List<String> validateGetSyncCriminalAppearance(
            GetSyncCriminalAppearanceRequest getSyncCriminalAppearanceRequest);

    List<String> validateGetSyncCriminalHearingRestriction(
            GetSyncCriminalHearingRestrictionRequest getSyncCriminalHearingRestrictionRequest);
}
