package gov.nsa.kore.ng.model.node;

import gov.nsa.kore.ng.model.EvaluationParameter;
import gov.nsa.kore.ng.model.EvaluationResult;
import gov.nsa.kore.ng.model.EvaluationException;

import java.util.*;

public class AllNode extends AINode {
    private final Set<AINode> nodes;

    public AllNode(Set<AINode> nodes) {
        this.nodes = nodes;
    }

    @Override
    protected EvaluationResult evaluateImpl(String input, EvaluationParameter parameters) throws EvaluationException {
        List<String> compound = new LinkedList<>();
        AINode continueNode = null;
        for (AINode node : nodes) {
            if (node.appliesTo(input, true)) {
                EvaluationResult er = node.evaluate(input, parameters.fork());
                if (!er.success()) return EvaluationResult.fail(er.result().orElse("Failed to execute subnode"));
                if (er.continueNode().isPresent()) continueNode = er.continueNode().get();
                if (er.result().isPresent() && !er.result().get().isBlank()) compound.add(er.result().get());
            }
        }
        if (getContinueNode() != null) continueNode = getContinueNode();
        return EvaluationResult.success(String.join(" ", compound), continueNode);
    }

    @Override
    protected void initializeImpl(AINode rootNode) throws EvaluationException {
        for (AINode node : nodes) {
            node.initialize(rootNode);
        }
    }

    @Override
    public String getIcon(String input) {
        String icon = super.getIcon(input);
        if (icon != null) return icon;
        for (AINode node : nodes) {
            icon = node.getIcon(input);
            if (icon != null) return icon;
        }
        return null;
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
