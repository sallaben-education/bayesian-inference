package exact;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
			System.exit(2);
		}
		String query = args[1];
		if(query.equals("true") || query.equals("false") || query.length() == 0) {
			System.err.println("You must include a query variable!");
			System.exit(3);
		}
		Assignment evidence = parseEvidence(args);
		ExactInferer inf = new ExactInferer("./networks/" + filename, query, evidence);
		Distribution d = inf.ask();
		System.out.println("File: " + filename);
		System.out.println("Variables: " + inf.bn.getVariableList());
		System.out.println("Evidence: " + evidence);
		System.out.println("Query variable: " + query);
		System.out.println("Query distribution: " + d);
	}
	
	public static Assignment parseEvidence(String[] args) {
		Assignment evidence = new Assignment();
		List<RandomVariable> friends = new ArrayList<>();
		for(int i = 2; i < args.length; i++) {
			if(args[i].equals("true") || args[i].equals("false")) {
				while(!friends.isEmpty()) {
					evidence.put(friends.get(0), args[i]);
					friends.remove(0);
				}
			} else {
				friends.add(new RandomVariable(args[i]));
			}
		}
		return evidence;
	}

}
