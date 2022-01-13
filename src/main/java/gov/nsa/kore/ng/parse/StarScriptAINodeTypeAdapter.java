package gov.nsa.kore.ng.parse;

import gov.nsa.kore.ng.model.node.base.StarScriptAINode;
import gov.nsa.kore.ng.util.xml.NodeBuilder;
import gov.nsa.kore.ng.util.xml.TypeAdapter;
import gov.nsa.kore.ng.util.xml.XmlException;
import gov.nsa.kore.ng.util.xml.XmlParser;
import org.w3c.dom.Element;

public record StarScriptAINodeTypeAdapter<TNode extends StarScriptAINode, TBuilder extends StarScriptAINode.Builder<TNode>, TAdapter extends TypeAdapter.Serializer<TNode> & TypeAdapter.Deserializer<TBuilder>>(TAdapter inner) implements TypeAdapter.Serializer<TNode>, TypeAdapter.Deserializer<TBuilder> {
    @Override
    public String getNodeName() {
        return inner.getNodeName();
    }

    @Override
    public TBuilder deserialize(Element node, XmlParser parser) throws XmlException {
        TBuilder val = inner.deserialize(node, parser);
        val.setScriptSource(node.getTextContent().trim());
        return val;
    }

    @Override
    public void serializeTo(Element node, TNode value, XmlParser parser, NodeBuilder builder) throws XmlException {
        inner.serializeTo(node, value, parser, builder);
        node.setTextContent(value.getScriptSource());
    }
}
