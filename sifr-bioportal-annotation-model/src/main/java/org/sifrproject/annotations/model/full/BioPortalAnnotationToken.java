package org.sifrproject.annotations.model.full;


import org.sifrproject.annotations.api.model.AnnotationToken;
import org.sifrproject.annotations.api.model.context.ExperiencerContext;
import org.sifrproject.annotations.api.model.context.NegationContext;
import org.sifrproject.annotations.api.model.context.TemporalityContext;
import org.sifrproject.annotations.api.model.visitor.AnnotationVisitor;

public class BioPortalAnnotationToken implements AnnotationToken {
    private final int from;
    private final int to;
    private final String matchType;
    private final String text;

    private NegationContext negationContext;
    private ExperiencerContext experiencerContext;
    private TemporalityContext temporalityContext;


    public BioPortalAnnotationToken(int from, int to, String matchType, String text) {
        this.from = from;
        this.to = to;
        this.matchType = matchType;
        this.text = text;
    }

    @Override
    public int getFrom() {
        return from;
    }

    @Override
    public int getTo() {
        return to;
    }

    @Override
    public String getMatchType() {
        return matchType;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void accept(AnnotationVisitor annotationVisitor) {
        annotationVisitor.visitBefore(this);
        annotationVisitor.visitAfter(this);
    }

    @Override
    public NegationContext getNegationContext() {
        return negationContext;
    }

    @Override
    public void setNegationContext(NegationContext negationContext) {
        this.negationContext = negationContext;
    }

    @Override
    public ExperiencerContext getExperiencerContext() {
        return experiencerContext;
    }

    @Override
    public void setExperiencerContext(ExperiencerContext experiencerContext) {
        this.experiencerContext = experiencerContext;
    }

    @Override
    public TemporalityContext getTemporalityContext() {
        return temporalityContext;
    }

    @Override
    public void setTemporalityContext(TemporalityContext temporalityContext) {
        this.temporalityContext = temporalityContext;
    }
}
