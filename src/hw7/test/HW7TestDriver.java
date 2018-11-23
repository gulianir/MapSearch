package hw7.test;

import java.io.*;
import java.util.*;

import hw5.*;
import hw5.test.*;
import hw6.MarvelParser.MalformedDataException;
import hw7.*;

/**
 * This class implements a testing driver which reads test scripts from files
 * for your graph ADT and improved MarvelPaths application using Dijkstra's
 * algorithm.
 **/
public class HW7TestDriver {

    public static void main(String args[]) {
        try {
            if (args.length > 1) {
                printUsage();
                return;
            }

            HW5TestDriver td;

            if (args.length == 0) {
                td = new HW5TestDriver(new InputStreamReader(System.in),
                        new OutputStreamWriter(System.out));
            } else {

                String fileName = args[0];
                File tests = new File(fileName);

                if (tests.exists() || tests.canRead()) {
                    td = new HW5TestDriver(new FileReader(tests),
                            new OutputStreamWriter(System.out));
                } else {
                    System.err.println("Cannot read from " + tests.toString());
                    printUsage();
                    return;
                }
            }

            td.runTests();

        } catch (IOException e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
        }
    }

    private static void printUsage() {
        System.err.println("Usage:");
        System.err.println(
                "to read from a file: java hw5.test.HW5TestDriver <name of input script>");
        System.err.println(
                "to read from standard in: java hw5.test.HW5TestDriver");
    }

    /** String -> Graph: maps the names of graphs to the actual graph **/
    private final Map<String, Graph<String, Double>> graphs = new HashMap<String, Graph<String, Double>>();
    private final PrintWriter output;
    private final BufferedReader input;

    public HW7TestDriver(Reader r, Writer w) {
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    public void runTests() throws IOException {
        String inputLine;
        while ((inputLine = input.readLine()) != null) {
            if ((inputLine.trim().length() == 0)
                    || (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if (st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<String>();
                    while (st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }
    
    private void executeCommand(String command, List<String> arguments) {
        try {
            if (command.equals("CreateGraph")) {
                createGraph(arguments);
            } else if (command.equals("AddNode")) {
                addNode(arguments);
            } else if (command.equals("AddEdge")) {
                addEdge(arguments);
            } else if (command.equals("ListNodes")) {
                listNodes(arguments);
            } else if (command.equals("ListChildren")) {
                listChildren(arguments);
            } else if (command.equals("LoadGraph")) {
                loadGraph(arguments);
            } else if (command.equals("FindPath")) {
                findPath(arguments);
            } else {
                output.println("Unrecognized command: " + command);
            }
        } catch (Exception e) {
            output.println("Exception: " + e.toString());
        }
    }
    
    private void createGraph(List<String> arguments) {
        if (arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {
        // Insert your code here.

        graphs.put(graphName, new Graph<String,Double>());
        output.println("created graph " + graphName);
    }

    private void addNode(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to addNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {
        // Insert your code here.

        Graph<String,Double> graph = graphs.get(graphName);
        graph.addNode(new Node<String>(nodeName));
        output.println("added node " + nodeName + " to " + graphName);
    }

    private void addEdge(List<String> arguments) {
        if (arguments.size() != 4) {
            throw new CommandException("Bad arguments to addEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        double edgeLabel;
        try {
            edgeLabel = Double.parseDouble(arguments.get(3));
        } catch(NumberFormatException nfe) {
            throw new CommandException("Argument at 3 must be a number of type double");
        }

        addEdge(graphName, parentName, childName, edgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName,
            Double edgeLabel) {
        // Insert your code here.

        Graph<String,Double> graph = graphs.get(graphName);
        Node<String> parent = new Node<>(parentName);
        Node<String> child = new Node<>(childName);
        graph.addEdge(new Edge<String,Double>(parent, child, edgeLabel));
        output.println(String.format("added edge %.3f", edgeLabel) + 
                " from " + parentName + " to " + 
                childName + " in " + graphName);
    }

    private void listNodes(List<String> arguments) {
        if (arguments.size() != 1) {
            throw new CommandException("Bad arguments to listNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {
        // Insert your code here.

        Graph<String,Double> graph = graphs.get(graphName);
        Set<Node<String>> nodes = graph.getNodes();
        List<String> lstNodes = new ArrayList<String>();
        for(Node<String> node: nodes){
            lstNodes.add(node.toString());
        }
        Collections.sort(lstNodes);
        
        output.print(graphName + " contains:" );
        for(String node: lstNodes){
            output.print(" " + node);
        }
        output.println();
    }

    private void listChildren(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to listChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    private void listChildren(String graphName, String parentName) {
        // Insert your code here.

        Graph<String,Double> graph = graphs.get(graphName);
        Set<Edge<String,Double>> edges = graph.getEdgesOfNode
                                        (new Node<String>(parentName));
        List<String> lstChildren = new ArrayList<String>();
        for(Edge<String,Double> edge: edges){
            lstChildren.add(edge.getChild().toString() +
                    String.format("(%.3f)", edge.getLabel()));
        }
        Collections.sort(lstChildren);
        
        output.print("the children of " + parentName + " in " + graphName + " are:");
        for(String child: lstChildren){
            output.print(" " + child);
        }
        output.println();
    }
    
    private void loadGraph(List<String> arguments) 
            throws MalformedDataException {
        if (arguments.size() != 2) {
            throw new CommandException(
                    "Bad arguments to listChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String fileName = arguments.get(1);
        loadGraph(graphName, fileName);
    }

    private void loadGraph(String graphName, String fileName) 
            throws MalformedDataException {
        // Insert your code here.

        Graph<String,Double> graph = MarvelPaths2.createGraph("src/hw6/data/" + fileName);
        graphs.put(graphName, graph);
        output.println("loaded graph " + graphName);
    }
    
    private void findPath(List<String> arguments) {
        if (arguments.size() != 3) {
            throw new CommandException(
                    "Bad arguments to listChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String node1 = arguments.get(1).replace('_', ' ');
        String node2 = arguments.get(2).replace('_', ' ');
        findPath(graphName, node1, node2);
    }

    private void findPath(String graphName, String node1Str, String node2Str) {
        // Insert your code here.
        
        Node<String> node1 = new Node<>(node1Str);
        Node<String> node2 = new Node<>(node2Str);
        Graph<String,Double> graph = graphs.get(graphName);
        
        if ((!graph.getNodes().contains((node1))) && 
                (!graph.getNodes().contains((node2)))) {
            output.println("unknown character " + node1);
            output.println("unknown character " + node2);
        } else if (!(graph.getNodes().contains(node1))) {
            output.println("unknown character " + node1);
        } else if (!(graph.getNodes().contains(node1))) {
            output.println("unknown character " + node1);
        } else {
            List<Edge<String,Double>> edges = MarvelPaths2.findLeastCostPath(graph, node1, node2);
            String retStr = "path from " + node1Str + " to " + node2Str + ":";
            if(edges==null){
                retStr += "\n" + "no path found";
            }
            else{
                double currCost = 0.0;
                //avoid first element which has an edge to itself
                for(int i = 1; i<edges.size(); i++){
                    retStr += "\n" + edges.get(i).getParent() + 
                              " to " + edges.get(i).getChild() + 
                              " with weight " + 
                              String.format("%.3f", (edges.get(i).getLabel()-currCost));
                    currCost = edges.get(i).getLabel();
                }
                retStr += "\n" + "total cost: " + String.format("%.3f", currCost);
            }
            output.println(retStr);
        }
    }
    
    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }

        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }
}
