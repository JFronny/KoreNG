package gov.nsa.kore.ng.controller;

public interface LoadingProvider {
    void close();
    void setProgress(double d);
}
