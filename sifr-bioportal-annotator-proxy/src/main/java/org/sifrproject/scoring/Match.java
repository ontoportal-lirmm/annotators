package org.sifrproject.scoring;

class Match {
    String term;
    MatchType type;
    
    public Match(String term, MatchType type){
        this.term = term;
        this.type = type;
    }

    Match(String term, String type) {
        this.term = term;
        this.type = MatchType.valueOf(type);
    }
}
