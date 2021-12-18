package gov.nsa.kore.ng.util.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class XmlParser {
    private final Map<Class<?>, TypeAdapter<?>> typeAdapters = new HashMap<>();
    private final Set<TypeAdapterFactory> adapterFactories = new HashSet<>();
    private final DocumentBuilder documentBuilder;
    private final Transformer transformer;
    public XmlParser() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            documentBuilder = dbf.newDocumentBuilder();
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        } catch (ParserConfigurationException | TransformerConfigurationException e) {
            throw new RuntimeException("Could not configure XmlParser", e);
        }
    }

    public <T> XmlParser register(Class<T> klazz, TypeAdapter<T> adapter) {
        typeAdapters.put(klazz, adapter);
        return this;
    }

    public <T> XmlParser register(TypeAdapterFactory adapter) {
        adapterFactories.add(adapter);
        return this;
    }

    public <T> T deserialize(String document, Class<T> klazz) throws XmlException {
        try {
            Document doc = documentBuilder.parse(document);
            doc.getDocumentElement().normalize();
            return deserialize(doc.getDocumentElement(), klazz);
        } catch (SAXException | IOException e) {
            throw new XmlException("Could not deserialize document", e);
        }
    }

    public <T> T deserialize(InputStream document, Class<T> klazz) throws XmlException {
        try {
            Document doc = documentBuilder.parse(document);
            doc.getDocumentElement().normalize();
            return deserialize(doc.getDocumentElement(), klazz);
        } catch (SAXException | IOException e) {
            throw new XmlException("Could not deserialize document", e);
        }
    }

    public <T> T deserialize(Element node, Class<T> klazz) throws XmlException {
        return getAdapter(klazz, node.getNodeName()).deserialize(node, this);
    }

    public <T> String serialize(T object) throws XmlException {
        Document doc = documentBuilder.newDocument();
        TypeAdapter<T> adapter = (TypeAdapter<T>)getAdapter(object.getClass(), null);
        Element node = doc.createElement(adapter.getNodeName());
        adapter.serializeTo(node, object, this, NodeBuilder.create(doc));
        doc.appendChild(node);
        try (Writer out = new StringWriter()) {
            transformer.transform(new DOMSource(doc), new StreamResult(out));
            return out.toString();
        } catch (IOException | TransformerException e) {
            throw new XmlException("Could not write", e);
        }
    }

    public <T> TypeAdapter<T> getAdapter(Class<T> klazz, String nodeName) throws XmlException {
        TypeAdapter<T> adapter = null;
        for (Map.Entry<Class<?>, TypeAdapter<?>> entry : typeAdapters.entrySet()) {
            if (klazz.isAssignableFrom(entry.getKey()) && (nodeName == null || entry.getValue().getNodeName().equals(nodeName)))
                adapter = (TypeAdapter<T>) entry.getValue();
        }
        if (adapter == null) {
            for (TypeAdapterFactory factory : adapterFactories) {
                if (factory.appliesTo(klazz)) {
                    adapter = factory.build(klazz);
                    typeAdapters.put(klazz, adapter);
                    break;
                }
            }
            if (adapter == null)
                throw new XmlException("No type adapter found for " + klazz.getName());
        }
        return adapter;
    }
}
