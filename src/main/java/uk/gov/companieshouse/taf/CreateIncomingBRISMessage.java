package uk.gov.companieshouse.taf;

import eu.europa.ec.bris.v140.jaxb.br.company.detail.BRCompanyDetailsResponse;

/**
 * Created by jblandford on 04/05/2017.
 */
public interface CreateIncomingBRISMessage {

    void sendMessageToBRIS(String companyRef);
}
