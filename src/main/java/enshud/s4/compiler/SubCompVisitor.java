package enshud.s4.compiler;

import java.util.ArrayList;
import java.util.Deque;

public class SubCompVisitor implements Visitor {
	public ArrayList<Mold> _varList ;
	public ArrayList<Mold> _paramList = new ArrayList<Mold>();
	private Compiler _compiler;
	public ArrayList<String> _proNameList;
	private CompilerVisitor _cv;
	private WhatEbnf _flags;
	private ArrayList<String> _caslLines = new ArrayList<String>();
	Deque<Integer> _ifWhileNum ;
	private final String integerString="integer";
	private final String booleanString="boolean";
	private ArrayList<String> _assignSubscriptLines = new ArrayList<String>();
	private ArrayList<String> _stringList;
	private final String charString="char";
	private int _subCompNumber;





	public SubCompVisitor( Compiler c,CompilerVisitor cv,int subCompNumber) {
		@SuppressWarnings("unchecked")
		ArrayList<Mold> cloneFieldList = (ArrayList<Mold>) cv.getVarList().clone();
		_varList=cloneFieldList;
		_proNameList=cv.getProNameList();
		_compiler=c;
		_cv=cv;
		_flags=cv.getFlags();
		_stringList=cv.getStringList();
		_ifWhileNum=cv.getIfWhileNum();
		_subCompNumber=subCompNumber;
	}

	public VisitorInfo visit(Node node,ArrayList<VisitorInfo> viList) throws EnshudSemanticErrorException{
		VisitorInfo vi= new VisitorInfo();

		//フラグセット
		flagProcessing(node);
		//viを通した情報のやりとり
		viProcessing(vi,node,viList);
		//casの書き込み,viの操作
		codeMaking(vi,node,viList);
		//casへの書き込みのみ
		codeMaking2(vi,node);
		return vi;
	}

	public void flagProcessing(Node node) {
		lineChangeOutput(node);
		endWriteContents(node);
	}

	public void viProcessing(VisitorInfo vi,Node node,ArrayList<VisitorInfo> viList)throws EnshudSemanticErrorException {
		variableNameDec(vi,node);
		variableNameList(vi,node,viList);
		codeProcessing(vi,node,viList);
		constantProcessing(vi,node);
		charProcessing(vi,node);
		arrayMinMax(vi,node,viList);
		arrayMold(vi,node,viList);
		standardMold(vi,node);
		variablesAndMold(vi,node,viList);
		booleanProcessing(vi,node);
	}

	public void codeMaking(VisitorInfo vi,Node node,ArrayList<VisitorInfo> viList) throws EnshudSemanticErrorException{
		temporaryParameterNameList(vi,node,viList);
		temporaryParameterAndMold(vi,node,viList);
		temporaryParameterNameDec(vi,node);
		subProgramDeclarations(node);
		procedureNameDec(vi,node);
		stringWrite(vi,node);
		varUse(vi,node);
		varWithSub(vi,node,viList);
		simpleFormula(vi,node);
		termProcessing(vi,node);
		whileCondition(node);
		ifCondition(node);
		whileStatement(node);
		thenElseStatement(node);
		formulaProcessing(vi,node);
		endIf(node);
		orProcessing(vi,node);
		andProcessing(vi,node);
		notProcessing(vi,node);
		procCall(vi,node);
		subProgramHead(vi,node);
	}

	public void codeMaking2(VisitorInfo vi,Node node) {
		intWrite(vi,node);
		endWriteOrPara(node);
		charWrite(vi,node);
		endAssign(vi,node);
		endAssignSubscript(node);
	}

	/*
	 * ここからはflagsetでセットしたフラグを戻すメソッド
	 */

	public void endWriteContents(Node node) {
		if((_flags.getIsWrite()||_flags.getIsPara())&&!node.getWord().equals("WriteStatement")&&!node.getWord().equals("ParameterUse")) {
			_flags.setDepthOfWrite(_flags.getDepthOfWrite()-1);
		}
	}




	/*
	 * ここからコード作成
	 */


	public void endWriteOrPara(Node node) {
		if(node.getWord().equals("WriteStatement")) {
			_caslLines.add("\tCALL\tWRTLN");
			_flags.setIsWrite(false);
		}
		else if(node.getWord().equals("ParameterUse")) {
			_flags.setIsPara(false);
		}
		else if(node.getWord().equals("ReadStatement")) {
			_flags.setIsRead(false);
		}
	}

