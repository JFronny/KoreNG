package gov.nsa.kore.ng.parse;

import gov.nsa.kore.ng.model.AINode;
import gov.nsa.kore.ng.util.xml.NodeBuilder;
import gov.nsa.kore.ng.util.xml.TypeAdapter;
import gov.nsa.kore.ng.util.xml.XmlException;
import gov.nsa.kore.ng.util.xml.XmlParser;
import org.w3c.dom.Element;

public record AINodeTypeAdapter<T extends AINode>(TypeAdapter<T> inner) implements TypeAdapter<T> {
    private static final String CHANCE = "chance";
    private static final String ICON = "icon";
    private static final String REGEX = "regex";
    private static final String ID = "id";
    private static final String CONTINUE = "continue";
    @Override
    public T deserialize(Element node, XmlParser parser) throws XmlException {
        T val = inner.deserialize(node, parser);
        if (node.hasAttribute(CHANCE)) val.setChance(Double.parseDouble(node.getAttribute(CHANCE)));
        if (node.hasAttribute(ICON)) val.setIcon(node.getAttribute(ICON));
        if (node.hasAttribute(REGEX)) val.setRegex(node.getAttribute(REGEX));
        if (node.hasAttribute(ID)) val.setId(node.getAttribute(ID));
        if (node.hasAttribute(CONTINUE)) val.setContinueNode(node.getAttribute(CONTINUE));
        return val;
    }

    @Override
    public void serializeTo(Element node, T value, XmlParser parser, NodeBuilder builder) throws XmlException {
        inner.serializeTo(node, value, parser, builder);
        if (value.getChance() != null) node.setAttribute(CHANCE, value.getChance().toString());
        String ic = value.getIcon(null);
        if (ic != null) node.setAttribute(ICON, ic);
        if (value.getRegex() != null) node.setAttribute(REGEX, value.getRegex());
        if (value.getId() != null) node.setAttribute(ID, value.getId());
        if (value.getContinueNode() != null) node.setAttribute(CONTINUE, value.getContinueNode());
    }

    @Override
    public String getNodeName() {
        return inner.getNodeName();
    }
}
