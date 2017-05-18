package uk.gov.companieshouse.taf.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.taf.domain.OutgoingBRISMessage;
import uk.gov.companieshouse.taf.repository.OutgoingBRISMessageRepository;

@ComponentScan
@Service
public class OutgoingBRISMessageService {

    final Logger logger = LoggerFactory.getLogger(OutgoingBRISMessageService.class);

    @Autowired
    private OutgoingBRISMessageRepository outgoingBRISMessageRepository;

    public OutgoingBRISMessage findById(String id) {
        return outgoingBRISMessageRepository.findById(id);
    }

    public OutgoingBRISMessage save(OutgoingBRISMessage outgoingBRISMessage) {
        return outgoingBRISMessageRepository.save(outgoingBRISMessage);
    }

}
