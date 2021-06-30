package enshud.s1.lexer;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
public class LexerTest2 {
	@Rule
	public final Timeout globalTimeout = Timeout.seconds(10);
	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();

	private final ByteArrayOutputStream out = new ByteArrayOutputStream();
	private final ByteArrayOutputStream err = new ByteArrayOutputStream();

	private static final String TMP_OUT_TS = "tmp/out.ts";

	@Before
	public void before() throws IOException {
		final Path tmpOutPath = Paths.get(TMP_OUT_TS);
		Files.createDirectories(tmpOutPath.getParent());
		Files.deleteIfExists(tmpOutPath);

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
	public void testSelf01() {
		new Lexer().run("data2/pas/selftest1.pas", TMP_OUT_TS);
		final Path actual = Paths.get(TMP_OUT_TS);
		final Path expected = Paths.get("data2/ts/selfout1.ts");
		assertThat(actual).hasSameContentAs(expected);
		assertThat(err.toString().trim()).isEqualTo("inappropriate quotation");
	}
}
