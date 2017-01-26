package org.sifrproject.parameters;

import org.sifrproject.annotations.umls.UMLSGroup;
import org.sifrproject.annotations.umls.UMLSGroupIndex;
import org.sifrproject.annotations.umls.UMLSSemanticGroupsLoader;
import org.sifrproject.parameters.exceptions.InvalidParameterException;
import org.sifrproject.parameters.api.ParameterHandler;
import org.sifrproject.postannotation.api.PostAnnotationRegistry;
import org.sifrproject.util.UrlParameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SemanticGroupParameterHandler implements ParameterHandler {
    public SemanticGroupParameterHandler() {
    }

    @Override
    public void processParameter(UrlParameters parameters, PostAnnotationRegistry postAnnotationRegistry) throws InvalidParameterException {
        String unknownGroups = "";
        String[] initialSemanticTypes = parameters.get("semantic_types");
        String groups = parameters.getFirst("semantic_groups","");
        parameters.remove("semantic_groups");

        UMLSGroupIndex groupIndex = UMLSSemanticGroupsLoader.load();

        List<String> finalTypeParameters = new ArrayList<>();
            /*
             * If the user supplies both the groups= parameter and the semantic_types= parameter, we should keep the values
             * specified in the latter as we rewrite the url parameter
             */
        if (initialSemanticTypes != null && initialSemanticTypes.length > 0) {
            finalTypeParameters.addAll(Arrays.asList(initialSemanticTypes));
        }


        for (String groupName : groups.split(",")) {
            UMLSGroup group = groupIndex.getGroupByName(groupName);
            if (group != null) {
                finalTypeParameters.addAll(group.types());
            } else {
                //This allows us to handle, later on, the error case where one of the groups specified is invalid
                unknownGroups += " " + groupName;
            }
        }
        String[] typesParameter = new String[finalTypeParameters.size()];
        for (int i = 0; i < typesParameter.length; i++) {
            typesParameter[i] = finalTypeParameters.get(i);
        }

        parameters.put("semantic_types", typesParameter);
        if (!unknownGroups.isEmpty()) {
            throw new InvalidParameterException(String.format("Invalid group parameter values -- %s", unknownGroups));
        }
    }

}
