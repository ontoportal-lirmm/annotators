package org.sifrproject.annotatorclient.api;


import java.io.IOException;

public interface BioPortalAnnotator {
    String runQuery(BioPortalAnnotatorQuery query) throws IOException;
}
