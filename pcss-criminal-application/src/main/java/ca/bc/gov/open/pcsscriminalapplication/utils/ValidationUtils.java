package ca.bc.gov.open.pcsscriminalapplication.utils;

import org.apache.commons.lang3.StringUtils;

public class ValidationUtils {

    private ValidationUtils() {
        // empty constructor
    }

    public static boolean checkAgencyIdentifierTypeForErrors(String agencyIdentifierId) {
        return (StringUtils.isEmpty(agencyIdentifierId) || agencyIdentifierId.length() > 16);
    }

    public static boolean checkSystemIdTypeForErrors(String systemId) {
        return (StringUtils.isEmpty(systemId) || systemId.length() > 16);
    }

    public static boolean checkJustinNoTypeForErrors(String justinNo) {
        return (justinNo == null || justinNo.length() > 12);
    }

    public static boolean checkSystemSeqNoTypeForErrors(String systemSeqNo) {
        return (systemSeqNo == null || systemSeqNo.length() > 4);
    }

    public static boolean checkDateTimeTypeForErrors(String dateTime) {
        return (dateTime.length() != 21);
    }

    public static boolean checkConcurrencyControlTypeForErrors(String concurrencyControlNo) {
        return (concurrencyControlNo == null || concurrencyControlNo.length() > 12);
    }

    public static boolean checkAppearanceReasonCriminalTypeForErrors(String appearanceReasonType) {
        return (appearanceReasonType == null || appearanceReasonType.length() > 3);
    }

    public static boolean checkCourtRoomTypeForErrors(String courtRoomType) {
        return (courtRoomType == null || courtRoomType.length() > 6);
    }
}
