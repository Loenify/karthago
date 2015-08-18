package de.bg.fhdw.bfwi413a.karthago.xml;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.content.res.AssetManager;
import de.bg.fhdw.bfwi413a.karthago.SessionManagement;


public class XMLDomParserAndHandler{ //SCHAUEN OB ES AUCH OHNE EXTENDS GEHT!!!! Wichtig, Klasse muss von Activity erben für AssetManager
	
	DocumentBuilderFactory mFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder mBuilder;
	Document mDoc;
	
	//@auhtor Patrick
	private AssetManager am;
	SessionManagement session;
	ArrayList<String> list_ids;
	ArrayList<String> list_cardfile_id;
	ArrayList<String> list_answer_type;
	
	
	// END @author Patrick
	
	public XMLDomParserAndHandler (Context context){ // immer schön auf den Context aufpassen!!!! ;-) am besten übergeben wo der Construktor gestartet wird
		//@author Patrick
		
		session = new SessionManagement(context);
		am = context.getAssets();
		ifXMLFileExist();
		list_ids = new ArrayList<String>();
		list_cardfile_id = new ArrayList<String>();
		list_answer_type = new ArrayList<String>();
		// END @ author Patrick
		
	}

//	public Document getmDoc() {
//		return mDoc;
//	}
	
	//@author Patrick
	
	
	public void ifXMLFileExist(){
		try{
			mBuilder = mFactory.newDocumentBuilder();
			mDoc = mBuilder.parse(am.open("FrageXML.xml")); //Wir benutzen nur 1! XML-File, das hier als Dateinamen angegeben wird.
		}catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public ArrayList<String> getCardFileNames(){
		ArrayList<String> cardfile_names = new ArrayList<String>();
		
		mDoc.getDocumentElement().normalize();
		
		NodeList nList = mDoc.getElementsByTagName("Cards");
		
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
					
					
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;
				cardfile_names.add(eElement.getElementsByTagName("CardFile").item(0).getTextContent());
			}
		}
		
		return cardfile_names;
	}
	
	public Results getAllIDs(){
		
		mDoc.getDocumentElement().normalize();
		
		NodeList nList = mDoc.getElementsByTagName("Question");
		
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
					
					
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;
				list_ids.add(eElement.getElementsByTagName("QuestionId").item(0).getTextContent());
				list_answer_type.add(eElement.getElementsByTagName("AnswerType").item(0).getTextContent());
				
				Element eElementParent = (Element) nNode.getParentNode();
				list_cardfile_id.add(eElementParent.getElementsByTagName("CardFile").item(0).getTextContent());
				
			}
		}
		
		return new Results(list_ids, list_cardfile_id, list_answer_type);
	}
	
	public ArrayList<String> getQuestionTypes(ArrayList<String> requieredQuestionsID){
		ArrayList<String> questionTypes = new ArrayList<String>();
		
		
		
		return questionTypes;
	}
	
	public Results getRequiredQuestionAnswersAndCorrectAnswers(String questionID) {
		ArrayList<String> QuestionAndAnswers = new ArrayList<String>();
		ArrayList<String> CorrectAnswers = new ArrayList<String>();
		
		mDoc.getDocumentElement().normalize();
		
		NodeList nList = mDoc.getElementsByTagName("Question");
		
		 for (int j = 0; j < nList.getLength(); j++) {
	         Node nNode = nList.item(j);
	         if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	        	 Element el = (Element) nNode;
	         
		         if (el.getElementsByTagName("QuestionId").item(0).getTextContent().equals(questionID)) {
		        	 
		              QuestionAndAnswers.add(el.getElementsByTagName("QuestionText").item(0).getTextContent());
		              QuestionAndAnswers.add(el.getElementsByTagName("Answer1").item(0).getTextContent());
		              QuestionAndAnswers.add(el.getElementsByTagName("Answer2").item(0).getTextContent());
		              QuestionAndAnswers.add(el.getElementsByTagName("Answer3").item(0).getTextContent());
		              QuestionAndAnswers.add(el.getElementsByTagName("Answer4").item(0).getTextContent());
		              
		         }
	         
	         }
	     }
		 
		 
		 NodeList nListAnswers = mDoc.getElementsByTagName("Answers");
		 for (int k = 0; k < nListAnswers.getLength(); k++) {
	         Node nNode = nListAnswers.item(k);
	         if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	        	 Element el_answers = (Element) nNode;
	         
	        	 Node node_answers_parent = el_answers.getParentNode();
	        	 
	        	 Element el_answers_parent = (Element) node_answers_parent;
	        	 
		         if (el_answers_parent.getElementsByTagName("QuestionId").item(0).getTextContent().equals(questionID)) {
		        	 
		              NodeList ChildAnswers = el_answers.getChildNodes();
		              for(int l = 0; l < ChildAnswers.getLength(); l++){
		            	  Node nNodeAnswers = ChildAnswers.item(l);
		            	  if(nNodeAnswers.getNodeType() == Node.ELEMENT_NODE){
		            		  Element el_answers_deeper = (Element) nNodeAnswers;
		            		  if(el_answers_deeper.hasAttribute("correct")){
		            			  CorrectAnswers.add(el_answers_deeper.getTextContent().toString());
		            		  }
		            	  }
		              
		              
		         }
	         
	         }
	     }
		 }
		 
		return new Results(QuestionAndAnswers, CorrectAnswers);
	}

	//Return von 1 String und 1 ArrayList -> Konstruktor in Result mit den Argumenten
	public Results questionAndAnswersForFTAndGQuestions(String questionID){
		String Question = new String();
		ArrayList<String> CorrectAnswers = new ArrayList<String>();
		
		mDoc.getDocumentElement().normalize();
		
		NodeList nList = mDoc.getElementsByTagName("Question");
		
		for (int j = 0; j < nList.getLength(); j++) {
	         Node nNode = nList.item(j);
	         if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	        	 Element el = (Element) nNode;
	         
		         if (el.getElementsByTagName("QuestionId").item(0).getTextContent().equals(questionID)) {
		        	 
		              Question = el.getElementsByTagName("QuestionText").item(0).getTextContent();
		         }
	         }
		}
		
		NodeList nListAnswers = mDoc.getElementsByTagName("Answers");
		 for (int k = 0; k < nListAnswers.getLength(); k++) {
	         Node nNode = nListAnswers.item(k);
	         if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	        	 Element el_answers = (Element) nNode;
	         
	        	 Node node_answers_parent = el_answers.getParentNode();
	        	 
	        	 Element el_answers_parent = (Element) node_answers_parent;
	        	 
		         if (el_answers_parent.getElementsByTagName("QuestionId").item(0).getTextContent().equals(questionID)) {
		        	 
		              NodeList ChildAnswers = el_answers.getChildNodes();
		              for(int l = 0; l < ChildAnswers.getLength(); l++){
		            	  Node nNodeAnswers = ChildAnswers.item(l);
		            	  if(nNodeAnswers.getNodeType() == Node.ELEMENT_NODE){
		            		  Element el_answers_deeper = (Element) nNodeAnswers;
		            		  CorrectAnswers.add(el_answers_deeper.getTextContent().toString());
		            	  }
		              
		              
		         }
	         
		         }
	         }
		 }
		
		
		
		return new Results(Question, CorrectAnswers);
	}
	
	// ---- END @author Patrick ----
}
