package enshud.s3.checker;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;






public class Checker {

	List<String> _lineList;
	List<String> _tokenList;
	Node _root=new Node(0);

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

	static final String[] _statementsCheckList = new String[6];{
		_statementsCheckList[0]="SIF";
		_statementsCheckList[1]="SWHILE";
		_statementsCheckList[2]="SBEGIN";
		_statementsCheckList[3]="SIDENTIFIER";
		_statementsCheckList[4]="SREADLN";
		_statementsCheckList[5]="SWRITELN";
	}

	static final String[] _assignStatementsCheckList = new String[2];{
		_assignStatementsCheckList[0]="SLBRACKET";
		_assignStatementsCheckList[1]="SASSIGN";
	}


	/**
	 * サンプルmainメソッド．
	 * 単体テストの対象ではないので自由に改変しても良い．
	 */
	public static void main(final String[] args) {
		// normalの確認
		new Checker().run("data/ts/normal18.ts");
		/*new Checker().run("data/ts/normal02.ts");

		// synerrの確認
		new Checker().run("data/ts/synerr01.ts");
		new Checker().run("data/ts/synerr02.ts");

		// semerrの確認
		new Checker().run("data/ts/semerr01.ts");
		new Checker().run("data/ts/semerr02.ts");*/
	}

	/**
	 * TODO
	 *
	 * 開発対象となるChecker実行メソッド．
	 * 以下の仕様を満たすこと．
	 *
	 * 仕様:
	 * 第一引数で指定されたtsファイルを読み込み，意味解析を行う．
	 * 意味的に正しい場合は標準出力に"OK"を，正しくない場合は"Semantic error: line"という文字列とともに，
	 * 最初のエラーを見つけた行の番号を標準エラーに出力すること （例: "Semantic error: line 6"）．
	 * また，構文的なエラーが含まれる場合もエラーメッセージを表示すること（例： "Syntax error: line 1"）．
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

	public String splitintoWord(int linenum) {
		String result;
		String line=_lineList.get(linenum);
		String[] buf = line.split("\t");
		result = buf[0];//wordは1番目
		return result;
	}

	public int lineCalc(int tokenLine) {
		String[] buf = _lineList.get(tokenLine).split("\t");
		return Integer.parseInt(buf[3]);
	}


	public Node createNode(int line) {
		Node node=new Node(line);
		return node;
	}

	public Node createNode(int line,String s) {
		Node node=new Node(line);
		node.setWord(s);
		return node;
	}

	public Node tokenReadFromArray(int line,String[] s) throws EnshudSyntaxErrorException {
		Node node=createNode(line);
		for(String st : s) {
			  if(st.equals(_tokenList.get(node.getLine()))) {
				  node.setWordAndToken(splitintoWord(node.getLine()), _tokenList.get(node.getLine()));
				  node.setLine(node.getLine()+1);
				  return node;
			  }
		}
		throw new EnshudSyntaxErrorException(lineCalc(node.getLine()));
	}

	public Node tokenReadFromArray(int line,String[] s,String remark) throws EnshudSyntaxErrorException {
		Node node=createNode(line);
		for(String st : s) {
			  if(st.equals(_tokenList.get(node.getLine()))) {
				  node.setWordAndToken(splitintoWord(node.getLine()), _tokenList.get(node.getLine()));
					node.setRemark(remark);
				  node.setLine(node.getLine()+1);
				  return node;
			  }
		}
		throw new EnshudSyntaxErrorException(lineCalc(node.getLine()));
	}

	public Node tokenRead(int line,String token) throws EnshudSyntaxErrorException {
		Node node=createNode(line);
		if(!token.equals(_tokenList.get(node.getLine()))) {
			throw new EnshudSyntaxErrorException(lineCalc(node.getLine()));
		}
		else {
			node.setWordAndToken(splitintoWord(node.getLine()), _tokenList.get(node.getLine()));
			node.setLine(node.getLine()+1);
			return node;
		}
	}

	public Node tokenRead(int line,String token,String remark) throws EnshudSyntaxErrorException {
		Node node=createNode(line);
		if(!token.equals(_tokenList.get(node.getLine()))) {
			throw new EnshudSyntaxErrorException(lineCalc(node.getLine()));
		}
		else {
			node.setWordAndToken(splitintoWord(node.getLine()), _tokenList.get(node.getLine()));
			node.setRemark(remark);
			node.setLine(node.getLine()+1);
			return node;
		}
	}

	public boolean wordCheck(int line, String s) {
		return (s.equals(_tokenList.get(line)));
	}

	public Node wordCheckAndLine(Node node, String s) throws EnshudSyntaxErrorException {
		if(s.equals(_tokenList.get(node.getLine()))){
			node.setLine(node.getLine()+1);
			return node;
		}
		else{
			throw new EnshudSyntaxErrorException(lineCalc(node.getLine()));
		}
	}

	public boolean wordCheckFromArray(int line, String s[]) {
		for(String st : s) {
			  if(st.equals(_tokenList.get(line))) {
				  return true;
			  }
		}
		return false;
	}

	public Node programRead(Node oldnode) throws EnshudSyntaxErrorException {
		Node node=oldnode;
		node=wordCheckAndLine(node,"SPROGRAM");
		node.setWord("program");
		node.children.add(tokenRead(node.getLine(),"SIDENTIFIER"));
		node.setLine(node.children.get(0).getLine());
		node=wordCheckAndLine(node,"SSEMICOLON");
		node.children.add(blockRead(node.getLine()));
		node.setLine(node.children.get(1).getLine());
		node.children.add(topCompoundStatements(node.getLine()));
		node.setLine(node.children.get(2).getLine());
		node=wordCheckAndLine(node,"SDOT");
		return node;
	}

	public Node blockRead(int line) throws EnshudSyntaxErrorException{
		Node node=createNode(line,"block");
		node.children.add(variableDeclarations(node.getLine()));
		node.setLine(node.children.get(0).getLine());
		if(wordCheck(node.getLine(),"SPROCEDURE")){
			node.children.add(subProgramDeclarationsGroup(node.getLine()));
			node.setLine(node.children.get(1).getLine());
		}
		return node;
	}

	public Node variableDeclarations(int line) throws EnshudSyntaxErrorException {
		Node node=createNode(line,"Var");
		if(wordCheck(node.getLine(),"SVAR")) {
			node.setLine(node.getLine()+1);
			node.children.add(variableDeclarationsList(node.getLine()));
			node.setLine(node.children.get(0).getLine());
		}
		return node;
	}

	public Node variableDeclarationsList(int line) throws EnshudSyntaxErrorException {
		int numOfChild=0;
		Node node=variablesAndMold(line);
		if(wordCheck(node.getLine(),"SIDENTIFIER")) {
			Node parentNode=createNode(node.getLine(),"VariableDeclarationsList");
			parentNode.children.add(node);
			numOfChild++;
		   while(wordCheck(parentNode.getLine(),"SIDENTIFIER")) {
			   parentNode.children.add(variablesAndMold(parentNode.getLine()));
			   parentNode.setLine(parentNode.children.get(numOfChild).getLine());
			   numOfChild++;
		   }
		   return parentNode;
		}
		else {
			return node;
		}
	}

	public Node variablesAndMold(int line) throws EnshudSyntaxErrorException {
		Node node=createNode(line,"VariablesAndMold");
		node.children.add(variableNameList(node.getLine()));
		node.setLine(node.children.get(0).getLine());
		node=wordCheckAndLine(node,"SCOLON");
		node.children.add(moldRead(node.getLine()));
		node.setLine(node.children.get(1).getLine());
		node=wordCheckAndLine(node,"SSEMICOLON");
		return node;
	}

	public Node variableNameList(int line) throws EnshudSyntaxErrorException {
		int numOfChild=0;
		Node node=createNode(line,"VariableNameList");
		   node.children.add(tokenRead(node.getLine(),"SIDENTIFIER","VariableNameDec"));
		   node.setLine(node.children.get(numOfChild).getLine());
		   numOfChild++;
		   while(wordCheck(node.getLine(),"SCOMMA")) {
			   node.setLine(node.getLine()+1);
			   node.children.add(tokenRead(node.getLine(),"SIDENTIFIER","VariableNameDec"));
			   node.setLine(node.children.get(numOfChild).getLine());
			   numOfChild++;
		   }
		return node;
	}

	public Node moldRead(int line) throws EnshudSyntaxErrorException {
		Node node;
		if(wordCheck(line,"SARRAY")) {
			node=arrayMold(line);
		}
		else {
			node=tokenReadFromArray(line,_standardMoldList,"StandardMold");
		}
		return node;
	}

	public Node arrayMold(int line) throws EnshudSyntaxErrorException {
		Node node=createNode(line,"ArrayMold");
		node=wordCheckAndLine(node,"SARRAY");
		node=wordCheckAndLine(node,"SLBRACKET");
		node.children.add(arrayMinMax(node.getLine()));
		node.setLine(node.children.get(0).getLine());
		node=wordCheckAndLine(node,"SRBRACKET");
		node=wordCheckAndLine(node,"SOF");
		node.children.add(tokenReadFromArray(node.getLine(),_standardMoldList,"StandardMold"));
		node.setLine(node.children.get(1).getLine());
		return node;
	}

	public Node arrayMinMax(int line) throws EnshudSyntaxErrorException {
		Node node=createNode(line,"arrayMinMax");
		node.children.add(integerRead(node.getLine()));
		node.setLine(node.children.get(0).getLine());
		node=wordCheckAndLine(node,"SRANGE");
		node.children.add(integerRead(node.getLine()));
		node.setLine(node.children.get(1).getLine());
		return node;
	}


	public Node integerRead(int line) throws EnshudSyntaxErrorException {
		if(wordCheckFromArray(line,_codeList)) {
	        Node parentNode=tokenReadFromArray(line,_codeList,"Code");
			parentNode.children.add(tokenRead(parentNode.getLine(),"SCONSTANT"));
			parentNode.setLine(parentNode.children.get(0).getLine());
			return parentNode;
		}
		else {
			return tokenRead(line,"SCONSTANT");
		}
	}

	public Node subProgramDeclarationsGroup(int line) throws EnshudSyntaxErrorException {
		int numOfChild=0;
		Node node=subProgramDeclarations(line);
		node=wordCheckAndLine(node,"SSEMICOLON");
		if(wordCheck(node.getLine(),"SPROCEDURE")) {
			Node parentNode=createNode(node.getLine(),"SubProgramDeclarationsGroup");
			parentNode.children.add(node);
			numOfChild++;
		   while(wordCheck(parentNode.getLine(),"SPROCEDURE")) {
			   parentNode.children.add(subProgramDeclarations(parentNode.getLine()));
			   parentNode.setLine(parentNode.children.get(numOfChild).getLine());
			   numOfChild++;
			   parentNode=wordCheckAndLine(parentNode,"SSEMICOLON");
		   }
		   return parentNode;
		}
		else {
			return node;
		}
	}

	public Node subProgramDeclarations(int line) throws EnshudSyntaxErrorException {
		Node node=createNode(line,"SubProgramDeclarations");
		node.children.add(subProgramHead(node.getLine()));
		node.setLine(node.children.get(0).getLine());
		node.children.add(variableDeclarations(node.getLine()));
		node.setLine(node.children.get(1).getLine());
		node.children.add(compoundStatements(node.getLine()));
		node.setLine(node.children.get(2).getLine());
		return node;
	}

	public Node subProgramHead(int line) throws EnshudSyntaxErrorException {
		Node node=createNode(line,"SubProgramHead");
		node=wordCheckAndLine(node,"SPROCEDURE");
		node.children.add(tokenRead(node.getLine(),"SIDENTIFIER","ProcedureNameDec"));
		node.setLine(node.children.get(0).getLine());
		if(wordCheck(node.getLine(),"SLPAREN")) {
			node.children.add(temporaryParameter(node.getLine()));
			node.setLine(node.children.get(1).getLine());
		}
		node=wordCheckAndLine(node,"SSEMICOLON");
		return node;
	}

	public Node temporaryParameter(int line) throws EnshudSyntaxErrorException {
		if(!wordCheck(line,"SLPAREN")) {
			throw new EnshudSyntaxErrorException(lineCalc(line));
		}
		Node node=temporaryParameterList(line+1);
		node=wordCheckAndLine(node,"SRPAREN");
		return node;
	}

	public Node temporaryParameterList(int line) throws EnshudSyntaxErrorException {
		int numOfChild=0;
		Node node=temporaryParameterAndMold(line);
		if(wordCheck(node.getLine(),"SSEMICOLON")) {
			Node parentNode=createNode(node.getLine(),"VariableDeclarationsList");
			parentNode.children.add(node);
			numOfChild++;
		   while(wordCheck(parentNode.getLine(),"SSEMICOLON")) {
			   parentNode=wordCheckAndLine(parentNode,"SSEMICOLON");
			   parentNode.children.add(temporaryParameterAndMold(parentNode.getLine()));
			   parentNode.setLine(parentNode.children.get(numOfChild).getLine());
			   numOfChild++;
		   }
		   return parentNode;
		}
		else {
			return node;
		}
	}

	public Node temporaryParameterAndMold(int line) throws EnshudSyntaxErrorException {
		Node node=createNode(line,"TemporaryParameterAndMold");
		node.children.add(temporaryParameterNameList(node.getLine()));
		node.setLine(node.children.get(0).getLine());
		node=wordCheckAndLine(node,"SCOLON");
		node.children.add(tokenReadFromArray(node.getLine(),_standardMoldList,"StandardMold"));
		node.setLine(node.children.get(1).getLine());
		return node;
	}

	public Node temporaryParameterNameList(int line) throws EnshudSyntaxErrorException {
		int numOfChild=0;
		Node node=createNode(line,"TemporaryParameterNameList");
		   node.children.add(tokenRead(node.getLine(),"SIDENTIFIER","TemporaryParameterNameDec"));
		   node.setLine(node.children.get(numOfChild).getLine());
		   numOfChild++;
		   while(wordCheck(node.getLine(),"SCOMMA")) {
			   node.setLine(node.getLine()+1);
			   node.children.add(tokenRead(node.getLine(),"SIDENTIFIER","TemporaryParameterNameDec"));
			   node.setLine(node.children.get(numOfChild).getLine());
			   numOfChild++;
		   }
		return node;
	}

	public Node compoundStatements(int line) throws EnshudSyntaxErrorException {
		if(!wordCheck(line,"SBEGIN")) {
			throw new EnshudSyntaxErrorException(lineCalc(line));
		}
		Node node=statementsList(line+1,false);
		node=wordCheckAndLine(node,"SEND");
		return node;
	}

	public Node topCompoundStatements(int line) throws EnshudSyntaxErrorException {
		if(!wordCheck(line,"SBEGIN")) {
			throw new EnshudSyntaxErrorException(lineCalc(line));
		}
		Node node=statementsList(line+1,true);
		node=wordCheckAndLine(node,"SEND");
		return node;
	}

	public Node statementsList(int line,boolean isTop) throws EnshudSyntaxErrorException {
		Node node=createNode(line,"StatementsList");
		if(isTop)	{
			node.setRemark("Top");
		}
		int numOfChild=0;
		node.children.add(statementRead(node.getLine()));
		node.setLine(node.children.get(numOfChild).getLine());
		numOfChild++;
		node=wordCheckAndLine(node,"SSEMICOLON");
		while(wordCheckFromArray(node.getLine(),_statementsCheckList)) {
			node.children.add(statementRead(node.getLine()));
			node.setLine(node.children.get(numOfChild).getLine());
			numOfChild++;
			node=wordCheckAndLine(node,"SSEMICOLON");
		}
		return node;
	}

	public Node statementRead(int line) throws EnshudSyntaxErrorException {
		Node node;
		if(wordCheck(line,"SIF")) {
			node=ifThenStatement(line);
		}
		else if(wordCheck(line,"SWHILE")) {
			node=whileStatement(line);
		}
		else if(wordCheck(line,"SWRITELN")) {
			node=writeStatement(line);
		}
		else if(wordCheck(line,"SREADLN")) {
			node=readStatement(line);
		}
		else if(assignCheck(line)) {
			node=assignStatement(line);
		}
		else if(wordCheck(line,"SIDENTIFIER")) {
			node=procedureStatement(line);
		}
		else if(wordCheck(line,"SBEGIN")) {
			node=compoundStatements(line);
		}
		else {
			throw new EnshudSyntaxErrorException(lineCalc(line));
		}
		return node;
	}

	public Node ifThenStatement(int line) throws EnshudSyntaxErrorException {
		Node node=createNode(line,"IfThenStatement");
		node=wordCheckAndLine(node,"SIF");
		node.children.add(formulaRead(node.getLine()));
		node.setLine(node.children.get(0).getLine());
		node=wordCheckAndLine(node,"STHEN");
		node.children.add(compoundStatements(node.getLine()));
		node.setLine(node.children.get(1).getLine());
		if(wordCheck(node.getLine(),"SELSE")) {
			node=wordCheckAndLine(node,"SELSE");
			node.children.add(compoundStatements(node.getLine()));
			node.setLine(node.children.get(2).getLine());
		}
		return node;
	}

	public Node whileStatement(int line) throws EnshudSyntaxErrorException {
		Node node=createNode(line,"WhileStatement");
		node=wordCheckAndLine(node,"SWHILE");
		node.children.add(formulaRead(node.getLine()));
		node.setLine(node.children.get(0).getLine());
		node=wordCheckAndLine(node,"SDO");
		node.children.add(compoundStatements(node.getLine()));
		node.setLine(node.children.get(1).getLine());
		return node;
	}

	public Node readStatement(int line) throws EnshudSyntaxErrorException {
		Node node=createNode(line,"ReadStatement");
        node.children.add(tokenRead(node.getLine(),"SREADLN"));
        node.setLine(node.children.get(0).getLine());
        if(wordCheck(node.getLine(),"SLPAREN")) {
            node=wordCheckAndLine(node,"SLPAREN");
            node.children.add(variableList(node.getLine()));
            node.setLine(node.children.get(1).getLine());
            node=wordCheckAndLine(node,"SRPAREN");
        }
		return node;
	}

	public Node writeStatement(int line) throws EnshudSyntaxErrorException {
		Node node=createNode(line,"WriteStatement");
        node.children.add(tokenRead(node.getLine(),"SWRITELN"));
        node.setLine(node.children.get(0).getLine());
        if(wordCheck(node.getLine(),"SLPAREN")) {
            node=wordCheckAndLine(node,"SLPAREN");
            node.children.add(formulaList(node.getLine()));
            node.setLine(node.children.get(1).getLine());
            node=wordCheckAndLine(node,"SRPAREN");
        }
		return node;
	}

	public Node procedureStatement(int line) throws EnshudSyntaxErrorException {
		Node node=createNode(line,"ProcedureStatement");
		node.children.add(tokenRead(node.getLine(),"SIDENTIFIER","ProcedureNameUse"));
		node.setLine(node.children.get(0).getLine());
		if(wordCheck(node.getLine(),"SLPAREN")) {
			node=wordCheckAndLine(node,"SLPAREN");
			node.children.add(formulaList(node.getLine()));
			node.setLine(node.children.get(1).getLine());
			node=wordCheckAndLine(node,"SRPAREN");
		}
		return node;
	}

	public Node assignStatement(int line) throws EnshudSyntaxErrorException {
		Node node=createNode(line,"AssignStatement");
		node.children.add(variableRead(node.getLine()));
		node.setLine(node.children.get(0).getLine());
		node=wordCheckAndLine(node,"SASSIGN");
		node.children.add(formulaRead(node.getLine()));
		node.setLine(node.children.get(1).getLine());
		return node;
	}

	public boolean assignCheck(int line) {
		if(wordCheck(line,"SIDENTIFIER")&&wordCheckFromArray(line+1,_assignStatementsCheckList)) {
			return true;
		}
		return false;
	}

	public Node formulaList(int line) throws EnshudSyntaxErrorException {
		int numOfChild=0;
		Node node=formulaRead(line);
		if(wordCheck(node.getLine(),"SCOMMA")) {
			Node parentNode=createNode(node.getLine(),"FormulaList");
			parentNode.children.add(node);
			numOfChild++;
		   while(wordCheck(parentNode.getLine(),"SCOMMA")) {
			   parentNode.setLine(parentNode.getLine()+1);
			   parentNode.children.add(formulaRead(parentNode.getLine()));
			   parentNode.setLine(parentNode.children.get(numOfChild).getLine());
			   numOfChild++;
		   }
		   return parentNode;
		}
		else {
			return node;
		}
	}

	public Node formulaRead(int line) throws EnshudSyntaxErrorException {
		Node node=simpleFormula(line);
		if(wordCheckFromArray(node.getLine(),_relationalOperatorList)) {
			Node parentNode=tokenReadFromArray(node.getLine(),_relationalOperatorList,"Formula");
			parentNode.children.add(node);
			parentNode.children.add(simpleFormula(parentNode.getLine()));
			parentNode.setLine(parentNode.children.get(1).getLine());
			return parentNode;
		}
		return node;
	}

	public Node simpleFormula(int line) throws EnshudSyntaxErrorException {
		ArrayList<Node> nodeList = new ArrayList<Node>();
		int numOfTerm=0;
		nodeList.add(termRead(line,true));
		int readingLine=nodeList.get(0).getLine();
		while(wordCheckFromArray(readingLine,_additiveOperatorList)) {
			nodeList.add(tokenReadFromArray(readingLine,_additiveOperatorList,"SimpleFormula"));
			readingLine++;
			numOfTerm++;
			nodeList.get(numOfTerm).children.add(nodeList.get(numOfTerm-1));
			nodeList.get(numOfTerm).children.add(termRead(readingLine,false));
			readingLine=nodeList.get(numOfTerm).children.get(1).getLine();
		}
		nodeList.get(numOfTerm).setLine(readingLine);
		return nodeList.get(numOfTerm);
	}

	public Node termRead(int line,boolean isFirst) throws EnshudSyntaxErrorException {
		ArrayList<Node> nodeList = new ArrayList<Node>();
		int numOfFactor=0;
		if(isFirst) {
			nodeList.add(firstFactor(line));
		}
		else  {
			nodeList.add(factorRead(line));
		}
		int readingLine=nodeList.get(0).getLine();
		while(wordCheckFromArray(readingLine,_multiplicationOperatorList)) {
			nodeList.add(tokenReadFromArray(readingLine,_multiplicationOperatorList,"Term"));
			readingLine++;
			numOfFactor++;
			nodeList.get(numOfFactor).children.add(nodeList.get(numOfFactor-1));
			nodeList.get(numOfFactor).children.add(factorRead(readingLine));
			readingLine=nodeList.get(numOfFactor).children.get(1).getLine();
		}
		nodeList.get(numOfFactor).setLine(readingLine);
		return nodeList.get(numOfFactor);
	}


	public Node factorRead(int line) throws EnshudSyntaxErrorException {
		Node node;
		if(wordCheck(line,"SIDENTIFIER")) {
			node=variableRead(line);
		}
		else if(wordCheckFromArray(line,_constantList)) {
			node=tokenReadFromArray(line,_constantList,"Constant");;
		}
		else if(wordCheck(line,"SLPAREN")) {
			node=formulaRead(line+1);
			node=wordCheckAndLine(node,"SRPAREN");
		}
		else if(wordCheck(line,"SNOT")) {
			node=tokenRead(line,"SNOT");
			node.children.add(factorRead(node.getLine()));
			node.setLine(node.children.get(0).getLine());
		}
		else {
			throw new EnshudSyntaxErrorException(lineCalc(line));
		}
		return node;
	}

	public Node firstFactor(int line) throws EnshudSyntaxErrorException {
		Node node;
		if(wordCheckFromArray(line,_codeList)) {
			node=(tokenReadFromArray(line,_codeList,"Code"));
			node.children.add(factorRead(node.getLine()));
			node.setLine(node.children.get(0).getLine());
		}
		else {
			node=factorRead(line);
		}
		return node;
	}


	public Node variableRead(int line) throws EnshudSyntaxErrorException {
		Node node=tokenRead(line,"SIDENTIFIER","VariableUse");
		if(wordCheck(node.getLine(),"SLBRACKET")) {
			Node parentNode=createNode(node.getLine(),"VariableWithSubscript");
			parentNode.children.add(node);
			parentNode=wordCheckAndLine(parentNode,"SLBRACKET");
			parentNode.children.add(formulaRead(parentNode.getLine()));
			parentNode.setLine(parentNode.children.get(1).getLine());
			parentNode=wordCheckAndLine(parentNode,"SRBRACKET");
			return parentNode;
		}
		return node;
	}

	public Node variableList(int line) throws EnshudSyntaxErrorException {
		int numOfChild=0;
		Node node=variableRead(line);
		if(wordCheck(node.getLine(),"SCOMMA")) {
			Node parentNode=createNode(node.getLine(),"VariableList");
			parentNode.children.add(node);
			numOfChild++;
		   while(wordCheck(parentNode.getLine(),"SCOMMA")) {
			   parentNode.setLine(parentNode.getLine()+1);
			   parentNode.children.add(variableRead(parentNode.getLine()));
			   parentNode.setLine(parentNode.children.get(numOfChild).getLine());
			   numOfChild++;
		   }
		   return parentNode;
		}
		else {
			return node;
		}
	}

	public void treeDisplay(Node root,int level) {
		for(int i=0;i<level;i++) {
			System.out.print("  ");
		}
		System.out.println("$" + root.getWord()+"^"+root.getRemark()+"^"+root.getLine());
		for(Node node: root.children) {
			treeDisplay(node,level+1);
		}
	}

	public void treeOutput(Node root,int level,String outputFileName) {
		FileWriter fw = null;
		try {
        	 fw=new FileWriter(outputFileName,true);
             PrintWriter pw = new PrintWriter(fw);
    		for(int i=0;i<level;i++) {
    			pw.print("  ");
    		}
            pw.println("$" + root.getWord());
    		pw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
        	for(Node node: root.children) {
    			treeOutput(node,level+1,outputFileName);
    		}
        	try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}

	public void semErrorCheck() {
		   try {
				_root.accept(new CheckerVisitor(this));
				System.out.println("OK");
			} catch (EnshudSemanticErrorException e) {
				System.err.println("Semantic error: line "+ e.getLineNumber() );
			}
	}



	public void run(final String inputFileName) {
		_lineList = splitintoLine(inputFileName);
		if(_lineList!=null) {
		   _tokenList = new ArrayList<String>();
		   for(int i=0 ; i< _lineList.size() ; i++) {
			   _tokenList.add(splitintoToken(_lineList.get(i)));
		   }
		   try {
			   _root=programRead(_root);
			   semErrorCheck();
			   //treeDisplay(_root,0);
		   }catch(EnshudSyntaxErrorException e) {
			   System.err.println("Syntax error: line "+ e.getLineNumber() );
		   }

		}
	}

	public void run(final String inputFileName,final String treeOutputFileName) {
		_lineList = splitintoLine(inputFileName);
		if(_lineList!=null) {
		   _tokenList = new ArrayList<String>();
		   for(int i=0 ; i< _lineList.size() ; i++) {
			   _tokenList.add(splitintoToken(_lineList.get(i)));
		   }
		   try {
			   _root=programRead(_root);
			   System.out.println("OK");
			   treeDisplay(_root,0);
			   treeOutput(_root,0,treeOutputFileName);
		   }catch(EnshudSyntaxErrorException e) {
			   System.err.println("Syntax error: line "+ e.getLineNumber() );
		   }
		}
	}
}
