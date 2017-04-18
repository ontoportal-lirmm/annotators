package org.sifrproject.annotations.output;


import org.sifrproject.annotations.api.output.AbstractOutputGeneratorDispatcher;
import org.sifrproject.annotations.output.brat.BratOutputGenerator;
import org.sifrproject.annotations.output.brat.EHealthQuaeroBratOutputGenerator;
import org.sifrproject.annotations.output.error.ErrorOutputGenerator;
import org.sifrproject.annotations.output.jena.JenaOutputGenerator;
import org.sifrproject.annotations.output.json.JSONLazyOutputGenerator;

public class LIRMMOutputGeneratorDispatcher extends AbstractOutputGeneratorDispatcher {

    public static final String ERROR_OUTPUT = "error";

    @SuppressWarnings("HardcodedFileSeparator")
    public LIRMMOutputGeneratorDispatcher() {
        super();
        registerGenerator("json", new JSONLazyOutputGenerator());
        registerGenerator("rdf", new JenaOutputGenerator("rdf/xml"));
        registerGenerator("turtle", new JenaOutputGenerator("ttl"));
        registerGenerator("quaero", new EHealthQuaeroBratOutputGenerator(false, false, false));
        registerGenerator("quaerodg", new EHealthQuaeroBratOutputGenerator(true, false, false));
        registerGenerator("quaerosg", new EHealthQuaeroBratOutputGenerator(true, true, false));
        registerGenerator("quaerodgsg", new EHealthQuaeroBratOutputGenerator(true, true, false));
        registerGenerator("quaeroimg", new EHealthQuaeroBratOutputGenerator(false, false, true));
        registerGenerator("quaerodgimg", new EHealthQuaeroBratOutputGenerator(true, false, true));
        registerGenerator("brat", new BratOutputGenerator());
        registerGenerator(ERROR_OUTPUT, new ErrorOutputGenerator());
    }

}
