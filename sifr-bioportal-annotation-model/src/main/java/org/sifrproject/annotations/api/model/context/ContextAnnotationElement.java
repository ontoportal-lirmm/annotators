package org.sifrproject.annotations.api.model.context;


public interface ContextAnnotationElement {
    NegationContext getNegationContext();

    TemporalityContext getTemporalityContext();

    ExperiencerContext getExperiencerContext();

    void setNegationContext(NegationContext negationContext);

    void setTemporalityContext(TemporalityContext temporalityContext);

    void setExperiencerContext(ExperiencerContext experiencerContext);
}
