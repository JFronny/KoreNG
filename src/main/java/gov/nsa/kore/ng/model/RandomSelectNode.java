package gov.nsa.kore.ng.model;

import java.util.*;

public class RandomSelectNode extends AINode {
    private final Map<Double, AINode> nodes = new LinkedHashMap<>();
    private AINode chosenNode;

    public RandomSelectNode(Set<AINode> nodes) {
        for (AINode node : nodes) {
            Double ch = node.getChance();
            this.nodes.put(ch == null ? 1 : ch, node);
        }
    }

    @Override
    public String evaluateImpl(String input, List<String> parameters) {
        if (chosenNode == null) chooseNode(input);
        return chosenNode.evaluate(input, parameters);
    }

    @Override
    public String getIcon(String input) {
        chooseNode(input);
        String chosenIcon = chosenNode.getIcon(input);
        if (chosenIcon == null) chosenIcon = super.getIcon(input);
        return chosenIcon;
    }

    private void chooseNode(String input) {
        Set<Map.Entry<Double, AINode>> validNodes = new LinkedHashSet<>();
        double totalWeight = 0;
        for (Map.Entry<Double, AINode> entry : nodes.entrySet()) {
            if (entry.getValue().appliesTo(input, false)) {
                validNodes.add(entry);
                totalWeight += entry.getKey();
            }
        }
        double r = Math.random() * totalWeight;
        for (Map.Entry<Double, AINode> entry : validNodes) {
            r -= entry.getKey();
            if (r <= 0) {
                chosenNode = entry.getValue();
                break;
            }
        }
    }

    public Set<AINode> getChildren() {
        return Set.copyOf(nodes.values());
    }
}
