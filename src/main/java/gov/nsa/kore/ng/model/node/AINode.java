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
    private boolean initialized = false;
    private Double chance = null;
    private Pattern regex = null;
    private String icon = null;
    private String continueNode = null;
    private String id = null;
    private AINode continueNodeResolved = null;

    public boolean appliesTo(String input, boolean considerChance) {
        return input != null && (regex == null || regex.matcher(input).matches())
                && (!considerChance || chance == null || Main.RND.nextDouble() <= chance);
    }

    protected abstract EvaluationResult evaluateImpl(String input, EvaluationParameter parameters) throws EvaluationException;
    protected abstract void initializeImpl(AINode rootNode) throws EvaluationException;

    public EvaluationResult evaluate(String input, EvaluationParameter parameters) {
        if (!initialized) return EvaluationResult.fail("Attempted to evaluate uninitialized node");
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

    public void initialize(AINode rootNode) throws EvaluationException {
        if (continueNode != null)
            continueNodeResolved = rootNode.getNodeById(continueNode).orElseThrow(() -> new EvaluationException("Could not find node: " + continueNode));
        initializeImpl(rootNode);
        initialized = true;
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

    public AINode getContinueNode() {
        return continueNodeResolved;
    }

    public String getContinueNodeName() {
        return continueNode;
    }

    public String getId() {
        return id;
    }

    protected Optional<AINode> getNodeById(String id) {
        if (Objects.equals(id, this.id)) return Optional.of(this);
        return Optional.empty();
    }

    public void setChance(Double chance) {
        if (initialized) throw new IllegalStateException("This node has already been initialized");
        this.chance = chance;
    }

    public void setIcon(String icon) {
        if (initialized) throw new IllegalStateException("This node has already been initialized");
        this.icon = icon;
    }

    public void setRegex(String regex) {
        if (initialized) throw new IllegalStateException("This node has already been initialized");
        this.regex = Pattern.compile(regex);
    }

    public void setContinueNode(String continueNode) {
        if (initialized) throw new IllegalStateException("This node has already been initialized");
        this.continueNode = continueNode;
    }

    public void setId(String id) {
        if (initialized) throw new IllegalStateException("This node has already been initialized");
        this.id = id;
    }
}
