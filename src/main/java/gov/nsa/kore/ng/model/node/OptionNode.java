package gov.nsa.kore.ng.model.node;

import gov.nsa.kore.ng.Main;
import gov.nsa.kore.ng.model.EvaluationException;
import gov.nsa.kore.ng.model.EvaluationParameter;
import gov.nsa.kore.ng.model.EvaluationResult;
import gov.nsa.kore.ng.model.node.base.StarScriptAINode;
import meteordevelopment.starscript.utils.StarscriptError;

public class OptionNode extends StarScriptAINode {
    @Override
    public EvaluationResult evaluateImpl(String input, EvaluationParameter parameters) throws EvaluationException {
        parameters.loadValues(Main.STAR_SCRIPT);
        try {
            return EvaluationResult.success(Main.STAR_SCRIPT.run(script), getContinueNode());
        }
        catch (StarscriptError error) {
            throw new EvaluationException("Could not execute starscript", error);
        }
    }

    public static class Builder extends StarScriptAINode.Builder<OptionNode> {
        @Override
        protected OptionNode getInstance() {
            return new OptionNode();
        }
    }
}
