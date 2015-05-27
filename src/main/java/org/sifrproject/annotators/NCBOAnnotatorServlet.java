package org.sifrproject.annotators;

public class NCBOAnnotatorServlet extends AbstractAnnotatorServlet {
    private static final long serialVersionUID = -5389865918905005981L;

    @Override
    protected String getAnnotatorBaseURL() {
    	annotatorURI = "http://data.bioontology.org/annotator";
        return "http://data.bioontology.org/annotator?";
    }
}
