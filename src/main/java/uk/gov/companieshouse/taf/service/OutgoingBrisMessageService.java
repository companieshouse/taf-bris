package uk.gov.companieshouse.taf.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.taf.domain.OutgoingBrisMessage;
import uk.gov.companieshouse.taf.repository.OutgoingBrisMessageRepository;

@Service
public class OutgoingBrisMessageService {

    final Logger logger = LoggerFactory.getLogger(OutgoingBrisMessageService.class);

    @Autowired
    private OutgoingBrisMessageRepository outgoingBrisMessageRepository;

    public OutgoingBrisMessage save(OutgoingBrisMessage outgoingBrisMessage) {
        logger.info("Outgoing message from test:- {}", outgoingBrisMessage);
        return outgoingBrisMessageRepository.save(outgoingBrisMessage);
    }

}
