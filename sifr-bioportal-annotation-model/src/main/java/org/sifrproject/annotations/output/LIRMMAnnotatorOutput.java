package org.sifrproject.annotations.output;


import org.sifrproject.annotations.api.output.AnnotatorOutput;

public class LIRMMAnnotatorOutput implements AnnotatorOutput {
    private String content;
    private String mimeType;

    public LIRMMAnnotatorOutput(String content, String mimeType) {
        this.content = content;
        this.mimeType = mimeType;
    }

    public String getContent() {
        return content;
    }

    public String getMimeType() {
        return mimeType;
    }
}
