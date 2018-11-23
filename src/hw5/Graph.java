package hw5;

import java.util.*;

/**
 * Graph represents a mutable mathematical graph data structure.
 * All nodes and labeled edges are represented as Nodes and Edges.
 * Please see their documentations. 
 * The graph is labeled: i.e. the edges are given labels. 
 * The graph is directed: i.e. there is a concept of direction with the edges.
 * Finally, edges of a particular node are unique: there can be at most one
 * outgoing edge from a node to another node with that particular name. 
 * 
 * @specfield nodes : list of all the nodes in the graph
 * @specfield edges : list of outgoing edges for each node
 *                    (containing the names of the nodes that 
 *                    the edges point to)
 * @param <N> The type of the Node. Should implement Comparable. 
 * @param <E> The type of the Edge's label. Should implement Comparable. 
 * @author Rohan Guliani
 * @version 0.1
 *
 */

public class Graph<N extends Comparable<N>, E extends Comparable<E>> {

    /* ***** REP INVARIANT/ABSTRACTION FUNCTION ***** */
    /*
     * Rep Invariant: 
     *      The adjacent list != null and 
     *      No node in the graph is null and 
     *      no set of edges is null and
     *      no edge for any node is null and 
     *      no name for any edge is null and 
     *      every edge points to a valid (existing) node in the graph.
     * 
     * Abstraction Function: AF(this) where this is an instantiation of graph =
     *      -{} if this is an empty graph with no nodes
     *      -{n1={n*, n**, ...}, n2={n***, ...}} 
     *       where n1, n2, ... represent all the
     *       nodes in the graph and n*, n**, ... 
     *       represent the outgoing (valid) nodes
     *       for each node in the graph.
     */

    private static boolean TEST_FLAG = false;
    private Map<Node<N>, Set<Edge<N,E>>> adj_list;

    /**
     * Creates a new, empty graph.
     * 
     * @effects Creates a new, empty graph.
     */
    public Graph() {
        adj_list = new HashMap<Node<N>, Set<Edge<N,E>>>();
        checkRep();
    }
    
    /**
     * Private constructor that takes in a Map, makes a duplicate of it,
     * and creates a unique graph.
     * @effects Creates a new but possibly non-empty graph
     * @param graph: The pre-existing graph
     */
    private Graph(Map<Node<N>, Set<Edge<N, E>>> graph){
        adj_list = new HashMap<>(graph);
        checkRep();
    }

    /**
     * Adds a new node to the graph.
     * 
     * @modifies the "nodes" field
     * @effects adds a node to "nodes" if the parameter is a valid node 
     *          (see @return branch)
     * @param node:
     *            The node to be added.
     * @throws IllegalArgumentException
     *             if node is null
     * @return true if successfully added or false if the graph already
     *         contained the node (and hence wasn't added)
     */
    public boolean addNode(Node<N> node) {
        checkRep();
        checkNull(node);
        if (adj_list.containsKey(node)) {
            return false;
        }
        adj_list.put(node, new HashSet<Edge<N,E>>());
        checkRep();
        return true;
    }

    /**
     * Adds an outgoing edge from node key to node target.
     * 
     * @modifies this.nodes(key).edges, i.e. the edges field that corresponds
     *           to the node to which an edge is to be added. 
     * @effects adds a valid edge to this.nodes(key).edges if both key and target
     *          are valid (see @return branch)           
     * @param key:
     *            The node that the edge is to be added to.
     * @param target:
     *            The node on the receiving side of the edge.
     * @param edgeLabel
     *            The label to give to the edge
     * @throws IllegalArgumentException
     *             if either key or target or edgeName is null
     * @return true if successfully added or false if the graph didn't contain
     *         the node key or the graph did contain the key but the exact
     *         same edge (with same name) already existed
     */
    public boolean addEdge(Node<N> key, Node<N> target, E edgeLabel) {
        checkNull(key);
        checkNull(target);
        checkNull(edgeLabel);
        Edge<N,E> newEdge = new Edge<N,E>(key, target, edgeLabel);
        return addEdge(newEdge);
    }
    
