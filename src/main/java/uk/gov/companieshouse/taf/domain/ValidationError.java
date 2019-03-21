package uk.gov.companieshouse.taf.domain;


import eu.europa.ec.bris.jaxb.br.components.aggregate.v1_4.MessageObjectType;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Used by JAXB to handle Validation errors that come back from BRIS.
 */
@XmlRootElement
public class ValidationError extends MessageObjectType {
}
