package gov.nsa.kore.ng.model.node;

import gov.nsa.kore.ng.model.EvaluationParameter;
import gov.nsa.kore.ng.model.EvaluationResult;
import gov.nsa.kore.ng.model.EvaluationException;

import java.util.*;

public class RandomSelectNode extends AINode {
    private final Map<AINode, Double> nodes = new LinkedHashMap<>();
    private AINode chosenNode;

    public RandomSelectNode(Set<AINode> nodes) {
        for (AINode node : nodes) {
            Double ch = node.getChance();
            this.nodes.put(node, ch == null ? 1 : ch);
        }
    }

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
        Set<Map.Entry<AINode, Double>> validNodes = new LinkedHashSet<>();
        double totalWeight = 0;
        for (Map.Entry<AINode, Double> entry : nodes.entrySet()) {
            if (entry.getKey().appliesTo(input, false)) {
                validNodes.add(entry);
                totalWeight += entry.getValue();
            }
        }
        double r = Math.random() * totalWeight;
        for (Map.Entry<AINode, Double> entry : validNodes) {
            r -= entry.getValue();
            if (r <= 0) {
                chosenNode = entry.getKey();
                break;
            }
        }
    }

    public Set<AINode> getChildren() {
        return nodes.keySet();
    }

    @Override
    public Optional<AINode> getNodeById(String id) {
        return super.getNodeById(id).or(() -> {
            for (AINode node : nodes.keySet()) {
                Optional<AINode> option = node.getNodeById(id);
                if (option.isPresent()) return option;
            }
            return Optional.empty();
        });
    }
}
