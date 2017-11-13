package org.sifrproject.annotations.umls;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Utility class that allows to load (from the classpath, by default /semgroups.ssv, which should be bundled
 * with the maven dependency) the UMLS semantic group index
 */
public final class UMLSSemanticGroupsLoader {
    private final static Logger logger = LoggerFactory.getLogger(UMLSSemanticGroupsLoader.class);

    private UMLSSemanticGroupsLoader() {
    }

    /**
     * Load the index from the default classpath location and return an instance of {@code UMLSGroupIndex}
     * @return and instance of {@code UMLSGroupIndex} where the semantic groups are loaded
     */
    public static UMLSGroupIndex load() {
        return load(UMLSSemanticGroupsLoader.class.getResourceAsStream("/semgroups.ssv"));
    }

    /**
     * Load the index from from the supplied InputStream and return an instance of {@code UMLSGroupIndex}
     * @return and instance of {@code UMLSGroupIndex} where the semantic groups are loaded
     */
    @SuppressWarnings("all")
    public static UMLSGroupIndex load(InputStream stream) {
        DefaultUMLSGroupIndex groupIndex = new DefaultUMLSGroupIndex();
        if (stream == null) {
            logger.error("Invalid stream");
        } else {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                String line = reader.readLine();
                while(line !=null && !line.isEmpty()){
                    String[] fields = line.split(" ");
                    String name = fields[0];
                    String typeString = fields[1];
                    String[] types = typeString.split(",");
                    DefaultUMLSGroup group = new DefaultUMLSGroup(name);
                    for (String type : types) {
                        group.addType(type);
                        groupIndex.addGroupByType(type, group);
                    }
                    groupIndex.addGroupByName(group);
                    line = reader.readLine();
                }

            } catch (IOException e) {
                logger.error(e.getLocalizedMessage());
            }

        }
        return groupIndex;
    }
}
