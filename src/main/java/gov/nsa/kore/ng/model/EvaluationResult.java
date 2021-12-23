package gov.nsa.kore.ng.model;

import java.util.Optional;

public record EvaluationResult(boolean success, Optional<String> result, Optional<String> continueNode) {
    public static EvaluationResult fail(String message) {
        return new EvaluationResult(false, Optional.of(message), Optional.empty());
    }

    public static EvaluationResult success(String message) {
        return new EvaluationResult(true, Optional.of(message), Optional.empty());
    }

    public static EvaluationResult success(String message, String continueNode) {
        return new EvaluationResult(true, Optional.of(message), continueNode == null ? Optional.empty() : Optional.of(continueNode));
    }

    public EvaluationResult orContinue(String continueNode) {
        if (this.continueNode.isPresent()) return this;
        return new EvaluationResult(success, result, success && continueNode != null ? Optional.of(continueNode) : Optional.empty());
    }
}
