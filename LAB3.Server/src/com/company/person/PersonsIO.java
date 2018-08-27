package com.company.person;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.*;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.company.date.*;
import org.xml.sax.SAXParseException;

/**This class is to read Persons class from xml-file and to make xml-file using Persons class.
 * Uses DOM library to work with XML.
 * @author Rostislav Pekhovsky 2018
 * @version 0.1
 * @see Persons*/
public class PersonsIO {
	/*--------------------------------------read---------------------------------------------*/
	/**to fulfill persons by xml-file
	 * @param location location of xml-file
	 * @param persons persons to fulfill by xml-file*/
	public static boolean read(String location, String xldLocation, Persons persons) {
			if (!validateXML(new File(location), new File(xldLocation))) {
				System.out.println("Validation hasn't been completed!");
				return false;
			}

		try {
			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = documentBuilder.parse(location);
			getPersonsFromDoc(document.getDocumentElement(), persons);	
			
		} catch (ParserConfigurationException | SAXException | IOException ex) {
			ex.printStackTrace(System.out);
			return false;
		}
		return true;
	}
	/**gets persons from document
	 * @param document main node
	 * @param persons persons to fulfill by xml-file*/
	private static void getPersonsFromDoc(Node document, Persons persons) {
		NodeList nodePersons = document.getChildNodes();
		persons.lastId = Integer.parseInt(document.getAttributes().getNamedItem("lastId").getTextContent());

		for (int i = 0; i < nodePersons.getLength(); i++) {
			Node nodePerson = nodePersons.item(i);
			if (nodePerson.getNodeType() != Node.TEXT_NODE) {
				Person person = getPersonFromNode(nodePerson);
				persons.add(person);
			} 
		}
	}

	/**gets person from node
	 * @param node node with person data
	 * @return fulfilled person*/
	private static Person getPersonFromNode(Node node) {
		
		NodeList nodes = node.getChildNodes();
		Person person = new Person();
		
		for (int j = 0; j < nodes.getLength(); j++) {
			
			Node prop = nodes.item(j);    
			
			if (prop.getNodeType() != Node.TEXT_NODE) {
				
				String propName = prop.getChildNodes().item(0).getTextContent();

				System.out.println(propName);

				if (prop.getNodeName().equals("id")) {
					person.id = Integer.parseInt(propName);
				}
				if (prop.getNodeName().equals("GivenName")) {
					person.givenName = propName;
				}
				if (prop.getNodeName().equals("MiddleName")) {
					person.middleName = propName;
				}
				if (prop.getNodeName().equals("Surname")) {
					person.surname = propName;
				}
				if (prop.getNodeName().equals("DateOfBirth")) {
					person.dateOfBirth = DateTranslation.convertStringToDate(propName);
				}
				if (prop.getNodeName().equals("CellPhone")) {
					person.cellPhone = propName;
				}
				if (prop.getNodeName().equals("Experience")) {
					person.experience = propName;
				}
				if (prop.getNodeName().equals("Skill")) {
					person.addSkill(propName);
				}
			}
		}
		return person;
	}


	public static boolean validateXML(File xml, File xsd) {
		try {
			SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(xsd).newValidator().validate(new StreamSource(xml));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/*--------------------------------------write---------------------------------------------*/
	/**to fulfill xml-file by persons
	 * @param location location of xml-file
	 * @param persons persons */
	public static boolean write(String location, Persons persons) {
		
		try {	
			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = documentBuilder.newDocument();	
			getDocFromPersons(document, persons);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(location));
			transformer.transform(source, result);
			return true;

		} catch (ParserConfigurationException | TransformerException ex) {
			//ex.printStackTrace(System.out);
			return false;
		}
	}

	/**gets document from persons
	 * @param document main node
	 * @param persons persons */
	private static void getDocFromPersons(Document document, Persons persons) {

		document.createAttribute("lastId");
		Element rootElement = document.createElement("Catalogue");
		rootElement.setAttribute("lastId", persons.lastId.toString());
		document.appendChild(rootElement);


		int id = 0;
		for (Person person : persons) {
			id++;
			person.id = id;
			
			Element personElement = document.createElement("Person");
			rootElement.appendChild(personElement);

			Element idElement = document.createElement("id");
			personElement.appendChild(idElement);
			idElement.appendChild(document.createTextNode(person.id.toString()));

			Element givenNameElement = document.createElement("GivenName");
			personElement.appendChild(givenNameElement);	
			givenNameElement.appendChild(document.createTextNode(person.givenName));
				
			Element middleNameElement = document.createElement("MiddleName");
			personElement.appendChild(middleNameElement);	
			middleNameElement.appendChild(document.createTextNode(person.middleName));
			
			Element surnameElement = document.createElement("Surname");
			personElement.appendChild(surnameElement);
			surnameElement.appendChild(document.createTextNode(person.surname));
			
			Element dateOfBirthElement = document.createElement("DateOfBirth");
			personElement.appendChild(dateOfBirthElement);	
			dateOfBirthElement.appendChild(document.createTextNode(DateTranslation.convertDateToString(person.dateOfBirth)));	
		
			Element cellPhoneElement = document.createElement("CellPhone");
			personElement.appendChild(cellPhoneElement);	
			cellPhoneElement.appendChild(document.createTextNode(person.cellPhone));
			
			Element experienceElement = document.createElement("Experience");
			personElement.appendChild(experienceElement);	
			experienceElement.appendChild(document.createTextNode(person.experience));
			
			for (int i = 0; i < person.skills.size(); i++) {	
				Element skillElement = document.createElement("Skill");
				personElement.appendChild(skillElement);	
				skillElement.appendChild(document.createTextNode(person.skills.get(i)));
			}
		}
		//power shell
	}
	/*private static void getXMLFromZip() {

		byte[] buffer = new byte[1024];

     try {
		//get the zip file content
		ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream("List.zip"));
		//get the zipped file list entry
		ZipEntry zipEntry = zipInputStream.getNextEntry();

		File newFile = new File("List.xml");

		FileOutputStream fileOutputStream = new FileOutputStream(newFile);

		int len;
		while ((len = zipInputStream.read(buffer)) > 0) {
			fileOutputStream.write(buffer, 0, len);
		}

		 fileOutputStream.close();
		 zipInputStream.closeEntry();
		 zipInputStream.close();

		System.out.println("Done");

	}catch(IOException ex){
		ex.printStackTrace();
	}
}


	private static void getZIPfromXML() {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream("List.zip");
			ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
			zipOutputStream.putNextEntry(new ZipEntry("List.xml"));
			zipOutputStream.closeEntry();
			zipOutputStream.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

*/

}













