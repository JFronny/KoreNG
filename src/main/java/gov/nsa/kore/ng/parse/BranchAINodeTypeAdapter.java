package gov.nsa.kore.ng.parse;

import gov.nsa.kore.ng.model.node.base.AINode;
import gov.nsa.kore.ng.model.node.base.BranchAINode;
import gov.nsa.kore.ng.util.RegexUtil;
import gov.nsa.kore.ng.util.xml.NodeBuilder;
import gov.nsa.kore.ng.util.xml.TypeAdapter;
import gov.nsa.kore.ng.util.xml.XmlException;
import gov.nsa.kore.ng.util.xml.XmlParser;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.util.LinkedHashSet;
import java.util.Set;

public record BranchAINodeTypeAdapter<TNode extends BranchAINode, TBuilder extends BranchAINode.Builder<TNode>, TAdapter extends TypeAdapter.Serializer<TNode> & TypeAdapter.Deserializer<TBuilder>>(TAdapter inner) implements TypeAdapter.Serializer<TNode>, TypeAdapter.Deserializer<TBuilder> {
    @Override
    public String getNodeName() {
        return inner.getNodeName();
    }

    @Override
    public TBuilder deserialize(Element node, XmlParser parser) throws XmlException {
        TBuilder val = inner.deserialize(node, parser);
        NodeList children = node.getChildNodes();
        Set<AINode.Builder<?>> aiNodes = new LinkedHashSet<>();
        for (int i = 0; i < children.getLength(); i++) {
            Node childNode = children.item(i);
            if (childNode instanceof Element childElement) {
                aiNodes.add(parser.deserialize(childElement, AINode.Builder.class));
            }
            else {
                if (childNode instanceof Text text && RegexUtil.WHITESPACE.test(text.getWholeText()))
                    continue;
                throw new XmlException("Expected node to only contain other full nodes, not " + childNode + " (" + childNode.getClass() + ")");
            }
        }
        val.setNodes(aiNodes);
        return val;
    }

    @Override
    public void serializeTo(Element node, TNode value, XmlParser parser, NodeBuilder builder) throws XmlException {
        inner.serializeTo(node, value, parser, builder);
        for (AINode child : value.getChildren()) {
            Serializer<AINode> adapter = (Serializer<AINode>) parser.getAdapter(child.getClass(), Serializer.class, null);
            Element childNode = builder.createElement(adapter.getNodeName());
            adapter.serializeTo(childNode, child, parser, builder);
            node.appendChild(childNode);
        }
    }
}
