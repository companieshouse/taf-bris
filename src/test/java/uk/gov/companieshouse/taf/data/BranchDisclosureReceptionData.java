package uk.gov.companieshouse.taf.data;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("BranchDisclosureReception")
public class BranchDisclosureReceptionData extends RequestData {
}