	public void subProgramDeclarations(Node root) {
		if(root.getWord().equals("SubProgramDeclarations")) {
			int numOfPara=numOfPara();
			if(numOfPara>0) {
				addCasl("\tLD\tGR1, 0, GR8");
				addCasl("\tADDA\tGR8, ="+numOfPara);
				addCasl("\tST\tGR1, 0, GR8");
			}
			_caslLines.add("\tRET");
		}
	}

	public VisitorInfo subProgramHead(VisitorInfo vi,Node node)  {
		if(node.getWord().equals("SubProgramHead")) {
			addCasl("\tLD\tGR1, GR8");
			int numOfPara=numOfPara();
			addCasl("\tADDA\tGR1, ="+numOfPara);
			int parameterHead=parameterHead();
			for(int i=0;i<numOfPara;i++) {
				parameterStore(parameterHead,i);
			}
		}
		return vi;
	}



	public void parameterStore(int head,int num) {
		addCasl("\tLD\tGR2, 0, GR1");
		addCasl("\tLD\tGR3, ="+(head+num));
		addCasl("\tST\tGR2, VAR, GR3");
		addCasl("\tSUBA\tGR1, =1");
	}

	public int numOfPara() {
		int numOfPara=0;
		for(Mold m:_varList) {
			if(m.getIsParm()&&m.getSubCompNumber()==_subCompNumber) {
				numOfPara++;
			}
		}
		return numOfPara;
	}

	public int parameterHead() {
		for(Mold m:_varList) {
			if(m.getIsParm()&&m.getSubCompNumber()==_subCompNumber) {
				return m.getSirialNumber();
			}
		}
		return -1;
	}

	public void endIf(Node node) {
		if(node.getWord().equals("IfThenStatement")) {
			int num=_ifWhileNum.pop();
			_caslLines.add("ENDIF"+num+"\tNOP");
		}
	}

	public void whileCondition(Node node) {
		if(node.getWord().equals("ConditionalStatement")&&_flags.getIsWhileCondition()) {
			_caslLines.add("BOOL"+(_flags.getNumOfIfWhile()-1)+"\tNOP");
			_caslLines.add("\tPOP\tGR1");
			_caslLines.add("\tCPA\tGR1, =#FFFF");
			_caslLines.add("\tJZE\tENDLP"+(_flags.getNumOfIfWhile()-1));
			_flags.setIsWhileCondition(false);
		}
	}

	public void ifCondition(Node node) {
		if(node.getWord().equals("ConditionalStatement")&&_flags.getIsIfCondition()) {
			_caslLines.add("BOOL"+(_flags.getNumOfIfWhile()-1)+"\tNOP");
			_caslLines.add("\tPOP\tGR1");
			_caslLines.add("\tCPA\tGR1, =#FFFF");
			_caslLines.add("\tJZE\tELSE"+(_flags.getNumOfIfWhile()-1));
			_flags.setIsIfCondition(false);

		}
	}

	public void whileStatement(Node node) {
		if(node.getWord().equals("WhileStatement")) {
			int num=_ifWhileNum.pop();
			_caslLines.add("\tJUMP\tLOOP"+num);
			_caslLines.add("ENDLP"+num+"\tNOP");
		}
	}

	public void thenElseStatement(Node node) {
		if(node.getWord().equals("ThenStatement")) {
			int num=_ifWhileNum.peek();
			_caslLines.add("\tJUMP\tENDIF"+num);
			_caslLines.add("ELSE"+num+"\tNOP");
		}
	}

	public void endAssignSubscript(Node node) {
		if(node.getWord().equals("Subscript")&&_flags.getIsAssignLeft()&&_flags.getDepthOfAssign()==1) {
			_flags.setIsAssignSubscript(false);
			_flags.setDepthOfAssign(_flags.getDepthOfAssign()-1);
		}
		else if(node.getWord().equals("Subscript")&&_flags.getIsAssignLeft()&&_flags.getDepthOfAssign()>1) {
			_flags.setDepthOfAssign(_flags.getDepthOfAssign()-1);
		}
	}

