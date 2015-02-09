package org.sifrproject.annotators;

public class SIFRAnnotatorServlet extends AbstractAnnotatorServlet {
    private static final long serialVersionUID = 2833332884636729837L;

    @Override
    protected String getAnnotatorBaseURL() {
        return   AnnotatorUtils.getBioPortalUri() + "/annotator?" 
               + AnnotatorUtils.getBioPortalOntologiesURLParameter("SIFR") +"&";
    }
}
