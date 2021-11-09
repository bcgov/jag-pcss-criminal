package ca.bc.gov.open.pcsscriminalapplication.utils;

public class ValidationUtils {

    public static boolean checkRequestAgencyIdentifierIdForErrors(String requestAgencyIdentifierId) {
        return requestAgencyIdentifierId.length() > 16;
    }

    public static boolean checkSystemIdTypeForErrors(String systemId) {
        return systemId.length() > 16;
    }

    public static boolean checkJustinNoTypeForErrors(String justinNo) {
        return justinNo.length() > 12;
    }

    public static boolean checkSystemSeqNoTypeForErrors(String systemSeqNo) {
        return systemSeqNo.length() > 4;
    }

    public static boolean checkDateTimeTypeForErrors(String dateTime) {
        return dateTime.length() != 21;
    }

    public static boolean checkConcurrencyControlTypeForErrors(String concurrencyControlNo) {
        return concurrencyControlNo.length() > 12;
    }
}
