package enshud.s2.parser;

public class InputOrOutputSentencePointer implements MyFunctionPointer {

	Parser callerParser;
	public InputOrOutputSentencePointer(Parser p) {
		this.callerParser=p;
	}

	@Override
	public ReadingInfo doit(ReadingInfo ri) {
		return callerParser.inputOrOutputSentence(ri);
	}

	@Override
	public boolean check(ReadingInfo ri) {
		final String[] ioList= new String[2];{
			ioList[0]="SWRITELN";
			ioList[1]="SREADLN";
		}
		return callerParser.sameWordFromArrayLookAhead(ri, ioList);
	}
}
