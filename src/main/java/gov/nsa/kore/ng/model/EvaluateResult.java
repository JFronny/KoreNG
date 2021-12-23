package gov.nsa.kore.ng.model;

import java.util.Optional;

public record EvaluateResult(boolean success, Optional<String> result, Optional<String> continueNode) {
    public static EvaluateResult fail(String message) {
        return new EvaluateResult(false, Optional.of(message), Optional.empty());
    }

    public static EvaluateResult success(String message) {
        return new EvaluateResult(true, Optional.of(message), Optional.empty());
    }

    public static EvaluateResult success(String message, String continueNode) {
        return new EvaluateResult(true, Optional.of(message), Optional.of(continueNode));
    }

    EvaluateResult orContinue(String continueNode) {
        if (this.continueNode.isPresent()) return this;
        return new EvaluateResult(success, result, success && continueNode != null ? Optional.of(continueNode) : Optional.empty());
    }
}
