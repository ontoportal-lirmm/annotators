package org.sifrproject.util;

import java.util.Map;

public interface RequestGenerator extends Map<String, String> {
    String getFirst(String name, String defaultValue);

}
