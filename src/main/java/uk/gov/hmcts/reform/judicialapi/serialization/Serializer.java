package uk.gov.hmcts.reform.judicialapi.serialization;

public interface Serializer<T> {

    String serialize(T data);
}
