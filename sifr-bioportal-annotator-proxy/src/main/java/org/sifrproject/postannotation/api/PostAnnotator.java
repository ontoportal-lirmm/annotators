package org.sifrproject.postannotation.api;

import org.sifrproject.annotations.api.model.Annotation;

import java.util.List;

/**
 * Appends annotations to an existing Bioportal Annotation Model.
 * The annotations in question should be supported by the annotation model
 */
public interface PostAnnotator {
    /**
     * Perform the post-annotation
     *
     * @param annotations The list of existing annotations
     * @param text        The original text that yielded the annotations. Many NLP-based post-annotation procedures will require
     *                    the original text. Post annotators that do not require the original text can safely ignore the parameter.
     */
    void postAnnotate(List<Annotation> annotations, String text);
}
