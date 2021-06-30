package enshud.s3.checker;
import java.util.ArrayList;


public class Node {
	private int line;
	private String word;
	private String token;
	private String remark;
	public ArrayList<Node> children = new ArrayList<Node>();

	public Node() {
		remark=" ";
		token=" ";
	}
	public Node(int i) {
		line=i;
		remark=" ";
		token=" ";
	}

	public int getLine() {
		return line;
	}
	public void setLine(int i) {
		line=i;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String s) {
		word=s;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String s) {
		token=s;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String s) {
		remark=s;
	}
	public void setWordAndToken(String givenWord,String givenToken) {
		word=givenWord;
		token=givenToken;
	}

	public VisitorInfo accept(Visitor v) throws EnshudSemanticErrorException {
		 v.flagSet(this);
		 ArrayList<VisitorInfo> viList = new ArrayList<VisitorInfo>();
		 for(Node n:this.children) {
			 viList.add(n.accept(v));
		 }
		return v.visit(this,viList);
	}

}
