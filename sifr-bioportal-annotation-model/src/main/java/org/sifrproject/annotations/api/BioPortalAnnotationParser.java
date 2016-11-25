package org.sifrproject.annotations.api;

import java.io.IOException;
import java.util.List;

public interface BioPortalAnnotationParser {
    void parseAnnotations(String queryResponse) throws IOException;

    List<BioPortalAnnotation> annotations();
}
