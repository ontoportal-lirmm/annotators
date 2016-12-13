package org.sifrproject.annotations.output;


import org.sifrproject.annotations.api.output.AbstractOutputGeneratorDispatcher;
import org.sifrproject.annotations.output.brat.BratOutputGenerator;
import org.sifrproject.annotations.output.jena.JenaOutputGenerator;
import org.sifrproject.annotations.output.json.lazy.JSONLazyOutputGenerator;

public class LIRMMOutputGeneratorDispatcher extends AbstractOutputGeneratorDispatcher {

    public LIRMMOutputGeneratorDispatcher(String annotatorURI) {
        super();
        registerGenerator("json", new JSONLazyOutputGenerator());
        registerGenerator("rdf", new JenaOutputGenerator("rdf/xml", annotatorURI));
        registerGenerator("turtle", new JenaOutputGenerator("ttl", annotatorURI));
        registerGenerator("brat", new BratOutputGenerator());
    }

}
