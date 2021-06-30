package enshud.s3.checker;

import java.util.ArrayList;

public class VisitorInfo {
	public ArrayList<String> variableNameList= new ArrayList<String>();
	public ArrayList<String> proNameList= new ArrayList<String>();
	private String variableName;
	private String proName;
	private String mold;
	private boolean isArray;
	private boolean cannotSingle;
	private int min;
	private int max;


	public VisitorInfo() {
		mold="";
		proName="";
		variableName="";
	}

	public String getVariableName() {
		return variableName;
	}
	public void setVariableName(String s) {
		variableName=s;
	}
	public String getProName() {
		return proName;
	}
	public void setProName(String s) {
		proName=s;
	}
	public String getMold() {
		return mold;
	}
	public void setMold(String s) {
		mold=s;
	}
	public int getMin() {
		return min;
	}
	public void setMin(int i) {
		min=i;
	}
	public int getMax() {
		return max;
	}
	public void setMax(int i) {
		max=i;
	}
	public boolean getIsArray() {
		return isArray;
	}
	public void setIsArray(boolean b) {
		isArray=b;
	}
	public boolean getCannotSingle() {
		return cannotSingle;
	}
	public void setCannotSingle(boolean b) {
		cannotSingle=b;
	}
	public void setMinMax(int min,int max) {
		this.min=min;
		this.max=max;
	}

}
