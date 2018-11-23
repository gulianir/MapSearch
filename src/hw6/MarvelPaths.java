package hw6;

import java.util.*;

import hw5.*;
import hw6.MarvelParser.MalformedDataException;

public class MarvelPaths {
    
    //RI and AF would usually go here, but since MarvelPaths contains
    //just static methods, it doesn't represent an ADT. 
    
    /** 
     * A static method that takes a fileName containing a valid set of
     * "characters" and "books" (more generally, desired nodes and
     * connecting edges) and creates a new graph storing the data. 
     * @param fileName: The file to create the graph from
     * @return The created graph
     * @throws MalformedDataException if the file is not valid 
     */
    public static Graph<String,String> createGraph(String fileName)
            throws MalformedDataException {
        Set<String> characters = new HashSet<String>();
        Map<String, List<String>> books = new HashMap<String, List<String>>();
        MarvelParser.parseData(fileName, characters, books);
        
        Graph<String,String> g = new Graph<>();

        for (String character : characters) {
            g.addNode(new Node<String>(character));
        }
        for (String book : books.keySet()) {
            List<String> charactersOfBook = books.get(book);
            for (int i = 0; i < charactersOfBook.size(); i++) {
                for (int j = 0; j < charactersOfBook.size(); j++) {
                    if (!(j == i)) { //connect everything except the edge with itself
                        g.addEdge(new Node<String>(charactersOfBook.get(i)),
                                new Node<String>(charactersOfBook.get(j)), book);
                    }
                }
            }
        }
        return g;
    }
    
    /**
     * A static method that takes a Graph, a start node, and an end node,
     * and returns a List of edges representing the shortest path from
     * the start node to the end node. The path is built up starting from
     * the first element of the List to the next, etc. 
     * @param graph: The graph to operate on 
     * @param start: The start node 
     * @param end: The end node
     * @requires All parameters to be non-null and for start and end to be 
     *           valid nodes in the graph
     * @return A list of Edges traversed from start node to end node, or 
     *         null if no path was found. 
     */
    public static List<Edge<String,String>> findPath(Graph<String,String> graph, 
                                                     Node<String> start, 
                                                     Node<String> end) {

        Graph.checkNull(graph);
        Graph.checkNull(start);
        Graph.checkNull(end);

        if (!(graph.getNodes().contains(start))) {
            throw new IllegalArgumentException("start character not valid");
        }

        if (!(graph.getNodes().contains(end))) {
            throw new IllegalArgumentException("end character not valid");
        }
        
        Queue<Node<String>> worklist = new LinkedList<Node<String>>();
        Map<Node<String>, List<Edge<String,String>>> paths = new HashMap<>();

        worklist.add(start);
        paths.put(start, new ArrayList<Edge<String,String>>());
        
        while (!(worklist.isEmpty())) {
            Node<String> character = worklist.remove();
            
            if (character.equals(end)) {
                return paths.get(character);
            }
            
            //Tree set for sorting 
            //(As Edge documentation states, Edges are already sorted
            //the way we want.)
            Set<Edge<String,String>> edges = new TreeSet<Edge<String,String>>
                                             (graph.getEdgesOfNode(character));

            for (Edge<String,String> edge : edges) {
                Node<String> child = edge.getChild();

                if (!(paths.containsKey(child))) {
                    List<Edge<String,String>> path = paths.get(character);
                    List<Edge<String,String>> newPath = new ArrayList<Edge<String,String>>(path);
                    newPath.add(edge);
                    paths.put(child, newPath);
                    worklist.add(child);
                }
            }
        }

        // no path exists from start node to end node
        return null;
    }
    
    public static void main(String[] args) throws MalformedDataException{
        
        @SuppressWarnings("resource")
        Scanner scan = new Scanner(System.in);
        
        Graph<String,String> g = createGraph("src/hw6/data/marvel.tsv");
        System.out.println("Hi. Please enter two characters, and "
                + "I'll find the shortest path between them for you.");
        
        System.out.print("First character: ");
        String char1 = scan.nextLine();
        System.out.print("Second character: ");
        String char2 = scan.nextLine();
        
        Node<String> node1 = new Node<>(char1);
        Node<String> node2 = new Node<>(char2);
        
        if(!g.getNodes().contains(node1) && !g.getNodes().contains(node2)){
            System.out.println("Both characters entered are invalid buddy, try again");
        }
        else if(!g.getNodes().contains(node1)){
            System.out.println(char1 + " is invalid buddy. Try again.");
        }
        else if(!g.getNodes().contains(node2)){
            System.out.println(char2 + " is invalid buddy. Try again.");
        }
        else{
            List<Edge<String,String>> edges = MarvelPaths.findPath(g, node1, node2);
            String retStr = "path from " + char1 + " to " + char2 + ":";
            if(edges==null){
                retStr += "\n" + "no path found";
            }
            else{
                for (Edge<String,String> edge : edges) {
                    retStr += "\n" + edge.getParent() + " to " + edge.getChild() + 
                            " via " + edge.getLabel();
                }
            }
            System.out.println(retStr);
        }
           
    }


}
