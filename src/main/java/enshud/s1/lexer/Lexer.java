package enshud.s1.lexer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class Lexer {

	final String[] _token = new String[46];{
		_token[0]="SAND";
        _token[1]="SARRAY";
        _token[2]="SBEGIN";
        _token[3]="SBOOLEAN";
        _token[4]="SCHAR";
        _token[5]="SDIVD";
        _token[6]="SDO";
        _token[7]="SELSE";
        _token[8]="SEND";
        _token[9]="SFALSE";
        _token[10]="SIF";
        _token[11]="SINTEGER";
        _token[12]="SMOD";
        _token[13]="SNOT";
        _token[14]="SOF";
        _token[15]="SOR";
        _token[16]="SPROCEDURE";
        _token[17]="SPROGRAM";
        _token[18]="SREADLN";
        _token[19]="STHEN";
        _token[20]="STRUE";
        _token[21]="SVAR";
        _token[22]="SWHILE";
        _token[23]="SWRITELN";
        _token[24]="SEQUAL";
        _token[25]="SNOTEQUAL";
        _token[26]="SLESS";
        _token[27]="SLESSEQUAL";
        _token[28]="SGREATEQUAL";
        _token[29]="SGREAT";
        _token[30]="SPLUS";
        _token[31]="SMINUS";
        _token[32]="SSTAR";
        _token[33]="SLPAREN";
        _token[34]="SRPAREN";
        _token[35]="SLBRACKET";
        _token[36]="SRBRACKET";
        _token[37]="SSEMICOLON";
        _token[38]="SCOLON";
        _token[39]="SRANGE";
        _token[40]="SASSIGN";
        _token[41]="SCOMMA";
        _token[42]="SDOT";
        _token[43]="SIDENTIFIER";
        _token[44]="SCONSTANT";
        _token[45]="STRING";
	}

	HashMap<String,Integer> _tokenNum = new HashMap<String,Integer>();{
        _tokenNum.put("and",0);
        _tokenNum.put("array",1);
        _tokenNum.put("begin",2);
        _tokenNum.put("boolean",3);
        _tokenNum.put("char",4);
        _tokenNum.put("div",5);
        _tokenNum.put("/",5);
        _tokenNum.put("do",6);
        _tokenNum.put("else",7);
        _tokenNum.put("end",8);
        _tokenNum.put("false",9);
        _tokenNum.put("if",10);
        _tokenNum.put("integer",11);
        _tokenNum.put("mod",12);
        _tokenNum.put("not",13);
        _tokenNum.put("of",14);
        _tokenNum.put("or",15);
        _tokenNum.put("procedure",16);
        _tokenNum.put("program",17);
        _tokenNum.put("readln",18);
        _tokenNum.put("then",19);
        _tokenNum.put("true",20);
        _tokenNum.put("var",21);
        _tokenNum.put("while",22);
        _tokenNum.put("writeln",23);
        _tokenNum.put("=",24);
        _tokenNum.put("<>",25);
        _tokenNum.put("<",26);
        _tokenNum.put("<=",27);
        _tokenNum.put(">=",28);
        _tokenNum.put(">",29);
        _tokenNum.put("+",30);
        _tokenNum.put("-",31);
        _tokenNum.put("*",32);
        _tokenNum.put("(",33);
        _tokenNum.put(")",34);
        _tokenNum.put("[",35);
        _tokenNum.put("]",36);
        _tokenNum.put(";",37);
        _tokenNum.put(":",38);
        _tokenNum.put("..",39);
        _tokenNum.put(":=",40);
        _tokenNum.put(",",41);
        _tokenNum.put(".",42);
        _tokenNum.put("namae",43);
        _tokenNum.put("hugounashiseisuu",44);
        _tokenNum.put("mojireru",45);
	}

	PrintWriter _pw;

	/**
	 * サンプルmainメソッド．
	 * 単体テストの対象ではないので自由に改変しても良い．
	 */
	public static void main(final String[] args) {
		// normalの確認
		new Lexer().run("data2/pas/read1.pas", "tmp/test.ts");
		//new Lexer().run("data/pas/normal02.pas", "tmp/out2.ts");
		//new Lexer().run("data/pas/normal03.pas", "tmp/out3.ts");
	}

	/**
	 * TODO
	 *
	 * 開発対象となるLexer実行メソッド．
	 * 以下の仕様を満たすこと．
	 *
	 * 仕様:
	 * 第一引数で指定されたpasファイルを読み込み，トークン列に分割する．
	 * トークン列は第二引数で指定されたtsファイルに書き出すこと．
	 * 正常に処理が終了した場合は標準出力に"OK"を，
	 * 入力ファイルが見つからない場合は標準エラーに"File not found"と出力して終了すること．
	 *
	 * @param inputFileName 入力pasファイル名
	 * @param outputFileName 出力tsファイル名
	 */

	public boolean isAlphabet(int ch) {
        if('a'<=ch && ch<='z' || 'A'<=ch && ch<='Z')
            return true;
        else
            return false;
	}

    public boolean isNumber(int ch) {
        if('0'<=ch && ch<='9')
            return true;
        else
            return false;
    }

    public boolean isSingleSpecificSymbol(int ch) {
        if(ch=='+'||ch=='-'||ch=='*'||ch=='/'||ch=='='||ch=='('||ch==')'||ch=='['||ch==']'||ch==','||ch==';')
            return true;
        else
            return false;
    }

    public String tokenSearch(String s) {
    	String token="";
      if(_tokenNum.containsKey(s)) {//特殊記号または綴り記号
    	  token=_token[_tokenNum.get(s)];
      }
      else {//その他は最初の文字で種類を判断
    	  int first=s.charAt(0);
          if(isAlphabet(first)) {
              token = "SIDENTIFIER";
          }else if(isNumber(first)) {
              token = "SCONSTANT";
          }else if(first=='\'') {
              token = "SSTRING";
          }else if(first=='{') {
              token = "COMMENT";
          }else {
              token = "ERROR";
          }
      }
    	return token;
    }

    public int idSearch(String s) {
    	int id;
      if(_tokenNum.containsKey(s)) {//特殊記号または綴り記号
    	  id=_tokenNum.get(s);
      }
      else {//その他は最初の文字で種類を判断
    	  int first=s.charAt(0);
          if(isAlphabet(first)) {
              id = 43;
          }else if(isNumber(first)) {
              id = 44;
          }else if(first=='\'') {
              id = 45;
          }else if(first=='{') {
              id = 46;
          }else {
              id = -1;
          }
      }
    	return id;
    }

    public void Output(String word,int NumOfLines) {
    	int id;
    	String token;
        id=idSearch(word);
        token=tokenSearch(word);
        if(id!=46) {
        _pw.println(word+"\t"+token+"\t"+id+"\t"+NumOfLines);
        }
    }

	public void run(final String inputFileName, final String outputFileName) {

		// TODO
        int NumOfLines=1;
        String word="";
		int readChara;
		boolean quotationCheck=false;
		try
		{
			FileReader fr = new FileReader( inputFileName );
			_pw = new PrintWriter(outputFileName);
			BufferedReader br = new BufferedReader(fr);
			readChara = br.read();
            while( readChara != -1 ) {
            	if(isAlphabet(readChara)) {
                     word=word+(char)readChara;
                     readChara=br.read();
                     while(isAlphabet(readChara)||isNumber(readChara)) {
                         word=word+(char)readChara;
                         readChara=br.read();
                     }
                     Output(word,NumOfLines);
                     word="";
            	}
            	else if(isNumber(readChara)) {
                    word=word+(char)readChara;
                    readChara=br.read();
                    while(isNumber(readChara)) {
                        word=word+(char)readChara;
                        readChara=br.read();
                    }
                    Output(word,NumOfLines);
                    word="";
            	}
            	else if(isSingleSpecificSymbol(readChara)) {
            		word=word+(char)readChara;
                    Output(word,NumOfLines);
                    word="";
                    readChara=br.read();
            	}
            	else if(readChara==':'||readChara=='>') {
                    word=word+(char)readChara;
                    readChara=br.read();
                    if(readChara=='=') {
                        word=word+(char)readChara;
                        Output(word,NumOfLines);
                        word="";
                        readChara=br.read();
                    }
                    else {
                        Output(word,NumOfLines);
                        word="";
                    }
            	}
            	else if(readChara=='<') {
                    word=word+(char)readChara;
                    readChara=br.read();
                    if(readChara=='='||readChara=='>') {
                        word=word+(char)readChara;
                        Output(word,NumOfLines);
                        word="";
                        readChara=br.read();
                    }
                    else {
                        Output(word,NumOfLines);
                        word="";
                    }
            	}
            	else if(readChara=='.') {
                    word=word+(char)readChara;
                    readChara=br.read();
                    if(readChara=='.') {
                        word=word+(char)readChara;
                        Output(word,NumOfLines);
                        word="";
                        readChara=br.read();
                    }
                    else {
                        Output(word,NumOfLines);
                        word="";
                    }
            	}
            	else if(readChara=='\n') {
            		readChara=br.read();
            		NumOfLines++;
            	}
            	else if(readChara==' '||readChara=='\t') {
            		readChara=br.read();
            	}
            	else if(readChara=='\'') {
                    word=word+(char)readChara;
                    readChara=br.read();
                    while(readChara!='\''&&readChara!='\n'&&readChara!=-1) {
                        word=word+(char)readChara;
                        readChara=br.read();
                    }
                    if(readChara!='\'') {
                    	quotationCheck=true;
                        word=word+(char)readChara;
                        readChara=br.read();
                        Output("%",NumOfLines);
                        word="";
                    }
                    else {
                    word=word+(char)readChara;
                    readChara=br.read();
                    Output(word,NumOfLines);
                    word="";
                    }
            	}
            	else if(readChara=='{') {
                    word=word+(char)readChara;
                    readChara=br.read();
                    while(readChara!='}'&&readChara!='\n'&&readChara!=-1) {
                        word=word+(char)readChara;
                        readChara=br.read();
                    }
                    word=word+(char)readChara;
                    readChara=br.read();
                    Output(word,NumOfLines);
                    word="";
            	}
            	else {
            		word=word+(char)readChara;
                    Output(word,NumOfLines);
                    word="";
                    readChara=br.read();
            	}
            	if(quotationCheck) break;
            }
            br.close();  //frも同時にclose
            _pw.close();
            if(quotationCheck) {
            	System.err.println("inappropriate quotation");
            }
            else {
            System.out.println("OK");
            }
		}
		catch( FileNotFoundException e ){
			System.err.println("File not found");
		}
        catch( IOException e )
        {
            System.err.println(e);
        }

	}
}