	public void endAssign(VisitorInfo vi,Node node) {
		if(node.getWord().equals("AssignStatement")) {
			String varName=node.children.get(0).getWord();
			String varName2="";
			if(varName.equals("VariableWithSubscript")) {
				varName2=node.children.get(0).children.get(0).getWord();
			}
			for(Mold m:_varList) {
				if(varName.equals(m.getName())&&!m.getIsArray()&&!m.getCannotUse()) {
					_caslLines.add("\tLD\tGR2, ="+m.getSirialNumber());
					_caslLines.add("\tPOP\tGR1");
					_caslLines.add("\tST\tGR1, VAR, GR2");
				}
				else if(varName2.equals(m.getName())&&m.getIsArray()&&!m.getCannotUse()) {
					subscriptOutput();
					_caslLines.add("\tPOP\tGR2");
					_caslLines.add("\tADDA\tGR2, ="+(m.getSirialNumber()-m.getMin()));
					_caslLines.add("\tPOP\tGR1");
					_caslLines.add("\tST\tGR1, VAR, GR2");
				}
			}
		}
	}


	public VisitorInfo stringWrite(VisitorInfo vi,Node node)  {
		if(node.getToken().equals("SSTRING")&&_flags.getIsWrite()) {
			_stringList.add("CHAR"+_stringList.size()+"\tDC\t"+node.getWord());
			vi.setStr(node.getWord().substring(1, node.getWord().length()-1));
			_caslLines.add("\tLD\tGR1, ="+vi.getStr().length());
			_caslLines.add("\tPUSH\t0, GR1");
			_caslLines.add("\tLAD\tGR2, CHAR"+(_stringList.size()-1));
			_caslLines.add("\tPUSH\t0, GR2");
			_caslLines.add("\tPOP\tGR2");
			_caslLines.add("\tPOP\tGR1");
			_caslLines.add("\tCALL\tWRTSTR");
		}
		return vi;
	}

	public VisitorInfo charWrite(VisitorInfo vi,Node node)  {
		if((node.getRemark().equals("VariableUse")||node.getWord().equals("VariableWithSubscript"))&&vi.getMold().equals(charString)&&_flags.getIsWrite()&&_flags.getDepthOfWrite()==1) {
			_caslLines.add("\tPOP\tGR2");
			_caslLines.add("\tCALL\tWRTCH");
		}
		return vi;
	}


	public VisitorInfo intWrite(VisitorInfo vi,Node node)  {
		if(vi.getMold().equals(integerString)&&_flags.getIsWrite()&&_flags.getDepthOfWrite()==1) {
			_caslLines.add("\tPOP\tGR2");
			_caslLines.add("\tCALL\tWRTINT");
		}
		return vi;
	}


	public VisitorInfo procCall(VisitorInfo vi,Node node)  {
		if(node.getWord().equals("ProcedureStatement")) {
			for(int i=0;i<_proNameList.size();i++) {
				if(node.children.get(0).getWord().equals(_proNameList.get(i))) {
					_caslLines.add("\tCALL\tPROC"+i);
					recursionParamLoad(node.children.get(1));
				}
			}
		}
		return vi;
	}

	public void recursionParamLoad(Node node)  {
		if(node.children.size()>0) {
			int numOfParam=numOfPara();
			int head=parameterHead();
			addCasl("\tLD\tGR4, GR8" );
			addCasl("\tADDA\tGR4, ="+numOfParam);
			for(int i=0;i<numOfParam;i++) {
				addCasl("\tLD\tGR2, 0, GR4" );
				addCasl("\tLD\tGR3, ="+(head+i));
				addCasl("\tST\tGR2, VAR, GR3 ;;;;;;;;;;;;;");
				addCasl("\tSUBA\tGR4, =1");
			}
		}
	}

	public VisitorInfo varUse(VisitorInfo vi,Node node)  {
		if(node.getRemark().equals("VariableUse")&&(!_flags.getIsAssignLeft()||_flags.getIsAssignSubscript())&&!_flags.getIsRead()) {
			for(Mold m:_varList) {
				if(node.getWord().equals(m.getName())&&!m.getIsArray()&&!m.getCannotUse()) {
					vi.setMold(m.getMold());
					vi.setVariableName(m.getName());
					addCasl("\tLD\tGR2, ="+m.getSirialNumber());
					addCasl("\tLD\tGR1, VAR, GR2 ");
					addCasl("\tPUSH\t0, GR1");
				}
			}
		}
		else if(node.getRemark().equals("VariableUse")&&_flags.getIsAssignLeft()&&!_flags.getIsAssignSubscript()) {
			for(Mold m:_varList) {
				if(node.getWord().equals(m.getName())&&!m.getIsArray()&&!m.getCannotUse()) {
					_flags.setIsAssignLeft(false);
					vi.setVariableName(m.getName());
					vi.setMold(m.getMold());
				}
			}
		}
		return vi;
	}

