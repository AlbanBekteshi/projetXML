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
		Deque<Route> chemins = bfs(cca3Depart, cca3Arrivee);
		createFile(fichierDestination, cca3Depart, cca3Arrivee, chemins);
	}



	
	public void calculerItineraireMinimisantPopulationTotale(String cca3Depart, String cca3Arrivee, String fichierDestination) {
		//djikastra
		
		Deque<Route> route = djikistra(cca3Depart,cca3Arrivee);
		
		
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
	
	private Deque<Route> djikistra(String cca3Depart,String cca3Arrive){
		Map<Country, Long> provisoire = new HashMap<>();
		Map<Country, Long> finale = new HashMap<>();
		Map<Country,Country> parents= new HashMap<>();
		Deque<Route> queue = new ArrayDeque<Route>();
		Country depart = correspondanceCca3Country.get(cca3Depart);
		Country arrive = correspondanceCca3Country.get(cca3Arrive);
		
		
		
		provisoire.put(depart, 0L);
		
		while(!provisoire.isEmpty() && !finale.containsKey(arrive)) {
			
			long min = Long.MAX_VALUE;
			Country paysMin=depart;
					
			
			for (Map.Entry<Country, Long> mapentry : provisoire.entrySet()) {
				if(mapentry.getValue()<min) {
					min = mapentry.getValue();
					paysMin = mapentry.getKey();		
				}
			}
			//Route route = new Route(paysMin, depart.getCca3());
			//parents.put(paysMin, route);
			//pays voisin retien pays min 
			
			finale.put(paysMin, min);
			provisoire.remove(paysMin);
			
			ArrayList<Route> frontieres = outputRoutes.get(paysMin);
			for (Route route : frontieres){
				Country paysVoisin = correspondanceCca3Country.get(route.getFinish());
				//faire les verifs
				if(!provisoire.containsKey(paysVoisin)) {
					parents.put(paysVoisin, paysMin);
					provisoire.put(paysVoisin, paysVoisin.getPopulation());	
				}
				else {
					long popPaysProv = provisoire.get(paysVoisin);
					if(paysVoisin.getPopulation()<popPaysProv) {
						//provisoire.get(paysVoisin).
					}
					
				}
			}
			

			
			depart= paysMin;
			
		}
		return queue;
	}
	
	private Deque<Route> bfs (String cca3Depart,String cca3Arrive){
		Country depart = correspondanceCca3Country.get(cca3Depart);
		Country arrive = correspondanceCca3Country.get(cca3Arrive);
		
		Deque<Country> queue = new ArrayDeque<Country>();
		Set<Country> visites = new HashSet<Country>();
		Map<Country,Route> chemins = new HashMap<Country, Route>();
		
		visites.add(depart);
		queue.add(depart);
		System.out.println(depart);
		
		while (!depart.equals(arrive) && !queue.isEmpty()) {
			depart = queue.remove();
			ArrayList<Route> routes = outputRoutes.get(depart);
			if (routes != null) {
				for (Route route :routes) {
					
					Country frontalier = correspondanceCca3Country.get(route.getFinish());
					if(!visites.contains(frontalier)) {
						queue.add(frontalier);
						visites.add(frontalier);
						chemins.putIfAbsent(frontalier, route);
					}
				}
			}
		}
		depart = arrive;
		Deque<Route> routes = new ArrayDeque<Route>();
		Route route;
		while ((route = chemins.get(depart))!=null) {
			routes.addFirst(route);
			depart = route.getStart();
		}
		Route lastRoute = new Route(arrive, cca3Depart);
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
			arrivee.setValue(this.correspondanceCca3Country.get(destination).getName());
			rootElement.setAttributeNode(arrivee);
			Attr depart = doc.createAttribute("depart");
			depart.setValue(this.correspondanceCca3Country.get(origin).getName());
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
