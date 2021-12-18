package gov.nsa.kore.ng.util.xml;

public class XmlException extends Exception {
    public XmlException(String message) {
        super(message);
    }

    public XmlException(String message, Throwable cause) {
        super(message, cause);
    }
}
