package gov.nsa.kore.ng.util.xml;

import org.w3c.dom.Element;

public interface TypeAdapter {
    String getNodeName();

    interface Deserializer<T> extends TypeAdapter {
        T deserialize(Element node, XmlParser parser) throws XmlException;
    }

    interface Serializer<T> extends TypeAdapter {
        void serializeTo(Element node, T value, XmlParser parser, NodeBuilder builder) throws XmlException;
    }
}
