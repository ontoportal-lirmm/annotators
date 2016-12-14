package org.sifrproject.annotations.umls;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class UMLSSemanticGroupsLoader {
    private final static Logger logger = LoggerFactory.getLogger(UMLSSemanticGroupsLoader.class);

    private UMLSSemanticGroupsLoader() {
    }

    public static UMLSGroupIndex load() {
        return load(UMLSSemanticGroupsLoader.class.getResourceAsStream("/semgroups.ssv"));
    }

    public static UMLSGroupIndex load(InputStream stream) {
        UMLSGroupIndex groupIndex = new UMLSGroupIndex();
        if (stream == null) {
            logger.error("Invalid stream");
        } else {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                reader.lines().forEach(line -> {
                    String[] fields = line.split(" ");
                    String name = fields[0];
                    String typeString = fields[1];
                    String[] types = typeString.split(",");
                    UMLSGroup group = new UMLSGroup(name);
                    for (String type : types) {
                        group.addType(type);
                        groupIndex.addGroupByType(type, group);
                    }
                    groupIndex.addGroupByName(group);
                });
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage());
            }

        }
        return groupIndex;
    }
}
