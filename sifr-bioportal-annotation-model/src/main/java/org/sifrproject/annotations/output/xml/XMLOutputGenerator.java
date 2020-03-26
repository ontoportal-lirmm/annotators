package org.sifrproject.annotations.output.xml;

import org.sifrproject.annotations.api.model.*;
import org.sifrproject.annotations.api.output.AnnotatorOutput;
import org.sifrproject.annotations.api.output.OutputGenerator;
import org.sifrproject.annotations.output.LIRMMAnnotatorOutput;

import java.util.List;

public class XMLOutputGenerator implements OutputGenerator {
    @SuppressWarnings("LawOfDemeter")
    @Override
    public AnnotatorOutput generate(final List<Annotation> annotations, final String annotatorURI, final String sourceText) {
        final StringBuilder contents = new StringBuilder();
        contents.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(System.lineSeparator());
        contents.append("<annotationCollection>").append(System.lineSeparator());
        for (final Annotation annotation : annotations) {
            contents.append("\t<annotation>").append(System.lineSeparator());
            final AnnotatedClass annotatedClass = annotation.getAnnotatedClass();
            if (annotatedClass != null) {
                contents.append("\t\t<annotatedClass>").append(System.lineSeparator());
                contents.append("\t\t\t<id>").append(annotatedClass.getId()).append("</id>").append(System.lineSeparator());
                contents.append("\t\t\t<type>").append(annotatedClass.getType()).append("</type>").append(System.lineSeparator());
                final Links links = annotatedClass.getLinks();
                if (links!=null){
                    final LinkMetadata linkContext = links.getLinksContextMetadata();
                    contents.append("\t\t\t<linksCollection>").append(System.lineSeparator());
                    contents.append("\t\t\t\t<links>").append(System.lineSeparator());

                    contents.append("\t\t\t\t</links>").append(System.lineSeparator());


                    contents.append("\t\t\t</linksCollection>").append(System.lineSeparator());
                }
                contents.append("\t\t</annotatedClass>").append(System.lineSeparator());
            }
            contents.append("\t</annotation>").append(System.lineSeparator());
        }
        contents.append("</annotationCollection>").append(System.lineSeparator());

        return new LIRMMAnnotatorOutput(contents.toString(), "text/xml");
    }
}
