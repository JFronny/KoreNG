package gov.nsa.kore.ng.model.node;

import gov.nsa.kore.ng.Main;
import gov.nsa.kore.ng.model.EvaluationParameter;
import gov.nsa.kore.ng.model.EvaluationResult;
import gov.nsa.kore.ng.model.EvaluationException;
import gov.nsa.kore.ng.util.RegexUtil;
import gov.nsa.kore.ng.util.xml.XmlException;
import meteordevelopment.starscript.Script;
import meteordevelopment.starscript.compiler.Compiler;
import meteordevelopment.starscript.compiler.Parser;
import meteordevelopment.starscript.utils.Error;
import meteordevelopment.starscript.utils.StarscriptError;
import meteordevelopment.starscript.value.Value;

public class OptionNode extends AINode {
    private final Script script;
    private final String scriptSource;

    public OptionNode(String scriptSource) throws XmlException {
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
    public EvaluationResult evaluateImpl(String input, EvaluationParameter parameters) throws EvaluationException {
        for (int i = 0, parametersSize = parameters.parameters().size(); i < parametersSize; i++) {
            String parameter = parameters.parameters().get(i);
            Value value;
            if (RegexUtil.DOUBLE.test(parameter))
                value = Value.number(Double.parseDouble(parameter));
            else if (RegexUtil.BOOL.test(parameter))
                value = Value.bool(RegexUtil.TRUE.test(parameter));
            else
                value = Value.string(parameter);
            Main.STAR_SCRIPT.set("p" + i, value);
        }
        try {
            return EvaluationResult.success(Main.STAR_SCRIPT.run(script), getContinueNode());
        }
        catch (StarscriptError error) {
            throw new EvaluationException("Could not execute starscript", error);
        }
    }

    public String getScriptSource() {
        return scriptSource;
    }
}
