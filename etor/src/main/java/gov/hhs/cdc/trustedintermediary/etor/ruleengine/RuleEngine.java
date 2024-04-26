package gov.hhs.cdc.trustedintermediary.etor.ruleengine;

/** Manages the application of rules loaded from a definitions file using the RuleLoader. */
public interface RuleEngine {
    void unloadRules();

    void ensureRulesLoaded();

    void runRules(FhirResource<?> resource);
}
