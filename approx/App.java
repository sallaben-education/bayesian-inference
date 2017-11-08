package approx;

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
		System.out.println("---------------------");
		System.out.println("Approximate Inference");
		System.out.println("* * * * * * * * * * *");
		Assignment evidence = parseEvidence(args);
		ApproxInferer inf = new ApproxInferer("./networks/" + filename, samples, query, evidence);
		Distribution rs = inf.rsample();
		Distribution lw = inf.lweight();
		System.out.println("File: " + filename);
		System.out.println("Number of samples: " + samples);
		System.out.println("Variables: " + inf.bn.getVariableList());
		System.out.println("Evidence: " + evidence);
		System.out.println("Query variable: " + query);
		System.out.println("Rejection sampling distribution: " + rs);
		System.out.println("Likelihood weighting distribution: " + lw);
		System.out.println("---------------------");
		System.exit(0);
	}
	
	public static Assignment parseEvidence(String[] args) {
		Assignment evidence = new Assignment();
		List<RandomVariable> friends = new ArrayList<>();
		boolean val = true;
		for(int i = 3; i < args.length; i++) {
			if(val) {
				friends.add(new RandomVariable(args[i]));
			} else {
				evidence.put(friends.get(0), args[i]);
				friends.remove(0);
			}
			val = !val;
		}
		return evidence;
	}

}
