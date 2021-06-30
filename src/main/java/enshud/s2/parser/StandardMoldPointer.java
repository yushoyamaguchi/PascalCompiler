package enshud.s2.parser;

public class StandardMoldPointer implements MyFunctionPointer {

	Parser callerParser;
	public StandardMoldPointer(Parser p) {
		this.callerParser=p;
	}

	@Override
	public ReadingInfo doit(ReadingInfo ri) {
		return callerParser.standardMold(ri);
	}

	@Override
	public boolean check(ReadingInfo ri) {
		return callerParser.standardMold(ri).getState();
	}

}
