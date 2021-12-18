package gov.nsa.kore.ng.model;

import gov.nsa.kore.ng.Main;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AINode {
    private Double chance = null;
    private Pattern regex = null;
    private String icon = null;

    public boolean appliesTo(String input, boolean considerChance) {
        return input != null && (regex == null || regex.matcher(input).matches())
                && (!considerChance || chance == null || Main.RND.nextDouble() <= chance);
    }

    protected abstract String evaluateImpl(String input, List<String> parameters);

    public String evaluate(String input, List<String> parameters) {
        if (regex != null) {
            Matcher matcher = regex.matcher(input);
            if (!matcher.find()) throw new RuntimeException("Attempted to evaluate AINode to which this input doesn't apply");
            for (int i = 0; i < matcher.groupCount(); i++) {
                parameters.add(matcher.group(i + 1));
            }
        }
        return evaluateImpl(input, parameters);
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

    public void setChance(Double chance) {
        this.chance = chance;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setRegex(String regex) {
        this.regex = Pattern.compile(regex);
    }
}
