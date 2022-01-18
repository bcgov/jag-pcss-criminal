package ca.bc.gov.open.pcsscriminalapplication.service;

import ca.bc.gov.open.wsdl.pcss.one.GetClosedFileRequest;
import ca.bc.gov.open.wsdl.pcss.one.GetFileDetailCriminalRequest;
import ca.bc.gov.open.wsdl.pcss.one.SetFileNoteRequest;
import java.util.List;

public interface FileValidator {

    List<String> validateGetClosedFile(GetClosedFileRequest getClosedFileRequest);

    List<String> validateGetFileDetailCriminal(
            GetFileDetailCriminalRequest getFileDetailCriminalRequest);

    List<String> validateGetFileDetailCriminalSecure(
            ca.bc.gov.open.wsdl.pcss.secure.one.GetFileDetailCriminalRequest
                    getFileDetailCriminalRequest);

    List<String> validateSetFileNote(SetFileNoteRequest setFileNoteRequest);
}
