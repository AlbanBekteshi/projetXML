import java.io.File;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Graph {
	
	private Map<String, Country> correspondanceCca3Country;
	private Map<Country,Set<Route>> outputRoutes;
	
	public Graph() {
		correspondanceCca3Country = new HashMap<String, Country>();
	}
	
	public void constructFromXML (String xmlFile) throws Exception {
		File xml = new File(xmlFile);
		DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuild = docBuildFact.newDocumentBuilder();
		Document doc = docBuild.parse(xml);
		NodeList countries = doc.getElementsByTagName("country");
		for(int i=0; i<countries.getLength(); i++) {
			Node country = countries.item(i);
			Element elCountry = (Element) country;
			String name = elCountry.getAttribute("name");
			String capital = elCountry.getAttribute("capital");
			String cca3 = elCountry.getAttribute("cca3");
			String currency = elCountry.getAttribute("currency");
			String languages = elCountry.getAttribute("languages");
			String latlng = elCountry.getAttribute("latlng");
			int population = Integer.parseInt(elCountry.getAttribute("population"));
			String region = elCountry.getAttribute("region");
			String subregion = elCountry.getAttribute("subregion");
			Country c = new Country(name, capital, cca3, currency, languages, latlng, population, region, subregion);
			correspondanceCca3Country.put(cca3, c);
			ajouterSommet(c);
		}
		for(int i=0; i<countries.getLength(); i++) {
			Node country = countries.item(i);
			Element elCountry = (Element) country;
			String cca3 = elCountry.getAttribute("cca3");
			NodeList routes = elCountry.getElementsByTagName("border");
			for(int j=0; j<routes.getLength(); j++) {
				Node route = routes.item(j);
				Element elRoute = (Element) route;
				String finish = elRoute.getTextContent();
				Route r = new Route(correspondanceCca3Country.get(cca3), correspondanceCca3Country.get(finish));
				ajouterArc(r);
			}
		}
	}
	
	public void calculerItineraireMinimisantNombreDeFrontieres(String cca3Depart, String cca3Arrivee, String fichierDestination) {
		
	}
	
	public void calculerItineraireMinimisantPopulationTotale(String cca3Depart, String cca3Arrivee, String fichierDestination) {
		
	}
	
	
	
	public Country getCountry(String cca3) {
		return correspondanceCca3Country.get(cca3);
	}
	
	
	
	public void ajouterSommet(Country c) {
		outputRoutes.put(c, new HashSet<Route>());

	}
	
	public void ajouterArc(Route r) {
		outputRoutes.get(r.getStart()).add(r);
	}
	
	public Set<Route> arcsSortants(Country c) {
		return outputRoutes.get(c);
	}
	
	public boolean sontAdjacents(Country c1, Country c2) {
		Set<Route> routes = outputRoutes.get(c1);
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
}