    /**
     * Adds an outgoing edge from node key to node target.
     * @modifies this.nodes(key).edges, i.e. the edges field that corresponds
     *           to the node to which an edge is to be added. Here 
     *           key = newEdge.getParent(); 
     * @effects adds node target to this.nodes(key).edges if both key and node
     *          are valid (see @return branch). Here
     *          node = newEdge.getChild();
     * @param newEdge: the Edge to be added
     * @throws IllegalArgumentException if newEdge is null
     * @return true if successfully added or false if the graph didn't contain
     *         the node key or the graph did contain the key but the exact
     *         same edge (with same name) already existed
     */
    public boolean addEdge(Edge<N,E> newEdge){
        checkRep();
        checkNull(newEdge);
        Node<N> key = newEdge.getParent();
        Node<N> target = newEdge.getChild();
                
        if (!adj_list.containsKey(key) || !adj_list.containsKey(target))
            return false;
        Set<Edge<N,E>> targetSet = adj_list.get(key);
        if (targetSet.contains(newEdge)) {
            return false;
        } else {
            targetSet.add(newEdge);
            checkRep();
            return true;
        }
    }

    /**
     * Returns the nodes in the graph.
     * 
     * @return A Set containing the nodes in the graph.
     */
    public Set<Node<N>> getNodes() {
        return new HashSet<Node<N>>(adj_list.keySet());
    }

    /**
     * Returns a set of edges for the given node.
     * 
     * @param node:
     *            The node for which edges are desired.
     * @throws IllegalArgumentException if node is null or doesn't correspond
     *         to a valid node in the graph
     * @return A Set of edges for the given node
     */
    public Set<Edge<N,E>> getEdgesOfNode(Node<N> node) {
        checkNull(node);
        if(!adj_list.containsKey(node)){
            throw new IllegalArgumentException("Not a valid node");
        }
        return new HashSet<Edge<N,E>>(adj_list.get(node));
    }
    
    /**
     * Returns the graph as a Map.
     * @return The graph as a Map<Node, Set<Edge>>
     */
    public Map<Node<N>, Set<Edge<N,E>>> getGraph(){
        return new HashMap<Node<N>, Set<Edge<N,E>>>(adj_list);
    }

    /**
     * Returns a string representation of the entire graph, including all its
     * nodes and edges for each node.
     * 
     * @return a string representation of the entire graph. 
     */
    public String toString() {
        return adj_list.toString();
    }
    
    /**
     * Removes an outgoing edge for a particular node.
     * 
     * @modifies this.nodes(fromNode).edges 
     * @effects removes toNode from this.nodes(fromNode).edges if
     *          fromNode and toNode are valid (see @return branch)
     * @param fromNode:
     *            The node for which an edge is to be removed.
     * @param toNode:
     *            The node the edge to be taken out is pointing to.
     * @param edgeLabel: the label of the edge
     * @throws IllegalArgumentException
     *             if either fromNode or toNode or edgeName is null.
     * @return true if the edge was successfully removed, or false if fromNode
     *         was not a valid node in the graph or toNode was not a valid
     *         outgoing edge from that node.
     */
    public boolean removeEdge(Node<N> fromNode, Node<N> toNode, E edgeLabel) {
        checkNull(fromNode);
        checkNull(toNode);
        checkNull(edgeLabel);
        Edge<N,E> target = new Edge<>(fromNode, toNode, edgeLabel);
        return removeEdge(target);
    }
    
    /**
     * Removes an outgoing edge for a particular node.
     * 
     * @modifies this.nodes(fromNode).edges where fromNode is target's parent
     * @effects removes toNode from this.nodes(fromNode).edges if
     *          fromNode and toNode are valid (see @return branch)
     *          where toNode is target's child
     * @param target: The Edge to be removed
     * @throws IllegalArgumentException
     *             if target is null.
     * @return true if the edge was successfully removed, or false if fromNode
     *         was not a valid node in the graph or toNode was not a valid
     *         outgoing edge from that node.
     */
    public boolean removeEdge(Edge<N,E> target){
        checkRep();
        checkNull(target);
        Node<N> fromNode = target.getParent();
        if (adj_list.containsKey(fromNode)) {
            Set<Edge<N,E>> edges = adj_list.get(fromNode);
            if (edges.contains(target)) {
                edges.remove(target);
                checkRep();
                return true;
            }
        }
        return false;
        
    }

