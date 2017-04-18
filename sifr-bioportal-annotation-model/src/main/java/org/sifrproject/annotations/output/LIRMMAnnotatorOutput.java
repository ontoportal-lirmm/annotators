package org.sifrproject.annotations.output;


import org.sifrproject.annotations.api.output.AnnotatorOutput;

/**
 * Default implementation of AnnotatorOutput
 */
public class LIRMMAnnotatorOutput implements AnnotatorOutput {
    private final String content;
    private final String mimeType;

    public LIRMMAnnotatorOutput(final String content, final String mimeType) {
        this.content = content;
        this.mimeType = mimeType;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }
}
