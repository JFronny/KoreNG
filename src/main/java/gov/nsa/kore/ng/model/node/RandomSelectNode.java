package gov.nsa.kore.ng.model.node;

import gov.nsa.kore.ng.model.EvaluationParameter;
import gov.nsa.kore.ng.model.EvaluationResult;
import gov.nsa.kore.ng.model.EvaluationException;
import gov.nsa.kore.ng.model.node.base.AINode;
import gov.nsa.kore.ng.model.node.base.BranchAINode;

import java.util.*;

public class RandomSelectNode extends BranchAINode {
    private AINode chosenNode;

    @Override
    public EvaluationResult evaluateImpl(String input, EvaluationParameter parameters) throws EvaluationException {
        if (chosenNode == null) chooseNode(input);
        return chosenNode.evaluate(input, parameters).orContinue(getContinueNode());
    }

    @Override
    public String getIcon(String input) {
        chooseNode(input);
        String chosenIcon = chosenNode.getIcon(input);
        if (chosenIcon == null) chosenIcon = super.getIcon(input);
        return chosenIcon;
    }

    private void chooseNode(String input) {
        Set<AINode> validNodes = new LinkedHashSet<>();
        double totalWeight = 0;
        for (AINode entry : nodes) {
            if (entry.appliesTo(input, false)) {
                validNodes.add(entry);
                totalWeight += entry.getChance();
            }
        }
        double r = Math.random() * totalWeight;
        for (AINode entry : validNodes) {
            r -= entry.getChance();
            if (r <= 0) {
                chosenNode = entry;
                break;
            }
        }
    }

    public static class Builder extends BranchAINode.Builder<RandomSelectNode> {
        @Override
        protected RandomSelectNode getInstance() {
            return new RandomSelectNode();
        }
    }
}
