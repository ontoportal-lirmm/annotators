package org.sifrproject.annotations.model;


import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.WriterConfig;
import org.sifrproject.annotations.api.model.AnnotationToken;
import org.sifrproject.annotations.api.model.LazyModelElement;
import org.sifrproject.annotations.api.model.context.ExperiencerContext;
import org.sifrproject.annotations.api.model.context.NegationContext;
import org.sifrproject.annotations.api.model.context.TemporalityContext;

/**
 * Default lazy dereference implementation of AnnotationToken. Cannot be constructed directly, please use the corresponding
 * factory, {@link BioPortalLazyAnnotationFactory}
 */
public class BioPortalLazyAnnotationToken implements AnnotationToken, LazyModelElement {
    private int from;
    private int to;
    private String matchType;
    private String text;

    private NegationContext negationContext;
    private ExperiencerContext experiencerContext;
    private TemporalityContext temporalityContext;

    private final JsonObject jsonObject;

    @Override
    public JsonValue getJSONObject() {
        return jsonObject;
    }


    BioPortalLazyAnnotationToken(final JsonObject jsonObject) {
        to = -1;
        from = -1;
        text = "";
        matchType = "";
        this.jsonObject = jsonObject;
    }

    @Override
    public int getFrom() {
        if (from < 0) {
            from = jsonObject.get("from").asInt() - 1;
        }
        return from;
    }

    @Override
    public int getTo() {
        if (to < 0) {
            to = jsonObject.get("to").asInt();
        }
        return to;
    }

    @Override
    public String getMatchType() {
        if (matchType.isEmpty()) {
            matchType = jsonObject.get("matchType").asString();
        }
        return matchType;
    }

    @Override
    public String getText() {
        if (text.isEmpty()) {
            text = jsonObject.get("text").asString();
        }
        return text;
    }

    @Override
    public String toString() {
        return jsonObject.toString(WriterConfig.PRETTY_PRINT);
    }

    @SuppressWarnings("all")
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

    @SuppressWarnings("all")
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
        if(jsonObject.get("negationContext")!=null) {
            final String negContext = jsonObject.get("negationContext").asString();
            if ((negationContext == null) && (negContext != null)) {
                negationContext = NegationContext.valueOf(negContext);
            }
        }
        return negationContext;
    }

    @SuppressWarnings("All")
    @Override
    public void setNegationContext(NegationContext negationContext) {
            this.negationContext = negationContext;
            jsonObject.add("negationContext", negationContext.name());
    }

    @Override
    public ExperiencerContext getExperiencerContext() {
        if(jsonObject.get("experiencerContext")!=null) {
            final String expeContext = jsonObject.get("experiencerContext").asString();
            if ((experiencerContext == null) && (expeContext != null)) {
                experiencerContext = ExperiencerContext.valueOf(expeContext);
            }
        }
        return experiencerContext;
    }


    @SuppressWarnings("All")
    @Override
    public void setExperiencerContext(ExperiencerContext experiencerContext) {
        this.experiencerContext = experiencerContext;
        jsonObject.add("experiencerContext", experiencerContext.name());
    }

    @Override
    public TemporalityContext getTemporalityContext() {
        if(jsonObject.get("temporalityContext")!=null) {
            final String tempoContext = jsonObject.get("temporalityContext").asString();
            if ((temporalityContext == null) && (tempoContext != null)) {
                temporalityContext = TemporalityContext.valueOf(tempoContext);
            }
        }
        return temporalityContext;
    }

    @SuppressWarnings("All")
    @Override
    public void setTemporalityContext(TemporalityContext temporalityContext) {
        this.temporalityContext = temporalityContext;
        jsonObject.add("temporalityContext", temporalityContext.name());
    }
}
