package enshud.s2.parser;

public class ArrayMoldPointer implements MyFunctionPointer {

	Parser callerParser;
	public ArrayMoldPointer(Parser p) {
		this.callerParser=p;
	}

	@Override
	public ReadingInfo doit(ReadingInfo ri) {
		return callerParser.arrayMold(ri);
	}

	@Override
	public boolean check(ReadingInfo ri) {
		return callerParser.sameWordLookAhead(ri, "SARRAY");
	}



}
