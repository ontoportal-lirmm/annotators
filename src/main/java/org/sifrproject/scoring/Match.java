package org.sifrproject.scoring;

public class Match {
    public String term;
    public MatchType type;
    
    public Match(String term, MatchType type){
        this.term = term;
        this.type = type;
    }
    public Match(String term, String type){
        this.term = term;
        this.type = MatchType.valueOf(type);
    }
}
