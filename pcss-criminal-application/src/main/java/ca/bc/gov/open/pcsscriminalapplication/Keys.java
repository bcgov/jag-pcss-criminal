package ca.bc.gov.open.pcsscriminalapplication;

public class Keys {

    public static final String SOAP_NAMESPACE = "http://courts.gov.bc.ca/xml/ns/pcss/criminal/v1";

    //Methods
    public static final String SOAP_METHOD_APPEARANCE = "getAppearanceCriminal";

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

    //Endpoints
    public static final String ORDS_APPEARANCE = "appearance";

}
