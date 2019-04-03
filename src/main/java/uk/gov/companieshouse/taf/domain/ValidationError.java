package uk.gov.companieshouse.taf.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ValidationError {

    private BrisMessageHeaderType header;

    public BrisMessageHeaderType getHeader() {
        return header;
    }

    public void setHeader(BrisMessageHeaderType header) {
        this.header = header;
    }

}