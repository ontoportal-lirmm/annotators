package org.sifrproject.scoring;

import java.util.ArrayList;


public class CvalueTerme {
	public ArrayList<String> Position = new ArrayList<String>();
	public int Freq;
	public double cvalue;
	public CvalueTerme (ArrayList<String> Position, int Freq, double cvalue){
	    this.Freq=Freq;
	    this.Position=Position;
	    this.cvalue=cvalue;
    }
}
