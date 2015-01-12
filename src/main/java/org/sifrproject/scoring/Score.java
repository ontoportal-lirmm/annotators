package org.sifrproject.scoring;

import java.util.ArrayList;

import org.json.simple.JSONObject;

public class Score {
    public ArrayList<String> TermesAnnotes;
    public int score;
    public double scoreF;
    public double scoreF2;
    public JSONObject annotatedClass;
    public Boolean trie; 
    public Boolean trieF;
    public Boolean trieF2;
    public Boolean herarchie; 
    public int ClassementScore;
    public int ClassementScoreF;
    public int ClassementScoreF2;
    
    Score(ArrayList<String> TermesAnnotes,int score,double scoreF, double scoreF2,JSONObject annotatedClass,Boolean trie,Boolean trieF,Boolean trieF2,Boolean herarchie,int ClassementScore, int ClassementScoreF, int ClassementScoreF2){
        this.TermesAnnotes=TermesAnnotes; 
        this.score=score;
        this.scoreF=scoreF;
        this.scoreF2=scoreF2;
        this.annotatedClass=annotatedClass;
        this.trie=trie;
        this.trieF=trieF; 
        this.trieF2=trieF2; 
        this.herarchie=herarchie;
        this.ClassementScore=ClassementScore;
        this.ClassementScoreF=ClassementScoreF;
        this.ClassementScoreF2=ClassementScoreF2;
    }
}
