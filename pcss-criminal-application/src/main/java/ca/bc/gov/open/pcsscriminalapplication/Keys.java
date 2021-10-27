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

    //Endpoints
    public static final String ORDS_APPEARANCE = "appearance";

}
