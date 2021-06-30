package enshud;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import enshud.casl.CaslSimulator;
import enshud.s1.lexer.Lexer;
import enshud.s2.parser.Parser;
import enshud.s3.checker.Checker;
import enshud.s4.compiler.Compiler;

/**
 * !!! このコードは編集禁止 !!!
 * 
 */
public class Main {

	/**
	 * トラブル対策用のmain実行用クラス （サーバ用）．
	 * 
	 * 実行例：
	 * $ cd $WORKSPACE%/enshud
	 * $ java -jar enshud.jar enshud.Main lexer    data/pas/normal01.pas tmp/out.ts
	 * $ java -jar enshud.jar enshud.Main parser   data/ts/normal01.ts
	 * $ java -jar enshud.jar enshud.Main checker  data/ts/normal01.ts
	 * $ java -jar enshud.jar enshud.Main compiler data/ts/normal01.ts   tmp/out.cas
	 * $ java -jar enshud.jar enshud.Main casl     tmp/out.cas           tmp/out.ans
	 * $ java -jar enshud.jar enshud.Main all      data/pas/normal01.pas tmp/out.ans
	 * 
	 * Casl実行に標準入力を渡す場合：
	 * $ java -jar enshud.jar enshud.Main compiler data/ts/normal04.ts tmp/out.cas
	 * $ java -jar enshud.jar enshud.Main casl     tmp/out.cas         tmp/out.ans 36 48
	 */
	public static void main(final String[] args) {
		//		String s = "alll";
		//		System.out.println(s.matches("lexer|compiler|casl|all"));
		//		System.exit(0);

		if (args.length < 2) {
			printUsage();
			return;
		}
		final String subcommand = args[0];
		final String in = args[1];

		String out = "";
		if (subcommand.matches("lexer|compiler|casl|all")) {
			if (args.length < 3) {
				printUsage();
				return;
			}
			out = args[2];
		}

		if ("lexer".equals(subcommand)) {
			new Lexer().run(in, out);
		} else if ("parser".equals(subcommand)) {
			new Parser().run(in);
		} else if ("checker".equals(subcommand)) {
			new Checker().run(in);
		} else if ("compiler".equals(subcommand)) {
			new Compiler().run(in, out);
		} else if ("casl".equals(subcommand)) {
			final String[] params = shiftArray(args, 2);
			CaslSimulator.run(in, out, params);
		} else if ("all".equals(subcommand)) {
			if (!Files.isDirectory(Paths.get(out))) {
				System.out.println("error: specify an output dir instead of file");
				return;
			}
			final String base = out + "/" + getBaseName(in);
			final String ts = base + ".ts";
			final String cas = base + ".cas";
			final String ans = base + ".ans";
			final String[] params = shiftArray(args, 2);

			new Lexer().run(in, ts);
			new Compiler().run(ts, cas);
			CaslSimulator.run(cas, ans, params);
		} else {
			printUsage();
			return;
		}
	}

	private static void printUsage() {
		System.out.println("usage:");
		System.out.println("  lexer    in.pas out.ts");
		System.out.println("  parser   in.pas");
		System.out.println("  checker  in.pas");
		System.out.println("  compiler in.ts  out.cas");
		System.out.println("  casl     in.cas out.ans");
		System.out.println("  all      in.pas tmp/");
	}

	private static String getBaseName(final String name) {
		final String f = Paths.get(name).getFileName().toString();
		final int n = f.lastIndexOf('.');
		if (n > 0) {
			return f.substring(0, n);
		}
		return f;
	}

	private static String[] shiftArray(final String[] args, final int n) {
		final List<String> list = new ArrayList<String>(Arrays.asList(args));
		for (int i = 0; i <= n; i++) {
			list.remove(0);
		}
		return list.toArray(new String[list.size()]);
	}
}
