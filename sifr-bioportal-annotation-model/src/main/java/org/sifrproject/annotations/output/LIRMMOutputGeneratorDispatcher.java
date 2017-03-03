package org.sifrproject.annotations.output;


import org.sifrproject.annotations.api.output.AbstractOutputGeneratorDispatcher;
import org.sifrproject.annotations.output.brat.BratOutputGenerator;
import org.sifrproject.annotations.output.brat.eHealthQuaeroBratOutputGenerator;
import org.sifrproject.annotations.output.error.ErrorOutputGenerator;
import org.sifrproject.annotations.output.jena.JenaOutputGenerator;
import org.sifrproject.annotations.output.json.JSONLazyOutputGenerator;

public class LIRMMOutputGeneratorDispatcher extends AbstractOutputGeneratorDispatcher {

    public LIRMMOutputGeneratorDispatcher() {
        super();
        registerGenerator("json", new JSONLazyOutputGenerator());
        registerGenerator("rdf", new JenaOutputGenerator("rdf/xml"));
        registerGenerator("turtle", new JenaOutputGenerator("ttl"));
        registerGenerator("quaero", new eHealthQuaeroBratOutputGenerator(false));
        registerGenerator("quaerod", new eHealthQuaeroBratOutputGenerator(true));
        registerGenerator("brat", new BratOutputGenerator());
        registerGenerator("error", new ErrorOutputGenerator());
    }

}