	public VisitorInfo varWithSub(VisitorInfo vi,Node node,ArrayList<VisitorInfo> viList)  {
		if(node.getWord().equals("VariableWithSubscript")&&_flags.getIsAssignLeft()&&!_flags.getIsAssignSubscript()) {
			_flags.setIsAssignLeft(false);
		}
		else if(node.getWord().equals("VariableWithSubscript")&&(!_flags.getIsAssignLeft()||_flags.getIsAssignSubscript())&&!_flags.getIsRead()) {
			for(Mold m:_varList) {
				if(node.children.get(0).getWord().equals(m.getName())) {
					addCasl("\tPOP\tGR2");
					addCasl("\tADDA\tGR2, ="+(m.getSirialNumber()-m.getMin()));
					addCasl("\tLD\tGR1, VAR, GR2 ");
					addCasl("\tPUSH\t0, GR1");
					vi.setMold(m.getMold());
				}
			}
		}
		return vi;
	}

	public void subscriptOutput() {
		for(String s:_assignSubscriptLines) {
			_caslLines.add(s);
		}
		_assignSubscriptLines.clear();
	}

	public VisitorInfo codeProcessing(VisitorInfo vi,Node root,ArrayList<VisitorInfo> viList) throws EnshudSemanticErrorException {
		if(root.getRemark().equals("Code")&&root.getToken().equals("SPLUS")) {
			vi.setValue(viList.get(0).getValue());
			vi.setMold(integerString);
		}
		else if(root.getRemark().equals("Code")&&root.getToken().equals("SMINUS")) {
			vi.setValue(-1*viList.get(0).getValue());
			vi.setMold(integerString);
			addCasl("\tPOP\tGR2");
			addCasl("\tLD\tGR1, =0");
			addCasl("\tSUBA\tGR1, GR2");
			addCasl("\tPUSH\t0, GR1");
		}
		return vi;
	}


	public VisitorInfo constantProcessing(VisitorInfo vi,Node root) throws EnshudSemanticErrorException {
		if(root.getToken().equals("SCONSTANT")) {
			vi.setValue(Integer.parseInt(root.getWord()));
			vi.setMold(integerString);
			addCasl("\tPUSH\t"+vi.getValue());
		}
		return vi;
	}

	public VisitorInfo charProcessing(VisitorInfo vi,Node root) throws EnshudSemanticErrorException {
		if(root.getToken().equals("SSTRING")&&!_flags.getIsWrite()) {
			vi.setMold(charString);
			addCasl("\tLD\tGR1, ="+root.getWord());
			addCasl("\tPUSH\t0, GR1");
		}
		return vi;
	}

	public VisitorInfo booleanProcessing(VisitorInfo vi,Node root) throws EnshudSemanticErrorException {
		if(root.getToken().equals("STRUE")) {
			vi.setBoo(true);
			vi.setMold(booleanString);
			addCasl("\tPUSH\t#0000");
		}
		else if(root.getToken().equals("SFALSE")) {
			vi.setBoo(false);
			vi.setMold(booleanString);
			addCasl("\tPUSH\t#FFFF");
		}
		return vi;
	}

	public VisitorInfo formulaProcessing(VisitorInfo vi,Node root) throws EnshudSemanticErrorException {
		if(root.getRemark().equals("Formula")) {
			addCasl("\tPOP\tGR2");
			addCasl("\tPOP\tGR1");
			addCasl("\tCPA\tGR1, GR2");
			jumpOutput(root);
			vi.setMold(booleanString);
		}
		return vi;
	}

	public void jumpOutput(Node root) throws EnshudSemanticErrorException {
		if(root.getWord().equals("=")) {
			equalProcessing();
		}
		else if(root.getWord().equals("<>")) {
			notEqual();
		}
		else if(root.getWord().equals("<")) {
			lessProcessing();
		}
		else if(root.getWord().equals("<=")) {
			lessEqual();
		}
		else if(root.getWord().equals(">")) {
			greatProcessing();
		}
		else if(root.getWord().equals(">=")) {
			greatEqual();
		}
	}

