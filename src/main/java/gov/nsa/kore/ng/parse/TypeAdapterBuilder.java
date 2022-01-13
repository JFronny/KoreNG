package gov.nsa.kore.ng.parse;

import gov.nsa.kore.ng.model.node.base.BranchAINode;
import gov.nsa.kore.ng.model.node.base.StarScriptAINode;
import gov.nsa.kore.ng.util.xml.NodeBuilder;
import gov.nsa.kore.ng.util.xml.TypeAdapter;
import gov.nsa.kore.ng.util.xml.XmlException;
import gov.nsa.kore.ng.util.xml.XmlParser;
import org.w3c.dom.Element;

import java.util.function.Supplier;

public class TypeAdapterBuilder {
    public static <TNode extends BranchAINode, TBuilder extends BranchAINode.Builder<TNode>> TypeAdapter buildRecursive(Supplier<TBuilder> builders, String name) {
        return new AINodeTypeAdapter<>(new BranchAINodeTypeAdapter<>(new SimpleBranchNodeTypeAdapter<>(builders, name)));
    }

    // Method name shortened to be the same length as above
    public static <TNode extends StarScriptAINode, TBuilder extends StarScriptAINode.Builder<TNode>> TypeAdapter buildStarScrpt(Supplier<TBuilder> builders, String name) {
        return new AINodeTypeAdapter<>(new StarScriptAINodeTypeAdapter<>(new SimpleStarScriptNodeTypeAdapter<>(builders, name)));
    }

    public record SimpleBranchNodeTypeAdapter<TNode extends BranchAINode, TBuilder extends BranchAINode.Builder<TNode>>(Supplier<TBuilder> builders, String name) implements TypeAdapter.Serializer<TNode>, TypeAdapter.Deserializer<TBuilder> {
        @Override
        public String getNodeName() {
            return name;
        }

        @Override
        public TBuilder deserialize(Element node, XmlParser parser) throws XmlException {
            return builders.get();
        }

        @Override
        public void serializeTo(Element node, TNode value, XmlParser parser, NodeBuilder builder) throws XmlException {
        }
    }

    public record SimpleStarScriptNodeTypeAdapter<TNode extends StarScriptAINode, TBuilder extends StarScriptAINode.Builder<TNode>>(Supplier<TBuilder> builders, String name) implements TypeAdapter.Serializer<TNode>, TypeAdapter.Deserializer<TBuilder> {
        @Override
        public String getNodeName() {
            return name;
        }

        @Override
        public TBuilder deserialize(Element node, XmlParser parser) throws XmlException {
            return builders.get();
        }

        @Override
        public void serializeTo(Element node, TNode value, XmlParser parser, NodeBuilder builder) throws XmlException {
        }
    }
}
