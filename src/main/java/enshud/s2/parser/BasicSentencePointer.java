package enshud.s2.parser;

public class BasicSentencePointer implements MyFunctionPointer {

	Parser callerParser;
	public BasicSentencePointer(Parser p) {
		this.callerParser=p;
	}

	@Override
	public ReadingInfo doit(ReadingInfo ri) {
		return callerParser.basicSentence(ri);
	}

	@Override
	public boolean check(ReadingInfo ri) {
		final String[] basicSentenceCheckList = new String[4];{
			basicSentenceCheckList[0]="SBEGIN";
			basicSentenceCheckList[1]="SIDENTIFIER";
			basicSentenceCheckList[2]="SREADLN";
			basicSentenceCheckList[3]="SWRITELN";
		}
		return callerParser.sameWordFromArrayLookAhead(ri, basicSentenceCheckList);
	}

}
