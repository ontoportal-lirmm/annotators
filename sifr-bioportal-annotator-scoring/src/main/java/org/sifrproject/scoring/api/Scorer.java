package org.sifrproject.scoring.api;

import org.sifrproject.annotations.api.model.Annotation;
import org.sifrproject.annotations.api.model.ScoreableElement;

import java.util.List;
import java.util.Map;

public interface

Scorer {
    Map<String, ScoreableElement> compute(List<Annotation> annotations);
}
