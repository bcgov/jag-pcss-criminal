package ca.bc.gov.open.pcsscriminalapplication;

public class Keys {

    private Keys() {
        //Empty Constructor
    }

    public static final String SOAP_NAMESPACE = "http://courts.gov.bc.ca/xml/ns/pcss/criminal/v1";

    //Methods
    public static final String SOAP_METHOD_APPEARANCE = "getAppearanceCriminal";
    public static final String SOAP_METHOD_APPEARANCE_SECURE = "getAppearanceCriminalSecure";
    public static final String SOAP_METHOD_APPEARANCE_APPR_METHOD = "getAppearanceCriminalApprMethod";
    public static final String SOAP_METHOD_APPEARANCE_APPR_METHOD_SECURE = "getAppearanceCriminalApprMethodSecure";
    public static final String SOAP_METHOD_APPEARANCE_RESOURCE = "getAppearanceCriminalResource";
    public static final String SOAP_METHOD_APPEARANCE_COUNT = "getAppearanceCriminalCount";
    public static final String SOAP_METHOD_APPEARANCE_COUNT_SECURE = "getAppearanceCriminalCountSecure";
    public static final String SOAP_METHOD_SET_APPEARANCE = "setAppearanceCriminal";
    public static final String SOAP_METHOD_SET_APPEARANCE_METHOD = "setAppearanceMethodCriminal";
    public static final String SOAP_METHOD_FILE_CLOSED = "getClosedFile";
    public static final String SOAP_METHOD_FILE_DETAIL = "getFileDetailCriminal";
    public static final String SOAP_METHOD_FILE_DETAIL_SECURE = "getFileDetailCriminalSecure";
    public static final String SOAP_METHOD_SET_FILE_NOTE = "setFileNote";
    public static final String SOAP_METHOD_HEARING_RESTRICTION_CRIMINAL = "setHearingRestrictionCriminal";
    public static final String SOAP_METHOD_HEALTH = "getHealth";
    public static final String SOAP_METHOD_PING = "getPing";
    public static final String SOAP_METHOD_PERSONNEL_AVAILABILITY = "getGetPersonnelAvailability";
    public static final String SOAP_METHOD_PERSONNEL_DETAIL = "getPersonnelAvailDetail";
    public static final String SOAP_METHOD_PERSONNEL_SEARCH = "getPersonnelSearch";
    public static final String SOAP_METHOD_SYNC_APPEARANCE = "getSyncCriminalAppearance";
    public static final String SOAP_METHOD_SYNC_HEARING = "getSyncCriminalHearingRestriction";
    public static final String SOAP_METHOD_CROWN_FILE_DETAIL = "setCrownFileDetail";

    //Messages
    public static final String DATE_ERROR_MESSAGE = "Bad date format or missing date received";
    public static final String ORDS_ERROR_MESSAGE = "Error received from ORDS";

    //Query Params
    public static final String QUERY_AGENT_ID = "requestAgenId";
    public static final String QUERY_PART_ID = "requestPartId";
    public static final String QUERY_REQUEST_DATE = "requestDtm";
    public static final String QUERY_JUSTIN_NO = "justinNo";
    public static final String QUERY_FUTURE_FLAG = "futureYN";
    public static final String QUERY_HISTORY_FLAG = "historyYN";
    public static final String QUERY_APPEARANCE_ID = "appearanceId";
    public static final String QUERY_APPEARANCE_CD = "appearanceCd";
    public static final String QUERY_APPLICATION_CD = "applicationCd";
    public static final String QUERY_PERSON_CD = "personCd";
    public static final String QUERY_AGENCY_IDENTIFIER = "agencyIdentifier";
    public static final String QUERY_FROM_DATE = "fromDtm";
    public static final String QUERY_TO_DATE = "toDtm";
    public static final String QUERY_AVAILABILITY_DATE = "availabilityDt";
    public static final String QUERY_PAAS_PART_ID = "paasPartId";
    public static final String QUERY_SEARCH_TYPE_CD = "searchTypeCd";
    public static final String QUERY_SEARCH_TEXT = "searchText";

    //Endpoints
    public static final String ORDS_APPEARANCE = "appearance";
    public static final String ORDS_APPEARANCE_SECURE = "secure/appearance";
    public static final String ORDS_APPEARANCE_METHOD = "appearance-method";
    public static final String ORDS_APPEARANCE_METHOD_SECURE = "secure/appearance-method";
    public static final String ORDS_APPEARANCE_COUNT = "appearance-count";
    public static final String ORDS_APPEARANCE_COUNT_SECURE = "secure/appearance-count";
    public static final String ORDS_APPEARANCE_RESOURCE = "appearance-resource";
    public static final String ORDS_HEARING = "hearing";
    public static final String ORDS_HEALTH = "health";
    public static final String ORDS_PING = "ping";
    public static final String ORDS_PERSONNEL_AVAILABILITY = "personnel/availability";
    public static final String ORDS_PERSONNEL_DETAIL = "personnel/availability-detail";
    public static final String ORDS_PERSONNEL_SEARCH = "personnel/search";
    public static final String ORDS_SYNC_APPEARANCE = "sync/appearance";
    public static final String ORDS_SYNC_HEARING = "sync/hearing";
    public static final String ORDS_CROWN_FILE_DETAIL = "crown/file-detail";

    //Logs
    public static final String LOG_RECEIVED = "{}: received";
    public static final String LOG_ORDS = "{}: making ORDS request";
    public static final String LOG_SUCCESS = "{}: successful";

}
