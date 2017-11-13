package org.sifrproject.postannotation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.sifrproject.annotations.api.input.AnnotationParser;
import org.sifrproject.annotations.api.model.AnnotatedClass;
import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.annotations.api.model.AnnotationToken;
import org.sifrproject.annotations.umls.UMLSGroup;
import org.sifrproject.postannotation.api.PostAnnotationFilter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Adds score annotations
 */
public class SemanticGroupAndTypePostAnnotationFilter implements PostAnnotationFilter {


    private final Map<UMLSGroup, Double> userGroups;
    private final List<String> userSemanticTypes;


    public SemanticGroupAndTypePostAnnotationFilter(final Collection<UMLSGroup> userGroups, final List<String> userSemanticTypes) {
        this.userGroups = new HashMap<>();


        double currentWeight = userGroups.size();
        for (final UMLSGroup group : userGroups) {
            group.setWeight(currentWeight);
            this.userGroups.put(group, currentWeight);
            currentWeight -= 1;

        }

        this.userSemanticTypes = new ArrayList<>(Collections.unmodifiableList(userSemanticTypes));
    }

    @Override
    public void postAnnotate(final List<Annotation> annotations, final String text) {
        final Map<AnnotationToken, List<Annotation>> perTokenAnnotations = AnnotationParser.perTokenAnnotations(annotations);
        for (final Map.Entry<AnnotationToken, List<Annotation>> annotationTokenListEntry : perTokenAnnotations.entrySet()) {
            final List<Annotation> tokenAnnotations = annotationTokenListEntry.getValue();
            tokenAnnotations.forEach(this::keepUniqueGroupForAnnotation);
            if (tokenAnnotations.size() > 1) {
                tokenAnnotations
                        .stream()
                        .filter(annotation -> (annotation
                                .getAnnotatedClass()
                                .getSemanticGroups() != null) && (!annotation
                                .getAnnotatedClass()
                                .getSemanticGroups()
                                .isEmpty()))
                        .collect(Collectors.toList())
                        .sort((o1, o2) ->
                                Double.compare(
                                        o2
                                                .getAnnotatedClass()
                                                .getSemanticGroups()
                                                .iterator()
                                                .next()
                                                .getWeight(),
                                        o1
                                                .getAnnotatedClass()
                                                .getSemanticGroups()
                                                .iterator()
                                                .next()
                                                .getWeight()));
                final Collection<Annotation> best = new ArrayList<>();
                best.add(tokenAnnotations.get(0));
                tokenAnnotations.retainAll(best);
            }
        }
    }

    @SuppressWarnings("LawOfDemeter")
    private void keepUniqueGroupForAnnotation(final Annotation annotation) {
        final AnnotatedClass annotatedClass = annotation.getAnnotatedClass();
        List<UMLSGroup> groups = new ArrayList<>(annotatedClass
                .getSemanticGroups());

        assignGroupWeights(groups);

        groups.sort(Comparator
                .comparingDouble(UMLSGroup::getWeight)
                .reversed());
        groups = groups
                .stream()
                .filter(userGroups::containsKey)
                .collect(Collectors.toList());
        if (!groups.isEmpty()) {
            final List<UMLSGroup> finalGroup = new ArrayList<>();
            finalGroup.add(groups
                    .get(0));
            annotatedClass.setSemanticGroups(finalGroup);
        }
    }

    private void assignGroupWeights(final Iterable<UMLSGroup> groups) {
        for (final UMLSGroup group : groups) {
            if (userGroups.containsKey(group)) {
                group.setWeight(userGroups.get(group));
            }
        }
    }

    @SuppressWarnings("MethodParameterOfConcreteClass")
    private static class WeightedSemanticType implements Comparable<WeightedSemanticType> {
        private final String type;
        private final double weight;

        WeightedSemanticType(final String type, final double weight) {
            this.type = type;
            this.weight = weight;
        }

        String getType() {
            return type;
        }

        double getWeight() {
            return weight;
        }

        @Override
        public int compareTo(final WeightedSemanticType o) {
            return Double.compare(o.weight, weight);
        }

        @SuppressWarnings("All")
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            WeightedSemanticType that = (WeightedSemanticType) o;

            return new EqualsBuilder()
                    .append(getWeight(), that.getWeight())
                    .append(getType(), that.getType())
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(getType())
                    .append(getWeight())
                    .toHashCode();
        }
    }


}
