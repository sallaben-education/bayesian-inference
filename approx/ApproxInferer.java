package approx;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import bn.core.Assignment;
import bn.core.BayesianNetwork;
import bn.core.Distribution;
import bn.core.RandomVariable;
import bn.parser.BIFLexer;
import bn.parser.BIFParser;
import bn.parser.XMLBIFParser;

/**
 * 
 * @author Steven Allaben
 *
 */
public class ApproxInferer {
	
	BayesianNetwork bn;
	Assignment evidence;
	RandomVariable query;
	int samples;
	
	/*
	 * Constructor. Reads in a .xml or .bif file from the filesystem in the /networks/ folder.
	 * Uses Prof. Ferguson's parser code to interpret the network file as a BayesianNetwork.
	 */
	public ApproxInferer(String filename, int samples, String query, Assignment evidence) 
			throws IOException, ParserConfigurationException, SAXException {
		if(filename.endsWith(".xml")) {
			XMLBIFParser xbp = new XMLBIFParser();
			bn = xbp.readNetworkFromFile(filename);
		} else if(filename.endsWith(".bif")) {
			InputStream input = new FileInputStream(filename);
			BIFLexer bl = new BIFLexer(input);
			BIFParser bp = new BIFParser(bl);
			bn = bp.parseNetwork();
		} else {
			System.err.println("Input file [" + filename + "] does not have extension .xml or .bif!");
			System.exit(5);
		}
		evidence.match(bn.getVariableList());
		this.evidence = evidence;
		this.query = bn.getVariableByName(query);
		this.samples = samples;
	}
	
	/*
	 * Returns an Assignment randomly sampled from the prior knowledge in the BN
	 */
	public Assignment sample() {
		List<RandomVariable> vars = bn.getVariableListTopologicallySorted();
		Assignment s = new Assignment();
		for(RandomVariable v : vars) {
			s.put(v, bn.getNodeForVariable(v).sample(s));
		}		
		return s;
	}
	
	/*
	 * Using rejection sampling, returns a distribution for the query variable 
	 * reflecting the frequency of each value in the domain over a number 
	 * of random samples
	 */
	public Distribution rsample() {
		Distribution d = new Distribution(query);
		for(Object obj : query.getDomain()) {
			d.put(obj, 0.0);
		}
		for(int i = 0; i < samples; i++) {
			Assignment x = sample();
			if(x.consistent(evidence)) {
				d.put(x.get(query), d.get(x.get(query)) + 1.0);
			}
		}
		d.normalize();
		return d;
	}
	
	/*
	 * Returns an Assignment sampled from the prior knowledge in the BN
	 * This Assignment has a relevant .weight: the likelihood that the event
	 * happened, according to the evidence
	 */
	public Assignment wsample() {
		List<RandomVariable> vars = bn.getVariableListTopologicallySorted();
		Assignment s = new Assignment();
		for(RandomVariable v : vars) {
			if(s.containsKey(v)) {
				s.setWeight(s.weight * bn.getProb(v, s));
			} else {
				s.put(v, bn.getNodeForVariable(v).sample(s));
			}
		}		
		return s;
	}
	
	/*
	 * Using likelihood weighting, returns a distribution for the query variable 
	 * reflecting the frequency of each value in the domain over a number 
	 * of samples generated from the evidence values
	 */
	public Distribution lweight() {
		Distribution d = new Distribution(query);
		for(Object obj : query.getDomain()) {
			d.put(obj, 0);
		}
		for(int i = 0; i < samples; i++) {
			Assignment x = wsample();
			if(x.consistent(evidence)) {
				d.put(x.get(query), d.get(x.get(query)) + x.weight);
			}
		}
		d.normalize();
		return d;
	}

}
