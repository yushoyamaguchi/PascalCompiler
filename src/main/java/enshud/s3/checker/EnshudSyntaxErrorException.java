package enshud.s3.checker;



public class EnshudSyntaxErrorException extends Exception {
	  int lineNumber; // 何行目で構文エラーが起きたかを保持する．

	 public  EnshudSyntaxErrorException(int lineNumber) {
	     this.lineNumber = lineNumber; // コンストラクタで行番号を保持．
	  }


	  public int getLineNumber() {
		  return lineNumber;
	  }
}
