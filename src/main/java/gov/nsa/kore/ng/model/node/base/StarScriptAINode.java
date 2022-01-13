package gov.nsa.kore.ng.model.node.base;

import gov.nsa.kore.ng.util.xml.XmlException;
import meteordevelopment.starscript.Script;
import meteordevelopment.starscript.compiler.Compiler;
import meteordevelopment.starscript.compiler.Parser;
import meteordevelopment.starscript.utils.Error;

public abstract class StarScriptAINode extends AINode {
    protected Script script;
    protected String scriptSource;

    public String getScriptSource() {
        return scriptSource;
    }

    public static abstract class Builder<T extends StarScriptAINode> extends AINode.Builder<T> {
        private String scriptSource = "";

        public void setScriptSource(String scriptSource) {
            this.scriptSource = scriptSource;
        }

        @Override
        public T build() throws XmlException {
            T node = super.build();
            node.scriptSource = scriptSource;
            Parser.Result result = Parser.parse(scriptSource);
            if (result.hasErrors()) {
                StringBuilder issues = new StringBuilder("Discovered the following issues in the starscript " + scriptSource + ":");
                for (Error error : result.errors) {
                    issues.append('\n').append(error.toString());
                }
                throw new XmlException(issues.toString());
            }
            node.script = Compiler.compile(result);
            return node;
        }
    }
}
