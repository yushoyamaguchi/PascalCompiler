package enshud.s4.compiler;

public class Mold {
	private String name;
	private String mold;
	private boolean isArray;
	private boolean isSub;
	private boolean isParm;
	private int min;
	private int max;
	private int sirialNumber;
	private int subCompNumber;
	private int value;
	private boolean cannotUse;




	public Mold(String name,String mold,boolean isArray) {
		this.name=name;
		this.mold=mold;
		this.isArray=isArray;
	}



	public Mold(String name,String mold,boolean isArray,int min,int max,int sirialNumber,boolean isSub) {
		this.name=name;
		this.mold=mold;
		this.isArray=isArray;
		this.sirialNumber=sirialNumber;
		this.min=min;
		this.max=max;
		this.isSub=isSub;
	}

	public Mold(String name,String mold,boolean isArray,int min,int max,int sirialNumber) {
		this.name=name;
		this.mold=mold;
		this.isArray=isArray;
		this.sirialNumber=sirialNumber;
		this.min=min;
		this.max=max;
	}

	public Mold(String name,String mold,boolean isArray,int sirialNumber) {
		this.name=name;
		this.mold=mold;
		this.isArray=isArray;
		this.sirialNumber=sirialNumber;
	}


	public Mold(String name,String mold,boolean isArray,int sirialNumber,boolean isSub) {
		this.name=name;
		this.mold=mold;
		this.isArray=isArray;
		this.sirialNumber=sirialNumber;
		this.isSub=isSub;
	}

	public Mold(String name,String mold,boolean isArray,int sirialNumber,boolean isSub,boolean isParm,int subCompNumber) {
		this.name=name;
		this.mold=mold;
		this.isArray=isArray;
		this.sirialNumber=sirialNumber;
		this.isSub=isSub;
		this.isParm=isParm;
		this.subCompNumber=subCompNumber;
	}

	public Mold(boolean isSub,String name,String mold,boolean isArray) {
		this.name=name;
		this.mold=mold;
		this.isArray=isArray;
		this.isSub=isSub;
	}

	public String getName() {
		return name;
	}
	public void setName(String s) {
		name=s;
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
	public void setIsSub(boolean b) {
		isSub=b;
	}
	public boolean getIsSub() {
		return isSub;
	}

	public void setIsParm(boolean b) {
		isParm=b;
	}
	public boolean getIsParm() {
		return isParm;
	}
	public void setIsArray(boolean b) {
		isArray=b;
	}
	public void setMinMax(int min,int max) {
		this.min=min;
		this.max=max;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int i) {
		value=i;
	}
	public int getSirialNumber() {
		return sirialNumber;
	}
	public void setSirialNumber(int i) {
		sirialNumber=i;
	}

	public boolean getCannotUse() {
		return cannotUse;
	}

	public void setCannotUse(boolean cannotUse) {
		this.cannotUse = cannotUse;
	}

	public int getSubCompNumber() {
		return subCompNumber;
	}

	public void setSubCompNumber(int subCompNumber) {
		this.subCompNumber = subCompNumber;
	}
}
