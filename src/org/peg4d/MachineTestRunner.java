package org.peg4d;

import org.peg4d.vm.Machine;
import org.peg4d.vm.MachineContext;


public class MachineTestRunner {
	private final static String startPoint = "File";
	private static String pegFileName;
	private static String sourceFileName;

	public static void main(String[] args) {
		// parse argument
		final int size = args.length;
		if(size != 3) {
			System.err.println("illegal argument");
			showHelpAndExit(1);
		}
		for(int i = 0; i < size; i++) {
			String arg = args[i];
			if(arg.equals("-p") && i + 1 < size) {
				pegFileName = args[++i];
			}
			else if(!arg.startsWith("-")) {
				sourceFileName = args[i];
			}
			else {
				System.err.println("illegal argument");
				showHelpAndExit(1);
			}
		}

		// parse source file using traditional peg
		Grammar peg = new GrammarFactory().newGrammar("main", pegFileName);
		ParsingContext tradContext = peg.newParserContext(Main.loadSource(peg, sourceFileName));
		ParsingObject tradObj = tradContext.parseNode(startPoint);
		if(tradContext.hasUnconsumedCharacter()) {	// for debug purpose
			long pos = tradContext.getPosition();
			System.out.println("++++ debug message of traditional peg parser ++++");
			if(pos > 0) {
				tradContext.showPosition("consumed", pos-1);
			}
			tradContext.showPosition("unconsumed", pos);
		}

		// parse source file using peg vm
		peg = new GrammarFactory().newGrammar("main", pegFileName);
		CodeGenerator formatter = new CodeGenerator();
		peg.show(startPoint, formatter);
		Machine machine = new Machine();
		ParsingSource source = Main.loadSource(peg, sourceFileName);
		ParsingObject emptyObject = new ParsingObject(peg.getModelTag("#empty"), source, 0);
		MachineContext mc = new MachineContext(emptyObject, source, 0);
		ParsingObject mObj = machine.run(mc, 1, formatter.opList.ArrayValues);
		if(mc.hasUnconsumedCharacter()) {	// for debug purpose
			long pos = mc.getPosition();
			System.out.println("++++ debug message of peg vm ++++");
			if(pos > 0) {
				mc.showPosition("consumed", pos-1);
			}
			mc.showPosition("unconsumed", pos);
		}

		// compare object structure
		boolean result = compare(tradObj, mObj);

		if(result) {
			System.out.println("[success]: " + sourceFileName);
		}
		else {
			System.out.println("[failed]: " + sourceFileName);
		}
		System.exit(result ? 0 : 1);
	}

	private static void showHelpAndExit(int status) {
		System.err.println("option:");
		System.err.println("\t-p [peg file] [source file]");
		System.exit(1);
	}

	private static boolean compare(ParsingObject trad, ParsingObject mc) {
		// check error
		if(trad.isFailure() || trad.is(ParsingTag.tagId("#error"))) {
			System.err.println("parsing error happened");
			System.err.println(trad);
			return false;
		}
		
		// check object existence
		if(mc == null) {
			System.err.println("not exist object");
			return false;
		}
		
		// compare tag
		if(!mc.is(trad.getTag().tagId)) {
			System.err.println("unmatched tag: " + trad.getTag() + ", " + mc.getTag());
			return false;
		}

		// compare child size
		final int tradSize = trad.size();
		if(tradSize != mc.size()) {
			System.err.println("unmatched size: " + trad.size() + ", " + mc.size());
			return false;
		}

		// comapre text
		if(!trad.getText().equals(mc.getText())) {
			System.err.println("unmatched text");
			return false;
		}

		// compare child
		for(int i = 0; i < tradSize; i++) {
			if(!compare(trad.get(i), mc.get(i))) {
				return false;
			}
		}
		return true;
	}
}
