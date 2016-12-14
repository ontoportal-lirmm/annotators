package org.sifrproject.scoring;

import org.junit.Test;
import org.sifrproject.scoring.cvalue.CValueEvaluator;
import org.sifrproject.scoring.cvalue.CValueTerm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestCValue {

	@Test
	public void test() {
		// test list
		ArrayList<String> termList = new ArrayList<>();
		for(int i=0;i<5;i++) termList.add("melanoma");
		for(int i=0;i<3;i++) termList.add("skin melanoma");

		// ref cvalues
		HashMap<String,Double>  refCValue    = new HashMap<>(2);
		HashMap<String,Integer> refFrequency = new HashMap<>(2);
		refFrequency.put("skin melanoma",3);
		refFrequency.put("melanoma",     5);  
		refCValue.put("skin melanoma",3.);   // not nested, log2(2 words)=1  *  3(frequency)
		refCValue.put("melanoma",     0.);   // nested but  log2(1 words)=0 

		// compute and test
		List<CValueTerm> cvalueTerms  = new CValueEvaluator(termList).getTerms(true);
		HashSet<String> terms = new HashSet<>(cvalueTerms.size());
		for(CValueTerm ct: cvalueTerms){
			String term = ct.getTerm();
			int    freq = ct.getFrequency();
			double cval = ct.getCValue();
			assertEquals("frequency of '"+term+"'", (int)refFrequency.get(term), freq);
			assertEquals("cvalue of '"+term+"'",         refCValue.get(term),    cval, .00001);
			terms.add(term);
		}
		assertEquals("cvalueTerms size: "+cvalueTerms.size(), 2, cvalueTerms.size());
	}	
}
