package org.sifrproject.annotators;

public class IBCAnnotatorServlet extends AbstractAnnotatorServlet {
    private static final long serialVersionUID = 7156067787669333735L;

    @Override
    protected String getAnnotatorBaseURL() {
    	annotatorURI = "http://bioportal.lirmm.fr/ibc_annotator";
        return   AnnotatorUtils.getBioPortalUri() + "/annotator?" 
               + AnnotatorUtils.getBioPortalOntologiesURLParameter("IBC") +"&";
    }
}
