package org.sifrproject.scoring.cvalue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class CValueEvaluator {

    private ArrayList<CValueTerm> terms;
    private static final int MIN_FREQUENCY = 1;

    public CValueEvaluator(List<String> terms){
        this.terms = new ArrayList<>(terms.size());
        HashMap<String, Integer> index = new HashMap<>(terms.size());

        // fill this.terms
        for (String term : terms) {
            String normalizeTerm = normalizeTerm(term);

            // add the term if not already in the list,
            // otherwise increase its frequency
            if(!index.containsKey(normalizeTerm)) {
                index.put(normalizeTerm,this.terms.size());
                this.terms.add(new CValueTerm(normalizeTerm));
            }else {
                int i = index.get(normalizeTerm);
                this.terms.get(i).incFrequency();
            }
        }

        // filter EvaluatedTerm with too low frequency
        for (Iterator<CValueTerm> it=this.terms.iterator(); it.hasNext();)
            if (it.next().getFrequency() < MIN_FREQUENCY)
                it.remove();


        this.compute();
    }
    
    public static String normalizeTerm(String term){
    	return term.trim().toLowerCase();
    }


    private void compute(){
        double ln2 = Math.log(2.);

        // sort terms by word number
        terms.sort(CValueTerm.wordNumberComparator);

        for(int i=0;i<terms.size();i++) {
            CValueTerm term = terms.get(i);

            // find other term containing this one
            double containerSum = 0;
            int containerCount = 0;

            int j=0;
            CValueTerm jTerm = terms.get(j);
            while(j<i && jTerm.getWordNumber() > term.getWordNumber()){
                if(jTerm.contains(term)){
                    containerSum += jTerm.getFrequency();
                    containerCount++;
                }
                j++;
                jTerm = terms.get(j);
            }

            // compute cvalue
            double factor = term.getFrequency();
            if (containerCount>0)
                    factor -= containerSum/containerCount;

            double cvalue = Math.log(term.getWordNumber()) / ln2 * factor;  // rm log(... <+1> )
            term.setCValue(cvalue);
        }
    }

    public List<CValueTerm> getTerms(boolean sort){
        if(sort)
            terms.sort(CValueTerm.importanceComparator);
        return terms;
    }
}
