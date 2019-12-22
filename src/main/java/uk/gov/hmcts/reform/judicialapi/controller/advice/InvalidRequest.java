package uk.gov.hmcts.reform.judicialapi.controller.advice;

public class InvalidRequest extends RuntimeException {

    public InvalidRequest(String message) {
        super(message);
    }
}
