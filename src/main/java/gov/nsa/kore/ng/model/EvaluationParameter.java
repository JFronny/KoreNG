package gov.nsa.kore.ng.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public record EvaluationParameter(List<String> parameters) {
    public EvaluationParameter fork() {
        return new EvaluationParameter(new LinkedList<>(parameters));
    }

    public EvaluationParameter(String input) {
        this(new LinkedList<>(Set.of(input)));
    }
}
