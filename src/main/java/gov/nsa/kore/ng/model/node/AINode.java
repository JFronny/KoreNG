package gov.nsa.kore.ng.model.node;

import gov.nsa.kore.ng.Main;
import gov.nsa.kore.ng.model.EvaluationParameter;
import gov.nsa.kore.ng.model.EvaluationResult;
import gov.nsa.kore.ng.model.EvaluationException;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AINode {
    private Double chance = null;
    private Pattern regex = null;
    private String icon = null;
    private String continueNode = null;
    private String id = null;

    public boolean appliesTo(String input, boolean considerChance) {
        return input != null && (regex == null || regex.matcher(input).matches())
                && (!considerChance || chance == null || Main.RND.nextDouble() <= chance);
    }

    protected abstract EvaluationResult evaluateImpl(String input, EvaluationParameter parameters) throws EvaluationException;

    public EvaluationResult evaluate(String input, EvaluationParameter parameters) {
        if (regex != null) {
            Matcher matcher = regex.matcher(input);
            if (!matcher.find()) return EvaluationResult.fail("Attempted to evaluate AINode to which this input doesn't apply");
            for (int i = 0; i < matcher.groupCount(); i++) {
                parameters.parameters().add(matcher.group(i + 1));
            }
        }
        try {
            return evaluateImpl(input, parameters);
        } catch (EvaluationException e) {
            return EvaluationResult.fail(e.toString());
        }
    }

    public String getIcon(String input) {
        return icon;
    }

    public Double getChance() {
        return chance;
    }

    public String getRegex() {
        return regex.toString();
    }

    public String getContinueNode() {
        return continueNode;
    }

    public String getId() {
        return id;
    }

    public Optional<AINode> getNodeById(String id) {
        if (Objects.equals(id, this.id)) return Optional.of(this);
        return Optional.empty();
    }

    public void setChance(Double chance) {
        this.chance = chance;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setRegex(String regex) {
        this.regex = Pattern.compile(regex);
    }

    public void setContinueNode(String continueNode) {
        this.continueNode = continueNode;
    }

    public void setId(String id) {
        this.id = id;
    }
}
