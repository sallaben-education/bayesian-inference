package approx;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import bn.core.Assignment;
import bn.core.Distribution;
import bn.core.RandomVariable;

/**
 * 
 * @author Steven Allaben
 *
 */
public class App {

	/*
	 * Main method of package approx
	 */
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
		System.out.println("Rejection sampling distribution: " + 
				(check(rs) ? rs : "{More samples necessary to compute distribution!}"));
		System.out.println("Likelihood weighting distribution: " + 
				(check(lw) ? lw : "{More samples necessary to compute distribution!}"));
		System.out.println("---------------------");
		System.exit(0);
	}
	
	/*
	 * Checks each value in the distribution to ensure that at least one sample has been
	 * recorded for that value. If none have, the distribution was not able to be computed.
	 */
	public static boolean check(Distribution d) {
		for(Double value : d.values()) {
			if(Double.isNaN(value)) {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * Parses "evidence" in the form of VAR1 VAL1 VAR2 VAL2 
	 * (after filename, query, and sample arguments).
	 */
	public static Assignment parseEvidence(String[] args) {
		Assignment evidence = new Assignment();
		RandomVariable v = null;
		boolean val = true;
		for(int i = 3; i < args.length; i++) {
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
