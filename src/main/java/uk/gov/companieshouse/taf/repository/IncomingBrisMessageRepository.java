package uk.gov.companieshouse.taf.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import uk.gov.companieshouse.taf.domain.IncomingBrisMessage;

/**
 * Spring Data MongoDB repository for the IncomingBrisMessage entity.
 */
@Repository
public interface IncomingBrisMessageRepository extends
        MongoRepository<IncomingBrisMessage, String> {

    @Query("{ 'correlationId' : ?0 }")
    IncomingBrisMessage findOneByCorrelationId(String correlationId);
}
