package gov.nsa.kore.ng.model.node;

import gov.nsa.kore.ng.Main;
import gov.nsa.kore.ng.model.EvaluationException;
import gov.nsa.kore.ng.model.EvaluationParameter;
import gov.nsa.kore.ng.model.EvaluationResult;
import gov.nsa.kore.ng.model.node.base.StarScriptAINode;
import meteordevelopment.starscript.utils.StarscriptError;

public class StoreNode extends StarScriptAINode {
    @Override
    protected EvaluationResult evaluateImpl(String input, EvaluationParameter parameters) throws EvaluationException {
        parameters.loadValues(Main.STAR_SCRIPT);
        try {
            Main.STAR_SCRIPT.global.set(getId(), Main.STAR_SCRIPT.buildValue(Main.STAR_SCRIPT.run(script)));
            return EvaluationResult.success(null, getContinueNode());
        }
        catch (StarscriptError error) {
            throw new EvaluationException("Could not execute starscript", error);
        }
    }

    public static class Builder extends StarScriptAINode.Builder<StoreNode> {
        @Override
        protected StoreNode getInstance() {
            return new StoreNode();
        }
    }
}
