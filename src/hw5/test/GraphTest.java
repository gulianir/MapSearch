package hw5.test;

import static org.junit.Assert.*;

import org.junit.*;
import java.util.*;
import hw5.*;

public class GraphTest {
    
    private static final Node<String> Node1 = new Node<>("Node1");
    private static final Node<String> Node2 = new Node<>("Node2");
    private static final Node<String> badNode = new Node<>("BAD");

    
    private static Graph<String,String> createGraph(){
        return new Graph<>();
    }
    
    private static Graph<String,String> createGraph1Node(){
        Graph<String,String> g = createGraph();
        g.addNode(Node1);
        return g;
    }
    
    private static Graph<String,String> createGraph2Nodes(){
        Graph<String,String> g = createGraph1Node();
        g.addNode(Node2);
        return g;        
    }
    
    Graph<String,String> graph1;
    boolean addValid;
    
    Graph<String,String> graph2;
    boolean testValidRemove;
    
    Graph<String,String> graph3;
    boolean addBadEdge1;
    boolean addBadEdge2;
    boolean removeBadEdge1;
    boolean removeBadEdge2;
    boolean addValidEdge1;
    boolean addValidEdge2;
    
    Graph<String,String> graph4;
    boolean removeValidEdge1;
    boolean removeValidEdge2;
    
    Graph<String,String> graph5;
    boolean removeValidNode;
    
    @Before
    public void init(){
        graph1 = createGraph();
        addValid = graph1.addNode(Node1);
        
        graph2 = createGraph1Node();
        testValidRemove = graph2.removeNode(Node1);
        
        graph3 = createGraph2Nodes();
        addBadEdge1 = graph3.addEdge(Node1, badNode, "Edge1");
        addBadEdge2 = graph3.addEdge(badNode, Node1, "Edge2");
        removeBadEdge1 = graph3.removeEdge(Node1, badNode, "Edge1");
        removeBadEdge2 = graph3.removeEdge(badNode, Node1, "Edge1");
        addValidEdge1 = graph3.addEdge(Node1, Node1, "Edge1");
        addValidEdge2 = graph3.addEdge(Node1, Node2, "Edge2");
        
        graph4 = createGraph2Nodes();
        graph4.addEdge(Node1, Node1, "Edge1");
        graph4.addEdge(Node1, Node2, "Edge2");
        removeValidEdge1 = graph4.removeEdge(Node1, Node1, "Edge1");
        removeValidEdge2 = graph4.removeEdge(Node1, Node2, "Edge2");
        
        graph5 = createGraph2Nodes();
        graph5.addEdge(Node1, Node1, "Edge1");
        graph5.addEdge(Node1, Node2, "Edge2");
        removeValidNode = graph5.removeNode(Node2);

    }
    
    @Test
    public void initEmpty() {
        assertEquals(createGraph().size(), 0);        
    }
    
    @Test
    public void initGetNodes() {
        assertEquals(createGraph().getNodes(), new HashSet<Node<String>>());
    }
    
    @Test
    public void initGetGraph() {
        assertEquals(createGraph().getGraph(), 
                new HashMap<Node<String>, Set<Edge<String,String>>>());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void addNull(){
        createGraph().addNode(null);
    }
      
    @Test
    public void addValidNode(){
        assertTrue(addValid);
    }
    
    @Test
    public void addDuplicateNode(){
        assertFalse(graph1.addNode(Node1));
    }
    
    @Test
    public void checkSizeAfterDuplicateAdded(){
        assertEquals(graph1.size(), 1);
    }
    
    @Test
    public void checkWhatNodeIsThereAfterDupAdded(){
        assertEquals(graph1.getNodes().toString(), "[Node1]");
    }
    
    @Test
    public void removeNonExistingNode(){
        assertFalse(graph1.removeNode(Node2));
    }
           
    @Test
    public void removeExistingNode(){
        assertTrue(testValidRemove);
    }
    
    @Test
    public void checkEmptyAfterRemoval(){
        assertTrue(graph2.isEmpty());
    }
    
    @Test
    public void remove1NodeFrom2(){
        assertTrue(graph3.removeNode(Node2));
        assertEquals(graph3.getNodes().toString(), "[Node1]");
    }
        
    @Test
    public void addBadEdges(){
        assertFalse(addBadEdge1);
        assertFalse(addBadEdge2);
    }   
    
    @Test
    public void removeEdges(){
        assertFalse(removeBadEdge1);
        assertFalse(removeBadEdge2);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void getEdgesOfInvalidNode(){
        assertEquals(createGraph().getEdgesOfNode(Node1).toString(), "[]");
    }
    
    @Test
    public void getEdgesOfEmptyNode(){
        assertEquals(createGraph1Node().getEdgesOfNode(Node1).toString(), "[]");
    }
         
    @Test
    public void addValidEdges(){
        assertTrue(addValidEdge1);
        assertTrue(addValidEdge2);
        assertEquals(graph3.getEdgesOfNode(Node1).toString(), 
                "[Edge Edge1 from Node1 to Node1, Edge Edge2 from Node1 to Node2]");
    }
    
    @Test
    public void removeValidEdges(){
        assertTrue(removeValidEdge1);
        assertTrue(removeValidEdge2);
        assertEquals(graph4.getEdgesOfNode(Node1).toString(), "[]");
    }
    
    
    @Test
    public void removeValidNode(){
        assertTrue(removeValidNode);
        //Node2 removed
        assertEquals(graph5.getNodes().toString(), "[Node1]");
        //Edge from Node1 to Node 2 also removed
        assertEquals(graph5.getEdgesOfNode(Node1).toString(), "[Edge Edge1 from Node1 to Node1]");
    }
    
    @Test
    public void testDup(){
        Graph<String,String> temp1 = createGraph1Node();
        Graph<String,String> temp2 = temp1.dup();
        
        temp1.addNode(Node2);
        
        assertEquals(temp1.getNodes().toString(), "[Node1, Node2]");
        assertEquals(temp2.getNodes().toString(), "[Node1]");
        
        assertFalse(temp1.toString().equals(temp2.toString()));
    }
    
    @Test
    public void testGetGraph(){
        Graph<String,String> g = createGraph2Nodes();
        Map<Node<String>, Set<Edge<String,String>>> map = new HashMap<>();
        map.put(Node1, new HashSet<Edge<String,String>>());
        map.put(Node2, new HashSet<Edge<String,String>>());
        
        assertEquals(g.getGraph(), map);
    }
    
    @Test
    public void testSize(){
        Graph<String,String> g = createGraph2Nodes();
        assertEquals(g.size(), 2);
    }
    
    @Test
    public void testIsEmpty(){
        Graph<String,String> g = createGraph();
        assertTrue(g.isEmpty());
    }
    
    
    
}
