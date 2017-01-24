package org.sifrproject.annotations.api.model;


import org.sifrproject.annotations.api.model.context.ContextAnnotationElement;

/**
 * Data interface specifying an annotation token
 */
public interface AnnotationToken extends ContextAnnotationElement {
    /**
     * Get the starting character offset of the token in the source text
     * @return the starting character offset of the token in the source text
     */
    int getFrom();

    /**
     * Get the end character offset of the token in the source text
     * @return the end character offset of the token in the source text
     */
    int getTo();

    /**
     * Get the type of the match as specified in NCBO Bioportal (SYN or PREF)
     * @return The type of the match
     */
    String getMatchType();

    /**
     * Get the text of the token
     * @return the text of the token
     */
    String getText();

}
