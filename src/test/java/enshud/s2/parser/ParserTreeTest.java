package enshud.s2.parser;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.commons.io.output.TeeOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ParserTreeTest {
	@Rule
	public final Timeout globalTimeout = Timeout.seconds(10);
	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();

	private final ByteArrayOutputStream out = new ByteArrayOutputStream();
	private final ByteArrayOutputStream err = new ByteArrayOutputStream();

	@Before
	public void before() {
		final TeeOutputStream outTree = new TeeOutputStream(System.out, out);
		final TeeOutputStream errTree = new TeeOutputStream(System.err, err);
		System.setOut(new PrintStream(outTree));
		System.setErr(new PrintStream(errTree));
	}

	@After
	public void after() throws IOException {
		out.close();
		err.close();
	}

	@Test
	public void testNormal01() {
		new ParserTree().run("data/ts/normal01.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	@Test
	public void testNormal02() {
		new ParserTree().run("data/ts/normal02.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	@Test
	public void testNormal03() {
		new ParserTree().run("data/ts/normal03.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	@Test
	public void testNormal04() {
		new ParserTree().run("data/ts/normal04.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	@Test
	public void testNormal05() {
		new ParserTree().run("data/ts/normal05.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	@Test
	public void testNormal06() {
		new ParserTree().run("data/ts/normal06.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	@Test
	public void testNormal07() {
		new ParserTree().run("data/ts/normal07.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	@Test
	public void testNormal08() {
		new ParserTree().run("data/ts/normal08.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	@Test
	public void testNormal09() {
		new ParserTree().run("data/ts/normal09.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	@Test
	public void testNormal10() {
		new ParserTree().run("data/ts/normal10.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	@Test
	public void testNormal11() {
		new ParserTree().run("data/ts/normal11.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	@Test
	public void testNormal12() {
		new ParserTree().run("data/ts/normal12.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	@Test
	public void testNormal13() {
		new ParserTree().run("data/ts/normal13.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	@Test
	public void testNormal14() {
		new ParserTree().run("data/ts/normal14.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	@Test
	public void testNormal15() {
		new ParserTree().run("data/ts/normal15.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	@Test
	public void testNormal16() {
		new ParserTree().run("data/ts/normal16.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	@Test
	public void testNormal17() {
		new ParserTree().run("data/ts/normal17.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	@Test
	public void testNormal18() {
		new ParserTree().run("data/ts/normal18.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	@Test
	public void testNormal19() {
		new ParserTree().run("data/ts/normal19.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	@Test
	public void testNormal20() {
		new ParserTree().run("data/ts/normal20.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	////////////////////////////////////////////////////////////////////////////////

	@Test
	public void testSynerr01() {
		new ParserTree().run("data/ts/synerr01.ts");
		assertThat(err.toString().trim()).isEqualTo("Syntax error: line 1");
	}

	@Test
	public void testSynerr02() {
		new ParserTree().run("data/ts/synerr02.ts");
		assertThat(err.toString().trim()).isEqualTo("Syntax error: line 3");
	}

	@Test
	public void testSynerr03() {
		new ParserTree().run("data/ts/synerr03.ts");
		assertThat(err.toString().trim()).isEqualTo("Syntax error: line 8");
	}

	@Test
	public void testSynerr04() {
		new ParserTree().run("data/ts/synerr04.ts");
		assertThat(err.toString().trim()).isEqualTo("Syntax error: line 10");
	}

	@Test
	public void testSynerr05() {
		new ParserTree().run("data/ts/synerr05.ts");
		assertThat(err.toString().trim()).isEqualTo("Syntax error: line 11");
	}

	@Test
	public void testSynerr06() {
		new ParserTree().run("data/ts/synerr06.ts");
		assertThat(err.toString().trim()).isEqualTo("Syntax error: line 13");
	}

	@Test
	public void testSynerr07() {
		new ParserTree().run("data/ts/synerr07.ts");
		assertThat(err.toString().trim()).isEqualTo("Syntax error: line 30");
	}

	@Test
	public void testSynerr08() {
		new ParserTree().run("data/ts/synerr08.ts");
		assertThat(err.toString().trim()).isEqualTo("Syntax error: line 31");
	}

	////////////////////////////////////////////////////////////////////////////////

	@Test
	public void testSemerr01() {
		new ParserTree().run("data/ts/semerr01.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	@Test
	public void testSemerr02() {
		new ParserTree().run("data/ts/semerr02.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	@Test
	public void testSemerr03() {
		new ParserTree().run("data/ts/semerr03.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	@Test
	public void testSemerr04() {
		new ParserTree().run("data/ts/semerr04.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	@Test
	public void testSemerr05() {
		new ParserTree().run("data/ts/semerr05.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	@Test
	public void testSemerr06() {
		new ParserTree().run("data/ts/semerr06.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	@Test
	public void testSemerr07() {
		new ParserTree().run("data/ts/semerr07.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	@Test
	public void testSemerr08() {
		new ParserTree().run("data/ts/semerr08.ts");
		assertThat(out.toString().trim()).isEqualTo("OK");
	}

	////////////////////////////////////////////////////////////////////////////////

	/**
	 *  入力ファイルが存在しない場合に正しく動作するか
	 */
	@Test
	public void testXInputFileNotFound() {
		new ParserTree().run("data/ts/xxxxxxxx.ts");
		assertThat(err.toString().trim()).isEqualTo("File not found");
	}

}
