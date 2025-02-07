

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class C3PO {
	
	private int autonomy;
    private List<Route> routes;
    private Map<String, Map<String, Integer>> routeMap;
    
    
    private static class Route {
        public String origin;
        public String destination;
        public int travelTime;
    }
    
    private static class FalconData {
        public int autonomy;
        public List<Route> routes;
    }    
    
    private static class EmpireData {
        public int countdown;
        public List<BountyHunter> bounty_hunters;
    }
    
    private static class BountyHunter {
        public String planet;
        public int day;
    }
    
    private static class PathState {
        String planet;
        int currentDay;
        int autonomy;
        int hunterEncounters;
        
        PathState(String planet, int currentDay, int autonomy, int hunterEncounters) {
            this.planet = planet;
            this.currentDay = currentDay;
            this.autonomy = autonomy;
            this.hunterEncounters = hunterEncounters;
        }
    }
    
    
	public C3PO(File millenniumFalconJsonFile) {
        
		try {
			ObjectMapper mapper = new ObjectMapper();
			FalconData  falcon = mapper.readValue(millenniumFalconJsonFile, FalconData.class);
			
			this.autonomy = (Integer) falcon.autonomy;
			this.routes = (List<Route>) falcon.routes;
			
			this.routeMap = new HashMap<>();
			
			for (Route route : routes) {
			    String origin = route.origin;
			    String destination = route.destination;
			    int time = route.travelTime;
			    
			    if (!routeMap.containsKey(origin)) {
			        routeMap.put(origin, new HashMap<>());
			    }
			    if (!routeMap.containsKey(destination)) {
			        routeMap.put(destination, new HashMap<>());
			    }
			    
			    routeMap.get(origin).put(destination, time);
			    routeMap.get(destination).put(origin, time); // Routes can be travelled in any direction
			    
			}
			
		} catch (StreamReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
        
    }
	
	
    public double giveMeTheOdds(File empireJsonFile) throws StreamReadException, DatabindException, IOException {
 
        
    	ObjectMapper mapper = new ObjectMapper();
        EmpireData empire = mapper.readValue(empireJsonFile, EmpireData.class);
		

		Map<String, Set<Integer>> hunters = new HashMap<>();
		for (BountyHunter hunter : empire.bounty_hunters) {
		    String planet = hunter.planet;
		    int day = hunter.day;
		    
		    if (!hunters.containsKey(planet)) {
		        hunters.put(planet, new HashSet<>());
		    }
		    hunters.get(planet).add(day);
		}
		
		
		Set<PathState> visited = new HashSet<>();
        List<Integer> encounterCounts = new ArrayList<>();
        findPaths("Tatooine", 0, autonomy, 0, (int) empire.countdown, hunters, visited, encounterCounts);
        
        if (encounterCounts.isEmpty()) {
            return 0.0;
        }
        
        double bestProba = 0;
        double currentProba = 0;
        for (int encounters : encounterCounts) {
            currentProba = calculateProba(encounters);
            bestProba = (currentProba > bestProba) ? currentProba : bestProba;
        }
        
        return bestProba;
    	        
    }
    
  

    private void findPaths(String currentPlanet, int currentDay, int autonomy, int hunterEncounters, int countdown, Map<String, Set<Integer>> hunters, Set<PathState> visited, List<Integer> encounterCounts) {
    	
    	if (currentDay > countdown) return;
        if (currentPlanet.equals("Endor")) {
            encounterCounts.add(hunterEncounters);         
            return;
        }
        
        PathState state = new PathState(currentPlanet, currentDay, autonomy, hunterEncounters);
        if (visited.contains(state)) return;
        visited.add(state);
        
        Map<String, Integer> destinations = routeMap.getOrDefault(currentPlanet, Collections.emptyMap());
        for (Entry<String, Integer> dest : destinations.entrySet()) {
            String nextPlanet = dest.getKey();
            int travelTime = dest.getValue();

            if (travelTime <= autonomy) {
                int newEncounters = hunterEncounters;
                if (hunters.containsKey(currentPlanet) && hunters.get(currentPlanet).contains(currentDay)) newEncounters++;
                
                
                findPaths(nextPlanet, currentDay + travelTime, autonomy - travelTime, newEncounters, countdown, hunters, visited, encounterCounts);
            }
            
            for (int waitDays = 1; currentDay + waitDays + travelTime <= countdown; waitDays++) {
                int newEncounters = hunterEncounters;
                
                for (int i = 0; i <= waitDays; i++) {
                    if (hunters.containsKey(currentPlanet) && hunters.get(currentPlanet).contains(currentDay + i)) newEncounters++;

                }
                
                findPaths(nextPlanet, currentDay + waitDays + travelTime, this.autonomy - travelTime, newEncounters, countdown, hunters, visited, encounterCounts);
            }
        }
		
	}
    
	private double calculateProba(int q) {
		
		if (q == 0) return 1;
		
		double proba = 0.1;

        for (int k = 2; k <= q; k++) {
            proba += Math.pow(9, k-1) / Math.pow(10, k);
        }
        return 1 - proba;
		
	}

	

}
