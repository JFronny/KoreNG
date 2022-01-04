package gov.nsa.kore.ng.model;

import gov.nsa.kore.ng.model.node.AINode;

import java.util.Optional;

public record EvaluationResult(boolean success, Optional<String> result, Optional<AINode> continueNode) {
    public static EvaluationResult fail(String message) {
        return new EvaluationResult(false, Optional.of(message), Optional.empty());
    }

    public static EvaluationResult success(String message) {
        return success(message, null);
    }

    public static EvaluationResult success(String message, AINode continueNode) {
        return new EvaluationResult(true, message == null ? Optional.empty() : Optional.of(message), continueNode == null ? Optional.empty() : Optional.of(continueNode));
    }

    public EvaluationResult orContinue(AINode continueNode) {
        if (this.continueNode.isPresent()) return this;
        return new EvaluationResult(success, result, success && continueNode != null ? Optional.of(continueNode) : Optional.empty());
    }
}
