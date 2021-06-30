package enshud.s3.checker;

import java.util.ArrayList;

public class CheckerVisitor implements Visitor {

	public ArrayList<Mold> _fieldList = new ArrayList<Mold>();
	public ArrayList<String> _proNameList= new ArrayList<String>();
	public ArrayList<SubProgramVisitor> _subVisitorList=new ArrayList<SubProgramVisitor>();
	private boolean _isSub=false;
	private Checker _checker;
	private int _numOfSub=0;
	private final String integerString="integer";
	private final String booleanString="boolean";

    public CheckerVisitor() {

    }

	public CheckerVisitor(Checker c) {
		_checker=c;
	}

	public boolean wordCheckFromArray(String s,String[] array) {
		for(String scheck:array) {
			if(s.equals(scheck)) return true;
		}
		return false;
	}

	@Override
	public void flagSet(Node root) throws EnshudSemanticErrorException{
		if(root.getRemark().equals("Top")) setIsSub(false);
		else subProgramDeclarations(root);
	}

	public void subProgramDeclarations(Node root) throws EnshudSemanticErrorException{
		if(root.getWord().equals("SubProgramDeclarations")) {
			setIsSub(true);
			_subVisitorList.add(new SubProgramVisitor(this._fieldList,_checker,this._proNameList));
			setNumOfSub(getNumOfSub() + 1);
		}
	}


	@Override
	public VisitorInfo visit(Node root,ArrayList<VisitorInfo> viList) throws EnshudSemanticErrorException{
		VisitorInfo vi=new VisitorInfo();
		if(_isSub) {
			procedureNameDec(vi,root);
			vi=_subVisitorList.get(getNumOfSub()-1).visit(root, viList);
		}
		else {
			variableNameDec(vi,root);
			standardMold(vi,root);
			variableNameList(vi,root,viList);
			arrayMold(vi,root,viList);
			variablesAndMold(vi,root,viList);
			constantProcessing(vi,root);
			stringProcessing(vi,root);
			trueFalse(vi,root);
			variableProcessing(vi,root);
			variableWithSubscript(vi,root,viList);
			codeProcessing(vi,root,viList);
			notProcessing(vi,root,viList);
			termProcessing(vi,root,viList);
			simpleFormula(vi,root,viList);
			formulaProcessing(vi,root);
			andProcessing(vi,root,viList);
			orProcessing(vi,root,viList);
			assignStatement(vi,root,viList);
			procedureNameUse(vi,root);
			ifThenStatement(vi,root,viList);
			whileStatement(vi,root,viList);
		}
		return vi;
	}

	public VisitorInfo variableNameDec(VisitorInfo vi,Node root) throws EnshudSemanticErrorException {
		if("VariableNameDec".equals(root.getRemark())) {
			for(Mold m:_fieldList) {
				if(m.getName().equals(root.getWord())) {
					throw new EnshudSemanticErrorException(_checker.lineCalc(root.getLine()));
				}
			}
			vi.setVariableName(root.getWord());
		}
		return vi;
	}



