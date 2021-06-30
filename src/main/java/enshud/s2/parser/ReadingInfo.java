package enshud.s2.parser;

public class ReadingInfo {

	private boolean state;
	private int line;

	ReadingInfo() {
	}

	ReadingInfo(boolean b ,int i) {
		state=b;
		line=i;
	}

	ReadingInfo(ReadingInfo ri) {
		state=ri.getState();
		line=ri.getLine();
	}

	public boolean getState() {
		return state;
	}
	public void setState(boolean b) {
		state=b;
	}
	public int getLine() {
		return line;
	}
	public void setLine(int i) {
		line=i;
	}
	public void setAll(ReadingInfo ri) {
		state=ri.getState();
		line=ri.getLine();
	}
	public void setEach(boolean b,int i) {
		state=b;
		line=i;
	}
}
