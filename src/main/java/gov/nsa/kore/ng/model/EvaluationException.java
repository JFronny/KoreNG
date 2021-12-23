package gov.nsa.kore.ng.model;

public class EvaluationException extends Exception {
    public EvaluationException(String message) {
        super(message);
    }

    public EvaluationException(String message, Throwable cause) {
        super(message, cause);
    }
}
