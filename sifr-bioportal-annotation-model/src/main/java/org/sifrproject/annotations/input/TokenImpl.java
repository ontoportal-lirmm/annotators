package org.sifrproject.annotations.input;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.sifrproject.annotations.api.input.Token;
import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.annotations.api.model.AnnotationToken;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TokenImpl implements Token {

    private final AnnotationToken annotationToken;
    private final Set<Annotation> annotations;


    public TokenImpl(final AnnotationToken annotationToken) {
        this.annotationToken = annotationToken;
        annotations = new HashSet<>();
    }

    @Override
    public AnnotationToken getAnnotationToken() {
        return annotationToken;
    }

    @Override
    public void addAnnotation(final Annotation annotation) {
        annotations.add(annotation);
    }

    @Override
    public Collection<Annotation> getAnnotations() {
        return Collections.unmodifiableSet(annotations);
    }

    @SuppressWarnings("LawOfDemeter")
    @Override
    public int compareTo(final Token o) {
        return Integer.compare(annotationToken.getFrom(), o.getAnnotationToken().getFrom());
    }


    @Override
    public boolean equals(final Object o) {
        final boolean ret;
        if (this == o) {
            ret = true;
        } else {
            if ((o == null) || (getClass() != o.getClass())) {
                ret = false;
            } else {
                final Token token = (Token) o;

                ret = new EqualsBuilder()
                        .append(getAnnotationToken(), token.getAnnotationToken())
                        .isEquals();
            }
        }
        return ret;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getAnnotationToken())
                .toHashCode();
    }
}
