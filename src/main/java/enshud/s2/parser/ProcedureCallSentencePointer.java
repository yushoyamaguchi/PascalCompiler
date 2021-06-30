package enshud.s2.parser;

public class ProcedureCallSentencePointer implements MyFunctionPointer {

	Parser callerParser;
	public ProcedureCallSentencePointer(Parser p) {
		this.callerParser=p;
	}

	@Override
	public ReadingInfo doit(ReadingInfo ri) {
		return callerParser.procedureCallSentence(ri);
	}

	@Override
	public boolean check(ReadingInfo ri) {
		return callerParser.sameWordLookAhead(ri, "SIDENTIFIER");
	}
}
