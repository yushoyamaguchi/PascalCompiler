package enshud.s2.parser;
import java.util.ArrayList;


public class Node {
	private int line;
	private String word;
	private String token;
	public ArrayList<Node> children = new ArrayList<Node>();

	public Node() {

	}
	public Node(int i) {
		line=i;
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
	public void setWordAndToken(String givenWord,String givenToken) {
		word=givenWord;
		token=givenToken;
	}

}
