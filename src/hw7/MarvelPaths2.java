package hw7;

import java.util.*;

import hw5.*;
import hw6.MarvelParser.MalformedDataException;

public class MarvelPaths2 {
    
    //RI and AF would usually go here, but since MarvelPaths contains
    //just static methods, it doesn't represent an ADT. 
    
    /** 
     * A static method that takes a fileName containing a valid set of
     * "characters" and "books" (more generally, desired nodes and
     * connecting edges) and creates a new graph storing the data.
     * The graph is formatted as follows: the nodes of the graph
     * contain the "characters" from the file, and the edges
     * contain the set of characters that it has appeared with. 
     * The edges are directed and are weighted in proportion with
     * the frequency of the character with another character in all
     * the books.
     * @param fileName: The file to create the graph from
     * @return The created graph
     * @throws MalformedDataException if the file is not valid 
     */
    public static Graph<String,Double> createGraph(String fileName)
            throws MalformedDataException {
        
        Graph.checkNull(fileName);
        HashMap<String,HashMap<String,Double>> counts = 
                new HashMap<>();
        
        MarvelParser2.parseData(fileName, counts);
        Graph<String,Double> graph = new Graph<>();
        
        // add the nodes of the graph
        for(String child: counts.keySet()){
            graph.addNode(new Node<String>(child));
        }
        
        // add the frequencies
        for(String child: counts.keySet()){
            HashMap<String,Double> count = counts.get(child);
            for(String subChild: count.keySet()){
                double total = count.get(subChild);
                graph.addEdge(new Node<String>(child), 
                              new Node<String>(subChild),
                              1.0/total);
            }
        }
        return graph;
    }
    
    /**
     * A static method that takes a Graph, a start node, and an end node,
     * and returns a List of edges representing the least cost path from
     * the start node to the end node. The path is built up starting from
     * the first element of the List to the next, etc. 
     * @param <N> The node type of the graph passed in. Should implement
     *            Comparable as documented as the Node class. 
     * @param graph: The graph to operate on 
     * @param start: The start node 
     * @param end: The end node
     * @requires All parameters to be non-null and for start and end to be 
     *           valid nodes in the graph
     * @return A list of Edges traversed from start node to end node, or 
     *         null if no path was found. 
     */
    public static <N extends Comparable<N>> List<Edge<N, Double>> findLeastCostPath(
            Graph<N, Double> graph, Node<N> start, Node<N> end) {

        Graph.checkNull(graph);
        Graph.checkNull(start);
        Graph.checkNull(end);

        if (!(graph.getNodes().contains(start))) {
            throw new IllegalArgumentException("start character not valid");
        }

        if (!(graph.getNodes().contains(end))) {
            throw new IllegalArgumentException("end character not valid");
        }

        // Need to pass in a comparator since Lists don't implement Comparable
        PriorityQueue<List<Edge<N, Double>>> active = new PriorityQueue<List<Edge<N, Double>>>(
                new Comparator<List<Edge<N, Double>>>() {
                    public int compare(List<Edge<N, Double>> path1,
                            List<Edge<N, Double>> path2) {
                        Edge<N, Double> dest1 = path1.get(path1.size() - 1);
                        Edge<N, Double> dest2 = path2.get(path2.size() - 1);
                        if (dest1.getLabel().equals(dest2.getLabel()))
                            return path1.size() - path2.size();

                        return dest1.getLabel().compareTo(dest2.getLabel());
                    }
                });
        Set<Node<N>> finished = new HashSet<>();

        // init
        List<Edge<N, Double>> beginPath = new ArrayList<>();
        beginPath.add(new Edge<N, Double>(start, start, 0.0));
        active.add(beginPath);

        while (!active.isEmpty()) {
            List<Edge<N, Double>> minPath = active.remove();
            Edge<N, Double> lastEdge = minPath.get(minPath.size() - 1);
            Node<N> minDest = lastEdge.getChild();
            double minCost = lastEdge.getLabel();
            if (minDest.equals(end)) {
                return minPath;
            }
            if (finished.contains(minDest)) {
                continue;
            }

            Set<Edge<N, Double>> edges = graph.getEdgesOfNode(minDest);
            for (Edge<N, Double> edge : edges) {
                if (!finished.contains(edge.getChild())) {
                    double newCost = minCost + edge.getLabel();
                    ArrayList<Edge<N, Double>> newPath = new ArrayList<>(
                            minPath);
                    newPath.add(new Edge<N, Double>(edge.getParent(),
                            edge.getChild(), newCost));
                    active.add(newPath);
                }
            }
            finished.add(minDest);
        }
        // if no path existed
        return null;

    }
    
    public static void main(String[] args) throws MalformedDataException{
        
        @SuppressWarnings("resource")
        Scanner scan = new Scanner(System.in);
        Graph<String,Double> g = createGraph("src/hw6/data/marvel.tsv");
        System.out.println("Hi. Please enter two characters, and "
                + "I'll find the least cost path between them for you.");
        
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
            List<Edge<String,Double>> edges = MarvelPaths2.findLeastCostPath(g, node1, node2);
            String retStr = "path from " + node1.getData() + " to " + node2.getData() + ":";
            if(edges==null){
                retStr += "\n" + "no path found";
            }
            else{
                double currCost = 0.0;
                for (Edge<String,Double> edge : edges) {
                    retStr += "\n" + edge.getParent() + 
                              " to " + edge.getChild() + 
                              " with weight " + 
                              String.format("%.3f", (edge.getLabel()-currCost));
                    currCost = edge.getLabel();
                }
                retStr += "\n" + "total cost: " + String.format("%.3f", currCost);
            }
            System.out.println(retStr);
        }
     
    }

}

