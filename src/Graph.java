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
	
	private HashMap<String, Country> correspondanceCca3Country;
	private Map<Country,ArrayList<Route>> outputRoutes;
//	private ArrayDeque<Country> file = new ArrayDeque<>();
//	private ArrayList<String> visites = new ArrayList<>();	
//	private HashMap<String, String> trajet = new HashMap<String,String>();
	
	public Graph() {
		correspondanceCca3Country = new HashMap<>();
		outputRoutes = new HashMap<>();
	}
	
	
	private Deque<Route> bfs (String cca3Depart,String cca3Arrive){
		Country depart = correspondanceCca3Country.get(cca3Depart);
		Country arrive = correspondanceCca3Country.get(cca3Arrive);
		
		Deque<Country> queue = new ArrayDeque<Country>();
		Set<Country> visites = new HashSet<Country>();
		Map<Country,Route> chemins = new HashMap<Country, Route>();
		ArrayList<Route> frontieres = outputRoutes.get(depart);
		
		for (int i =0 ; i<frontieres.size();i++) {
			String payscca3 = frontieres.get(i).getFinish();
			queue.add(correspondanceCca3Country.get(payscca3));
		}
		
		
		visites.add(depart);
		Country position = depart;
		System.out.println(position);
		
		while (!position.equals(arrive) && !queue.isEmpty()) {
			position = queue.remove();
			ArrayList<Route> routes = outputRoutes.get(position);
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
		return routes;
	}
	
	public void calculerItineraireMinimisantNombreDeFrontieres(String cca3Depart, String cca3Arrivee, String fichierDestination) {
		Deque<Route> chemins = bfs(cca3Depart, cca3Arrivee);
	}
	// BFS doit être fait avec une HashMap
//	public void calculerItineraireMinimisantNombreDeFrontieres(String cca3Depart, String cca3Arrivee, String fichierDestination) {
//		
//		//clé = sommet, valeur = sommet depuis lequel on est arrivé OU si info sur la map valeur = l'arc de par ou on vient
//		//Map<String,String> trajet = new HashMap<>();
//		
//		
//		visites.add(cca3Depart);
//		Country depart = correspondanceCca3Country.get(cca3Depart);
//		ArrayList<Route> frontieres = outputRoutes.get(depart);
//		
//		for (int i =0 ; i<frontieres.size();i++) {
//			String payscca3 = frontieres.get(i).getFinish();
//			file.add(correspondanceCca3Country.get(payscca3));
//		}
//		
//		for (int i = 0; i<file.size();i++) {
//			
//		}
//		
//		/*Country depart = correspondanceCca3Country.get(cca3Depart);
//		ArrayList<Route>borders = outputRoutes.get(depart);
//		
//		for(Route b : borders) {
//			file.add(b.getStart());
//			trajet.put(cca3Depart, b.getStart().getCca3());
//		}*/
//		
//		/******TEST**********/
//		
//		//définir le payse de départ
//		boolean arrived = false;
//		//on enlève le pays de départ de la hashmap et on le sauvegarde dans une linkedlist
//		while(!arrived) {
//			//pour chaque border du pays de départ (utiliser foreach de border ou autre)
//				//si on n'est pas encore passé par ce pays border
//					//on avance dans ce pays
//					//on ajoute ce pays à la linkedlist
//					//on ajoute ce pays à la hashmap
//					//si le border dans lequel on est est égal au pays ou l'on veut aller
//						arrived = true;
//						//on renvoie la linkedlist
//		}
//		//createFile(fichierDestination, cca3Depart, cca3Arrivee,/*Une linkedList à implémenter*/);
//	}
//	
	
	public void calculerItineraireMinimisantPopulationTotale(String cca3Depart, String cca3Arrivee, String fichierDestination) {
		//djikastra
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
	
	private void createFile(String pathName, String origin, String destination, LinkedList<Country> countries) {
		
		Country country;
		long sommePop = countries.stream().map((item) -> item.getPopulation()).reduce((a,b)->Long.sum(a, b)).orElse((long) 0);
		
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			
			//création itinéraire
			Element rootElement = doc.createElement("itinéraire");
			doc.appendChild(rootElement);
			
			Attr arrivee = doc.createAttribute("arrivee");
			arrivee.setValue(this.correspondanceCca3Country.get(destination).getName());
			rootElement.setAttributeNode(arrivee);
			Attr depart = doc.createAttribute("depart");
			depart.setValue(this.correspondanceCca3Country.get(origin).getName());
			rootElement.setAttributeNode(depart);
			Attr nbPays = doc.createAttribute("nbPays");
			nbPays.setValue(String.valueOf(countries.size()));
			rootElement.setAttributeNode(nbPays);
			Attr sommePopulation = doc.createAttribute("sommePopulation");
			sommePopulation.setValue(String.valueOf(sommePop));
			rootElement.setAttributeNode(sommePopulation);
			
			//création pays
			while(countries.size() > 0) {
				country = countries.poll();
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
