package enshud.s2.parser;

public class FormulaWithParenPointer implements MyFunctionPointer {

	Parser callerParser;
	public FormulaWithParenPointer(Parser p) {
		this.callerParser=p;
	}

	@Override
	public ReadingInfo doit(ReadingInfo ri) {
		return callerParser.formulaWithParen(ri);
	}

	@Override
	public boolean check(ReadingInfo ri) {
		return callerParser.sameWordLookAhead(ri, "SLPAREN");
	}
}
