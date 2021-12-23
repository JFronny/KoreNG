package gov.nsa.kore.ng.model.node;

import gov.nsa.kore.ng.Main;
import gov.nsa.kore.ng.model.EvaluationException;
import gov.nsa.kore.ng.model.EvaluationParameter;
import gov.nsa.kore.ng.model.EvaluationResult;
import gov.nsa.kore.ng.util.xml.XmlException;
import meteordevelopment.starscript.Script;
import meteordevelopment.starscript.compiler.Compiler;
import meteordevelopment.starscript.compiler.Parser;
import meteordevelopment.starscript.utils.Error;
import meteordevelopment.starscript.utils.StarscriptError;

public class StoreNode extends AINode {
    private final Script script;
    private final String scriptSource;

    public StoreNode(String scriptSource) throws XmlException {
        this.scriptSource = scriptSource;
        Parser.Result result = Parser.parse(scriptSource);
        if (result.hasErrors()) {
            StringBuilder issues = new StringBuilder("Discovered the following issues in the starscript " + scriptSource + ":");
            for (Error error : result.errors) {
                issues.append('\n').append(error.toString());
            }
            throw new XmlException(issues.toString());
        }
        this.script = Compiler.compile(result);
    }

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

    public String getScriptSource() {
        return scriptSource;
    }
}