	public VisitorInfo standardMold(VisitorInfo vi,Node root) throws EnshudSemanticErrorException {
		 if(root.getRemark().equals("StandardMold")) {
			vi.setMold(root.getWord());
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

	public VisitorInfo arrayMold(VisitorInfo vi,Node root,ArrayList<VisitorInfo> viList) throws EnshudSemanticErrorException {
		if(root.getWord().equals("ArrayMold")) {
			vi.setMold(viList.get(1).getMold());
			vi.setIsArray(true);
		}
		return vi;
	}

	public VisitorInfo variablesAndMold(VisitorInfo vi,Node root,ArrayList<VisitorInfo> viList) throws EnshudSemanticErrorException {
		if(root.getWord().equals("VariablesAndMold")) {
			for(String varName:viList.get(0).variableNameList) {
				if(!viList.get(1).getIsArray())_fieldList.add(new Mold(varName,viList.get(1).getMold(),false));
				else if(viList.get(1).getIsArray())_fieldList.add(new Mold(varName,viList.get(1).getMold(),true));
			}
		}
		return vi;
	}

	public VisitorInfo constantProcessing(VisitorInfo vi,Node root) throws EnshudSemanticErrorException {
		if(root.getToken().equals("SCONSTANT")) {
			vi.setMold(integerString);
		}
		return vi;
	}

	public VisitorInfo stringProcessing(VisitorInfo vi,Node root) throws EnshudSemanticErrorException {
		if(root.getToken().equals("SSTRING")) {
			vi.setMold("char");
		}
		return vi;
	}

	public VisitorInfo trueFalse(VisitorInfo vi,Node root) throws EnshudSemanticErrorException {
		if(root.getToken().equals("STRUE")||root.getToken().equals("SFALSE")) {
			vi.setMold(booleanString);
		}
		return vi;
	}

	public VisitorInfo codeProcessing(VisitorInfo vi,Node root,ArrayList<VisitorInfo> viList) throws EnshudSemanticErrorException {
		if(root.getRemark().equals("Code")) {
			if(moldReturn(root,viList.get(0)).equals(integerString)) vi.setMold(integerString);
			else throw new EnshudSemanticErrorException(_checker.lineCalc(root.getLine()));
		}
		return vi;
	}

	public VisitorInfo notProcessing(VisitorInfo vi,Node root,ArrayList<VisitorInfo> viList) throws EnshudSemanticErrorException {
		if(root.getToken().equals("SNOT")) {
			if(moldReturn(root,viList.get(0)).equals(booleanString)) vi.setMold(booleanString);
			else throw new EnshudSemanticErrorException(_checker.lineCalc(root.getLine()));
		}
		return vi;
	}

	public VisitorInfo variableWithSubscript(VisitorInfo vi,Node root,ArrayList<VisitorInfo> viList) throws EnshudSemanticErrorException {
		if(root.getWord().equals("VariableWithSubscript")) {
			vi.setIsArray(true);
			vi.setMold(viList.get(0).getMold());
			if(!moldReturn(root,viList.get(1)).equals(integerString)) {
				throw new EnshudSemanticErrorException(_checker.lineCalc(root.getLine()));
			}
		}
		return vi;
	}

	public VisitorInfo variableProcessing(VisitorInfo vi,Node root) throws EnshudSemanticErrorException {
		if(root.getRemark().equals("VariableUse")) {
			for(Mold m:_fieldList) {
				if(m.getIsArray()&&m.getName().equals(root.getWord())) {
					vi.setCannotSingle(true);
					vi.setMold(m.getMold());
					return vi;
				}
				else if(m.getName().equals(root.getWord())) {
					vi.setMold(m.getMold());
					return vi;
				}
			}
			throw new EnshudSemanticErrorException(_checker.lineCalc(root.getLine()));
		}
		return vi;
	}


	public VisitorInfo termProcessing(VisitorInfo vi,Node root,ArrayList<VisitorInfo> viList) throws EnshudSemanticErrorException {
		if(root.getRemark().equals("Term")&&!root.getToken().equals("SAND")) {
			for(VisitorInfo visInfo:viList) {
				if(!moldReturn(root,visInfo).equals(integerString)) {
					throw new EnshudSemanticErrorException(_checker.lineCalc(root.getLine()));
				}
			}
			vi.setMold(integerString);
		}
		return vi;
	}

	public VisitorInfo andProcessing(VisitorInfo vi,Node root,ArrayList<VisitorInfo> viList) throws EnshudSemanticErrorException {
		if(root.getRemark().equals("Term")&&root.getToken().equals("SAND")) {
			for(VisitorInfo visInfo:viList) {
				if(!moldReturn(root,visInfo).equals(booleanString)) {
					throw new EnshudSemanticErrorException(_checker.lineCalc(root.getLine()));
				}
			}
			vi.setMold(booleanString);
		}
		return vi;
	}

	public VisitorInfo simpleFormula(VisitorInfo vi,Node root,ArrayList<VisitorInfo> viList) throws EnshudSemanticErrorException {
		if(root.getRemark().equals("SimpleFormula")&&!root.getToken().equals("SOR")) {
			for(VisitorInfo visInfo:viList) {
				if(!moldReturn(root,visInfo).equals(integerString)) {
					throw new EnshudSemanticErrorException(_checker.lineCalc(root.getLine()));
				}
			}
			vi.setMold(integerString);
		}
		return vi;
	}

	public VisitorInfo orProcessing(VisitorInfo vi,Node root,ArrayList<VisitorInfo> viList) throws EnshudSemanticErrorException {
		if(root.getRemark().equals("SimpleFormula")&&root.getToken().equals("SOR")) {
			for(VisitorInfo visInfo:viList) {
				if(!moldReturn(root,visInfo).equals(booleanString)) {
					throw new EnshudSemanticErrorException(_checker.lineCalc(root.getLine()));
				}
			}
			vi.setMold(booleanString);
		}
		return vi;
	}

	public VisitorInfo formulaProcessing(VisitorInfo vi,Node root) throws EnshudSemanticErrorException {
		if(root.getRemark().equals("Formula")) {
			vi.setMold(booleanString);
		}
		return vi;
	}

	public VisitorInfo assignStatement(VisitorInfo vi,Node root,ArrayList<VisitorInfo> viList) throws EnshudSemanticErrorException {
		if(root.getWord().equals("AssignStatement")&&!moldReturn(root,viList.get(0)).equals(moldReturn(root,viList.get(1)))) {
				throw new EnshudSemanticErrorException(_checker.lineCalc(root.getLine()));
		}
		return vi;
	}

	public VisitorInfo procedureNameDec(VisitorInfo vi,Node root) throws EnshudSemanticErrorException {
		if(root.getRemark().equals("ProcedureNameDec")) {
			_proNameList.add(root.getWord());
		}
		return vi;
	}

	public VisitorInfo procedureNameUse(VisitorInfo vi,Node root) throws EnshudSemanticErrorException {
		if(root.getRemark().equals("ProcedureNameUse")) {
			for(String s:_proNameList) {
				if(s.equals(root.getWord())) {
					return vi;
				}
			}
			throw new EnshudSemanticErrorException(_checker.lineCalc(root.getLine()));
		}
		return vi;
	}

	public VisitorInfo ifThenStatement(VisitorInfo vi,Node root,ArrayList<VisitorInfo> viList) throws EnshudSemanticErrorException {
		if(root.getWord().equals("IfThenStatement")&&!moldReturn(root,viList.get(0)).equals(booleanString)) {
				throw new EnshudSemanticErrorException(_checker.lineCalc(root.getLine()));
		}
		return vi;
	}

	public VisitorInfo whileStatement(VisitorInfo vi,Node root,ArrayList<VisitorInfo> viList) throws EnshudSemanticErrorException {
		if(root.getWord().equals("WhileStatement")&&!moldReturn(root,viList.get(0)).equals(booleanString)) {
				throw new EnshudSemanticErrorException(_checker.lineCalc(root.getLine()));
		}
		return vi;
	}

	public String moldReturn(Node root,VisitorInfo vi) throws EnshudSemanticErrorException {
		if(!vi.getCannotSingle()) {
			return vi.getMold();
		}
		else {
			throw new EnshudSemanticErrorException(_checker.lineCalc(root.getLine()));
		}
	}

	public int getNumOfSub() {
		return _numOfSub;
	}

	public void setNumOfSub(int _numOfSub) {
		this._numOfSub = _numOfSub;
	}

	public boolean getIsSub() {
		return _isSub;
	}

	public void setIsSub(boolean b) {
		_isSub = b;
	}

}
