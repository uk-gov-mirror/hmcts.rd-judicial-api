package uk.gov.hmcts.reform.judicialapi.serialization;

public interface Deserializer<T> {

    T deserialize(String source);
}
