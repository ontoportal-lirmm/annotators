package org.sifrproject.annotators;

public class IBCAnnotatorServlet extends AbstractAnnotatorServlet {
    private static final long serialVersionUID = 7156067787669333735L;

    @Override
    protected String getAnnotatorBaseURL() {
        return   AnnotatorUtils.getBioPortalUri() + "/annotator?" 
               + AnnotatorUtils.getBioPortalOntologiesURLParameter("IBC") +"&";
    }
}
