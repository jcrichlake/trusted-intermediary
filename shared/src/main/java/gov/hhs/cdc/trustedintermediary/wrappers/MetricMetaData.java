package gov.hhs.cdc.trustedintermediary.wrappers;

/** Interface to provide a blueprint for working with metadate */
public interface MetricMetaData {

    <T> void put(String value, T step);
}
