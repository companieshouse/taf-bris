package uk.gov.companieshouse.taf.domain;

import eu.europa.ec.bris.v140.jaxb.br.aggregate.MessageObjectType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Used by JAXB to handle Validation errors that come back from BRIS.
 */
@XmlRootElement
public class ValidationError extends MessageObjectType{
}
