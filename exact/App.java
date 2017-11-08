package exact;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import bn.core.Assignment;
import bn.core.Distribution;
import bn.core.RandomVariable;

public class App {

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		if(args.length < 2) {
			System.err.println("You must include a network file and query variable!");
			System.exit(1);
		}
		String filename = args[0];
		if(!(new File("./networks/" + filename).isFile())) {
			System.err.println("You must include a valid filename! [" + filename + "] does not exist in /networks/.");
			System.exit(3);
		}
		String query = args[1];
		if(query.equals("true") || query.equals("false") || query.length() == 0) {
			System.err.println("You must include a query variable!");
			System.exit(4);
		}
		System.out.println("---------------");
		System.out.println("Exact Inference");
		System.out.println("* * * * * * * *");
		Assignment evidence = parseEvidence(args);
		ExactInferer inf = new ExactInferer("./networks/" + filename, query, evidence);
		Distribution d = inf.ask();
		System.out.println("File: " + filename);
		System.out.println("Variables: " + inf.bn.getVariableList());
		System.out.println("Evidence: " + evidence);
		System.out.println("Query variable: " + query);
		System.out.println("Query distribution: " + d);
		System.out.println("---------------");
		System.exit(0);
	}
	
	public static Assignment parseEvidence(String[] args) {
		Assignment evidence = new Assignment();
		RandomVariable v = null;
		boolean val = true;
		for(int i = 2; i < args.length; i++) {
			if(val) {
				v = new RandomVariable(args[i]);
			} else {
				evidence.put(v, args[i]);
			}
			val = !val;
		}
		return evidence;
	}

}
