package enshud.s2.parser;

public class ConstantPointer implements MyFunctionPointer {

	Parser callerParser;
	public ConstantPointer(Parser p) {
		this.callerParser=p;
	}

	@Override
	public ReadingInfo doit(ReadingInfo ri) {
		return callerParser.constantRead(ri);
	}

	@Override
	public boolean check(ReadingInfo ri) {
		return callerParser.constantRead(ri).getState();
	}
}
