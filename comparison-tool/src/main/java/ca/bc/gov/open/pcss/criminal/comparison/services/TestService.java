package ca.bc.gov.open.pcss.criminal.comparison.services;

import ca.bc.gov.open.pcss.criminal.comparison.config.DualProtocolSaajSoapMessageFactory;
import ca.bc.gov.open.pcss.criminal.comparison.config.WebServiceSenderWithAuth;
import ca.bc.gov.open.pcsscriminalcommon.utils.InstantSoapConverter;
import ca.bc.gov.open.wsdl.pcss.three.AvailablePersonType;
import ca.bc.gov.open.wsdl.pcss.three.OfficerSearchType;
import ca.bc.gov.open.wsdl.pcss.three.YesNoType;
import ca.bc.gov.open.wsdl.pcss.two.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Stream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Change;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.diff.changetype.container.ListChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

@Service
public class TestService {
    private final WebServiceTemplate webServiceTemplate = new WebServiceTemplate();

    private final WebServiceSenderWithAuth webServiceSenderWithAuth;

    private final Javers javers = JaversBuilder.javers().build();

    @Autowired
    public TestService(WebServiceSenderWithAuth webServiceSenderWithAuth) {
        this.webServiceSenderWithAuth = webServiceSenderWithAuth;
    }

    @Value("${host.api_host}")
    private String apiHost;

    @Value("${host.wm_host}")
    private String wmHost;

    @Value("${host.username}")
    private String username;

    @Value("${host.password}")
    private String password;

    private final String RAID = "83.0001";
    private final String partId = RAID;
    private final Instant dtm = Instant.now();

    private PrintWriter fileOutput;
    private static final String outputDir = "comparison-tool/results/";

    private int overallDiff = 0;

    public void runCompares() throws IOException {
        System.out.println("INFO: PCSS Criminal Diff testing started");

        getAppearanceCriminalCompare();
        getAppearanceMethodCriminalCompare();
        getAppearanceCriminalCountCompare();
        getAppearanceCriminalResourceCompare();
        getClosedFileCompare();
        getCrownAssignmentCompare();
        getFileDetailCriminalCompare();
        getPersonalAvailabilityCompare();
        getPersonnelAvailDetailCompare();
        getPersonnelSearchCompare();
        getSyncCriminalAppearanceCompare();
        getSyncCriminalHearingRestrictionCompare();
    }

    private void getSyncCriminalHearingRestrictionCompare() throws IOException {
        GetSyncCriminalHearingRestriction request = new GetSyncCriminalHearingRestriction();
        GetSyncCriminalHearingRestrictionRequest one =
                new GetSyncCriminalHearingRestrictionRequest();
        ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionRequest two =
                new ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalHearingRestrictionRequest();

        request.setGetSyncCriminalHearingRestrictionRequest(one);
        one.setGetSyncCriminalHearingRestrictionRequest(two);

        two.setRequestDtm(dtm);
        two.setRequestAgencyIdentifierId(RAID);
        two.setRequestPartId(partId);
        two.setProcessUpToDtm(ZonedDateTime.now().plusYears(20).toInstant());

        fileOutput =
                new PrintWriter(
                        outputDir + "getSyncCriminalHearingRestriction.txt",
                        StandardCharsets.UTF_8);

        System.out.println("\nINFO: getSyncCriminalHearingRestriction with 20 year future ");

        if (!compare(new GetSyncCriminalHearingRestrictionResponse(), request)) {
            fileOutput.println("\nINFO: getSyncCriminalHearingRestriction with 20 year future ");
        }
    }

