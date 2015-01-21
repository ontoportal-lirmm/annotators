package org.sifrproject.annotators;

public class SIFRAnnotatorServlet extends AbstractAnnotatorServlet {
    private static final long serialVersionUID = 2833332884636729837L;

    @Override
    protected String getAnnotatorBaseURL() {
        return   AnnotatorUtils.getBioPortalUri() + "/annotator?" 
               + "apikey=fc4abd1d-ca3e-4e21-96f0-15a16e030889&"
               + AnnotatorUtils.getBioPortalOntologiesURLParameter("SIFR") +"&";
    }
}
