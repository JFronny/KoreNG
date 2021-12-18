package gov.nsa.kore.ng.parse;

import gov.nsa.kore.ng.model.AINode;
import gov.nsa.kore.ng.model.RandomSelectNode;
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

public class RandomSelectNodeTypeAdapter implements TypeAdapter<RandomSelectNode> {
    @Override
    public RandomSelectNode deserialize(Element node, XmlParser parser) throws XmlException {
        NodeList children = node.getChildNodes();
        Set<AINode> aiNodes = new LinkedHashSet<>();
        for (int i = 0; i < children.getLength(); i++) {
            Node childNode = children.item(i);
            if (childNode instanceof Element childElement) {
                aiNodes.add(parser.deserialize(childElement, AINode.class));
            }
            else {
                if (childNode instanceof Text text) {
                    if (RegexUtil.WHITESPACE.test(text.getWholeText()))
                        continue;
                }
                throw new XmlException("Expected DownNode to only contain other full nodes, not " + childNode + " (" + childNode.getClass() + ")");
            }
        }
        return new RandomSelectNode(aiNodes);
    }

    @Override
    public void serializeTo(Element node, RandomSelectNode value, XmlParser parser, NodeBuilder builder) throws XmlException {
        for (AINode child : value.getChildren()) {
            TypeAdapter<AINode> adapter = (TypeAdapter<AINode>) parser.getAdapter(child.getClass(), null);
            Element childNode = builder.createElement(adapter.getNodeName());
            adapter.serializeTo(childNode, child, parser, builder);
            node.appendChild(childNode);
        }
    }

    @Override
    public String getNodeName() {
        return "random";
    }
}
