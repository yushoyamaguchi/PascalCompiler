package enshud.s2.parser;

public class EnshudSyntaxErrorException extends Exception {
	  int lineNumber; // 何行目で構文エラーが起きたかを保持する．

	  public EnshudSyntaxErrorException(int lineNumber) {
	     this.lineNumber = lineNumber; // コンストラクタで行番号を保持．
	  }

	  EnshudSyntaxErrorException(int lineNumber,ParserTree pt) {
		     this.lineNumber = lineNumber; // コンストラクタで行番号を保持．
		  }

	  public int getlineNumber() {
		  return lineNumber;
	  }
}