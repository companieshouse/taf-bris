package uk.gov.companieshouse.taf.util;

import eu.europa.ec.bris.v140.jaxb.br.company.detail.BRCompanyDetailsRequest;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import uk.gov.companieshouse.taf.domain.IncomingBrisMessage;
import uk.gov.companieshouse.taf.stepsdef.RequestData;

/**
 * This class can be used to load the relevant test data for the test framework.
 */

@Component
public class TestDataHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestDataHelper.class);
    private static final String COMPANY_PROFILE = "company_profile";
    private static final String COMPANY_FILING_HISTORY = "company_filing_history";
    private static final String TEST_DATA_FOLDER = "testdata";

    @Value("${default.company.number}")
    private String defaultCompanyNumber;

    @Autowired
    @Qualifier("BrisMongoDbTemplate")
    private MongoTemplate brisMongoDbTemplate;

    @Autowired
    private JAXBContext jaxbContext;

    private static final String BRIS_INCOMING_COLLECTION = "incoming_messages";

    @Autowired
    @Qualifier("CompanyProfileMongoDbTemplate")
    private MongoTemplate companyProfileMongoTemplate;

    @Autowired
    @Qualifier("CompanyFilingHistoryMongoDbTemplate")
    private MongoTemplate companyFilingHistoryMongoTemplate;

    @Autowired
    private RequestData requestData;

    /**
     * Set up the test data for the specified company.
     *
     * @param companyNumbers The company numbers to load JSON files for
     */
    public void setUpTestData(List<String> companyNumbers) {
        if (CollectionUtils.isEmpty(companyNumbers)) {
            throw new RuntimeException("Cannot pass an empty list of companies.");
        }

        ClassLoader classLoader = getClass().getClassLoader();

        // Load data for each company required for the test
        for (String companyNumber : companyNumbers) {

            // Firstly locate the folder containing the test data for the relevant company..
            File file = new File(classLoader.getResource(TEST_DATA_FOLDER
                    + "//" + companyNumber).getFile());

            if (!file.isDirectory()) {
                throw new RuntimeException("Unable to locate the test folder for the company "
                        + companyNumber);
            }

            File[] testFiles = file.listFiles();

            // Ensure that there are test file for the selected company
            if (testFiles == null) {
                throw new RuntimeException("No test files located for company "
                        + companyNumber);
            }

            // Then iterate through each file and save it to MongoDB
            for (File testFile : testFiles) {
                if (StringUtils.contains(testFile.getName(),
                        COMPANY_FILING_HISTORY.replace("_", "-"))) {
                    // Then load a company filing history
                    companyFilingHistoryMongoTemplate.insert(getJsonFromFile(testFile),
                            COMPANY_FILING_HISTORY);
                } else if (StringUtils.contains(testFile.getName(),
                        COMPANY_PROFILE.replace("_", "-"))) {
                    // Otherwise add a company profile
                    companyProfileMongoTemplate.insert(getJsonFromFile(testFile),
                            COMPANY_PROFILE);
                }
            }
        }
        LOGGER.info("Test data entered for company/companies: {}", companyNumbers);
    }

    /**
     * Method to remove the company records from MongoDB.
     *
     * @param companyNumbers The company numbers to be used to remove records from MongoDB
     */
    public void tearDownTestData(List<String> companyNumbers) {

        if (CollectionUtils.isEmpty(companyNumbers)) {
            throw new RuntimeException("Cannot pass an empty list of companies.");
        }

        for (String companyNumber : companyNumbers) {
            companyProfileMongoTemplate.remove(new Query(Criteria.where("_id")
                    .is(companyNumber)), COMPANY_PROFILE);
            companyFilingHistoryMongoTemplate.remove(new Query(Criteria.where("company_number")
                            .is(companyNumber)),
                    COMPANY_FILING_HISTORY);
        }

        LOGGER.info("Test data removed for company/companies: {}", companyNumbers);
    }

    private static String getJsonFromFile(File jsonFile) {
        JSONParser parser = new JSONParser();
        Object obj;
        try {
            obj = parser.parse(new FileReader(jsonFile));
        } catch (IOException | ParseException ex) {
            throw new RuntimeException("Unable to read JSON file " + ex.getMessage());
        }

        JSONObject jsonObject = (JSONObject) obj;
        return jsonObject.toJSONString();
    }

    /**
     * Helper method to setup test data required to force the BRIS application to
     * reject a message with a duplicate corrlelation id and message id.
     */
    public void setupDataForDuplicateMessageTest() {

        // First add the duplicate request directly to the Bris Incoming collection so that it will
        // clash when we try to send another request with the same correlation and message id
        IncomingBrisMessage incomingBrisMessage = new IncomingBrisMessage();

        incomingBrisMessage.setCorrelationId(requestData.getCorrelationId());
        incomingBrisMessage.setMessageId(requestData.getMessageId());
        incomingBrisMessage.setMessageType("BRCompanyDetailsRequest");
        BRCompanyDetailsRequest request = RequestHelper.getCompanyDetailsRequest(
                requestData.getCorrelationId(),
                requestData.getMessageId(),
                defaultCompanyNumber,
                "EW",
                "UK");

        String xmlString;

        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            marshaller.marshal(request, sw);
            xmlString = sw.toString();

        } catch (JAXBException ex) {
            throw new RuntimeException("Unable to marshal duplicate message for the company "
                    + defaultCompanyNumber);
        }

        incomingBrisMessage.setMessage(xmlString);

        // Insert the message into the BRIS incoming collection
        brisMongoDbTemplate.insert(incomingBrisMessage,
                BRIS_INCOMING_COLLECTION);
    }
}
