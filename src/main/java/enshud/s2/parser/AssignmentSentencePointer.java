package enshud.s2.parser;

public class AssignmentSentencePointer implements MyFunctionPointer {

	Parser callerParser;
	public AssignmentSentencePointer(Parser p) {
		this.callerParser=p;
	}

	@Override
	public ReadingInfo doit(ReadingInfo ri) {
		return callerParser.assignmentSentence(ri);
	}

	@Override
	public boolean check(ReadingInfo oldRI) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		ri.setAll(callerParser.variableRead(oldRI));
		ri.setAll(callerParser.sameWord(ri, "SASSIGN"));
		return ri.getState();
	}

}
