package org.sifrproject.postannotation;


import edu.utah.bmi.nlp.core.SimpleParser;
import edu.utah.bmi.nlp.core.Span;
import edu.utah.bmi.nlp.fastcontext.FastContext;
import org.apache.commons.io.IOUtils;
import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.annotations.api.model.AnnotationToken;
import org.sifrproject.annotations.api.model.AnnotationTokens;
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

    private final boolean includeNegation;
    private final boolean includeExperiencer;
    private final boolean includeTemporality;
    private final FastContext context;

    public ContextPostAnnotationFilter(final String language, final boolean includeNegation, final boolean includeExperiencer, final boolean includeTemporality) throws IOException {
        this.includeNegation = includeNegation;
        this.includeExperiencer = includeExperiencer;
        this.includeTemporality = includeTemporality;
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


                if (includeNegation) {
                    final int index = results.indexOf("negated");
                    if (index >= 0) {
                        annotationToken.setNegationContext(NegationContext.valueOf(results
                                .get(index)
                                .toUpperCase()));
                    }
                }
                if (includeTemporality) {
                    int index = results.indexOf("historical");
                    if (index == -1) {
                        index = results.indexOf("hypothetical");
                    }
                    if (index >= 0) {
                        annotationToken.setTemporalityContext(TemporalityContext.valueOf(results
                                .get(index)
                                .toUpperCase()));
                    }
                }
                if (includeExperiencer) {
                    final int index = results.indexOf("nonpatient");
                    if (index > -1) {
                        annotationToken.setExperiencerContext(ExperiencerContext.OTHER);
                    }
                }
            }
        }


    }

}
