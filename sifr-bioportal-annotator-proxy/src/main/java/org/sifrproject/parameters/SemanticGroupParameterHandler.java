package org.sifrproject.parameters;

import org.sifrproject.annotations.umls.UMLSGroup;
import org.sifrproject.annotations.umls.UMLSGroupIndex;
import org.sifrproject.annotations.umls.UMLSSemanticGroupsLoader;
import org.sifrproject.parameters.api.ParameterHandler;
import org.sifrproject.parameters.exceptions.InvalidParameterException;
import org.sifrproject.postannotation.SemanticGroupAndTypePostAnnotationFilter;
import org.sifrproject.postannotation.api.PostAnnotationRegistry;
import org.sifrproject.util.RequestGenerator;

import java.util.*;
import java.util.stream.Collectors;

public final class SemanticGroupParameterHandler implements ParameterHandler {

    @Override
    public void processParameter(final RequestGenerator parameters, final PostAnnotationRegistry postAnnotationRegistry) throws InvalidParameterException {
        final String initialSemanticTypes = parameters.get("semantic_types");
        final String groups = parameters.getFirst("semantic_groups", "");
        final String uniqueGroup = parameters.getFirst("unique_groups", "");
        parameters.remove("semantic_groups");

        final UMLSGroupIndex groupIndex = UMLSSemanticGroupsLoader.load();

        final List<String> finalTypeParameters = new ArrayList<>();
            /*
             * If the user supplies both the groups= parameter and the semantic_types= parameter, we should keep the values
             * specified in the latter as we rewrite the url parameter
             */
        if ((initialSemanticTypes != null) && !initialSemanticTypes.isEmpty()) {
            finalTypeParameters.addAll(Arrays.asList(initialSemanticTypes.split(",")));
        }

        final String unknownGroups = generateUnknownGroupList(groupIndex, finalTypeParameters, groups);

        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < finalTypeParameters.size(); i++) {
            stringBuilder.append(finalTypeParameters.get(i));
            if (i < (finalTypeParameters.size() - 1)) {
                stringBuilder.append(",");
            }
        }

        parameters.put("semantic_types", stringBuilder.toString());
        if (!unknownGroups.isEmpty()) {
            throw new InvalidParameterException(String.format("Invalid group parameter values -- %s", unknownGroups));
        }
        handleUniqueGroup(postAnnotationRegistry, groupIndex, uniqueGroup, groups, initialSemanticTypes);
    }

    private void handleUniqueGroup(final PostAnnotationRegistry postAnnotationRegistry, final UMLSGroupIndex groupIndex, final String uniqueGroup, final String groups, final String initialSemanticTypes) {
        if (!uniqueGroup.isEmpty() && uniqueGroup.equals("true")) {
            final List<UMLSGroup> groupList = (groups.isEmpty()) ? Collections.emptyList() : Arrays
                    .stream(groups.split(","))
                    .map(groupIndex::getGroupByName)
                    .collect(Collectors.toList());

            final List<String> initialSemanticTypeList =
                    ((initialSemanticTypes != null) && initialSemanticTypes.isEmpty())
                            ? Collections.emptyList() :
                            Arrays
                                    .stream(groups.split(","))
                                    .collect(Collectors.toList());

            postAnnotationRegistry.registerPostAnnotator(new SemanticGroupAndTypePostAnnotationFilter(groupList, initialSemanticTypeList));
        }
    }


    private String generateUnknownGroupList(final UMLSGroupIndex groupIndex, final Collection<String> finalTypeParameters, final String groups) {
        final StringBuilder unknownGroups = new StringBuilder();
        for (final String groupName : groups.split(",")) {
            final UMLSGroup group = groupIndex.getGroupByName(groupName);
            if (group != null) {
                group.forEach(finalTypeParameters::add);
            } else {
                //This allows us to handle, later on, the error case where one of the groups specified is invalid
                unknownGroups
                        .append(" ")
                        .append(groupName);
            }
        }
        return unknownGroups.toString();
    }

}
