package org.sifrproject.scoring;

import java.util.Set;

public class Score {
    public Set<String> annotatedTerms;
    public double score;
    
    Score(Set<String> annotatedTerms,double score){
        this.annotatedTerms=annotatedTerms; 
        this.score=score;
    }
}
