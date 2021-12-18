package gov.nsa.kore.ng.model;

import java.util.List;
import java.util.Set;

public class DownNode extends AINode {
    private final Set<AINode> nodes;
    private AINode chosenNode;

    public DownNode(Set<AINode> nodes) {
        this.nodes = nodes;
    }

    @Override
    protected String evaluateImpl(String input, List<String> parameters) {
        if (chosenNode == null) chooseNode(input);
        return chosenNode.evaluate(input, parameters);
    }

    @Override
    public String getIcon(String input) {
        chooseNode(input);
        String chosenIcon = chosenNode.getIcon(input);
        if (chosenIcon == null) chosenIcon = super.getIcon(input);
        return chosenIcon;
    }

    private void chooseNode(String input) {
        for (AINode entry : nodes) {
            if (entry.appliesTo(input, true)) {
                chosenNode = entry;
                break;
            }
        }
    }

    public Set<AINode> getChildren() {
        return Set.copyOf(nodes);
    }
}
