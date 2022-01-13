package gov.nsa.kore.ng.model.node.base;

import gov.nsa.kore.ng.Main;
import gov.nsa.kore.ng.model.EvaluationParameter;
import gov.nsa.kore.ng.model.EvaluationResult;
import gov.nsa.kore.ng.model.EvaluationException;
import gov.nsa.kore.ng.util.xml.XmlException;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AINode {
    Double chance;
    Pattern regex;
    String icon;
    String id;
    AINode continueNode;

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

    public AINode getContinueNode() {
        return continueNode;
    }

    public String getId() {
        return id;
    }

    protected Optional<AINode> getNodeById(String id) {
        if (Objects.equals(id, this.id)) return Optional.of(this);
        return Optional.empty();
    }

    public static abstract class Builder<T extends AINode> {
        private Double chance = 1d;
        private String regex = null;
        private String icon = "far-comment-dots";
        private String id = null;
        private String continueNode = null;
        private T node;

        public void setChance(Double chance) {
            this.chance = chance;
        }

        public void setRegex(String regex) {
            this.regex = regex;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setContinueNode(String continueNode) {
            this.continueNode = continueNode;
        }

        protected abstract T getInstance();

        public T build() throws XmlException {
            try {
                node = getInstance();
                node.chance = chance;
                node.regex = regex == null ? null : Pattern.compile(regex);
                node.icon = icon;
                node.id = id;
            }
            catch (Throwable t) {
                throw new XmlException("Could not build node", t);
            }
            return node;
        }

        public T compile(AINode rootNode) throws XmlException {
            if (node == null) throw new XmlException("This builder was not built");
            if (continueNode != null) {
                Optional<AINode> con = rootNode.getNodeById(continueNode);
                if (con.isEmpty()) throw new XmlException("ContinueNode could not be resolved");
                node.continueNode = con.get();
            }
            return node;
        }
    }
}
