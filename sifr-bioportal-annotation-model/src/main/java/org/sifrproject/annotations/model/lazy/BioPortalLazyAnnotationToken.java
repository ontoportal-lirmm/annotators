package org.sifrproject.annotations.model.lazy;


import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONObject;
import org.sifrproject.annotations.api.model.AnnotationToken;
import org.sifrproject.annotations.api.model.context.ExperiencerContext;
import org.sifrproject.annotations.api.model.context.NegationContext;
import org.sifrproject.annotations.api.model.context.TemporalityContext;
import org.sifrproject.annotations.api.model.lazy.LazyModelElement;
import org.sifrproject.annotations.api.model.visitor.AnnotationVisitor;

public class BioPortalLazyAnnotationToken implements AnnotationToken, LazyModelElement {
    private int from;
    private int to;
    private String matchType;
    private String text;

    private NegationContext negationContext = null;
    private ExperiencerContext experiencerContext = null;
    private TemporalityContext temporalityContext = null;

    private JSONObject jsonObject;

    public JSONObject getJSONObject() {
        return jsonObject;
    }


    public BioPortalLazyAnnotationToken(JSONObject jsonObject) {
        to = -1;
        from = -1;
        text = "";
        matchType = "";
        this.jsonObject = jsonObject;
    }

    @Override
    public int getFrom() {
        if (from < 0) {
            from = ((Long) jsonObject.get("from")).intValue() - 1;
        }
        return from;
    }

    @Override
    public int getTo() {
        if (to < 0) {
            to = ((Long) jsonObject.get("to")).intValue();
        }
        return to;
    }

    @Override
    public String getMatchType() {
        if (matchType.isEmpty()) {
            matchType = (String) jsonObject.get("matchType");
        }
        return matchType;
    }

    @Override
    public String getText() {
        if (text.isEmpty()) {
            text = (String) jsonObject.get("text");
        }
        return text;
    }

    @Override
    public void accept(AnnotationVisitor annotationVisitor) {
        annotationVisitor.visitBefore(this);
        annotationVisitor.visitAfter(this);
    }

    @Override
    public String toString() {
        return StringEscapeUtils.unescapeJson(jsonObject.toJSONString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BioPortalLazyAnnotationToken)) return false;

        BioPortalLazyAnnotationToken that = (BioPortalLazyAnnotationToken) o;

        return getFrom() == that.getFrom() && getTo() == that.getTo() && (getMatchType() != null ?
                getMatchType().equals(that.getMatchType()) :
                that.getMatchType() == null) && (getText() != null ?
                getText().equals(that.getText()) :
                that.getText() == null);
    }

    @Override
    public int hashCode() {
        int result = getFrom();
        result = 31 * result + getTo();
        result = 31 * result + (getMatchType() != null ? getMatchType().hashCode() : 0);
        result = 31 * result + (getText() != null ? getText().hashCode() : 0);
        return result;
    }

    @Override
    public NegationContext getNegationContext() {
        if (negationContext == null && jsonObject.containsKey("negationContext")) {
            negationContext = NegationContext.valueOf(jsonObject.get("negationContext").toString());
        }
        return negationContext;
    }

    @Override
    public void setNegationContext(NegationContext negationContext) {
        this.negationContext = negationContext;
        jsonObject.put("negationContext", negationContext.toString());
    }

    @Override
    public ExperiencerContext getExperiencerContext() {
        if (experiencerContext == null && jsonObject.containsKey("experiencerContext")) {
            experiencerContext = ExperiencerContext.valueOf(jsonObject.get("experiencerContext").toString());
        }
        return experiencerContext;
    }

    @Override
    public void setExperiencerContext(ExperiencerContext experiencerContext) {
        this.experiencerContext = experiencerContext;
        jsonObject.put("experiencerContext", experiencerContext.toString());
    }

    @Override
    public TemporalityContext getTemporalityContext() {
        if (temporalityContext == null && jsonObject.containsKey("temporalityContext")) {
            temporalityContext = TemporalityContext.valueOf(jsonObject.get("temporalityContext").toString());
        }
        return temporalityContext;
    }

    @Override
    public void setTemporalityContext(TemporalityContext temporalityContext) {
        this.temporalityContext = temporalityContext;
        jsonObject.put("temporalityContext", temporalityContext.toString());
    }
}
