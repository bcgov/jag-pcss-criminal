package ca.bc.gov.open.pcsscriminalapplication;

public class Keys {

    private Keys() {
        // Empty Constructor
    }

    public static final String SOAP_NAMESPACE = "http://courts.gov.bc.ca/xml/ns/pcss/criminal/v1";

    // Methods
    public static final String SOAP_METHOD_APPEARANCE = "getAppearanceCriminal";
    public static final String SOAP_METHOD_APPEARANCE_SECURE = "getAppearanceCriminalSecure";
    public static final String SOAP_METHOD_APPEARANCE_APPR_METHOD =
            "getAppearanceCriminalApprMethod";
    public static final String SOAP_METHOD_APPEARANCE_APPR_METHOD_SECURE =
            "getAppearanceCriminalApprMethodSecure";
    public static final String SOAP_METHOD_APPEARANCE_RESOURCE = "getAppearanceCriminalResource";
    public static final String SOAP_METHOD_APPEARANCE_COUNT = "getAppearanceCriminalCount";
    public static final String SOAP_METHOD_APPEARANCE_COUNT_SECURE =
            "getAppearanceCriminalCountSecure";
    public static final String SOAP_METHOD_SET_APPEARANCE = "setAppearanceCriminal";
    public static final String SOAP_METHOD_SET_APPEARANCE_METHOD = "setAppearanceMethodCriminal";
    public static final String SOAP_METHOD_FILE_CLOSED = "getClosedFile";
    public static final String SOAP_METHOD_FILE_DETAIL = "getFileDetailCriminal";
    public static final String SOAP_METHOD_FILE_DETAIL_SECURE = "getFileDetailCriminalSecure";
    public static final String SOAP_METHOD_SET_FILE_NOTE = "setFileNote";
    public static final String SOAP_METHOD_HEARING_RESTRICTION_CRIMINAL =
            "setHearingRestrictionCriminal";
    public static final String SOAP_METHOD_HEALTH = "getHealth";
    public static final String SOAP_METHOD_PING = "getPing";
    public static final String SOAP_METHOD_PERSONNEL_AVAILABILITY = "getPersonnelAvailability";
    public static final String SOAP_METHOD_PERSONNEL_DETAIL = "getPersonnelAvailDetail";
    public static final String SOAP_METHOD_PERSONNEL_SEARCH = "getPersonnelSearch";
    public static final String SOAP_METHOD_SYNC_APPEARANCE = "getSyncCriminalAppearance";
    public static final String SOAP_METHOD_SYNC_HEARING = "getSyncCriminalHearingRestriction";
    public static final String SOAP_METHOD_CROWN_FILE_DETAIL = "setCrownFileDetail";
    public static final String SOAP_METHOD_CROWN_ASSIGNMENT = "getCrownAssignment";
    public static final String SOAP_METHOD_SET_CROWN_ASSIGNMENT = "setCrownAssignment";
    public static final String SOAP_METHOD_COUNSEL_DETAIL_CRIMINAL = "setCounselDetailCriminal";

    // Messages
    public static final String DATE_ERROR_MESSAGE = "Bad date format or missing date received";
    public static final String ORDS_ERROR_MESSAGE = "Error received from ORDS";
    public static final String VALIDATION_ERROR_MESSAGE = "Invalid data received";

    // Query Params
    public static final String QUERY_AGENT_ID = "agenid";
    public static final String QUERY_PART_ID = "rqstpartid";
    public static final String QUERY_PART_ID_SIMPLE = "partid";
    public static final String QUERY_REQUEST_DATE = "requestdtm";
    public static final String QUERY_JUSTIN_NO = "justinno";
    public static final String QUERY_FUTURE_FLAG = "futureyn";
    public static final String QUERY_HISTORY_FLAG = "historyyn";
    public static final String QUERY_APPEARANCE_ID = "apprid";
    public static final String QUERY_APPLICATION_CD = "applicationcd";
    public static final String QUERY_PERSON_CD = "parttypecd";
    public static final String QUERY_AGENCY_IDENTIFIER = "rqstagenid";
    public static final String QUERY_FROM_DATE = "fromdate";
    public static final String QUERY_TO_DATE = "todate";
    public static final String QUERY_AVAILABILITY_DATE = "shiftdt";
    public static final String QUERY_PAAS_PART_ID = "paaspartid";
    public static final String QUERY_SEARCH_TYPE_CD = "searchtypecd";
    public static final String QUERY_SEARCH_TEXT = "searchtxt";
    public static final String QUERY_SINCE_DATE = "sincedt";
    public static final String QUERY_PART_ID_LIST = "partidlist";
    public static final String QUERY_SYNC_TO_DATE = "procuptodtm";

    // Endpoints
    public static final String ORDS_APPEARANCE = "appearance";
    public static final String ORDS_APPEARANCE_SECURE = "secure/appearance";
    public static final String ORDS_APPEARANCE_METHOD = "appearance/method";
    public static final String ORDS_APPEARANCE_METHOD_SECURE = "secure/appearance/method";
    public static final String ORDS_APPEARANCE_COUNT = "appearance/counts";
    public static final String ORDS_APPEARANCE_COUNT_SECURE = "secure/appearance/counts";
    public static final String ORDS_APPEARANCE_RESOURCE = "appearance/resource";
    public static final String ORDS_HEARING = "hearing/restriction";
    public static final String ORDS_HEALTH = "health";
    public static final String ORDS_PING = "ping";
    public static final String ORDS_PERSONNEL_AVAILABILITY = "personnel/availability";
    public static final String ORDS_PERSONNEL_DETAIL = "personnel/availability/detail";
    public static final String ORDS_PERSONNEL_SEARCH = "personnel/search";
    public static final String ORDS_SYNC_APPEARANCE = "sync/appearance";
    public static final String ORDS_SYNC_HEARING = "sync/hearing-restriction";
    public static final String ORDS_CROWN_FILE_DETAIL = "crown/file-detail";
    public static final String ORDS_CROWN_ASSIGNMENT = "crown/assignment";
    public static final String ORDS_COUNSEL_DETAIL_CRIMINAL = "counsel/detail";
    public static final String ORDS_CLOSED_FILE = "closed-file";
    public static final String ORDS_FILE_DETAIL = "file/detail";
    public static final String ORDS_SECURE_FILE_DETAIL = "secure/file-detail";
    public static final String ORDS_FILE_NOTE = "file/note";

    // Logs
    public static final String LOG_RECEIVED = "{}: received";
    public static final String LOG_ORDS = "{}: making ORDS request";
    public static final String LOG_SUCCESS = "{}: successful";
    public static final String LOG_FAILED_VALIDATION = "{}: request object validation failed";

    // Response Codes
    public static final Integer FAILED_VALIDATION = -2;
}
