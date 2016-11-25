package org.sifrproject.annotatorclient;


import org.sifrproject.annotatorclient.api.BioPortalAnnotator;

public class BioportalAnnotatorFactory {
    public static BioPortalAnnotator createDefaultAnnotator(String uri, String apiKey) {
        return new DefaultBioPortalAnnotator(uri, apiKey);
    }
}
