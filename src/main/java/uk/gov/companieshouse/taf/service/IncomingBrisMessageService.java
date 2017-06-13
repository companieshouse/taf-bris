package uk.gov.companieshouse.taf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.taf.domain.IncomingBrisMessage;
import uk.gov.companieshouse.taf.repository.IncomingBrisMessageRepository;

@Service
public class IncomingBrisMessageService {

    @Autowired
    private IncomingBrisMessageRepository incomingBrisMessageRepository;

    /**
     * Finds an Incoming BRIS message by correlation ID.
     */
    public IncomingBrisMessage findOneByCorrelationId(String correlationId) {
        return incomingBrisMessageRepository.findOneByCorrelationId(correlationId);
    }
}
