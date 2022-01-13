package gov.nsa.kore.ng.util.xml;

public interface TypeAdapterFactory {
    <T> boolean appliesTo(Class<T> klazz);
    <T> TypeAdapter build(Class<T> klazz);
}
