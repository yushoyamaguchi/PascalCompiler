package enshud.s3.checker;

public class Mold {
	private String name;
	private String mold;
	private boolean isArray;
	private boolean isSub;
	private int min;
	private int max;


	public Mold(String name,String mold) {
		this.name=name;
		this.mold=mold;
	}

	public Mold(String name,String mold,boolean isArray) {
		this.name=name;
		this.mold=mold;
		this.isArray=isArray;
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
	public void setIsArray(boolean b) {
		isArray=b;
	}
	public void setMinMax(int min,int max) {
		this.min=min;
		this.max=max;
	}
}
