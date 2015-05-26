package org.sifrproject.annotators;

/**
 * Created by emonet on 26/05/15.
 */
public class AnnotatorPlusServlet extends AbstractAnnotatorServlet {
    private static final long serialVersionUID = -5389865918905005981L;

    @Override
    protected String getAnnotatorBaseURL() {
        annotatorURI = "http://vm-bioportal-vincent:8080/annotator";
        return annotatorURI + "?";
    }
}