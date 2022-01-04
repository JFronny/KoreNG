package gov.nsa.kore.ng.model.node;

import gov.nsa.kore.ng.model.EvaluationParameter;
import gov.nsa.kore.ng.model.EvaluationResult;
import gov.nsa.kore.ng.model.EvaluationException;

import java.util.Optional;
import java.util.Set;

public class DownNode extends AINode {
    private final Set<AINode> nodes;
    private AINode chosenNode;

    public DownNode(Set<AINode> nodes) {
        this.nodes = nodes;
    }

    @Override
    protected EvaluationResult evaluateImpl(String input, EvaluationParameter parameters) throws EvaluationException {
        if (chosenNode == null) chooseNode(input);
        return chosenNode.evaluate(input, parameters).orContinue(getContinueNode());
    }

    @Override
    protected void initializeImpl(AINode rootNode) throws EvaluationException {
        for (AINode node : nodes) {
            node.initialize(rootNode);
        }
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
}
