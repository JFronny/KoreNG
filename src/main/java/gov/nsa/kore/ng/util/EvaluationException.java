package gov.nsa.kore.ng.util;

public class EvaluationException extends Exception {
    public EvaluationException(String message) {
        super(message);
    }

    public EvaluationException(String message, Throwable cause) {
        super(message, cause);
    }
}
