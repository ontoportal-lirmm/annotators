package org.sifrproject.annotations.api.output;

/**
 * This interface specifies the output of the annotator
 */
public interface AnnotatorOutput {
    /**
     * The content of the output
     * @return the content of the output
     */
    String getContent();

    /**
     * Get the mime type of the content
     * @return the mime type of the content
     */
    String getMimeType();
}
