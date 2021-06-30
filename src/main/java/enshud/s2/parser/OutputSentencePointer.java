package enshud.s2.parser;

public class OutputSentencePointer implements MyFunctionPointer {

	Parser callerParser;
	public OutputSentencePointer(Parser p) {
		this.callerParser=p;
	}

	@Override
	public ReadingInfo doit(ReadingInfo ri) {
		return callerParser.outputSentence(ri);
	}

	@Override
	public boolean check(ReadingInfo ri) {
		return callerParser.sameWordLookAhead(ri, "SWRITELN");
	}
}