	public void equalProcessing() {
		if(_flags.getIsIfCondition()||_flags.getIsWhileCondition()) {
			addCasl("\tPUSH\t#0000");
			addCasl("\tJZE\tBOOL"+(_flags.getNumOfIfWhile()-1));
			addCasl("\tPOP\tGR5");
			addCasl("\tPUSH\t#FFFF");
		}
		else {
			addCasl("\tPUSH\t#0000");
			addCasl("\tJZE\tFML"+_flags.getNumOfFormula());
			addCasl("\tPOP\tGR5");
			addCasl("\tPUSH\t#FFFF");
			addCasl("FML"+_flags.getNumOfFormula()+"\tNOP");
			_flags.setNumOfFormula(_flags.getNumOfFormula()+1);
		}
	}

	public void notEqual() {
		if(_flags.getIsIfCondition()||_flags.getIsWhileCondition()) {
			addCasl("\tPUSH\t#0000");
			addCasl("\tJNZ\tBOOL"+(_flags.getNumOfIfWhile()-1));
			addCasl("\tPOP\tGR5");
			addCasl("\tPUSH\t#FFFF");
		}
		else {
			addCasl("\tPUSH\t#0000");
			addCasl("\tJNZ\tFML"+_flags.getNumOfFormula());
			addCasl("\tPOP\tGR5");
			addCasl("\tPUSH\t#FFFF");
			addCasl("FML"+_flags.getNumOfFormula()+"\tNOP");
			_flags.setNumOfFormula(_flags.getNumOfFormula()+1);
		}
	}

	public void lessProcessing() {
		if(_flags.getIsIfCondition()||_flags.getIsWhileCondition()) {
			addCasl("\tPUSH\t#0000");
			addCasl("\tJMI\tBOOL"+(_flags.getNumOfIfWhile()-1));
			addCasl("\tPOP\tGR5");
			addCasl("\tPUSH\t#FFFF");
		}
		else {
			addCasl("\tPUSH\t#0000");
			addCasl("\tJMI\tFML"+_flags.getNumOfFormula());
			addCasl("\tPOP\tGR5");
			addCasl("\tPUSH\t#FFFF");
			addCasl("FML"+_flags.getNumOfFormula()+"\tNOP");
			_flags.setNumOfFormula(_flags.getNumOfFormula()+1);
		}
	}

	public void lessEqual() {
		if(_flags.getIsIfCondition()||_flags.getIsWhileCondition()) {
			addCasl("\tPUSH\t#0000");
			addCasl("\tJMI\tBOOL"+(_flags.getNumOfIfWhile()-1));
			addCasl("\tJZE\tBOOL"+(_flags.getNumOfIfWhile()-1));
			addCasl("\tPOP\tGR5");
			addCasl("\tPUSH\t#FFFF");
		}
		else {
			addCasl("\tPUSH\t#0000");
			addCasl("\tJMI\tFML"+_flags.getNumOfFormula());
			addCasl("\tJZE\tFML"+_flags.getNumOfFormula());
			addCasl("\tPOP\tGR5");
			addCasl("\tPUSH\t#FFFF");
			addCasl("FML"+_flags.getNumOfFormula()+"\tNOP");
			_flags.setNumOfFormula(_flags.getNumOfFormula()+1);
		}
	}

	public void greatProcessing() {
		if(_flags.getIsIfCondition()||_flags.getIsWhileCondition()) {
			addCasl("\tPUSH\t#0000");
			addCasl("\tJPL\tBOOL"+(_flags.getNumOfIfWhile()-1));
			addCasl("\tPOP\tGR5");
			addCasl("\tPUSH\t#FFFF");
		}
		else {
			addCasl("\tPUSH\t#0000");
			addCasl("\tJPL\tFML"+_flags.getNumOfFormula());
			addCasl("\tPOP\tGR5");
			addCasl("\tPUSH\t#FFFF");
			addCasl("FML"+_flags.getNumOfFormula()+"\tNOP");
			_flags.setNumOfFormula(_flags.getNumOfFormula()+1);
		}
	}

	public void greatEqual() {
		if(_flags.getIsIfCondition()||_flags.getIsWhileCondition()) {
			addCasl("\tPUSH\t#0000");
			addCasl("\tJPL\tBOOL"+(_flags.getNumOfIfWhile()-1));
			addCasl("\tJZE\tBOOL"+(_flags.getNumOfIfWhile()-1));
			addCasl("\tPOP\tGR5");
			addCasl("\tPUSH\t#FFFF");
		}
		else {
			addCasl("\tPUSH\t#0000");
			addCasl("\tJPL\tFML"+_flags.getNumOfFormula());
			addCasl("\tJZE\tFML"+_flags.getNumOfFormula());
			addCasl("\tPOP\tGR5");
			addCasl("\tPUSH\t#FFFF");
			addCasl("FML"+_flags.getNumOfFormula()+"\tNOP");
			_flags.setNumOfFormula(_flags.getNumOfFormula()+1);
		}
	}