    /**
     * Removes a node from the graph, as well as all the edges that were
     * pointing to it.
     * 
     * @modifies this.nodes and this.nodes(n).edges for every node n in 
     *           the graph.
     * @effects removes the given node from this.nodes and removes all 
     *          edges in every node in the graph that pointed to the node
     *          that was removed. 
     * @param node:
     *            The node to be removed.
     * @throws IllegalArgumentException
     *             if node is null.
     * @return true if the node was successfully removed or false if node was
     *         not valid.
     */
    public boolean removeNode(Node<N> node) {
        checkRep();
        checkNull(node);
        if (adj_list.containsKey(node)) {
            // remove all edges pointing to this one, including from itself
            for(Node<N> other_node: adj_list.keySet()){
                Set<Edge<N,E>> edges = adj_list.get(other_node);
                for(Edge<N,E> edge: edges){
                   if(edge.getChild().equals(node)){
                       removeEdge(edge);
                   }
                }
                
            }
            adj_list.remove(node);
            checkRep();
            return true;
        }
        return false;
    }

    /**
     * Returns the number of nodes in the graph.
     * 
     * @return the number of nodes in the graph
     */
    public int size() {
        return adj_list.size();
    }
    
    /**
     * Returns whether or not the graph is empty
     * @return whether or not the graph is empty
     */
    public boolean isEmpty() {
        return adj_list.isEmpty();
    }
    
    /**
     * Duplicates this graph and returns a new, completely separate instance
     * of it.
     * @return A new graph with the same data. 
     */
    public Graph<N,E> dup(){
        checkRep();
        return new Graph<N,E>(adj_list);
    }    

    /**
     * Static helper method that checks whether 
     * or not the passed object is null.
     * 
     * @param param:
     *            the parameter to be checked.
     * @throws IllegalArgumentException
     *             if param is null, does nothing otherwise.
     */
    public static void checkNull(Object param) {
        if (param == null) {
            throw new IllegalArgumentException("Parameter is null");
        }
    }
        
    /**
     * Public static helper method that turns debugging
     * off to improve performance
     */
    public static void debugOff(){
        TEST_FLAG = false;
    }
    
    /**
     * Public static helper method that turns on 
     * debugging to add an extra layer of security
     * when performance is not an issue.
     */
    public static void debugOn(){
        TEST_FLAG = true;
    }
    
    /**
     * Checks the rep invariant and throws runtime invariants if the rep
     * invariant doesn't hold, and doesn't do anything else otherwise.
     * @throws RuntimeException if the graph instance does not satisfy
     *         the rep invariant.
     */
    private void checkRep() {
        if (TEST_FLAG) {
            if (adj_list == null) {
                throw new RuntimeException("The graph must not be null");
            } else if (adj_list.containsKey(null)) {
                throw new RuntimeException(
                        "The graph must not contain a null node");
            } else if (adj_list.containsValue(null)) {
                throw new RuntimeException(
                        "The graph must not contain a null set of edges");
            } else {
                Set<Node<N>> nodes = adj_list.keySet();
                for (Node<N> n : nodes) {
                    Set<Edge<N,E>> values = adj_list.get(n);
                    for (Edge<N,E> value : values) {
                        if (value == null) {
                            throw new RuntimeException(
                                    "The graph must not contain a node "
                                            + "that points to a null destination");
                        } else if (value.getLabel() == null
                                || value.getChild() == null
                                || value.getParent() == null) {
                            throw new RuntimeException(
                                    "The graph must not contain an edge with"
                                            + "a null field");
                        } else if (!adj_list.containsKey(value.getChild())
                                || !adj_list.containsKey(value.getParent())){
                            throw new RuntimeException(
                                    "The graph must not contain an edge "
                                  + "to or from a node that doesn't exist in the graph");
                        } else {

                            // do nothing
                        }
                    }
                }
            }
        }
    }
}
