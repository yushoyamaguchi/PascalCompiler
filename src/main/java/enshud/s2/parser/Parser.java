package enshud.s2.parser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Parser {

	List<String> _lineList;
	List<String> _tokenList;

	static final String[] _standardMoldList = new String[3];{
		_standardMoldList[0]="SINTEGER";
		_standardMoldList[1]="SCHAR";
		_standardMoldList[2]="SBOOLEAN";
	}

	static final String[] _codeList = new String[2];{
		_codeList[0]="SPLUS";
		_codeList[1]="SMINUS";
	}

	static final String[] _additiveOperatorList = new String[3];{
		_additiveOperatorList[0]="SPLUS";
		_additiveOperatorList[1]="SMINUS";
		_additiveOperatorList[2]="SOR";
	}

	static final String[] _relationalOperatorList = new String[6];{
		_relationalOperatorList[0]="SEQUAL";
		_relationalOperatorList[1]="SNOTEQUAL";
		_relationalOperatorList[2]="SLESS";
		_relationalOperatorList[3]="SLESSEQUAL";
		_relationalOperatorList[4]="SGREATEQUAL";
		_relationalOperatorList[5]="SGREAT";
	}

	static final String[] _multiplicationOperatorList = new String[4];{
		_multiplicationOperatorList[0]="SSTAR";
		_multiplicationOperatorList[1]="SDIVD";
		_multiplicationOperatorList[2]="SMOD";
		_multiplicationOperatorList[3]="SAND";
	}

	static final String[] _constantList = new String[4];{
		_constantList[0]="SCONSTANT";
		_constantList[1]="SSTRING";
		_constantList[2]="SFALSE";
		_constantList[3]="STRUE";
	}

	static final String[] _sentenceCheckList = new String[6];{
		_sentenceCheckList[0]="SIF";
		_sentenceCheckList[1]="SWHILE";
		_sentenceCheckList[2]="SBEGIN";
		_sentenceCheckList[3]="SIDENTIFIER";
		_sentenceCheckList[4]="SREADLN";
		_sentenceCheckList[5]="SWRITELN";
	}

	/**
	 * サンプルmainメソッド．
	 * 単体テストの対象ではないので自由に改変しても良い．
	 */
	public static void main(final String[] args) {
		// normalの確認
		new Parser().run("data/ts/synerr03.ts");
		//new Parser().run("data/ts/normal02.ts");

		// synerrの確認
		//new Parser().run("data/ts/synerr01.ts");
		//new Parser().run("data/ts/synerr02.ts");
	}

	/**
	 * TODO
	 *
	 * 開発対象となるParser実行メソッド．
	 * 以下の仕様を満たすこと．
	 *
	 * 仕様:
	 * 第一引数で指定されたtsファイルを読み込み，構文解析を行う．
	 * 構文が正しい場合は標準出力に"OK"を，正しくない場合は"Syntax error: line"という文字列とともに，
	 * 最初のエラーを見つけた行の番号を標準エラーに出力すること （例: "Syntax error: line 1"）．
	 * 入力ファイル内に複数のエラーが含まれる場合は，最初に見つけたエラーのみを出力すること．
	 * 入力ファイルが見つからない場合は標準エラーに"File not found"と出力して終了すること．
	 *
	 * @param inputFileName 入力tsファイル名
	 */

	public List<String> splitintoLine(String inputFileName) {
		try
        {
			 final List<String> inputLines = Files.readAllLines(Paths.get(inputFileName));
             return inputLines;
        }
		catch( IOException e )
        {
            System.err.println("File not found");
            return null;
        }
	}
	public String splitintoToken(String line) {
		String result;
		String[] buf = line.split("\t");
		result = buf[1];//tokenは2番目
		return result;
	}


    public ReadingInfo programRead() {
        ReadingInfo ri=new ReadingInfo(true,0);
        ri.setAll(sameWord(ri,"SPROGRAM"));
        ri.setAll(sameWord(ri,"SIDENTIFIER"));
        ri.setAll(sameWord(ri,"SSEMICOLON"));
        ri.setAll(blockRead(ri));
        ri.setAll(compoundSentence(ri));
        ri.setAll(sameWord(ri,"SDOT"));
    	return ri;
    }

	public ReadingInfo sameWord(ReadingInfo oldRI, String s) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
			ri.setState(s.equals(_tokenList.get(ri.getLine())));
	        ri.setLine(ri.getLine()+1);
		}
		return ri;
	}

	public boolean sameWordLookAhead(ReadingInfo ri, String s) {
		if(ri.getState()) {
			return (s.equals(_tokenList.get(ri.getLine())));
		}
		return false;
	}

	public ReadingInfo sameWordFromArray(ReadingInfo oldRI, String[] s) {
		ReadingInfo ri=new ReadingInfo(false,oldRI.getLine());
		if(oldRI.getState()) {
			for(String st : s) {
			  if(st.equals(_tokenList.get(ri.getLine()))) {
				ri.setState(true);
				break;
			  }
			}
	        ri.setLine(ri.getLine()+1);
		}
		return ri;
	}

	public boolean sameWordFromArrayLookAhead(ReadingInfo ri, String[] s) {
		if(ri.getState()) {
			for(String st : s) {
			  if(st.equals(_tokenList.get(ri.getLine()))) {
				  return true;
			  }
			}
		}
		return false;
	}

	public ReadingInfo blockRead(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
			ri.setAll(variableDeclarations(ri));
			ri.setAll(subProgramDeclarationGroup(ri));
		}
		return ri;
	}

	public ReadingInfo variableDeclarations(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
			if(sameWordLookAhead(ri,"SVAR")) {
				ri.setEach(true,ri.getLine()+1);
				ri.setAll(listOfVariableDeclarations(ri));
			}
		}
		return ri;
	}

	public ReadingInfo subProgramDeclarationGroup(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
			while(sameWordLookAhead(ri,"SPROCEDURE")) {
				ri.setAll(subProgramDeclaration(ri));
				ri.setAll(sameWord(ri,"SSEMICOLON"));
			}
		}
		return ri;
	}

	public ReadingInfo subProgramDeclaration(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
			ri.setAll(subProgramHead(ri));
			ri.setAll(variableDeclarations(ri));
			ri.setAll(compoundSentence(ri));
		}
		return ri;
	}

	public ReadingInfo subProgramHead(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
			ri.setAll(sameWord(ri,"SPROCEDURE"));
			ri.setAll(sameWord(ri,"SIDENTIFIER"));
			ri.setAll(temporaryParameter(ri));
			ri.setAll(sameWord(ri,"SSEMICOLON"));
		}
		return ri;
	}

	public ReadingInfo temporaryParameter(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
			if(sameWordLookAhead(ri,"SLPAREN")) {
				ri.setEach(true,ri.getLine()+1);
				ri.setAll(temporaryParameterList(ri));
				ri.setAll(sameWord(ri,"SRPAREN"));
			}
		}
		return ri;
	}

	public ReadingInfo temporaryParameterList(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
			ri.setAll(temporaryParameterNameList(ri));
			ri.setAll(sameWord(ri,"SCOLON"));
			ri.setAll(standardMold(ri));
			while(sameWordLookAhead(ri,"SSEMICOLON")) {
				ri.setEach(true,ri.getLine()+1);
				ri.setAll(temporaryParameterNameList(ri));
				ri.setAll(sameWord(ri,"SCOLON"));
				ri.setAll(standardMold(ri));
			}
		}
		return ri;
	}

	public ReadingInfo temporaryParameterNameList(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
			ri.setAll(sameWord(ri,"SIDENTIFIER"));
			while(sameWordLookAhead(ri,"SCOMMA")) {
				ri.setEach(true,ri.getLine()+1);
				ri.setAll(sameWord(ri,"SIDENTIFIER"));
			}
		}
		return ri;
	}


	public ReadingInfo listOfVariableDeclarations(ReadingInfo oldRI) {
		ReadingInfo ri=new ReadingInfo(oldRI);
        if(oldRI.getState()) {
        	ri.setAll(listOfVariableName(ri));
        	ri.setAll(sameWord(ri,"SCOLON"));
        	ri.setAll(moldRead(ri));
        	ri.setAll(sameWord(ri,"SSEMICOLON"));
        	while(sameWordLookAhead(ri,"SIDENTIFIER")) {
        		ri.setAll(listOfVariableName(ri));
            	ri.setAll(sameWord(ri,"SCOLON"));
            	ri.setAll(moldRead(ri));
            	ri.setAll(sameWord(ri,"SSEMICOLON"));
        	}
        }
		return ri;
	}

	public ReadingInfo listOfVariableName(ReadingInfo oldRI) {
		ReadingInfo ri=new ReadingInfo(oldRI);
        if(oldRI.getState()) {
        	ri.setAll(sameWord(ri,"SIDENTIFIER"));
        	while(sameWordLookAhead(ri,"SCOMMA")) {
        		ri.setEach(true,ri.getLine()+1);
        		ri.setAll(sameWord(ri,"SIDENTIFIER"));
        	}
        }
		return ri;
	}

	public ReadingInfo moldRead(ReadingInfo oldRI) {
		ReadingInfo ri=new ReadingInfo(false,oldRI.getLine());
        if(oldRI.getState()) {
			MyFunctionPointer[] fp=new MyFunctionPointer[2];
			fp[0]=new StandardMoldPointer(this);
			fp[1]=new ArrayMoldPointer(this);
			for(MyFunctionPointer mfp : fp) {
				if(mfp.check(oldRI)) {
					ri.setAll(mfp.doit(oldRI));
					break;
				}
			}
        }
		return ri;
	}

	public ReadingInfo standardMold(ReadingInfo oldRI) {
		ReadingInfo ri=new ReadingInfo(oldRI);
        if(oldRI.getState()) {
        	ri.setAll(sameWordFromArray(ri,_standardMoldList));
        }
		return ri;
	}
	public ReadingInfo arrayMold(ReadingInfo oldRI) {
		ReadingInfo ri=new ReadingInfo(oldRI);
        if(oldRI.getState()) {
        	ri.setAll(sameWord(ri,"SARRAY"));
        	ri.setAll(sameWord(ri,"SLBRACKET"));
        	ri.setAll(integerRead(ri));
        	ri.setAll(sameWord(ri,"SRANGE"));
        	ri.setAll(integerRead(ri));
        	ri.setAll(sameWord(ri,"SRBRACKET"));
        	ri.setAll(sameWord(ri,"SOF"));
        	ri.setAll(standardMold(ri));
        }
		return ri;
	}

	public ReadingInfo integerRead(ReadingInfo oldRI) {
		ReadingInfo ri=new ReadingInfo(oldRI);
        if(oldRI.getState()) {
        	if(sameWordFromArrayLookAhead(ri,_codeList)) {
        		ri.setEach(true,ri.getLine()+1);
        	}
        	ri.setAll(sameWord(ri,"SCONSTANT"));
        }
		return ri;
	}


	public ReadingInfo sentenceList(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
			ri.setAll(sentenceRead(ri));
			ri.setAll(sameWord(ri,"SSEMICOLON"));
			while(sameWordFromArrayLookAhead(ri,_sentenceCheckList)) {
				ri.setAll(sentenceRead(ri));
				ri.setAll(sameWord(ri,"SSEMICOLON"));
			}
		}
		return ri;
	}


	public ReadingInfo compoundSentence(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
			ri.setAll(sameWord(ri,"SBEGIN"));
			ri.setAll(sentenceList(ri));
			ri.setAll(sameWord(ri,"SEND"));
		}
		return ri;
	}

	public ReadingInfo sentenceRead(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(false,oldRI.getLine());
		if(oldRI.getState()) {
			MyFunctionPointer[] fp=new MyFunctionPointer[3];
			fp[0]=new BasicSentencePointer(this);
			fp[1]=new SentenceAPointer(this);
			fp[2]=new SentenceCPointer(this);
			for(MyFunctionPointer mfp : fp) {
				if(mfp.check(oldRI)) {
					ri.setAll(mfp.doit(oldRI));
					break;
				}
			}
		}
		return ri;
	}

	public ReadingInfo basicSentence(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(false,oldRI.getLine());
		if(oldRI.getState()) {
			MyFunctionPointer[] fp=new MyFunctionPointer[4];
			fp[0]=new AssignmentSentencePointer(this);
			fp[1]=new ProcedureCallSentencePointer(this);
			fp[2]=new InputOrOutputSentencePointer(this);
			fp[3]=new CompoundSentencePointer(this);
			for(MyFunctionPointer mfp : fp) {
				if(mfp.check(oldRI)) {
					ri.setAll(mfp.doit(oldRI));
					break;
				}
			}
		}
		return ri;
	}

	public ReadingInfo assignmentSentence(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
			ri.setAll(variableRead(ri));
			ri.setAll(sameWord(ri,"SASSIGN"));
			ri.setAll(formulaRead(ri));
		}
		return ri;
	}

	public ReadingInfo variableRead(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
			ri.setAll(sameWord(ri,"SIDENTIFIER"));
			ri.setAll(emptyOrVariableWithSubscripts(ri));
		}
		return ri;
	}

	public ReadingInfo emptyOrVariableWithSubscripts(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
			if(sameWordLookAhead(ri,"SLBRACKET")) {
				ri.setEach(true,ri.getLine()+1);
				ri.setAll(formulaRead(ri));
				ri.setAll(sameWord(ri,"SRBRACKET"));
			}
		}
		return ri;
	}

	public ReadingInfo formulaList(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
			ri.setAll(formulaRead(ri));
        	while(sameWordLookAhead(ri,"SCOMMA")) {
        		ri.setEach(true,ri.getLine()+1);
        		ri.setAll(formulaRead(ri));
        	}
		}
		return ri;
	}

	public ReadingInfo formulaRead(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
			ri.setAll(simpleFormula(ri));
        	if(sameWordFromArrayLookAhead(ri,_relationalOperatorList)) {
        		ri.setEach(true,ri.getLine()+1);
        		ri.setAll(simpleFormula(ri));
        	}
		}
		return ri;
	}

	public ReadingInfo simpleFormula(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
        	while(sameWordFromArrayLookAhead(ri,_codeList)) {
        		ri.setEach(true,ri.getLine()+1);
        	}
    		ri.setAll(termRead(ri));
        	while(sameWordFromArrayLookAhead(ri,_additiveOperatorList)) {
        		ri.setEach(true,ri.getLine()+1);
        		ri.setAll(termRead(ri));
        	}
		}
		return ri;
	}

	public ReadingInfo termRead(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
			ri.setAll(factorRead(ri));
        	while(sameWordFromArrayLookAhead(ri,_multiplicationOperatorList)) {
        		ri.setEach(true,ri.getLine()+1);
        		ri.setAll(factorRead(ri));
        	}
		}
		return ri;
	}

	public ReadingInfo factorRead(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(false,oldRI.getLine());
		if(oldRI.getState()) {
			MyFunctionPointer[] fp=new MyFunctionPointer[4];
			fp[0]=new VariablePointer(this);
			fp[1]=new ConstantPointer(this);
			fp[2]=new FormulaWithParenPointer(this);
			fp[3]=new NotFactorPointer(this);
			for(MyFunctionPointer mfp : fp) {
				if(mfp.check(oldRI)) {
					ri.setAll(mfp.doit(oldRI));
					break;
				}
			}
		}
		return ri;
	}

	public ReadingInfo constantRead(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
			ri.setAll(sameWordFromArray(ri,_constantList));
		}
		return ri;
	}

	public ReadingInfo formulaWithParen(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
			ri.setAll(sameWord(ri,"SLPAREN"));
			ri.setAll(formulaRead(ri));
			ri.setAll(sameWord(ri,"SRPAREN"));
		}
		return ri;
	}

	public ReadingInfo notFactor(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
			ri.setAll(sameWord(ri,"SNOT"));
			ri.setAll(factorRead(ri));
		}
		return ri;
	}

	public ReadingInfo procedureCallSentence(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
			ri.setAll(sameWord(ri,"SIDENTIFIER"));
        	if(sameWordLookAhead(ri,"SLPAREN")) {
        		ri.setEach(true,ri.getLine()+1);
        		ri.setAll(formulaList(ri));
        		ri.setAll(sameWord(ri,"SRPAREN"));
        	}
		}
		return ri;
	}

	public ReadingInfo inputOrOutputSentence(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(false,oldRI.getLine());
		if(oldRI.getState()) {
			MyFunctionPointer[] fp=new MyFunctionPointer[2];
			fp[0]=new InputSentencePointer(this);
			fp[1]=new OutputSentencePointer(this);
			for(MyFunctionPointer mfp : fp) {
				if(mfp.check(oldRI)) {
					ri.setAll(mfp.doit(oldRI));
					break;
				}
			}
		}
		return ri;
	}

	public ReadingInfo inputSentence(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
			ri.setAll(sameWord(ri,"SREADLN"));
    		ri.setAll(sameWord(ri,"SLPAREN"));
    		ri.setAll(variableList(ri));
    		ri.setAll(sameWord(ri,"SRPAREN"));
		}
		return ri;
	}

	public ReadingInfo outputSentence(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
			ri.setAll(sameWord(ri,"SWRITELN"));
    		ri.setAll(sameWord(ri,"SLPAREN"));
    		ri.setAll(formulaList(ri));
    		ri.setAll(sameWord(ri,"SRPAREN"));
		}
		return ri;
	}

	public ReadingInfo variableList(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
			ri.setAll(variableRead(ri));
        	while(sameWordLookAhead(ri,"SCOMMA")) {
        		ri.setEach(true,ri.getLine()+1);
        		ri.setAll(variableRead(ri));
        	}
		}
		return ri;
	}

	public ReadingInfo sentenceA(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
	        ri.setAll(sameWord(ri,"SIF"));
	        ri.setAll(formulaRead(ri));
	        ri.setAll(sameWord(ri,"STHEN"));
	        ri.setAll(compoundSentence(ri));
	        ri.setAll(emptyOrSentenceB(ri));
		}
		return ri;
	}

	public ReadingInfo emptyOrSentenceB(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
	        if(sameWordLookAhead(ri,"SELSE")) {
	        	ri.setEach(true,ri.getLine()+1);
	            ri.setAll(compoundSentence(ri));
	        }
		}
		return ri;
	}


	public ReadingInfo sentenceC(ReadingInfo oldRI ) {
		ReadingInfo ri=new ReadingInfo(oldRI);
		if(oldRI.getState()) {
	        ri.setAll(sameWord(ri,"SWHILE"));
	        ri.setAll(formulaRead(ri));
	        ri.setAll(sameWord(ri,"SDO"));
	        ri.setAll(compoundSentence(ri));
		}
		return ri;
	}


	public void run(final String inputFileName) {
		_lineList = splitintoLine(inputFileName);
		if(_lineList!=null) {
		   _tokenList = new ArrayList<String>();
		   ReadingInfo ri=new ReadingInfo(true,0);
		   for(int i=0 ; i< _lineList.size() ; i++) {
			   _tokenList.add(splitintoToken(_lineList.get(i)));
		   }
		   ri.setAll(programRead());
		   if(ri.getState()) {
			   System.out.println("OK");
		   }
		   else {
			   String[] buf = _lineList.get(ri.getLine()).split("\t");
			   System.err.println("Syntax error: line "+ Integer.parseInt(buf[3]));
		   }
		}
	}
}
