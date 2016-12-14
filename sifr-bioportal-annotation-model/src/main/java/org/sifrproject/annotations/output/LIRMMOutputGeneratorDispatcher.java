package org.sifrproject.annotations.output;


import org.sifrproject.annotations.api.output.AbstractOutputGeneratorDispatcher;
import org.sifrproject.annotations.output.brat.BratOutputGenerator;
import org.sifrproject.annotations.output.jena.JenaOutputGenerator;
import org.sifrproject.annotations.output.error.ErrorOutputGenerator;
import org.sifrproject.annotations.output.json.JSONOutputGenerator;

public class LIRMMOutputGeneratorDispatcher extends AbstractOutputGeneratorDispatcher {

    public LIRMMOutputGeneratorDispatcher() {
        super();
        registerGenerator("json", new JSONOutputGenerator());
        registerGenerator("rdf", new JenaOutputGenerator("rdf/xml"));
        registerGenerator("turtle", new JenaOutputGenerator("ttl"));
        registerGenerator("brat", new BratOutputGenerator());
        registerGenerator("error", new ErrorOutputGenerator());
    }

}
