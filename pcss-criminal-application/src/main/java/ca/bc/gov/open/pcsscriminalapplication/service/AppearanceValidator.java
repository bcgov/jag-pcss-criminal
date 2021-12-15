package ca.bc.gov.open.pcsscriminalapplication.service;


import java.util.List;

public interface AppearanceValidator {

    List<String> validateGetAppearanceCriminal(ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalRequest getAppearanceCriminalRequest);

    List<String> validateGetAppearanceCriminalApprMethod(ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalApprMethodRequest getAppearanceCriminalApprMethodRequest);

    List<String> validateGetAppearanceCriminalApprMethodSecure(ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalApprMethodSecureRequest getAppearanceCriminalApprMethodSecureRequest);

    List<String> validateGetAppearanceCriminalCount(ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalCountRequest getAppearanceCriminalCountReques);

    List<String> validateGetAppearanceCriminalCountSecure(ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalCountSecureRequest getAppearanceCriminalCountSecureRequest);

    List<String> validateGetAppearanceCriminalResource(ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResourceRequest getAppearanceCriminalResourceRequest);

    List<String> validateGetAppearanceCriminalSecure(ca.bc.gov.open.wsdl.pcss.secure.one.GetAppearanceCriminalSecureRequest getAppearanceCriminalSecureRequest);

    List<String> validateSetAppearanceCriminal(ca.bc.gov.open.wsdl.pcss.one.SetAppearanceCriminalRequest setAppearanceCriminalRequest);

    List<String> validateSetAppearanceMethodCriminal( ca.bc.gov.open.wsdl.pcss.one.SetAppearanceMethodCriminalRequest setAppearanceMethodCriminalRequest);



}
