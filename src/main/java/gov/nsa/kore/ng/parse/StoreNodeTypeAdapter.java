package gov.nsa.kore.ng.parse;

import gov.nsa.kore.ng.model.node.StoreNode;
import gov.nsa.kore.ng.util.xml.NodeBuilder;
import gov.nsa.kore.ng.util.xml.TypeAdapter;
import gov.nsa.kore.ng.util.xml.XmlException;
import gov.nsa.kore.ng.util.xml.XmlParser;
import org.w3c.dom.Element;

public class StoreNodeTypeAdapter implements TypeAdapter<StoreNode> {
    @Override
    public StoreNode deserialize(Element node, XmlParser parser) throws XmlException {
        if (!node.hasAttribute(AINodeTypeAdapter.ID)) throw new XmlException("\"store\"-nodes must specify an id");
        return new StoreNode(node.getTextContent().trim());
    }

    @Override
    public void serializeTo(Element node, StoreNode value, XmlParser parser, NodeBuilder builder) throws XmlException {
        node.setTextContent(value.getScriptSource());
    }

    @Override
    public String getNodeName() {
        return "store";
    }
}
