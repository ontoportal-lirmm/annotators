package org.sifrproject.postannotation;


import edu.utah.bmi.nlp.core.SimpleParser;
import edu.utah.bmi.nlp.core.Span;
import edu.utah.bmi.nlp.fastcontext.FastContext;
import org.apache.commons.io.IOUtils;
import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.annotations.api.model.AnnotationToken;
import org.sifrproject.annotations.api.model.AnnotationTokens;
import org.sifrproject.annotations.api.model.context.CertaintyContext;
import org.sifrproject.annotations.api.model.context.ExperiencerContext;
import org.sifrproject.annotations.api.model.context.NegationContext;
import org.sifrproject.annotations.api.model.context.TemporalityContext;
import org.sifrproject.postannotation.api.PostAnnotationFilter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Adds negation, experiencer and temporality annotations with Context
 */
public class ContextPostAnnotationFilter implements PostAnnotationFilter {

//    private static final Logger logger = LoggerFactory.getLogger(ContextPostAnnotationFilter.class);

    private final FastContext context;

    public ContextPostAnnotationFilter(final String language) throws IOException {
        final String ruleSetFileName = language.toLowerCase() + "_context.tsv";
        final ClassLoader classLoader = getClass().getClassLoader();
        final StringBuilder contents = new StringBuilder();
        try (final InputStream ruleSteam = classLoader.getResourceAsStream(ruleSetFileName)) {
            if (ruleSteam != null) {
                contents.append(IOUtils.toString(ruleSteam, "utf-8"));
            }
            context = new FastContext(contents.toString());
        }
    }

    private int findFirstTokenIndex(final List<Span> spans, final int beginOffset) {
        int spanIndex = 0;
        while ((spans.get(spanIndex).begin < beginOffset) && (spanIndex < spans.size())) {
            spanIndex++;
        }
        return spanIndex;
    }

    private int findLastTokenIndex(final List<Span> spans, final int endOffset, final int startingIndex) {
        int spanIndex = startingIndex;
        while ((spans.get(spanIndex).end < endOffset) && (spanIndex < spans.size())) {
            spanIndex++;
        }
        return spanIndex;
    }

    @Override
    public void postAnnotate(final List<Annotation> annotations, final String text) {
        // Segment the text in sentences
        final ArrayList<Span> textTokens = SimpleParser.tokenizeOnWhitespaces(text);


        for (final Annotation annotation : annotations) {
            final AnnotationTokens annotationTokens = annotation.getAnnotations();
            for (final AnnotationToken annotationToken : annotationTokens) {
                final String tokenText = annotationToken.getText();
                final int annotationTokenFrom = annotationToken.getFrom();

                final int currentConceptFrom = (tokenText.endsWith(" ")) ? (annotationTokenFrom - 1) :
                        annotationTokenFrom;
                final int currentConceptTo = annotationToken.getTo();
                final int tokenStartIndex = findFirstTokenIndex(textTokens, currentConceptFrom);
                final int tokenEndIndex = findLastTokenIndex(textTokens, currentConceptTo, tokenStartIndex);


                final List<String> results = context.processContext(textTokens, tokenStartIndex, tokenEndIndex, text, 30);

                final int uncertainty_index = results.indexOf("uncertain");
                if (uncertainty_index >= 0) {
                    annotationToken.setCertaintyContext(CertaintyContext.valueOf(results
                            .get(uncertainty_index)
                            .toUpperCase()));
                } else {
                    annotationToken.setCertaintyContext(CertaintyContext.CERTAIN);
                }


                final int negation_index = results.indexOf("negated");
                if (negation_index >= 0) {
                    annotationToken.setNegationContext(NegationContext.valueOf(results
                            .get(negation_index)
                            .toUpperCase()));
                } else {
                    annotationToken.setNegationContext(NegationContext.AFFIRMED);
                }


                int temporality_index = results.indexOf("historical");
                if (temporality_index == -1) {
                    temporality_index = results.indexOf("hypothetical");
                }
                if (temporality_index >= 0) {
                    annotationToken.setTemporalityContext(TemporalityContext.valueOf(results
                            .get(temporality_index)
                            .toUpperCase()));
                } else {
                    annotationToken.setTemporalityContext(TemporalityContext.RECENT);
                }


                final int experiencer_index = results.indexOf("nonpatient");
                if (experiencer_index > -1) {
                    annotationToken.setExperiencerContext(ExperiencerContext.NONPATIENT);
                } else {
                    annotationToken.setExperiencerContext(ExperiencerContext.PATIENT);
                }

            }
        }


    }

}
