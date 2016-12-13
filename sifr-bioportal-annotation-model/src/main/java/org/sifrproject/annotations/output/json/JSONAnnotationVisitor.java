package org.sifrproject.annotations.output.json;


import org.sifrproject.annotations.api.model.*;
import org.sifrproject.annotations.api.model.visitor.AnnotationVisitor;

public class JSONAnnotationVisitor implements AnnotationVisitor {
    private StringBuilder outputBuilder;
    private int annotationCounter = 0;
    private boolean isFirstAnnotationToken = true;
    private boolean isFirstHierarchyElement = true;
    private boolean isFirstMapping = true;

    JSONAnnotationVisitor(StringBuilder outputBuilder) {
        this.outputBuilder = outputBuilder;


    }

    @Override
    public void visitBefore(Annotation dispatcher) {
        if (annotationCounter > 0) {
            outputBuilder.append(",");
        }
        outputBuilder.append("{");
    }

    @Override
    public void visitAfter(Annotation dispatcher) {
        if (dispatcher.getScore() >= 0) {
            outputBuilder.append(String.format(",\"score\":\"%f\"", dispatcher.getScore()));
        }
        outputBuilder.append("}");
        annotationCounter++;
    }

    @Override
    public void visitBefore(AnnotatedClass dispatcher) {
        outputBuilder.append("\"annotatedClass\": {");
        outputBuilder.append(String.format("\"@id\": \"%s\",", dispatcher.getId()));
        outputBuilder.append(String.format("\"@type\": \"%s\",", dispatcher.getType()));
    }


    @Override
    public void visitAfter(AnnotatedClass dispatcher) {
        outputBuilder.append(String.format("\"@context\": {\"@vocab\":\"%s\"}", dispatcher.getContextVocab()));
        outputBuilder.append("}");
    }

    @Override
    public void visitBefore(Links dispatcher) {
        outputBuilder.append("\"links\": {");
        outputBuilder.append(String.format("\"self\": \"%s\",", dispatcher.getSelf()));
        outputBuilder.append(String.format("\"ontology\": \"%s\",", dispatcher.getOntology()));
        outputBuilder.append(String.format("\"children\": \"%s\",", dispatcher.getChildren()));
        outputBuilder.append(String.format("\"parents\": \"%s\",", dispatcher.getParents()));
        outputBuilder.append(String.format("\"descendants\": \"%s\",", dispatcher.getDescendants()));
        outputBuilder.append(String.format("\"ancestors\": \"%s\",", dispatcher.getAncestors()));
        outputBuilder.append(String.format("\"instances\": \"%s\",", dispatcher.getInstances()));
        outputBuilder.append(String.format("\"tree\": \"%s\",", dispatcher.getTree()));
        outputBuilder.append(String.format("\"notes\": \"%s\",", dispatcher.getNotes()));
        outputBuilder.append(String.format("\"mappings\": \"%s\",", dispatcher.getMappings()));
        outputBuilder.append(String.format("\"ui\": \"%s\",", dispatcher.getUi()));
    }

    @Override
    public void visitAfter(Links dispatcher) {
        outputBuilder.append("},");
    }

    @Override
    public void visitBefore(LinkContext dispatcher) {
        outputBuilder.append("\"@context\": {");
        outputBuilder.append(String.format("\"self\": \"%s\",", dispatcher.getSelf()));
        outputBuilder.append(String.format("\"ontology\": \"%s\",", dispatcher.getOntology()));
        outputBuilder.append(String.format("\"children\": \"%s\",", dispatcher.getChildren()));
        outputBuilder.append(String.format("\"parents\": \"%s\",", dispatcher.getParents()));
        outputBuilder.append(String.format("\"descendants\": \"%s\",", dispatcher.getDescendants()));
        outputBuilder.append(String.format("\"ancestors\": \"%s\",", dispatcher.getAncestors()));
        outputBuilder.append(String.format("\"instances\": \"%s\",", dispatcher.getInstances()));
        outputBuilder.append(String.format("\"tree\": \"%s\",", dispatcher.getTree()));
        outputBuilder.append(String.format("\"notes\": \"%s\",", dispatcher.getNotes()));
        outputBuilder.append(String.format("\"mappings\": \"%s\",", dispatcher.getMappings()));
        outputBuilder.append(String.format("\"ui\": \"%s\"", dispatcher.getUi()));
    }

    @Override
    public void visitAfter(LinkContext dispatcher) {
        outputBuilder.append("}");
    }


    @Override
    public void visitBefore(AnnotationTokens dispatcher) {
        outputBuilder.append(", \"annotations\": [");
        isFirstAnnotationToken = true;
    }

    @Override
    public void visitAfter(AnnotationTokens dispatcher) {
        outputBuilder.append("]");
    }

    @Override
    public void visitBefore(AnnotationToken dispatcher) {
        if (!isFirstAnnotationToken) {
            outputBuilder.append(",");
        } else {
            isFirstAnnotationToken = false;
        }
        outputBuilder.append("{");
        outputBuilder.append(String.format("\"from\":%d,", dispatcher.getFrom()));
        outputBuilder.append(String.format("\"to\":%d,", dispatcher.getTo()));
        outputBuilder.append(String.format("\"matchType\":\"%s\",", dispatcher.getMatchType()));
        outputBuilder.append(String.format("\"text\":\"%s\"", dispatcher.getText().toUpperCase()));
    }


    @Override
    public void visitAfter(AnnotationToken dispatcher) {
        outputBuilder.append("}");
    }

    @Override
    public void visitBefore(Hierarchy dispatcher) {
        outputBuilder.append(", \"hierarchy\": [");
        isFirstHierarchyElement = true;

    }

    @Override
    public void visitAfter(Hierarchy dispatcher) {
        outputBuilder.append("]");
    }


    @Override
    public void visitBefore(HierarchyElement dispatcher) {
        if (!isFirstHierarchyElement) {
            outputBuilder.append(",");
        }
        outputBuilder.append("{");
        isFirstHierarchyElement = false;

    }

    @Override
    public void visitAfter(HierarchyElement dispatcher) {
        if (dispatcher.getScore() >= 0) {
            outputBuilder.append(String.format(",\"score\": \"%f\"", dispatcher.getScore()));
        }
        outputBuilder.append(String.format(",\"distance\": %d", dispatcher.getDistance()));
        outputBuilder.append("}");
    }


    @Override
    public void visitBefore(Mapping dispatcher) {
        if (!isFirstMapping) {
            outputBuilder.append(",");
        }
        outputBuilder.append("{");
        isFirstMapping = false;
    }

    @Override
    public void visitAfter(Mapping dispatcher) {
        if (dispatcher.getScore() >= 0) {
            outputBuilder.append(String.format(",\"score\": \"%f\"", dispatcher.getScore()));
        }
        outputBuilder.append("}");
    }

    @Override
    public void visitBefore(Mappings dispatcher) {
        outputBuilder.append(", \"mappings\": [");
        isFirstMapping = true;
    }

    @Override
    public void visitAfter(Mappings dispatcher) {
        outputBuilder.append("]");
    }
}
