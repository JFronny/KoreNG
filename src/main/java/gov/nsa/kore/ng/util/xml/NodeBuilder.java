package gov.nsa.kore.ng.util.xml;

import org.w3c.dom.*;

public interface NodeBuilder {
    Element createElement(String name) throws DOMException;
    Text createTextNode(String name);
    Comment createComment(String name);
    CDATASection createCDATASection(String name) throws DOMException;

    static NodeBuilder create(Document document) {
        return new NodeBuilder() {
            @Override
            public Element createElement(String name) throws DOMException {
                return document.createElement(name);
            }

            @Override
            public Text createTextNode(String name) {
                return document.createTextNode(name);
            }

            @Override
            public Comment createComment(String name) {
                return document.createComment(name);
            }

            @Override
            public CDATASection createCDATASection(String name) throws DOMException {
                return document.createCDATASection(name);
            }
        };
    }
}
