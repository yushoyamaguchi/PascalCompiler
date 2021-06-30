package enshud.s2.parser;

public class InputSentencePointer implements MyFunctionPointer {

	Parser callerParser;
	public InputSentencePointer(Parser p) {
		this.callerParser=p;
	}

	@Override
	public ReadingInfo doit(ReadingInfo ri) {
		return callerParser.inputSentence(ri);
	}

	@Override
	public boolean check(ReadingInfo ri) {
		return callerParser.sameWordLookAhead(ri, "SREADLN");
	}

}
