package hw5.test;

import static org.junit.Assert.*;

import org.junit.*;
import hw5.*;

public class EdgeTest {
    
    private static final Node<String> Node1 = new Node<String>("Node1");
    private static final Node<String> Node2 = new Node<String>("Node2");
    
    public static final Edge<String,String> Edge1 = new Edge<>(Node1, Node1, "Edge1");
    public static final Edge<String,String> Edge2 = new Edge<>(Node1, Node2, "Edge2");
    public static final Edge<String,String> Edge3 = new Edge<>(Node2, Node1, "Edge3");
    public static final Edge<String,String> Edge4 = new Edge<>(Node2, Node2, "Edge4");
    
    
    @Test
    public void testToString(){
        assertEquals(Edge1.toString(), "Edge Edge1 from Node1 to Node1");
    }
    
    @Test
    public void testEquality(){
        assertNotEquals(Edge1, Edge2);
        assertEquals(Edge2, Edge2);
    }
    
    @Test
    public void testSort(){
        assertTrue(Edge1.compareTo(Edge2) < 0);
        Edge<String,String> extraEdge = new Edge<>(Node1, Node1, "extraEdge");
        assertTrue(Edge1.compareTo(extraEdge) < 0);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void illegalArgumentChild(){
        new Edge<>(new Node<String>("parent"), null, "badEdge");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void illegalArgumentParent(){
        new Edge<>(null, new Node<String>("child"), "badEdge");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void illegalArgumentName(){
        new Edge<>(new Node<String>("parent"), new Node<String>("child"), null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void illegalArgumentAll(){
        new Edge<>(null, null, null);
    }
    
    
}