    private void getSyncCriminalAppearanceCompare() throws IOException {
        int diffCounter = 0;
        GetSyncCriminalAppearance request = new GetSyncCriminalAppearance();
        GetSyncCriminalAppearanceRequest one = new GetSyncCriminalAppearanceRequest();
        ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceRequest two =
                new ca.bc.gov.open.wsdl.pcss.one.GetSyncCriminalAppearanceRequest();

        request.setGetSyncCriminalAppearanceRequest(one);
        one.setGetSyncCriminalAppearanceRequest(two);

        two.setRequestDtm(dtm);
        two.setRequestPartId(partId);
        two.setRequestAgencyIdentifierId(RAID);
        two.setProcessUpToDtm(ZonedDateTime.now().plusYears(20).toInstant());

        fileOutput =
                new PrintWriter(
                        outputDir + "getSyncCriminalAppearance.txt", StandardCharsets.UTF_8);

        System.out.println("\nINFO: getSyncCriminalAppearance with 20 year future ");

        if (!compare(new GetSyncCriminalAppearanceResponse(), request)) {
            fileOutput.println("\nINFO: getSyncCriminalAppearance with 20 year future ");
            ++diffCounter;
        }

        System.out.println(
                "########################################################\n"
                        + "INFO: getSyncCriminalAppearance Completed there are "
                        + diffCounter
                        + " diffs\n"
                        + "########################################################");

        fileOutput.println(
                "########################################################\n"
                        + "INFO: getSyncCriminalAppearance Completed there are "
                        + diffCounter
                        + " diffs\n"
                        + "########################################################");

        overallDiff += diffCounter;
        fileOutput.close();
    }

    private void getPersonnelSearchCompare() throws IOException {
        int diffCounter = 0;

        GetPersonnelSearch request = new GetPersonnelSearch();
        GetPersonnelSearchRequest one = new GetPersonnelSearchRequest();
        ca.bc.gov.open.wsdl.pcss.one.GetPersonnelSearchRequest two =
                new ca.bc.gov.open.wsdl.pcss.one.GetPersonnelSearchRequest();

        request.setGetPersonnelSearchRequest(one);
        one.setGetPersonnelSearchRequest(two);
        two.setRequestDtm(dtm);
        two.setRequestAgencyIdentifierId(RAID);
        two.setRequestPartId(partId);

        InputStream inputIds = getClass().getResourceAsStream("/getPersonnelSearchPartId.csv");
        assert inputIds != null;
        Scanner scanner = new Scanner(inputIds);

        InputStream inputNames = getClass().getResourceAsStream("/getPersonnelSearchNames.csv");
        assert inputNames != null;
        Scanner scanner2 = new Scanner(inputNames);

        List<String> names = new ArrayList<>();

        while (scanner2.hasNextLine()) {
            names.add(scanner2.nextLine());
        }

        fileOutput = new PrintWriter(outputDir + "getPersonnelSearch.txt", StandardCharsets.UTF_8);

        two.setSearchTypeCd(OfficerSearchType.NAME);
        Random rand = new Random();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            int idx = rand.nextInt(names.size() + 1);
            System.out.println(
                    "\nINFO: getPersonnelSearch with part id: "
                            + line
                            + " Name: "
                            + names.get(idx));
            two.setAgencyId(line);

            two.setSearchTxt(names.get(idx));
            if (!compare(new GetPersonnelSearchResponse(), request)) {
                fileOutput.println(
                        "\nINFO: getPersonnelSearch with part id: "
                                + line
                                + "name"
                                + names.get(idx));
                ++diffCounter;
            }
        }

        System.out.println(
                "########################################################\n"
                        + "INFO: getPersonnelSearch Completed there are "
                        + diffCounter
                        + " diffs\n"
                        + "########################################################");

        fileOutput.println(
                "########################################################\n"
                        + "INFO: getPersonnelSearch Completed there are "
                        + diffCounter
                        + " diffs\n"
                        + "########################################################");

