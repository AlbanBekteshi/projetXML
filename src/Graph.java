import java.io.File;
import java.util.*;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;
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
	
	private HashMap<String, Country> correspondanceCca3Country;
	private Map<Country,ArrayList<Route>> outputRoutes;
	
	public Graph() {
		correspondanceCca3Country = new HashMap<>();
		outputRoutes = new HashMap<>();
	}
	
	public void calculerItineraireMinimisantNombreDeFrontieres(String cca3Depart, String cca3Arrivee, String fichierDestination) {
		Deque<Route> trajet = bfs(cca3Depart, cca3Arrivee);
		createFile(fichierDestination, cca3Depart, cca3Arrivee, trajet);
	}

	public void calculerItineraireMinimisantPopulationTotale(String cca3Depart, String cca3Arrivee, String fichierDestination) {
		Deque<Route> trajet = dijkstra(cca3Depart,cca3Arrivee);
		createFile(fichierDestination, cca3Depart, cca3Arrivee, trajet);
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
	
	
	private Deque<Route> dijkstra(String cca3Depart,String cca3Arrivee){
		Map<Country, Long> provisoire = new HashMap<>();
		Map<Country, Long> finale = new HashMap<>();
		Map<Country,Country> parents = new HashMap<>();
		Country depart = getCountry(cca3Depart);
		Country arrivee = getCountry(cca3Arrivee);
		
		finale.put(depart, depart.getPopulation());
		provisoire.put(depart, depart.getPopulation());
		parents.put(depart, null);
		
		while(!depart.getCca3().equals(cca3Arrivee)) {
			long min = Long.MAX_VALUE;
			Country paysMin = depart;
			for(Route route : outputRoutes.get(paysMin)) {
				Country country = getCountry(route.getFinish());
				if(!finale.containsKey(country)) {
					if(provisoire.containsKey(country)) {
						if(provisoire.get(country) > country.getPopulation() + provisoire.get(paysMin)) {
							provisoire.put(country, country.getPopulation() + provisoire.get(paysMin));
							parents.put(country, depart);
						}
					}else {
						provisoire.put(country, country.getPopulation() + provisoire.get(paysMin));
						parents.put(country, depart);
					}
				}
			}
		
			provisoire.remove(paysMin);
			for (Map.Entry<Country, Long> mapentry : provisoire.entrySet()) {
				if(mapentry.getValue()<min) {
					min = mapentry.getValue();
					paysMin = mapentry.getKey();		
				}
			}
			finale.put(paysMin, min + depart.getPopulation());
			depart = paysMin;
		}

		Deque<Route> routes = new ArrayDeque<Route>();
		Country pays;
		while ((pays = parents.get(arrivee))!=null) {
			routes.addFirst(new Route(pays, arrivee.getCca3()));
			arrivee = pays;	
		}
		Route lastRoute = new Route(depart, cca3Depart);
		routes.add(lastRoute);
		return routes;
	}
	
	private Deque<Route> bfs (String cca3Depart,String cca3Arrivee){
		Deque<Country> queue = new ArrayDeque<Country>();
		Set<Country> visites = new HashSet<Country>();
		Map<Country,Route> chemins = new HashMap<Country, Route>();
		Country depart = getCountry(cca3Depart);
		Country arrivee = getCountry(cca3Arrivee);
		
		visites.add(depart);
		queue.add(depart);
		
		while (!depart.equals(arrivee) && !queue.isEmpty()) {
			depart = queue.remove();
			ArrayList<Route> routes = outputRoutes.get(depart);
			if (routes != null) {
				for (Route route : routes) {
					Country border = getCountry(route.getFinish());
					if(!visites.contains(border)) {
						queue.add(border);
						visites.add(border);
						chemins.putIfAbsent(border, route);
					}
				}
			}
		}
		depart = arrivee;
		Deque<Route> routes = new ArrayDeque<Route>();
		Route route;
		while ((route = chemins.get(depart))!=null) {
			routes.addFirst(route);
			depart = route.getStart();
		}
		Route lastRoute = new Route(arrivee, cca3Depart);
		routes.add(lastRoute);
		return routes;
	}
	
	private void createFile(String pathName, String origin, String destination, Deque<Route> routes) {
		
		Country country;
		long sommePop = routes.stream().map((item)-> item.getStart().getPopulation()).reduce((a, b)->Long.sum(a, b)).orElse((long)0);
		
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			
			//creation itineraire
			Element rootElement = doc.createElement("itineraire");
			doc.appendChild(rootElement);
			
			Attr arrivee = doc.createAttribute("arrivee");
			arrivee.setValue(this.getCountry(destination).getName());
			rootElement.setAttributeNode(arrivee);
			Attr depart = doc.createAttribute("depart");
			depart.setValue(this.getCountry(origin).getName());
			rootElement.setAttributeNode(depart);
			Attr nbPays = doc.createAttribute("nbPays");
			nbPays.setValue(String.valueOf(routes.size()));
			rootElement.setAttributeNode(nbPays);
			Attr sommePopulation = doc.createAttribute("sommePopulation");
			sommePopulation.setValue(String.valueOf(sommePop));
			rootElement.setAttributeNode(sommePopulation);
			
			//creation pays
			while(routes.size() > 0) {
				country = routes.remove().getStart();
				Element pays = doc.createElement("pays");
				rootElement.appendChild(pays);
				
				Attr cca3 = doc.createAttribute("cca3");
				cca3.setValue(country.getCca3());
				pays.setAttributeNode(cca3);
				Attr nom = doc.createAttribute("nom");
				nom.setValue(country.getName());
				pays.setAttributeNode(nom);
				Attr population = doc.createAttribute("population");
				population.setValue(String.valueOf(country.getPopulation()));
				pays.setAttributeNode(population);
			}
			
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
