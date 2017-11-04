package exact;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import bn.core.*;
import bn.parser.*;

public class ExactInferer {

	BayesianNetwork bn = new BayesianNetwork();
	
	public ExactInferer(String filename, String queryvar, Map<String, Boolean> evidence) 
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
			System.exit(4);
		}
	}
	
	public void print() {
		bn.print();
	}
	
}
