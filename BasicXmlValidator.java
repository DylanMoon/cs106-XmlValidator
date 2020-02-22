package xmlvalidator;

import static org.apache.commons.lang3.StringUtils.*;
import static sbcc.Core.*;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class BasicXmlValidator implements XmlValidator {

	@Override
	public List<String> validate(String xmlDocument) {// xmlDocument is the whole file as a single string
		BasicXmlTagStack tagStack = new BasicXmlTagStack();// create xmlTagStack
		ArrayList<String> error = new ArrayList();// create error ArrayList
		var p = Pattern.compile("(</?)[a-zA-Z0-9\". =#/]*?[^/]>");
		var m = p.matcher(xmlDocument);
		while (m.find()) {
			String tempTag = m.group().replaceAll("[<>]", "");// set tempTag to the current match and remove <>
			if (tempTag.startsWith("/")) {// tempTags is a closing tag
				tempTag = tempTag.replaceAll("/", "");
				if (tagStack.getCount() == 0) {// stack is empty "Orphan"

					error.add("Orphan closing tag");
					error.add(tempTag);
					error.add(Integer.toString(getLine(xmlDocument, m.start())));
					return error;
				}
				if (tagStack.peek(0).name.equals(tempTag)) {// tag is correctly closed, pop stack and continue
					tagStack.pop();
					continue;
				}
				// tag mismatch
				error.add("Tag mismatch");
				error.add(tagStack.peek(0).name);
				error.add(Integer.toString(getLine(xmlDocument, tagStack.peek(0).index)));
				error.add(tempTag);
				error.add(Integer.toString(getLine(xmlDocument, m.start())));
				return error;
			} else {
				if (tempTag.contains("=")) { // opening tag has an attribute
					String attributeName = substringBetween(tempTag, " ", "=");
					String attribute = substringAfter(tempTag, "=");
					if (!attribute.startsWith("\"")) {// attribute does not start with quotes

						error.add("Attribute not quoted");
						error.add(substringBefore(tempTag, " "));
						error.add(Integer.toString(getLine(xmlDocument, m.start())));
						error.add(attributeName);
						error.add(Integer.toString(getLine(xmlDocument, m.end())));
						return error;
					}
					tempTag = substringBetween(tempTag, "", " ");// remove the attributes from the tag
				}
				tagStack.push(new XmlTag(tempTag, m.start()));// opening tag, push to stack
				continue;
			}
		}
		if (tagStack.getCount() > 0) {// reached end of document, stack is not empty

			error.add("Unclosed tag at end");
			error.add(tagStack.peek(0).name);
			error.add(Integer.toString(getLine(xmlDocument, tagStack.peek(0).index)));
			return error;
		}
		return null;// no errors found
	}


	private int getLine(String xml, int index) {
		String sub = xml.substring(0, index);
		int count = countMatches(sub, "\n");
		return count + 1;
	}
}
