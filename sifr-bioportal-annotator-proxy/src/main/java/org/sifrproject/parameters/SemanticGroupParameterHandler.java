package org.sifrproject.parameters;

import org.sifrproject.annotations.umls.UMLSGroup;
import org.sifrproject.annotations.umls.UMLSGroupIndex;
import org.sifrproject.annotations.umls.UMLSSemanticGroupsLoader;
import org.sifrproject.parameters.api.ParameterHandler;
import org.sifrproject.parameters.exceptions.InvalidParameterException;
import org.sifrproject.postannotation.api.PostAnnotationRegistry;
import org.sifrproject.util.RequestGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SemanticGroupParameterHandler implements ParameterHandler {
    public SemanticGroupParameterHandler() {
    }

    @Override
    public void processParameter(RequestGenerator parameters, PostAnnotationRegistry postAnnotationRegistry) throws InvalidParameterException {
        String unknownGroups = "";
        String initialSemanticTypes = parameters.get("semantic_types");
        String groups = parameters.getFirst("semantic_groups","");
        parameters.remove("semantic_groups");

        UMLSGroupIndex groupIndex = UMLSSemanticGroupsLoader.load();

        List<String> finalTypeParameters = new ArrayList<>();
            /*
             * If the user supplies both the groups= parameter and the semantic_types= parameter, we should keep the values
             * specified in the latter as we rewrite the url parameter
             */
        if (initialSemanticTypes != null && !initialSemanticTypes.isEmpty()) {
            finalTypeParameters.addAll(Arrays.asList(initialSemanticTypes.split(",")));
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
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < finalTypeParameters.size(); i++) {
            stringBuilder.append(finalTypeParameters.get(i));
            if(i<finalTypeParameters.size()-1){
                stringBuilder.append(",");
            }
        }

        parameters.put("semantic_types", stringBuilder.toString());
        if (!unknownGroups.isEmpty()) {
            throw new InvalidParameterException(String.format("Invalid group parameter values -- %s", unknownGroups));
        }
    }

}
