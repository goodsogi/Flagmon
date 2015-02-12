package com.pluslibrary.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;

import com.pluslibrary.server.PlusXmlParser;

public class PlusDomParser extends PlusXmlParser {

	public ArrayList<ArrayList<PlusModel>> doIt(InputStream in) {
		ArrayList<ArrayList<PlusModel>> model = new ArrayList<ArrayList<PlusModel>>();

		ArrayList<PlusModel> data = new ArrayList<PlusModel>();

		Document doc = getDocument(in);

		// Get elements by name employee
		NodeList nodeList = doc.getElementsByTagName("ROW");

		/*
		 * for each <employee> element get text of name, salary and designation
		 */
		// Here, we have only one <employee> element

		if (nodeList.getLength() == 0)
			return null;
		String preCate = getValue((Element) nodeList.item(0), "cate");
		for (int i = 0; i < nodeList.getLength(); i++) {

			Element e = (Element) nodeList.item(i);

			if (!getValue(e, "cate").equals(preCate)) {
				model.add(data);
				data = new ArrayList<PlusModel>();
				preCate = getValue(e, "cate");

			}

			PlusModel b = new PlusModel();
			b.img1 = getValue(e, "img1");

			data.add(b);

		}

		if (preCate != null && !preCate.equals(""))
			model.add(data);

		return model;

	}

	// Returns the entire XML document
	public static Document getDocument(InputStream inputStream) {
		Document document = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// CDATA 파싱 가능하게 설정
		factory.setCoalescing(true);
		try {
			DocumentBuilder db = factory.newDocumentBuilder();
			InputSource inputSource = new InputSource(inputStream);
			document = db.parse(inputSource);
		} catch (ParserConfigurationException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		} catch (SAXException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		} catch (IOException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		}
		return document;
	}

	/*
	 * I take a XML element and the tag name, look for the tag and get the text
	 * content i.e for <employee><name>Kumar</name></employee> XML snippet if
	 * the Element points to employee node and tagName is name I will return
	 * Kumar. Calls the private method getTextNodeValue(node) which returns the
	 * text value, say in our example Kumar.
	 */
	public static String getValue(Element item, String name) {
		NodeList nodes = item.getElementsByTagName(name);
		// cdata 처리
		// return getTextNodeValue(nodes.item(0));
		return getElementValue(nodes.item(0));

	}

	static public final String getElementValue(Node elem) {
		try {
			Node child;
			if (elem != null) {
				if (elem.hasChildNodes()) {
					for (child = elem.getFirstChild(); child != null; child = child
							.getNextSibling()) {
						if (child.getNodeType() == Node.CDATA_SECTION_NODE
								|| child.getNodeType() == Node.TEXT_NODE) {
							return child.getNodeValue().trim();
						}
					}
				}
			}
			return "";
		} catch (DOMException e) {
			// Logger.logError(e);
			return "";
		}
	}

	static private final String getTextNodeValue(Node node) {
		Node child;
		if (node != null) {
			if (node.hasChildNodes()) {
				child = node.getFirstChild();
				while (child != null) {
					if (child.getNodeType() == Node.TEXT_NODE) {
						return child.getNodeValue();
					}
					child = child.getNextSibling();
				}
			}
		}
		return "";
	}
}
