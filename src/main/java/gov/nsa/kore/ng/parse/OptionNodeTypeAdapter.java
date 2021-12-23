package gov.nsa.kore.ng.parse;

import gov.nsa.kore.ng.model.node.OptionNode;
import gov.nsa.kore.ng.util.xml.NodeBuilder;
import gov.nsa.kore.ng.util.xml.TypeAdapter;
import gov.nsa.kore.ng.util.xml.XmlException;
import gov.nsa.kore.ng.util.xml.XmlParser;
import org.w3c.dom.Element;

public class OptionNodeTypeAdapter implements TypeAdapter<OptionNode> {
    @Override
    public OptionNode deserialize(Element node, XmlParser parser) throws XmlException {
        return new OptionNode(node.getTextContent().trim());
    }

    @Override
    public void serializeTo(Element node, OptionNode value, XmlParser parser, NodeBuilder builder) throws XmlException {
        node.setTextContent(value.getScriptSource());
    }

    @Override
    public String getNodeName() {
        return "option";
    }
}