	public VisitorInfo simpleFormula(VisitorInfo vi,Node root) throws EnshudSemanticErrorException {
		if(root.getRemark().equals("SimpleFormula")&&root.getToken().equals("SPLUS")) {
			addCasl("\tPOP\tGR2");
			addCasl("\tPOP\tGR1");
			addCasl("\tADDA\tGR1, GR2");
			addCasl("\tPUSH\t0, GR1");
			vi.setMold(integerString);
		}
		else if(root.getRemark().equals("SimpleFormula")&&root.getToken().equals("SMINUS")) {
			addCasl("\tPOP\tGR2");
			addCasl("\tPOP\tGR1");
			addCasl("\tSUBA\tGR1, GR2");
			addCasl("\tPUSH\t0, GR1");
			vi.setMold(integerString);
		}
		return vi;
	}

	public VisitorInfo orProcessing(VisitorInfo vi,Node root) throws EnshudSemanticErrorException {
		if(root.getRemark().equals("SimpleFormula")&&root.getToken().equals("SOR")) {
			addCasl("\tPOP\tGR2");
			addCasl("\tPOP\tGR1");
			addCasl("\tAND\tGR1, GR2");
			addCasl("\tPUSH\t0, GR1");
			vi.setMold(booleanString);
		}
		return vi;
	}

	public VisitorInfo termProcessing(VisitorInfo vi,Node root) throws EnshudSemanticErrorException {
		if(root.getRemark().equals("Term")&&root.getToken().equals("SSTAR")) {
			addCasl("\tPOP\tGR2");
			addCasl("\tPOP\tGR1");
			addCasl("\tCALL\tMULT");
			addCasl("\tPUSH\t0, GR2");
			vi.setMold(integerString);
		}
		else if(root.getRemark().equals("Term")&&root.getToken().equals("SDIVD")) {
			addCasl("\tPOP\tGR2");
			addCasl("\tPOP\tGR1");
			addCasl("\tCALL\tDIV");
			addCasl("\tPUSH\t0, GR2");
			vi.setMold(integerString);
		}
		else if(root.getRemark().equals("Term")&&root.getToken().equals("SMOD")) {
			addCasl("\tPOP\tGR2");
			addCasl("\tPOP\tGR1");
			addCasl("\tCALL\tDIV");
			addCasl("\tPUSH\t0, GR1");
			vi.setMold(integerString);
		}
		return vi;
	}

	public VisitorInfo andProcessing(VisitorInfo vi,Node root) throws EnshudSemanticErrorException {
		if(root.getRemark().equals("Term")&&root.getToken().equals("SAND")) {
			addCasl("\tPOP\tGR2");
			addCasl("\tPOP\tGR1");
			addCasl("\tOR\tGR1, GR2");
			addCasl("\tPUSH\t0, GR1");
			vi.setMold(booleanString);
		}
		return vi;
	}

	public VisitorInfo notProcessing(VisitorInfo vi,Node root) throws EnshudSemanticErrorException {
		if(root.getToken().equals("SNOT")) {
			addCasl("\tPOP\tGR1");
			addCasl("\tXOR\tGR1, =#FFFF");
			addCasl("\tPUSH\t0, GR1");
			vi.setMold(booleanString);
		}
		return vi;
	}

	public VisitorInfo procedureNameDec(VisitorInfo vi,Node root) throws EnshudSemanticErrorException {
		if(root.getRemark().equals("ProcedureNameDec")) {
			_caslLines.add("PROC"+(_proNameList.size()-1)+"\tNOP\t; proc");
		}
		return vi;
	}



	public VisitorInfo temporaryParameterNameList(VisitorInfo vi,Node root,ArrayList<VisitorInfo> viList) throws EnshudSemanticErrorException {
		if(root.getWord().equals("TemporaryParameterNameList")) {
			for(VisitorInfo visInfo:viList) {
				vi.variableNameList.add(visInfo.getVariableName());
			}
		}
		return vi;
	}

