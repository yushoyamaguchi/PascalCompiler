package enshud.s2.parser;

public class VariablePointer implements MyFunctionPointer {

	Parser callerParser;
	public VariablePointer(Parser p) {
		this.callerParser=p;
	}

	@Override
	public ReadingInfo doit(ReadingInfo ri) {
		return callerParser.variableRead(ri);
	}

	@Override
	public boolean check(ReadingInfo ri) {
		return callerParser.sameWordLookAhead(ri, "SIDENTIFIER");
	}
}
