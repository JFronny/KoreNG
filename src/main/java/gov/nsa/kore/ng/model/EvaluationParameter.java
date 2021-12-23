package gov.nsa.kore.ng.model;

import gov.nsa.kore.ng.util.ClearScript;

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

    public void loadValues(ClearScript target) {
        for (int i = 0, parametersSize = parameters().size(); i < parametersSize; i++) {
            target.set("p" + i, target.buildValue(parameters().get(i)));
        }
    }
}
