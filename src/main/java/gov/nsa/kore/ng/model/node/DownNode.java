package gov.nsa.kore.ng.model.node;

import gov.nsa.kore.ng.model.EvaluationException;
import gov.nsa.kore.ng.model.EvaluationParameter;
import gov.nsa.kore.ng.model.EvaluationResult;
import gov.nsa.kore.ng.model.node.base.AINode;
import gov.nsa.kore.ng.model.node.base.BranchAINode;

public class DownNode extends BranchAINode {
    private AINode chosenNode;

    @Override
    protected EvaluationResult evaluateImpl(String input, EvaluationParameter parameters) throws EvaluationException {
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
        for (AINode entry : nodes) {
            if (entry.appliesTo(input, true)) {
                chosenNode = entry;
                break;
            }
        }
    }

    public static class Builder extends BranchAINode.Builder<DownNode> {
        @Override
        protected DownNode getInstance() {
            return new DownNode();
        }
    }
}
