package gov.nsa.kore.ng.util.xml;

import org.w3c.dom.Element;

public interface TypeAdapter<T> {
    T deserialize(Element node, XmlParser parser) throws XmlException;
    void serializeTo(Element node, T value, XmlParser parser, NodeBuilder builder) throws XmlException;
    String getNodeName();
}
