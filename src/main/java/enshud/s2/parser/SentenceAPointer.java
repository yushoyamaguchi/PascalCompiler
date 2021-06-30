package enshud.s2.parser;

public class SentenceAPointer implements MyFunctionPointer {

	Parser callerParser;
	public SentenceAPointer(Parser p) {
		this.callerParser=p;
	}

	@Override
	public ReadingInfo doit(ReadingInfo ri) {
		return callerParser.sentenceA(ri);
	}

	@Override
	public boolean check(ReadingInfo ri) {
		return callerParser.sameWordLookAhead(ri, "SIF");
	}

}
