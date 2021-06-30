package enshud.s2.parser;

public class SentenceCPointer implements MyFunctionPointer {

	Parser callerParser;
	public SentenceCPointer(Parser p) {
		this.callerParser=p;
	}

	@Override
	public ReadingInfo doit(ReadingInfo ri) {
		return callerParser.sentenceC(ri);
	}

	@Override
	public boolean check(ReadingInfo ri) {
		return callerParser.sameWordLookAhead(ri, "SWHILE");
	}
}
