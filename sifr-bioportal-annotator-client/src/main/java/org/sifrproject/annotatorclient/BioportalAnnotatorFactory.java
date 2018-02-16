package org.sifrproject.annotatorclient;


import org.sifrproject.annotatorclient.api.BioPortalAnnotator;

public class BioportalAnnotatorFactory {
    public static BioPortalAnnotator createDefaultAnnotator(final String uri, final String apiKey) {
        return new DefaultBioPortalAnnotator(uri, apiKey);
    }
}
