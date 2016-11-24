package org.sifrproject.parameters;


import fr.lirmm.advance.annotatorapi.umls.groups.UMLSGroup;
import fr.lirmm.advance.annotatorapi.umls.groups.UMLSGroupIndex;
import fr.lirmm.advance.annotatorapi.umls.groups.UMLSSemanticGroupsLoader;
import org.sifrproject.util.UrlParameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class GroupParameterHandler {
    private GroupParameterHandler() {
    }

    public static String processGroupParameter(UrlParameters parameters, String initialSemanticTypes, String groups) {
        String unknownGroups = "";
        parameters.remove("groups");

        String[] groupValues = groups.split(",");


        UMLSGroupIndex groupIndex = UMLSSemanticGroupsLoader.load();

        List<String> finalTypeParameters = new ArrayList<>();
            /*
             * If the user supplies both the groups= parameter and the semantic_types= parameter, we should keep the values
             * specified in the latter as we rewrite the url parameter
             */
        if (!initialSemanticTypes.isEmpty()) {
            String[] initialTypes = initialSemanticTypes.split(",");
            finalTypeParameters.addAll(Arrays.asList(initialTypes));
        }


        for (String groupName : groupValues) {
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
        return unknownGroups;

    }
}
