package exact;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

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
		String queryvar = args[1];
		if(queryvar.equals("true") || queryvar.equals("false") || queryvar.length() == 0) {
			System.err.println("You must include a query variable!");
			System.exit(3);
		}
		Map<String, Boolean> evidence = parseEvidence(args);
		ExactInferer inf = new ExactInferer("./networks/" + filename, queryvar, evidence);
		inf.print();
	}
	
	public static Map<String, Boolean> parseEvidence(String[] args) {
		Map<String, Boolean> evidence = new HashMap<>();
		List<String> friends = new ArrayList<>();
		for(int i = 2; i < args.length; i++) {
			if(args[i].equals("true") || args[i].equals("false")) {
				boolean value = Boolean.parseBoolean(args[i]);
				while(!friends.isEmpty()) {
					evidence.put(friends.get(0), value);
					friends.remove(0);
				}
			} else {
				friends.add(args[i]);
			}
		}
		return evidence;
	}

}
