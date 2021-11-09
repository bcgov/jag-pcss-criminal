package ca.bc.gov.open.pcsscriminalapplication.utils;

public class ValidationUtils {

    private ValidationUtils() {
        //empty constructor
    }

    public static boolean checkRequestAgencyIdentifierIdForErrors(String requestAgencyIdentifierId) {
        return (requestAgencyIdentifierId != null && requestAgencyIdentifierId.length() > 16);
    }

    public static boolean checkSystemIdTypeForErrors(String systemId) {
        return (systemId == null || systemId.length() > 16);
    }

    public static boolean checkJustinNoTypeForErrors(String justinNo) {
        return (justinNo == null || justinNo.length() > 12);
    }

    public static boolean checkSystemSeqNoTypeForErrors(String systemSeqNo) {
        return (systemSeqNo == null || systemSeqNo.length() > 4);
    }

    public static boolean checkDateTimeTypeForErrors(String dateTime) {
        return (dateTime == null || dateTime.length() != 24);
    }

    public static boolean checkConcurrencyControlTypeForErrors(String concurrencyControlNo) {
        return (concurrencyControlNo == null || concurrencyControlNo.length() > 12);
    }

}
