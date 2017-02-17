package org.sifrproject.parameters.api;


import org.sifrproject.parameters.exceptions.InvalidParameterException;
import org.sifrproject.postannotation.api.PostAnnotationRegistry;
import org.sifrproject.util.RequestGenerator;

/**
 * Handler for a parameter passed to the Proxy Servlet. A parameter handler should be registered in a {@link ParameterRegistry}.
 * Naming conversion ParameterNameParameterHandler, where ParameterName is the name of the parameter in Java style.
 * For example, for a parameter names semantic_groups the hander name should be SemanticGroupsParameterHandler.
 * For a parameter named 'context', the handler name should be ContextParameterHandler and so forth...
 */
public interface ParameterHandler {
    /**
     * Callback that handles the parameter. Parameter handlers should directly register post-annotation components in
     * the {@link PostAnnotationRegistry} passed as a parameter of the callback.
     *
     * @param parameters             The URL parameters for the current query
     * @param postAnnotationRegistry The post annotation registry
     * @throws InvalidParameterException This exception must be thrown, if the format of the parameter or its options
     *                                   are incorrect or ill-formed.
     */
    void processParameter(RequestGenerator parameters, PostAnnotationRegistry postAnnotationRegistry) throws InvalidParameterException;
}
