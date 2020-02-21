package xmlvalidator;

import static sbcc.Core.*;

import java.io.*;

import static java.lang.System.*;
import static org.apache.commons.lang3.StringUtils.*;

/**
 * 
 * Dylan Moon CS106-62502
 *
 */
public class Main {

	public static void main(String[] args) throws IOException {
		BasicXmlValidator bxv = new BasicXmlValidator();
		String badXML1 = readFile("InvalidFile.xml");
		print(bxv.validate(badXML1));
		println();
		String badXML2 = readFile("InvalidFile2.xml");
		println(bxv.validate(badXML2));

	}
}
