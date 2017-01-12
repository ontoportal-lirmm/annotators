package org.sifrproject.postannotation;

import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.postannotation.api.PostAnnotationRegistry;
import org.sifrproject.postannotation.api.PostAnnotator;

import java.util.ArrayList;
import java.util.List;

public class LIRMMPostAnnotationRegistry implements PostAnnotationRegistry {

    private final List<PostAnnotator> postAnnotators = new ArrayList<>();

    @Override
    public void registerPostAnnotator(PostAnnotator postAnnotator) {
        postAnnotators.add(postAnnotator);
    }

    @Override
    public void apply(List<Annotation> annotations, String sourceText) {
        for (PostAnnotator postAnnotator : postAnnotators){
            postAnnotator.postAnnotate(annotations, sourceText);
        }
    }

    @Override
    public void clear() {
        postAnnotators.clear();
    }


}
