package com.company.user;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class UserIO {
	
	/*--------------------------------------read---------------------------------------------*/
	public static void read(String location, Users users) {
		
		try {	
			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = documentBuilder.parse(location);	
			getUsersFromDoc(document.getDocumentElement(), users);	
			
		} catch (ParserConfigurationException | SAXException | IOException ex) {
			ex.printStackTrace(System.out);
		}
	}
	
	private static void getUsersFromDoc(Node document, Users users) {

		users.lastId = Integer.parseInt(document.getAttributes().getNamedItem("lastId").getTextContent());
		NodeList nodeUsers = document.getChildNodes();

		for (int i = 0; i < nodeUsers.getLength(); i++) {
			Node nodeUser = nodeUsers.item(i);
			if (nodeUser.getNodeType() != Node.TEXT_NODE) {
				User user = getUserFromNode(nodeUser);
				users.add(user);
			} 
		}
	}
		
	private static User getUserFromNode(Node node) {
		
		NodeList nodes = node.getChildNodes();
		User user = new User();
		
		for (int j = 0; j < nodes.getLength(); j++) {
			
			Node prop = nodes.item(j);    
			
			if (prop.getNodeType() != Node.TEXT_NODE) {
				
				String propName = prop.getChildNodes().item(0).getTextContent();

				if(prop.getNodeName().equals("id")){
					user.id = Integer.parseInt(propName);
				}

				if (prop.getNodeName().equals("Login")) {
					user.login = propName;
				}
				
				if (prop.getNodeName().equals("AccessTag")) {
					user.addAccessTag(propName);
				}

				if (prop.getNodeName().equals("HashPassword")) {
					user.hashPassword = propName;
				}
			}
		}
		return user;
	}
	

	/*--------------------------------------write---------------------------------------------*/	
	public static boolean write(String location, Users users) {
		
		try {	
			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = documentBuilder.newDocument();	
			getDocFromUsers(document, users);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(location));
			transformer.transform(source, result);
			return true;
		} catch (ParserConfigurationException | TransformerException ex) {
			ex.printStackTrace(System.out);
			return false;
		}
	}
			
	private static void getDocFromUsers(Document document, Users users) {
		
		Element rootElement = document.createElement("UserList");
		rootElement.setAttribute("lastId", users.lastId.toString());
		document.appendChild(rootElement);
		
		for (User user : users) {
			Element userElement = document.createElement("Person");
			rootElement.appendChild(userElement);

			Element idElement = document.createElement("id");
			userElement.appendChild(idElement);
			idElement.appendChild(document.createTextNode(user.id.toString()));

			Element loginElement = document.createElement("Login");
			userElement.appendChild(loginElement);	
			loginElement.appendChild(document.createTextNode(user.login));
				
			Element hashPasswordElement = document.createElement("HashPassword");
			userElement.appendChild(hashPasswordElement);	
			hashPasswordElement.appendChild(document.createTextNode(user.hashPassword));
			
			for (int i = 0; i < user.accessTags.size(); i++) {	
				Element accessTagElement = document.createElement("AccessTag");
				userElement.appendChild(accessTagElement);	
				accessTagElement.appendChild(document.createTextNode(user.accessTags.get(i)));
			}
		}
	}
}
