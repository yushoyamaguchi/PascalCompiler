package enshud.s4.compiler;

import java.util.ArrayList;

public class SubProgramVisitor extends CheckerVisitor {



	public ArrayList<Mold> _varList ;
	private Compiler _compiler;
	public ArrayList<String> _proNameList;

	public SubProgramVisitor( ArrayList<Mold> fieldList,Compiler c,ArrayList<String> proNameList) {
		@SuppressWarnings("unchecked")
		ArrayList<Mold> cloneFieldList = (ArrayList<Mold>) fieldList.clone();
		_varList=cloneFieldList;
		_proNameList=proNameList;
		_compiler=c;
	}

	@Override
	public VisitorInfo visit(Node root, ArrayList<VisitorInfo> viList) throws EnshudSemanticErrorException {
		VisitorInfo vi=new VisitorInfo();
		variableNameDec(vi,root);
		standardMold(vi,root);
		variableNameList(vi,root,viList);
		temporaryParameterNameList(vi,root,viList);
		temporaryParameterNameDec(vi,root);
		arrayMold(vi,root,viList);
		variablesAndMold(vi,root,viList);
		temporaryParameterAndMold(vi,root,viList);
		constantProcessing(vi,root);
		stringProcessing(vi,root);
		trueFalse(vi,root);
		variableProcessing(vi,root);
		variableWithSubscript(vi,root,viList);
		subscriptProcessing(vi,root,viList);
		codeProcessing(vi,root,viList);
		notProcessing(vi,root,viList);
		termProcessing(vi,root,viList);
		simpleFormula(vi,root,viList);
		formulaProcessing(vi,root);
		andProcessing(vi,root,viList);
		orProcessing(vi,root,viList);
		assignStatement(vi,root,viList);
		ifThenStatement(vi,root,viList);
		whileStatement(vi,root,viList);
		procedureNameUse(vi,root);
		conditionalStatement(vi,root,viList);
		return vi;
	}

	@Override
	public void flagSet(Node node) throws EnshudSemanticErrorException {

	}

	public VisitorInfo variableNameDec(VisitorInfo vi,Node root) throws EnshudSemanticErrorException {
		if("VariableNameDec".equals(root.getRemark())) {
			int k=0;
			while(k<_varList.size()) {
				if(_varList.get(k).getName().equals(root.getWord())&&!_varList.get(k).getIsSub()) {
					_varList.remove(k);
				}
				else k++;
			}
			vi.setVariableName(root.getWord());
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

	public VisitorInfo temporaryParameterNameDec(VisitorInfo vi,Node root) throws EnshudSemanticErrorException {
		if("TemporaryParameterNameDec".equals(root.getRemark())) {
			int k=0;
			while(k<_varList.size()) {
				if(_varList.get(k).getName().equals(root.getWord())&&!_varList.get(k).getIsSub()) {
					_varList.remove(k);
				}
				else k++;
			}
			vi.setVariableName(root.getWord());
		}
		return vi;
	}


	public VisitorInfo variablesAndMold(VisitorInfo vi,Node root,ArrayList<VisitorInfo> viList) throws EnshudSemanticErrorException {
		if(root.getWord().equals("VariablesAndMold")) {
			for(String varName:viList.get(0).variableNameList) {
				if(!viList.get(1).getIsArray())_varList.add(new Mold(true,varName,viList.get(1).getMold(),false));
				else if(viList.get(1).getIsArray())_varList.add(new Mold(true,varName,viList.get(1).getMold(),true));
			}
		}
		return vi;
	}

	public VisitorInfo temporaryParameterAndMold(VisitorInfo vi,Node root,ArrayList<VisitorInfo> viList) throws EnshudSemanticErrorException {
		if(root.getWord().equals("TemporaryParameterAndMold")) {
			for(String varName:viList.get(0).variableNameList) {
				_varList.add(new Mold(true,varName,viList.get(1).getMold(),false));
			}
		}
		return vi;
	}


	public VisitorInfo variableProcessing(VisitorInfo vi,Node root) throws EnshudSemanticErrorException {
		if(root.getRemark().equals("VariableUse")) {
			for(Mold m:_varList) {
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
			throw new EnshudSemanticErrorException(_compiler.lineCalc(root.getLine()));
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
			throw new EnshudSemanticErrorException(_compiler.lineCalc(root.getLine()));
		}
		return vi;
	}

}
