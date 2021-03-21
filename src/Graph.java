import java.io.File;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Graph {
	
	private Map<String, Country> correspondanceCca3Country;
	private Map<Country,ArrayList<Route>> outputRoutes;
	private ArrayDeque<Country> file = new ArrayDeque<>();
	ArrayList<String> visite = new ArrayList<>();
	
	public Graph() {
		correspondanceCca3Country = new HashMap<String, Country>();
		outputRoutes = new HashMap<Country,ArrayList<Route>>();
	}
	
	public void calculerItineraireMinimisantNombreDeFrontieres(String cca3Depart, String cca3Arrivee, String fichierDestination) {
		
		
		Map<String,String> trajet = new HashMap<String, String>();
		visite.add(cca3Depart);
		Country depart = correspondanceCca3Country.get(cca3Depart);
		ArrayList<Route>borders = outputRoutes.get(depart);
		
		for(Route b : borders) {
			file.add(b.getStart());
			trajet.put(cca3Depart, b.getStart().getCca3());
		}
		
		
		createFile(fichierDestination);
	}
	
	private void calculerItineraireMinimisantNombreDeFrontieres(String cca3Depart) {
		
	}
	
	
	public void calculerItineraireMinimisantPopulationTotale(String cca3Depart, String cca3Arrivee, String fichierDestination) {
		
	}
	
	
	
	public Country getCountry(String cca3) {
		return correspondanceCca3Country.get(cca3);
	}
	
	
	
	public void ajouterSommet(Country c, String cca3) {
		correspondanceCca3Country.put(cca3, c);
		outputRoutes.put(c, new ArrayList<Route>());
	}
	
	public void ajouterArc(Route r) {
		outputRoutes.get(r.getStart()).add(r);
	}
	
	public ArrayList<Route> arcsSortants(Country c) {
		return outputRoutes.get(c);
	}
	
	public boolean sontAdjacents(Country c1, Country c2) {
		ArrayList<Route> routes = outputRoutes.get(c1);
		for(Route route : routes) {
			if(route.getFinish().equals(c2)) {
				return true;
			}
		}
		routes = outputRoutes.get(c2);
		for(Route route : routes) {
			if(route.getFinish().equals(c1)) {
				return true;
			}
		}
		return false;
	}
	
	private void createFile(String pathName) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			
			Element rootElement = doc.createElement("itinéraire");
			doc.appendChild(rootElement);
			Attr arrivee = doc.createAttribute("arrivee");
			rootElement.setAttributeNode(arrivee);
			Attr depart = doc.createAttribute("depart");
			rootElement.setAttributeNode(depart);
			Attr nbPays = doc.createAttribute("nbPays");
			rootElement.setAttributeNode(nbPays);
			Attr sommePopulation = doc.createAttribute("sommePopulation");
			rootElement.setAttributeNode(sommePopulation);
			
			Element pays = doc.createElement("pays");
			rootElement.appendChild(pays);
			Attr cca3 = doc.createAttribute("cca3");
			pays.setAttributeNode(cca3);
			Attr nom = doc.createAttribute("nom");
			pays.setAttributeNode(nom);
			Attr population = doc.createAttribute("population");
			pays.setAttributeNode(population);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(pathName));
			transformer.transform(source, result);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
