package approx;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import bn.core.Assignment;
import bn.core.BayesianNetwork;
import bn.core.RandomVariable;
import bn.parser.BIFLexer;
import bn.parser.BIFParser;
import bn.parser.XMLBIFParser;

public class ApproxInferer {
	
	BayesianNetwork bn = new BayesianNetwork();
	Assignment evidence;
	RandomVariable query;
	int samples;
	
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
		this.samples = samples;
		this.query = bn.getVariableByName(query);
		evidence.match(bn.getVariableList());
		this.evidence = evidence;
	}
	
	/*
	 * Returns an Assignment sampled from the prior knowledge in the BN
	 */
	public Assignment sample() {
		List<RandomVariable> vars = bn.getVariableListTopologicallySorted();
		Assignment s = new Assignment();
		for(RandomVariable v : vars) {
			s.put(v, bn.getNodeForVariable(v).sample(s));
		}		
		return s;
	}
	
	
}
