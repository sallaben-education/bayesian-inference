package approx;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import bn.core.Assignment;
import bn.core.RandomVariable;

public class App {

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		if(args.length < 3) {
			System.err.println("You must include a network file, number of samples, and query variable!");
			System.exit(1);
		}
		int samples = Integer.parseInt(args[0]);
		if(samples <= 0) {
			System.err.println("You must include a number of samples greater than 0!");
			System.exit(2);
		}
		String filename = args[1];
		if(!(new File("./networks/" + filename).isFile())) {
			System.err.println("You must include a valid filename! [" + filename + "] does not exist in /networks/.");
			System.exit(3);
		}
		String query = args[2];
		if(query.equals("true") || query.equals("false") || query.length() == 0) {
			System.err.println("You must include a query variable!");
			System.exit(4);
		}
		Assignment evidence = parseEvidence(args);
		ApproxInferer inf = new ApproxInferer("./networks/" + filename, samples, query, evidence);
		System.out.println("Query variable: " + query);
		System.out.println("Evidence: " + evidence);
		System.out.println("Variables: " + inf.bn.getVariableList());
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
