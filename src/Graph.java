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
	
	public Graph() {
		correspondanceCca3Country = new HashMap<String, Country>();
	}
	
	public void calculerItineraireMinimisantNombreDeFrontieres(String cca3Depart, String cca3Arrivee, String fichierDestination) {
		
	}
	
	public void calculerItineraireMinimisantPopulationTotale(String cca3Depart, String cca3Arrivee, String fichierDestination) {
		
	}
	
	public Country getCountry(String cca3) {
		return correspondanceCca3Country.get(cca3);
	}
}
