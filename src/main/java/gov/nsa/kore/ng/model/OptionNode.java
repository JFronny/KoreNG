package gov.nsa.kore.ng.model;

import gov.nsa.kore.ng.Main;
import gov.nsa.kore.ng.util.RegexUtil;
import gov.nsa.kore.ng.util.xml.XmlException;
import meteordevelopment.starscript.Script;
import meteordevelopment.starscript.compiler.Compiler;
import meteordevelopment.starscript.compiler.Parser;
import meteordevelopment.starscript.utils.Error;
import meteordevelopment.starscript.value.Value;

import java.util.List;

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
    public String evaluateImpl(String input, List<String> parameters) {
        for (int i = 0, parametersSize = parameters.size(); i < parametersSize; i++) {
            String parameter = parameters.get(i);
            Value value;
            if (RegexUtil.DOUBLE.test(parameter))
                value = Value.number(Double.parseDouble(parameter));
            else if (RegexUtil.BOOL.test(parameter))
                value = Value.bool(RegexUtil.TRUE.test(parameter));
            else
                value = Value.string(parameter);
            Main.STAR_SCRIPT.set("p" + i, value);
        }
        return Main.STAR_SCRIPT.run(script);
    }

    public String getScriptSource() {
        return scriptSource;
    }
}
