package enshud.s2.parser;

public class CompoundSentencePointer implements MyFunctionPointer {

	Parser callerParser;
	public CompoundSentencePointer(Parser p) {
		this.callerParser=p;
	}

	@Override
	public ReadingInfo doit(ReadingInfo ri) {
		return callerParser.compoundSentence(ri);
	}

	@Override
	public boolean check(ReadingInfo ri) {
		return callerParser.sameWordLookAhead(ri, "SBEGIN");
	}
}
