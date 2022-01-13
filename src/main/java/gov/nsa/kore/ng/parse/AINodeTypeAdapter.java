package gov.nsa.kore.ng.parse;

import gov.nsa.kore.ng.model.node.base.AINode;
import gov.nsa.kore.ng.util.xml.NodeBuilder;
import gov.nsa.kore.ng.util.xml.TypeAdapter;
import gov.nsa.kore.ng.util.xml.XmlException;
import gov.nsa.kore.ng.util.xml.XmlParser;
import org.w3c.dom.Element;

public record AINodeTypeAdapter<TNode extends AINode, TBuilder extends AINode.Builder<TNode>, TAdapter extends TypeAdapter.Serializer<TNode> & TypeAdapter.Deserializer<TBuilder>>(TAdapter inner) implements TypeAdapter.Serializer<TNode>, TypeAdapter.Deserializer<TBuilder> {
    public static final String CHANCE = "chance";
    public static final String ICON = "icon";
    public static final String REGEX = "regex";
    public static final String ID = "id";
    public static final String CONTINUE = "continue";
    @Override
    public TBuilder deserialize(Element node, XmlParser parser) throws XmlException {
        TBuilder val = inner.deserialize(node, parser);
        if (node.hasAttribute(CHANCE)) val.setChance(Double.parseDouble(node.getAttribute(CHANCE)));
        if (node.hasAttribute(ICON)) val.setIcon(node.getAttribute(ICON));
        if (node.hasAttribute(REGEX)) val.setRegex(node.getAttribute(REGEX));
        if (node.hasAttribute(ID)) val.setId(node.getAttribute(ID));
        if (node.hasAttribute(CONTINUE)) val.setContinueNode(node.getAttribute(CONTINUE));
        return val;
    }

    @Override
    public void serializeTo(Element node, TNode value, XmlParser parser, NodeBuilder builder) throws XmlException {
        inner.serializeTo(node, value, parser, builder);
        if (value.getChance() != null) node.setAttribute(CHANCE, value.getChance().toString());
        String ic = value.getIcon(null);
        if (ic != null) node.setAttribute(ICON, ic);
        if (value.getRegex() != null) node.setAttribute(REGEX, value.getRegex());
        if (value.getId() != null) node.setAttribute(ID, value.getId());
        if (value.getContinueNode() != null) node.setAttribute(CONTINUE, value.getContinueNode().getId());
    }

    @Override
    public String getNodeName() {
        return inner.getNodeName();
    }
}
