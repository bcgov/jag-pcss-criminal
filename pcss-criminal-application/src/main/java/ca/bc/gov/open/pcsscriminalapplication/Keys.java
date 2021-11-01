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
    public static final String SOAP_METHOD_HEALTH = "getHealth";
    public static final String SOAP_METHOD_PING = "getPing";

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
    public static final String QUERY_APPLICATION_CD = "apllicationCd";
    public static final String QUERY_AGENCY_IDENTIFIER = "agencyIdentifier";
    public static final String QUERY_FROM_DATE = "fromDtm";
    public static final String QUERY_TO_DATE = "toDtm";

    //Endpoints
    public static final String ORDS_APPEARANCE = "appearance";
    public static final String ORDS_APPEARANCE_SECURE = "secure/appearance";
    public static final String ORDS_APPEARANCE_METHOD = "appearance-method";
    public static final String ORDS_APPEARANCE_METHOD_SECURE = "secure/appearance-method";
    public static final String ORDS_APPEARANCE_COUNT = "appearance-count";
    public static final String ORDS_APPEARANCE_COUNT_SECURE = "secure/appearance-count";
    public static final String ORDS_APPEARANCE_RESOURCE = "appearance-resource";
    public static final String ORDS_HEALTH = "health";
    public static final String ORDS_PING = "ping";

}