	public VisitorInfo temporaryParameterAndMold(VisitorInfo vi,Node root,ArrayList<VisitorInfo> viList) throws EnshudSemanticErrorException {
		if(root.getWord().equals("TemporaryParameterAndMold")) {
			for(String varName:viList.get(0).variableNameList) {
				scoopRestrict(varName);
				_varList.add(new Mold(varName,viList.get(1).getMold(),false,_cv.getNumOfVar(),true,true,_subCompNumber));
				_cv.setNumOfVar(_cv.getNumOfVar()+1);
			}
		}
		return vi;
	}

	public VisitorInfo temporaryParameterNameDec(VisitorInfo vi,Node root) throws EnshudSemanticErrorException {
		if("TemporaryParameterNameDec".equals(root.getRemark())) {
			vi.setVariableName(root.getWord());
		}
		return vi;
	}


	public VisitorInfo variablesAndMold(VisitorInfo vi,Node root,ArrayList<VisitorInfo> viList) throws EnshudSemanticErrorException {
		if(root.getWord().equals("VariablesAndMold")) {
			for(String varName:viList.get(0).variableNameList) {
				scoopRestrict(varName);
				if(!viList.get(1).getIsArray()) {
					_varList.add(new Mold(varName,viList.get(1).getMold(),false,_cv.getNumOfVar(),true));
					_cv.setNumOfVar(_cv.getNumOfVar()+1);
				}
				else if(viList.get(1).getIsArray()) {
					_varList.add(new Mold(varName,viList.get(1).getMold(),true,viList.get(0).getMin(),viList.get(0).getMax(),_cv.getNumOfVar(),true));
					_cv.setNumOfVar(_cv.getNumOfVar()+viList.get(0).getMax()-viList.get(0).getMin()+1);
				}
			}
		}
		return vi;
	}

	public void scoopRestrict(String name) {
		for(Mold m:_varList) {
			if(m.getName().equals(name)) {
				m.setCannotUse(true);
			}
		}
	}

	public VisitorInfo standardMold(VisitorInfo vi,Node root) throws EnshudSemanticErrorException {
		if(root.getRemark().equals("StandardMold")) {
			vi.setMold(root.getWord());
		}
		return vi;
	}

	public VisitorInfo arrayMold(VisitorInfo vi,Node root,ArrayList<VisitorInfo> viList) throws EnshudSemanticErrorException {
		if(root.getWord().equals("ArrayMold")) {
			vi.setMin(viList.get(0).getMin());
			vi.setMax(viList.get(0).getMax());
			vi.setIsArray(true);
			vi.setMold(viList.get(1).getMold());
		}
		return vi;
	}

	public VisitorInfo arrayMinMax(VisitorInfo vi,Node root,ArrayList<VisitorInfo> viList) throws EnshudSemanticErrorException {
		if(root.getWord().equals("ArrayMinMax")) {
			vi.setMin(viList.get(0).getValue());
			vi.setMax(viList.get(1).getValue());
		}
		return vi;
	}

	public VisitorInfo variableNameDec(VisitorInfo vi,Node root) throws EnshudSemanticErrorException {
		if(root.getRemark().equals("VariableNameDec")) {
			vi.setVariableName(root.getWord());
		}
		return vi;
	}

	public VisitorInfo variableNameList(VisitorInfo vi,Node root,ArrayList<VisitorInfo> viList) throws EnshudSemanticErrorException {
		if(root.getWord().equals("VariableNameList")) {
			for(VisitorInfo visInfo:viList) {
				vi.variableNameList.add(visInfo.getVariableName());
			}
		}
		return vi;
	}

	public void lineChangeOutput(Node node) {
		if(!node.getWord().equals("Program")){
			if(_compiler.lineCalc(node.getLine())>_flags.getReadingLine()) {
				_flags.setReadingLine(_compiler.lineCalc(node.getLine()));
				_caslLines.add(";\tL"+_compiler.lineCalc(node.getLine()));
			}
	    }
	}

	public ArrayList<String> getCaslLines(){
		return _caslLines;
	}

	public void addCasl(String s) {
		if(!_flags.getIsAssignLeft()) {
			_caslLines.add(s);
		}
		else if(_flags.getIsAssignSubscript()) {
			_assignSubscriptLines.add(s+";;;");
		}
	}


	@Override
	public void flagSet(Node node) throws EnshudSemanticErrorException {

	}
}
