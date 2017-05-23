package uk.gov.companieshouse.taf.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.gov.companieshouse.taf.domain.OutgoingBrisMessage;

/**
 * Interface for outgoing BRIS message repository.
 */
public interface OutgoingBrisMessageRepository extends
        MongoRepository<OutgoingBrisMessage, String> {

    @SuppressWarnings("unchecked")
    OutgoingBrisMessage save(OutgoingBrisMessage saved);

    /**
     * Find OutgoingBrisMessage by id.
     *
     * @param id the message id
     * @return OutgoingBrisMessage
     */
    OutgoingBrisMessage findById(String id);


}