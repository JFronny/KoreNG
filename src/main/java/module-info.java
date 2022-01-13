module gov.nsa.kore.ng {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires starscript;

    requires java.xml;

    opens gov.nsa.kore.ng to javafx.fxml;
    exports gov.nsa.kore.ng;
    exports gov.nsa.kore.ng.model;
    exports gov.nsa.kore.ng.util;
    exports gov.nsa.kore.ng.util.xml;
    opens gov.nsa.kore.ng.util to javafx.fxml;
    exports gov.nsa.kore.ng.controller;
    opens gov.nsa.kore.ng.controller to javafx.fxml;
    exports gov.nsa.kore.ng.model.node;
    opens gov.nsa.kore.ng.model to javafx.fxml;
    exports gov.nsa.kore.ng.model.node.base;
}