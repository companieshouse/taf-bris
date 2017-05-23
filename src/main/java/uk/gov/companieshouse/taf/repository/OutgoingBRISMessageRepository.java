package uk.gov.companieshouse.taf.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.gov.companieshouse.taf.domain.OutgoingBRISMessage;

/**
 * Interface for outgoing BRIS message repository.
 */
public interface OutgoingBRISMessageRepository extends
        MongoRepository<OutgoingBRISMessage, String> {

    @SuppressWarnings("unchecked")
    OutgoingBRISMessage save(OutgoingBRISMessage saved);

    /**
     * Find OutgoingBRISMessage by id.
     *
     * @param id the message id
     * @return OutgoingBRISMessage
     */
    OutgoingBRISMessage findById(String id);


}