        overallDiff += diffCounter;
        fileOutput.close();
    }

    private void getPersonnelAvailDetailCompare() throws IOException {
        GetPersonnelAvailDetail request = new GetPersonnelAvailDetail();
        GetPersonnelAvailDetailRequest one = new GetPersonnelAvailDetailRequest();
        ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailDetailRequest two =
                new ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailDetailRequest();
        request.setGetPersonnelAvailDetailRequest(one);
        one.setGetPersonnelAvailDetailRequest(two);

        two.setRequestDtm(dtm);
        two.setRequestPartId(partId);
        two.setRequestAgencyIdentifierId(RAID);
        two.setAvailabilityDt(Instant.now());

        two.setPersonTypeCd(AvailablePersonType.O);
        overallDiff += personTypeDetailCompare(request);

        two.setPersonTypeCd(AvailablePersonType.C);
        overallDiff += personTypeDetailCompare(request);

        two.setPersonTypeCd(AvailablePersonType.D);
        overallDiff += personTypeDetailCompare(request);

        fileOutput.close();
    }

    private void getPersonalAvailabilityCompare() throws IOException {
        int diffCounter = 0;

        GetPersonnelAvailability request = new GetPersonnelAvailability();
        GetPersonnelAvailabilityRequest one = new GetPersonnelAvailabilityRequest();
        ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailabilityRequest two =
                new ca.bc.gov.open.wsdl.pcss.one.GetPersonnelAvailabilityRequest();
        request.setGetPersonnelAvailabilityRequest(one);
        one.setGetPersonnelAvailabilityRequest(two);
        two.setRequestDtm(dtm);
        two.setRequestAgencyIdentifierId(RAID);
        two.setRequestPartId(partId);
        two.setFromDt(ZonedDateTime.now().minusYears(1).toInstant());
        two.setToDt(ZonedDateTime.now().plusYears(1).toInstant());

        two.setPersonTypeCd(AvailablePersonType.D);
        diffCounter += personTypeCompare(request);

        two.setPersonTypeCd(AvailablePersonType.C);
        diffCounter += personTypeCompare(request);

        two.setPersonTypeCd(AvailablePersonType.O);
        diffCounter += personTypeCompare(request);

        System.out.println(
                "########################################################\n"
                        + "INFO: getPersonnelAvailability Completed there are "
                        + diffCounter
                        + " diffs\n"
                        + "########################################################");

        fileOutput.println(
                "########################################################\n"
                        + "INFO: getPersonnelAvailability Completed there are "
                        + diffCounter
                        + " diffs\n"
                        + "########################################################");

        overallDiff += diffCounter;
        fileOutput.close();
    }

    private int personTypeCompare(GetPersonnelAvailability request) throws IOException {
        int diffCounter = 0;
        InputStream inputIds =
                getClass().getResourceAsStream("/getPersonnelAvailabilityPartId.csv");
        assert inputIds != null;
        Scanner scanner = new Scanner(inputIds);
        fileOutput =
                new PrintWriter(outputDir + "getPersonnelAvailDetail.txt", StandardCharsets.UTF_8);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println("\nINFO: getPersonnelAvailDetail with partId: " + line);
            request.getGetPersonnelAvailabilityRequest()
                    .getGetPersonnelAvailabilityRequest()
                    .setPartIdList(line);
            if (!compare(new GetPersonnelAvailabilityResponse(), request)) {
                fileOutput.println("\nINFO: getPersonnelAvailDetail with partId: " + line);
                ++diffCounter;
            }
        }
        return diffCounter;
    }

    private int personTypeDetailCompare(GetPersonnelAvailDetail request) throws IOException {
        int diffCounter = 0;
        InputStream inputIds =
                getClass().getResourceAsStream("/getPersonnelAvailabilityDetailPartId.csv");
        assert inputIds != null;
        Scanner scanner = new Scanner(inputIds);
        fileOutput =
                new PrintWriter(outputDir + "getPersonnelAvailability.txt", StandardCharsets.UTF_8);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println("\nINFO: getPersonnelAvailability with partId: " + line);
            request.getGetPersonnelAvailDetailRequest()
                    .getGetPersonnelAvailDetailRequest()
                    .setPaasPartId(line);
            if (!compare(new GetPersonnelAvailDetailResponse(), request)) {
                fileOutput.println("\nINFO: getPersonnelAvailability with partId: " + line);
                ++diffCounter;
            }
        }
        return diffCounter;
    }

    private void getAppearanceMethodCriminalCompare() throws IOException {
        int diffCounter = 0;

        GetAppearanceCriminalApprMethod request = new GetAppearanceCriminalApprMethod();
        GetAppearanceCriminalApprMethodRequest one = new GetAppearanceCriminalApprMethodRequest();
        ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalApprMethodRequest two =
                new ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalApprMethodRequest();

        request.setGetAppearanceCriminalApprMethodRequest(one);
        one.setGetAppearanceCriminalApprMethodRequest(two);
        two.setRequestDtm(dtm);
        two.setRequestAgencyIdentifierId(RAID);
        two.setRequestPartId(partId);

        InputStream inputIds =
                getClass().getResourceAsStream("/getAppearanceCriminalApprMethodAppearanceId.csv");
        assert inputIds != null;
        Scanner scanner = new Scanner(inputIds);
        fileOutput =
                new PrintWriter(
                        outputDir + "getAppearanceCriminalApprMethod.txt", StandardCharsets.UTF_8);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println("\nINFO: CriminalAppearanceMethod with AppearanceId: " + line);
            two.setAppearanceId(line);
            if (!compare(new GetAppearanceCriminalApprMethodResponse(), request)) {
                fileOutput.println("\nINFO: CriminalAppearanceMethod with AppearanceId: " + line);
                ++diffCounter;
            }
        }

        System.out.println(
                "########################################################\n"
                        + "INFO: CriminalAppearanceMethod Completed there are "
                        + diffCounter
                        + " diffs\n"
                        + "########################################################");

        fileOutput.println(
                "########################################################\n"
                        + "INFO: CriminalAppearanceMethod Completed there are "
                        + diffCounter
                        + " diffs\n"
                        + "########################################################");

        overallDiff += diffCounter;
        fileOutput.close();
    }

    private void getAppearanceCriminalCountCompare() throws IOException {
        int diffCounter = 0;

        GetAppearanceCriminalCount request = new GetAppearanceCriminalCount();
        GetAppearanceCriminalCountRequest one = new GetAppearanceCriminalCountRequest();
        ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalCountRequest two =
                new ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalCountRequest();

        request.setGetAppearanceCriminalCountRequest(one);
        one.setGetAppearanceCriminalCountRequest(two);
        two.setRequestDtm(dtm);
        two.setRequestAgencyIdentifierId(RAID);
        two.setRequestPartId(partId);

        InputStream inputIds =
                getClass().getResourceAsStream("/getAppearanceCriminalCountAppearanceId.csv");
        assert inputIds != null;
        Scanner scanner = new Scanner(inputIds);
        fileOutput =
                new PrintWriter(
                        outputDir + "getAppearanceCriminalCount.txt", StandardCharsets.UTF_8);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println("\nINFO: getAppearanceCriminalCount with AppearanceId: " + line);
            two.setAppearanceId(line);
            if (!compare(new GetAppearanceCriminalCountResponse(), request)) {
                fileOutput.println("\nINFO: getAppearanceCriminalCount with AppearanceId: " + line);
                ++diffCounter;
            }
        }

        System.out.println(
                "########################################################\n"
                        + "INFO: getAppearanceCriminalCount Completed there are "
                        + diffCounter
                        + " diffs\n"
                        + "########################################################");

        fileOutput.println(
                "########################################################\n"
                        + "INFO: getAppearanceCriminalCount Completed there are "
                        + diffCounter
                        + " diffs\n"
                        + "########################################################");

        overallDiff += diffCounter;
        fileOutput.close();
    }

    private void getClosedFileCompare() throws IOException {
        int diffCounter = 0;
        GetClosedFile request = new GetClosedFile();
        GetClosedFileRequest one = new GetClosedFileRequest();
        ca.bc.gov.open.wsdl.pcss.one.GetClosedFileRequest two =
                new ca.bc.gov.open.wsdl.pcss.one.GetClosedFileRequest();

        request.setGetClosedFileRequest(one);
        one.setGetClosedFileRequest(two);
        two.setRequestDtm(dtm);
        two.setRequestAgencyIdentifierId(RAID);
        two.setRequestPartId(partId);

        two.setFromApprDt(ZonedDateTime.now().minusYears(20).toInstant());
        two.setToApprDt(ZonedDateTime.now().plusYears(20).toInstant());

        InputStream inputIds = getClass().getResourceAsStream("/getClosedFileCtrmAgenId.csv");
        assert inputIds != null;
        Scanner scanner = new Scanner(inputIds);
        fileOutput = new PrintWriter(outputDir + "getClosedFile.txt", StandardCharsets.UTF_8);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println("\nINFO: getClosedFile with AgencyId: " + line);
            two.setAgencyId(line);
            if (!compare(new GetClosedFileResponse(), request)) {
                fileOutput.println("\nINFO: getClosedFile with AgencyId: " + line);
                ++diffCounter;
            }
        }

        System.out.println(
                "########################################################\n"
                        + "INFO: getClosedFile Completed there are "
                        + diffCounter
                        + " diffs\n"
                        + "########################################################");

        fileOutput.println(
                "########################################################\n"
                        + "INFO: getClosedFile Completed there are "
                        + diffCounter
                        + " diffs\n"
                        + "########################################################");

        overallDiff += diffCounter;
        fileOutput.close();
    }

    private void getFileDetailCriminalCompare() throws IOException {
        int diffCounter = 0;
        GetFileDetailCriminal request = new GetFileDetailCriminal();
        GetFileDetailCriminalRequest one = new GetFileDetailCriminalRequest();
        ca.bc.gov.open.wsdl.pcss.one.GetFileDetailCriminalRequest two =
                new ca.bc.gov.open.wsdl.pcss.one.GetFileDetailCriminalRequest();

        request.setGetFileDetailCriminalRequest(one);
        one.setGetFileDetailCriminalRequest(two);
        two.setRequestDtm(dtm);
        two.setRequestAgencyIdentifierId(RAID);
        two.setRequestPartId(partId);

        InputStream inputIds = getClass().getResourceAsStream("/getFileDetailCriminalMdoc.csv");
        assert inputIds != null;
        Scanner scanner = new Scanner(inputIds);
        fileOutput =
                new PrintWriter(outputDir + " getFileDetailCriminal.txt", StandardCharsets.UTF_8);
        two.setApplicationCd("PCSS");
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println("\nINFO: getFileDetailCriminal with PCSS Mdoc: " + line);
            two.setJustinNo(line);
            if (!compare(new GetClosedFileResponse(), request)) {
                fileOutput.println("\nINFO: getFileDetailCriminal with PCSS Mdoc: " + line);
                ++diffCounter;
            }
        }

        inputIds = getClass().getResourceAsStream("/getFileDetailCriminalMdoc.csv");
        assert inputIds != null;
        scanner = new Scanner(inputIds);
        two.setApplicationCd("CCSS");
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println("\nINFO: getFileDetailCriminal CCSS with Mdoc: " + line);
            two.setJustinNo(line);
            if (!compare(new GetClosedFileResponse(), request)) {
                fileOutput.println("\nINFO: getFileDetailCriminal CCSS with Mdoc: " + line);
                ++diffCounter;
            }
        }

        System.out.println(
                "########################################################\n"
                        + "INFO: getFileDetailCriminal Completed there are "
                        + diffCounter
                        + " diffs\n"
                        + "########################################################");

        fileOutput.println(
                "########################################################\n"
                        + "INFO: getFileDetailCriminal Completed there are "
                        + diffCounter
                        + " diffs\n"
                        + "########################################################");

        overallDiff += diffCounter;
        fileOutput.close();
    }

    private void getCrownAssignmentCompare() throws IOException {
        int diffCounter = 0;
        GetCrownAssignment request = new GetCrownAssignment();
        GetCrownAssignmentRequest one = new GetCrownAssignmentRequest();
        ca.bc.gov.open.wsdl.pcss.one.GetCrownAssignmentRequest two =
                new ca.bc.gov.open.wsdl.pcss.one.GetCrownAssignmentRequest();
        request.setGetCrownAssignmentRequest(one);
        one.setGetCrownAssignmentRequest(two);

        two.setRequestDtm(dtm);
        two.setRequestAgencyIdentifierId(RAID);
        two.setRequestPartId(partId);

        InputStream inputIds = getClass().getResourceAsStream("/getCrownAssignmentJustinNo.csv");
        assert inputIds != null;
        Scanner scanner = new Scanner(inputIds);
        fileOutput = new PrintWriter(outputDir + "getCrownAssignment.txt", StandardCharsets.UTF_8);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println("\nINFO: getCrownAssignment with JustinNo: " + line);
            two.setJustinNo(line);

            if (!compare(new GetClosedFileResponse(), request)) {
                fileOutput.println("\nINFO: getCrownAssignment with JustinNo: " + line);
                ++diffCounter;
            }
        }

        two.setSinceDt(ZonedDateTime.now().minusYears(20).toInstant());
        two.setJustinNo(null);

        if (!compare(new GetClosedFileResponse(), request)) {
            fileOutput.println(
                    "\nINFO: getCrownAssignment with Since date: "
                            + InstantSoapConverter.print(
                                    ZonedDateTime.now().minusYears(20).toInstant()));
            ++diffCounter;
        }

        System.out.println(
                "########################################################\n"
                        + "INFO: getCrownAssignment Completed there are "
                        + diffCounter
                        + " diffs\n"
                        + "########################################################");

        fileOutput.println(
                "########################################################\n"
                        + "INFO: getCrownAssignment Completed there are "
                        + diffCounter
                        + " diffs\n"
                        + "########################################################");

        overallDiff += diffCounter;
        fileOutput.close();
    }

    private void getAppearanceCriminalResourceCompare() throws IOException {
        int diffCounter = 0;

        GetAppearanceCriminalResource request = new GetAppearanceCriminalResource();
        GetAppearanceCriminalResourceRequest one = new GetAppearanceCriminalResourceRequest();
        ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResourceRequest two =
                new ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalResourceRequest();

        request.setGetAppearanceCriminalResourceRequest(one);
        one.setGetAppearanceCriminalResourceRequest(two);
        two.setRequestDtm(dtm);
        two.setRequestAgencyIdentifierId(RAID);
        two.setRequestPartId(partId);

        InputStream inputIds =
                getClass().getResourceAsStream("/getAppearanceCriminalResourceAppearanceId.csv");
        assert inputIds != null;
        Scanner scanner = new Scanner(inputIds);
        fileOutput =
                new PrintWriter(
                        outputDir + "AppearanceCriminalResource.txt", StandardCharsets.UTF_8);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println("\nINFO: AppearanceCriminalResource with AppearanceId: " + line);
            two.setAppearanceId(line);
            if (!compare(new GetAppearanceCriminalCountResponse(), request)) {
                fileOutput.println("\nINFO: AppearanceCriminalResource with AppearanceId: " + line);
                ++diffCounter;
            }
        }

        System.out.println(
                "########################################################\n"
                        + "INFO: AppearanceCriminalResource Completed there are "
                        + diffCounter
                        + " diffs\n"
                        + "########################################################");

        fileOutput.println(
                "########################################################\n"
                        + "INFO: AppearanceCriminalResource Completed there are "
                        + diffCounter
                        + " diffs\n"
                        + "########################################################");

        overallDiff += diffCounter;
        fileOutput.close();
    }

    private void getAppearanceCriminalCompare() throws IOException {
        int diffCounter = 0;

        GetAppearanceCriminal request = new GetAppearanceCriminal();
        GetAppearanceCriminalRequest one = new GetAppearanceCriminalRequest();
        ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalRequest two =
                new ca.bc.gov.open.wsdl.pcss.one.GetAppearanceCriminalRequest();
        request.setGetAppearanceCriminalRequest(one);
        one.setGetAppearanceCriminalRequest(two);

        two.setRequestDtm(Instant.now());
        two.setRequestAgencyIdentifierId(RAID);
        two.setRequestPartId(partId);

        InputStream inputIds = getClass().getResourceAsStream("/getCriminalAppearanceParams.csv");
        assert inputIds != null;
        Scanner scanner = new Scanner(inputIds);
        fileOutput =
                new PrintWriter(outputDir + "GetCriminalAppearance.txt", StandardCharsets.UTF_8);

        for (int idx = 0; scanner.hasNextLine(); idx++) {
            String[] line = scanner.nextLine().split(",");

            switch (idx % 4) {
                case 0:
                    two.setFutureYN(YesNoType.Y);
                    two.setHistoryYN(YesNoType.Y);
                    break;
                case 1:
                    two.setFutureYN(YesNoType.N);
                    two.setHistoryYN(YesNoType.Y);
                    break;
                case 2:
                    two.setFutureYN(YesNoType.Y);
                    two.setHistoryYN(YesNoType.N);
                    break;
                case 3:
                    two.setFutureYN(YesNoType.N);
                    two.setHistoryYN(YesNoType.N);
                    break;
            }

            System.out.println(
                    "\nINFO: GetCriminalAppearance with AppearanceId: "
                            + line[0]
                            + " Justin number "
                            + line[1]
                            + " Future "
                            + two.getFutureYN().toString()
                            + " History "
                            + two.getHistoryYN().toString());

            two.setAppearanceId(line[0]);
            two.setJustinNo(line[1]);

            if (!compare(new GetAppearanceCriminalResponse(), request)) {
                fileOutput.println(
                        "\nINFO: GetCriminalAppearance with AppearanceId: "
                                + line[0]
                                + " Justin number "
                                + line[1]
                                + " Future "
                                + two.getFutureYN().toString()
                                + " History "
                                + two.getHistoryYN().toString());
                ++diffCounter;
            }
        }

        two.setFutureYN(YesNoType.Y);
        two.setHistoryYN(YesNoType.Y);
        two.setAppearanceId(null);
        two.setJustinNo("35674");
        if (!compare(new GetAppearanceCriminalResponse(), request)) {
            fileOutput.println(
                    "\nINFO: GetCriminalAppearance with AppearanceId: "
                            + two.getAppearanceId()
                            + " Justin number "
                            + two.getJustinNo()
                            + " Future "
                            + two.getFutureYN().toString()
                            + " History "
                            + two.getHistoryYN().toString());
            ++diffCounter;
        }
        System.out.println(
                "########################################################\n"
                        + "INFO: GetCriminalAppearance Completed there are "
                        + diffCounter
                        + " diffs\n"
                        + "########################################################");

        fileOutput.println(
                "########################################################\n"
                        + "INFO: GetCriminalAppearance Completed there are "
                        + diffCounter
                        + " diffs\n"
                        + "########################################################");

        overallDiff += diffCounter;
        fileOutput.close();
    }

    public <T, G> boolean compare(T response, G request) {

        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();

        DualProtocolSaajSoapMessageFactory saajSoapMessageFactory =
                new DualProtocolSaajSoapMessageFactory();
        saajSoapMessageFactory.setSoapVersion(SoapVersion.SOAP_12);
        saajSoapMessageFactory.afterPropertiesSet();

        HttpComponentsMessageSender httpComponentsMessageSender = new HttpComponentsMessageSender();
        httpComponentsMessageSender.setCredentials(
                new UsernamePasswordCredentials(username, password));

        webServiceTemplate.setMessageSender(webServiceSenderWithAuth);
        webServiceTemplate.setMessageFactory(saajSoapMessageFactory);
        jaxb2Marshaller.setContextPaths(
                "ca.bc.gov.open.wsdl.pcss.one",
                "ca.bc.gov.open.wsdl.pcss.two",
                "ca.bc.gov.open.wsdl.pcss.three");
        webServiceTemplate.setMarshaller(jaxb2Marshaller);
        webServiceTemplate.setUnmarshaller(jaxb2Marshaller);
        webServiceTemplate.afterPropertiesSet();

        T resultObjectWM = null;
        T resultObjectAPI = null;

        try {
            resultObjectWM = (T) webServiceTemplate.marshalSendAndReceive(wmHost, request);
            resultObjectAPI = (T) webServiceTemplate.marshalSendAndReceive(apiHost, request);
            Thread.sleep(5000);
        } catch (Exception e) {
            System.out.println("ERROR: Failed to send request... " + e);
            fileOutput.println("ERROR: Failed to send request... " + e);
        }

        Diff diff = javers.compare(resultObjectAPI, resultObjectWM);

        String responseClassName = response.getClass().getName();
        if (diff.hasChanges()) {
            printDiff(diff);
            return false;
        } else {
            if (resultObjectAPI == null && resultObjectWM == null)
                System.out.println(
                        "WARN: "
                                + responseClassName.substring(
                                        responseClassName.lastIndexOf('.') + 1)
                                + ": NULL responses");
            else
                System.out.println(
                        "INFO: "
                                + responseClassName.substring(
                                        responseClassName.lastIndexOf('.') + 1)
                                + ": No Diff Detected");
            return true;
        }
    }

    private <T> String printSoap(T obj) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = context.createMarshaller();

        StringWriter sw = new StringWriter();

        marshaller.marshal(obj, sw);
        return sw.toString();
    }

    private void printDiff(Diff diff) {

        List<Change> actualChanges = new ArrayList<>(diff.getChanges());

        actualChanges.removeIf(
                c -> {
                    if (c instanceof ValueChange) {
                        return ((ValueChange) c).getLeft() == null
                                && ((ValueChange) c).getRight().toString().isBlank();
                    }

                    return false;
                });

        int diffSize = actualChanges.size();

        if (diffSize == 0) {
            System.out.println("Only null and blank changes detected");
            return;
        }

        String[] header = new String[] {"Property", "API Response", "WM Response"};
        String[][] table = new String[diffSize + 1][3];
        table[0] = header;

        for (int i = 0; i < diffSize; ++i) {
            Change ch = actualChanges.get(i);

            if (ch instanceof ListChange) {
                String apiVal =
                        ((ListChange) ch).getLeft() == null
                                ? "null"
                                : ((ListChange) ch).getLeft().toString();
                String wmVal =
                        ((ListChange) ch).getRight() == null
                                ? "null"
                                : ((ListChange) ch).getRight().toString();
                table[i + 1][0] = ((ListChange) ch).getPropertyNameWithPath();
                table[i + 1][1] = apiVal;
                table[i + 1][2] = wmVal;
            } else if (ch instanceof ValueChange) {
                String apiVal =
                        ((ValueChange) ch).getLeft() == null
                                ? "null"
                                : ((ValueChange) ch).getLeft().toString();
                String wmVal =
                        ((ValueChange) ch).getRight() == null
                                ? "null"
                                : ((ValueChange) ch).getRight().toString();
                table[i + 1][0] = ((ValueChange) ch).getPropertyNameWithPath();
                table[i + 1][1] = apiVal;
                table[i + 1][2] = wmVal;
            }
        }

        boolean leftJustifiedRows = false;
        int totalColumnLength = 10;
        /*
         * Calculate appropriate Length of each column by looking at width of data in
         * each column.
         *
         * Map columnLengths is <column_number, column_length>
         */
        Map<Integer, Integer> columnLengths = new HashMap<>();
        Arrays.stream(table)
                .forEach(
                        a ->
                                Stream.iterate(0, (i -> i < a.length), (i -> ++i))
                                        .forEach(
                                                i -> {
                                                    if (columnLengths.get(i) == null) {
                                                        columnLengths.put(i, 0);
                                                    }
                                                    if (columnLengths.get(i) < a[i].length()) {
                                                        columnLengths.put(i, a[i].length());
                                                    }
                                                }));

        for (Map.Entry<Integer, Integer> e : columnLengths.entrySet()) {
            totalColumnLength += e.getValue();
        }
        fileOutput.println("=".repeat(totalColumnLength));
        System.out.println("=".repeat(totalColumnLength));

        final StringBuilder formatString = new StringBuilder("");
        String flag = leftJustifiedRows ? "-" : "";
        columnLengths.entrySet().stream()
                .forEach(e -> formatString.append("| %" + flag + e.getValue() + "s "));
        formatString.append("|\n");

        Stream.iterate(0, (i -> i < table.length), (i -> ++i))
                .forEach(
                        a -> {
                            fileOutput.printf(formatString.toString(), table[a]);
                            System.out.printf(formatString.toString(), table[a]);
                        });

        fileOutput.println("=".repeat(totalColumnLength));
        System.out.println("=".repeat(totalColumnLength));
    }
}
