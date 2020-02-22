package xmlvalidator;

import java.util.*;
import java.util.regex.*;

public class RegexTester {
	public ArrayList<String> regexTest(String xmlDocument) {

		ArrayList<String> tags = new ArrayList();
		var p = Pattern.compile("(</?)[a-zA-Z0-9\". =#/]*?[^/>]>");
		var m = p.matcher(xmlDocument);

		while (m.find()) {
			tags.add(m.group());
		}
		return tags;
	}
}
