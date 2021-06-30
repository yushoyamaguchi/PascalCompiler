package enshud.s3.checker;

public class EnshudSemanticErrorException extends Exception {
	int lineNumber;

	 public  EnshudSemanticErrorException(int lineNumber) {
	     this.lineNumber = lineNumber; // コンストラクタで行番号を保持．
	  }

	  public int getLineNumber() {
		  return lineNumber;
	  }
}
