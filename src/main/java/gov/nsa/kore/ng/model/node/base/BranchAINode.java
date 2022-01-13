package gov.nsa.kore.ng.model.node.base;

import gov.nsa.kore.ng.util.xml.XmlException;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public abstract class BranchAINode extends AINode {
    protected Set<AINode> nodes = new LinkedHashSet<>();

    public Set<AINode> getChildren() {
        return Set.copyOf(nodes);
    }

    @Override
    protected Optional<AINode> getNodeById(String id) {
        return super.getNodeById(id).or(() -> {
            for (AINode node : nodes) {
                Optional<AINode> option = node.getNodeById(id);
                if (option.isPresent()) return option;
            }
            return Optional.empty();
        });
    }

    public static abstract class Builder<T extends BranchAINode> extends AINode.Builder<T> {
        Set<AINode.Builder<?>> nodes = Set.of();

        public void setNodes(Set<AINode.Builder<?>> nodes) {
            this.nodes = nodes;
        }

        @Override
        public T build() throws XmlException {
            T node = super.build();
            for (AINode.Builder<?> builder : nodes) {
                node.nodes.add(builder.build());
            }
            return node;
        }

        @Override
        public T compile(AINode rootNode) throws XmlException {
            T node = super.compile(rootNode);
            for (AINode.Builder<?> builder : nodes) {
                builder.compile(rootNode);
            }
            return node;
        }
    }
}
