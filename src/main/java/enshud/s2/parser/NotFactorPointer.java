package enshud.s2.parser;

public class NotFactorPointer implements MyFunctionPointer {

	Parser callerParser;
	public NotFactorPointer(Parser p) {
		this.callerParser=p;
	}

	@Override
	public ReadingInfo doit(ReadingInfo ri) {
		return callerParser.notFactor(ri);
	}

	@Override
	public boolean check(ReadingInfo ri) {
		return callerParser.sameWordLookAhead(ri, "SNOT");
	}

}
