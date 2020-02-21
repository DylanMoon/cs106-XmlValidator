package xmlvalidator;

import static org.apache.commons.lang3.StringUtils.*;
import static sbcc.Core.*;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class BasicXmlValidator implements XmlValidator {

	@Override
	public List<String> validate(String xmlDocument) {// xmlDocument is the whole file as a single string
		BasicXmlTagStack bxts = new BasicXmlTagStack();// create xmlTagStack
		ArrayList<String> error = new ArrayList();// create error ArrayList
		var pattern = Pattern.compile("(</?)(.*?)([^/]>)");
		var matcher = pattern.matcher(xmlDocument);
		while (matcher.find()) {
			String tempTag = matcher.group().replaceAll("[<>]", "");
			if (tempTag.startsWith("?") || tempTag.startsWith("!")) {
				continue;
			}
			if (tempTag.contains(" ")) {
				tempTag = substringBetween(tempTag, "", " ");// cleaning up the tags
			}
			if (!tempTag.startsWith("/")) {
				bxts.push(new XmlTag(tempTag, matcher.start()));// opening tag, push to stack
				continue;
			} else {
				tempTag = tempTag.replaceAll("/", "");
				if (bxts.getCount() == 0) {// found closing tag but stack is empty
					error.add("Orphan closing tag");
					error.add(tempTag);
					error.add(Integer.toString(getLine(xmlDocument, matcher.start())));
					return error;
				}
				if (bxts.peek(0).name.equals(tempTag)) {// tag is correctly closed, pop stack and continue
					bxts.pop();
					continue;
				}
				if (!bxts.peek(0).name.equals(tempTag)) {// tag mismatch
					error.add("Tag mismatch");
					error.add(bxts.peek(0).name);
					error.add(Integer.toString(getLine(xmlDocument, bxts.peek(0).index)));
					error.add(tempTag);
					error.add(Integer.toString(getLine(xmlDocument, matcher.start())));
					return error;
				}

			}
		}
		if (bxts.getCount() > 0) {// reached end of document, stack is not empty
			error.add("Unclosed tag at end");
			error.add(bxts.peek(0).name);
			error.add(Integer.toString(getLine(xmlDocument, bxts.peek(0).index)));
			return error;
		}
		return null;// if no errors found
	}


	private int getLine(String xml, int index) {
		String sub = xml.substring(0, index);
		int count = countMatches(sub, "\n");
		return count + 1;
	}

}
