package hw8;

import hw5.*;
import hw6.MarvelParser.MalformedDataException;
import hw7.*;

import java.util.*;

/**
 * RouteFindingModel represents a model of the
 * campus route finding tool. 
 * 
 * @specfield buildingNames: Map from appreviated names to 
 *                           full names of the buildings
 *                           
 * @specfield buildingLocations: Map from appreviated building names
 *                               to their locations
 * @specfield campusPaths: a network of all possible paths with edge labels
 *                         as distances 
 *                         
 * @author Rohan Guliani
 *
 */
public class RouteFindingModel {
    
    /**
     * Rep Invariant: 
     *      -Neither of buildingNames, buildingLocs, or campusGraph is null, and
     *      -None of their elements (keys or values, nodes and edges respectfully)
     *       are null
     * 
     * 
     * Abstraction Function: 
     *      AF(this) = 
     *          this.abbreviatedBuildings => key set of buildingNames and buildingLocs
     *          this.longNameBuildings => values of buildingNames
     *          this.getLocations(abbreviatedBuildingName) => location of that building
     * 
     */
    
    private static final boolean TEST_FLAG = false;
    
    private Map<String,String> buildingNames;
    private Map<String, Point> buildingLocs;
    private Graph<Point, Double> campusGraph;
    
    /**
     * Constructor to create a new RouteFindingModel. 
     * @param buildings: file name for the valid buildings file
     * @param paths: file name for the valid campus paths file
     * @requires buildings and paths to be non-null
     * @throws MalformedDataException if either one of the files
     *         are corrupt
     */
    public RouteFindingModel(String buildings, String paths)
                                   throws MalformedDataException{
        
        Graph.checkNull(buildings);
        Graph.checkNull(paths);
        
        buildingNames = new HashMap<>();
        buildingLocs = new HashMap<>();
        campusGraph = new Graph<>();
        
        CampusParser.parseBuildings(buildings,
                                    buildingNames,
                                    buildingLocs);
        
        CampusParser.createCampusGraph(campusGraph, paths);
        checkRep();
    }
    
    /**
     * Get the set of abbreviated names
     * @return The set of abbreviatd names
     */
    public Set<String> getAbbreviatedBuildings(){
        return new HashSet<String>(buildingNames.keySet());
    }
    
    /**
     * Get the set of long building names
     * @return
     */
    public Set<String> getFullNameBuildings(){
        Collection<String> longNames = buildingNames.values();
        Set<String> longNamesSet = new HashSet<String>();
        
        for(String name: longNames){
            longNamesSet.add(name);
        }
        return longNamesSet;
    }
    
    /**
     * Return all the buildings; a map from abbreviated names to full names
     * @return: A map of all the buildings
     */
    public Map<String,String> getAllBuildings(){
        return new HashMap<String,String>(buildingNames);
    }
    
    /**
     * Get the abbreviated name that corresponds to the long building name
     * @param longName: the long name corresponding to the desired abbreviated name
     * @requires longName to be a non-null valid building name
     * @return The corresponding abbreviated name
     */
    public String getAbbreviatedName(String longName){
        Graph.checkNull(longName);
        for(String abbrName: buildingNames.keySet()){
            if(buildingNames.get(abbrName).equals(longName)){
                return abbrName;
            }
        }
        throw new IllegalArgumentException("Building does not exist");
    }
    
    /**
     * Get the long name for a corresponding abbreviated building name
     * @param abbrName: the abbreviated name for the desired long name
     * @requires abbrName to be non-null and a valid building name
     * @return The long name of the corresponding abbreviated name
     */
    public String getLongName(String abbrName){
        Graph.checkNull(abbrName);
        checkBuilding(abbrName);
        
        return buildingNames.get(abbrName);
    }
    
    /**
     * Return the location of a desired building
     * @param abbrName: the abbreviated name of the desired building
     * @return: The location (as a Point) of the desired building
     */
    public Point getLocation(String abbrName){
        Graph.checkNull(abbrName);
        checkBuilding(abbrName);
        
        return buildingLocs.get(abbrName);   
    }
    
    /**
     * Find the shortest path between two buildings, returned as a list of edges
     * @param startBuilding: The abbreviated name of the start building
     * @param endBuilding: The abbreviated name of the end building
     * @requires startBuilding and endBuilding to be non-null and valid buildings
     * @return: A list of edges that make up the shortest paath
     */
    public List<Edge<Point, Double>> findShortestPath(String startBuilding, String endBuilding){
        
        Graph.checkNull(startBuilding);
        Graph.checkNull(endBuilding);
        checkBuilding(startBuilding);
        checkBuilding(endBuilding);
        Point start = buildingLocs.get(startBuilding);
        Point end = buildingLocs.get(endBuilding);
        List<Edge<Point, Double>> path = MarvelPaths2.findLeastCostPath
                                        (campusGraph, 
                                         new Node<Point>(start), 
                                         new Node<Point> (end));
        
        return path;
    }
    
    /**
     * Check if the building exists in the map
     * @param abbrName: The abbreviated name of the desired building
     * @throws IllegalArgumentException if abbrName is null
     */
    private void checkBuilding(String abbrName){
        Graph.checkNull(abbrName);
        if(!buildingNames.containsKey(abbrName)){
            throw new IllegalArgumentException("Building does not exist");
        }
    }
    
    /**
     * Check if the rep invariant is satisfied
     */
    private void checkRep(){
        
        if (TEST_FLAG) {
            if (buildingNames == null)
                throw new RuntimeException("building names can't be null");

            if (buildingLocs == null)
                throw new RuntimeException("building locations can't be null");

            if (campusGraph == null)
                throw new RuntimeException(
                        "graph of campus paths can't be null");

            Set<String> names = buildingNames.keySet();
            for (String name : names) {
                if (name == null)
                    throw new RuntimeException(
                            "abbreviated name of a building can't be null.");

                if (buildingNames.get(name) == null)
                    throw new RuntimeException(
                            "full name of the building can't be null.");
            }

            Set<String> locations = buildingLocs.keySet();
            for (String name : locations) {
                if (name == null)
                    throw new RuntimeException(
                            "abbreviated name of the building can't be null.");

                if (buildingLocs.get(name) == null)
                    throw new RuntimeException(
                            "location of the building can't be null.");
            }

        }
    }
}
