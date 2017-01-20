package org.sifrproject.annotations.api.model.context;


/**
 * Data interface for an entity annotated with ConTeXT
 */
public interface ContextAnnotationElement {
    /**
     * Getter for the negation context
     * @return The negation context of the annotated element
     */
    NegationContext getNegationContext();

    /**
     * Getter for the temporality context
     * @return The temporality context of the annotated element
     */
    TemporalityContext getTemporalityContext();

    /**
     * Getter for the experiencer context
     * @return The experiencer context of the annotated element
     */
    ExperiencerContext getExperiencerContext();

    /**
     * Setter for the negation context of the annotated element
     * @param negationContext The negation context of the annotated element
     */
    void setNegationContext(NegationContext negationContext);

    /**
     * Setter for the temporality context of the annotated element
     * @param temporalityContext The temporality context of the annotated element
     */
    void setTemporalityContext(TemporalityContext temporalityContext);

    /**
     * Setter for the experiencer context of the annotated element
     * @param experiencerContext The experiencer context of the annotated element
     */
    void setExperiencerContext(ExperiencerContext experiencerContext);
}
