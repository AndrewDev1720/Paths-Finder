import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
public class Project6Tester{
  @Test
  public void testAddNodeandPrintGraph(){
    Graph graph = new Graph();
    //test 0
    assertTrue(graph.getStringGraph().equals(""));
    //test 1
    graph.addNode("A");
    assertTrue(graph.getStringGraph().equals("A"));
    //test many
    graph.addNode("B");
    assertTrue(graph.getStringGraph().equals("A\nB"));
    graph.addNode("D");
    graph.addNode("C");
    assertTrue(graph.getStringGraph().equals("A\nB\nC\nD"));
    //test Special
    graph.addNode("B");
    assertTrue(graph.getStringGraph().equals("A\nB\nC\nD"));
    graph.addNode("A");
    assertTrue(graph.getStringGraph().equals("A\nB\nC\nD"));
    graph.addNode("D");
    assertTrue(graph.getStringGraph().equals("A\nB\nC\nD"));
  }
  
  @Test
  public void testAddNodes(){
    Graph graph = new Graph();
    //test 0 
    String[] nodes = new String[]{};
    graph.addNodes(nodes);
    assertTrue(graph.getStringGraph().equals(""));
    //test 1
    nodes = new String[]{"B"};
    graph.addNodes(nodes);
    assertTrue(graph.getStringGraph().equals("B"));
    //test many
    nodes = new String[]{"D","C","A"};
    graph.addNodes(nodes);
    assertTrue(graph.getStringGraph().equals("A\nB\nC\nD"));
  }
  
  @Test
  public void testAddEdge(){
    //test 0
    Graph graph = new Graph();
    graph.addEdge("","");
    assertTrue(graph.getStringGraph().equals(""));
    //test 1
    Graph graph1 = new Graph();
    graph1.addEdge("A","A");
    assertTrue(graph1.getStringGraph().equals("A"));
    assertFalse(graph1.addEdge("A","A"));
    assertTrue(graph1.addEdge("D","A"));
    assertTrue(graph1.getStringGraph().equals("A D\nD A"));
    assertTrue(graph1.isConnected("A","D")); // also test for isConnected() method
    assertTrue(graph1.isConnected("D","A"));
    //test many
    Graph graph2 = new Graph();
    graph2.addEdge("Cle","Cin");
    graph2.addEdge("Cle","Col");
    graph2.addEdge("Cle","Buf");
    graph2.addEdge("Buf","Col");
    assertTrue(graph2.isConnected("Cle","Cin"));
    assertTrue(graph2.isConnected("Cle","Col"));
    assertTrue(graph2.isConnected("Cle","Buf"));
    assertTrue(graph2.isConnected("Col","Buf"));
    assertTrue(graph2.getStringGraph().equals("Buf Cle Col\nCin Cle\nCle Buf Cin Col\nCol Buf Cle"));
  }
  
  @Test
  public void testAddEdges(){
    Graph graph = new Graph();
    String[] to = new String[]{};
    //test 0
    assertFalse(graph.addEdges("A",to));
    assertTrue(graph.getStringGraph().equals("A"));
    
    //test 1
    graph = new Graph();
    to = new String[]{"B"};
    assertFalse(graph.addEdges("B",to));
    assertTrue(graph.getStringGraph().equals("B"));
    to = new String[]{"A"};
    assertTrue(graph.addEdges("B",to));
    assertTrue(graph.getStringGraph().equals("A B\nB A"));
    
    //test many
    to = new String[]{"E","D","H"};
    assertTrue(graph.addEdges("B",to));
    to = new String[]{"E","M","N"};
    assertFalse(graph.addEdges("B",to));  
    assertTrue(graph.getStringGraph().equals("A B\nB A D E H M N\nD B\nE B\nH B\nM B\nN B"));
  }
  
  @Test
  public void testRemoveNode(){
    Graph graph = new Graph();
    //test 0
    assertFalse(graph.removeNode("A"));
    assertFalse(graph.removeNode(""));
    assertFalse(graph.removeNode(" "));
    assertTrue(graph.getStringGraph().equals(""));
    
    //test 1
    assertTrue(graph.addNode("A"));
    assertTrue(graph.removeNode("A"));
    assertTrue(graph.getStringGraph().equals(""));
    
    //test 1 with edge
    assertTrue(graph.addNode("A"));
    assertTrue(graph.addEdge("A","E"));
    assertTrue(graph.getStringGraph().equals("A E\nE A")); 
    assertTrue(graph.isConnected("E","A"));
    assertTrue(graph.removeNode("A"));
    assertFalse(graph.isConnected("E","A"));
    assertTrue(graph.getStringGraph().equals("E"));  
    
    //test many
    String[] to = new String[]{"E","D","H"};
    assertTrue(graph.addEdges("A",to));
    assertTrue(graph.getStringGraph().equals("A D E H\nD A\nE A\nH A")); 
    assertTrue(graph.isConnected("H","A"));
    assertTrue(graph.isConnected("D","A"));
    assertTrue(graph.isConnected("E","A"));
    assertTrue(graph.removeNode("A"));
    assertFalse(graph.isConnected("H","A"));
    assertFalse(graph.isConnected("D","A"));
    assertFalse(graph.isConnected("E","A"));
    assertTrue(graph.getStringGraph().equals("D\nE\nH")); 
    assertTrue(graph.addEdge("E","H"));
    assertTrue(graph.addEdge("E","D"));
    assertTrue(graph.removeNode("H"));
    assertFalse(graph.isConnected("E","H"));
    assertTrue(graph.getStringGraph().equals("D E\nE D")); 
    assertFalse(graph.removeNode("A"));
  }
  
  @Test
  public void testRemoveNodes(){
    Graph graph = new Graph();
    String[] remove = new String[]{};
    //test 0
    assertFalse(graph.removeNodes(remove));
    assertTrue(graph.getStringGraph().equals("")); 
    remove = new String[]{"A"};
    assertFalse(graph.removeNodes(remove));
    assertTrue(graph.getStringGraph().equals("")); 
    remove = new String[]{"B","C","A"};
    assertFalse(graph.removeNodes(remove));
    assertTrue(graph.getStringGraph().equals("")); 
    
    //test 1
    remove = new String[]{"A"};
    String[] nodes = new String[]{"E"};
    assertFalse(graph.removeNodes(remove));
    assertTrue(graph.addNodes(nodes));
    remove = new String[]{"E","F","G"};
    assertFalse(graph.removeNodes(remove));
    assertFalse(graph.addNode(null));
    assertTrue(graph.getStringGraph().equals("")); 
    
    //test many
    graph = new Graph();
    assertTrue(graph.addEdges("A",new String[]{"B","C","D","E"}));
    assertTrue(graph.removeNodes(new String[]{"E","C","B","D"}));
    assertTrue(graph.getStringGraph().equals("A")); 
    assertTrue(graph.addEdges("A",new String[]{"B","C","D","E"}));
    assertTrue(graph.removeNodes(new String[]{"E","C","A"}));
    assertTrue(graph.getStringGraph().equals("B\nD")); 
    assertFalse(graph.removeNodes(new String[]{"B","G"}));
    assertTrue(graph.getStringGraph().equals("D")); 
  }
  
  @Test
  public void testRead() throws Exception{
    Graph g = new Graph();
    //test 0
    Graph graph = g.read("graph0.txt");
    assertTrue(graph.getStringGraph().equals("")); 
    
    //test 1
    graph = g.read("graph1.txt");
    assertTrue(graph.getStringGraph().equals("One"));
    
    //test many
    graph = g.read("graphMany.txt");
    assertTrue(graph.getStringGraph().equals("A B C D\nB A C\nC A B\nD A"));
    
    //test one edge
    graph = g.read("graphOneEdge.txt");
    assertTrue(graph.getStringGraph().equals("Cin Cle\nCle Cin"));
  }
  
  @Test
  public void testDFS() throws Exception{
    Graph g = new Graph();
    Graph graph = g.read("graph.txt");
    //test 0
    assertEquals(new String[]{},g.DFS("Cincinnati","Buffalo","alphabetical"));
    //test 1
    assertEquals(new String[]{"Cleveland"},graph.DFS("Cleveland","Cleveland","alphabetical"));
    //test many
    assertEquals(new String[]{"Cincinnati","Columbus","Cleveland","Buffalo"},graph.DFS("Cincinnati","Buffalo","alphabetical"));
    g.addEdges("A",new String[]{"B","C","D"});
    g.addEdges("B",new String[]{"C"});
    assertEquals(new String[]{"C","A","D"},g.DFS("C","D","alphabetical"));
    assertEquals(new String[]{"C","B","A","D"},g.DFS("C","D","reverse"));
    g = new Graph();
    g.addEdge("A","C");
    assertEquals(new String[]{"A"},g.DFS("A","A","alphabetical"));
    assertEquals(new String[]{"A"},g.DFS("A","A","reverse"));
    assertEquals(new String[]{"A","C"},g.DFS("A","C","alphabetical"));
    assertEquals(new String[]{"A","C"},g.DFS("A","C","reverse"));
    //test Special
    assertEquals(new String[]{},g.DFS("A","M","reverse"));
  }
  
  @Test
  public void testBFS() throws Exception{
    Graph g = new Graph();
    Graph graph = g.read("graph.txt");
    //test 0
    assertEquals(new String[]{},g.BFS("Cincinnati","Buffalo","alphabetical"));
    //test 1
    assertEquals(new String[]{"Cleveland"},graph.BFS("Cleveland","Cleveland","alphabetical"));
    //test many
    assertEquals(new String[]{"Cincinnati","Columbus","Cleveland","Buffalo"},graph.BFS("Cincinnati","Buffalo","alphabetical"));
    assertEquals(new String[]{"Cincinnati","Toledo","Cleveland","Buffalo"},graph.BFS("Cincinnati","Buffalo","reverse"));
    g.addEdges("A",new String[]{"B","C","D"});
    g.addEdges("B",new String[]{"C"});
    assertEquals(new String[]{"C","A","D"},g.BFS("C","D","alphabetical"));
    assertEquals(new String[]{"C","A","D"},g.BFS("C","D","reverse"));
    g = new Graph();
    g.addEdge("A","C");
    assertEquals(new String[]{"A"},g.BFS("A","A","alphabetical"));
    assertEquals(new String[]{"A"},g.BFS("A","A","reverse"));
    assertEquals(new String[]{"A","C"},g.BFS("A","C","alphabetical"));
    assertEquals(new String[]{"A","C"},g.BFS("A","C","reverse"));
  }
  
  @Test
  public void testIsConnected(){
    Graph g = new Graph();
    assertFalse(g.isConnected("A","B"));
    g.addNode("A");
    assertFalse(g.isConnected("A","B"));
    g.addNode("B");
    g.addEdge("A","B");
    assertTrue(g.isConnected("A","B"));
    assertFalse(g.isConnected("A","A"));
    assertFalse(g.isConnected("A",null));
  }
  
  @Test
  public void testAddWeightedEdge(){
    WeightedGraph g = new WeightedGraph();
    //test 1
    assertTrue(g.addWeightedEdge("A","B",10));
    assertTrue(g.isConnected("A","B"));
    assertFalse(g.addWeightedEdge("A","B",20));
    assertEquals(10,g.getEdge("A","B").getCost());
    assertEquals("A 10 B\nB",g.getStringWeightedGraph());
    //test many
    assertTrue(g.addWeightedEdge("C","A",20));
    assertTrue(g.addWeightedEdge("B","D",15));
    assertTrue(g.addWeightedEdge("A","C",5));
    assertEquals("A 5 C 10 B\nB 15 D\nC 20 A\nD",g.getStringWeightedGraph());
  }
  
  @Test
  public void testAddWeightedEdges(){
    WeightedGraph g = new WeightedGraph();
    //test 0
    assertFalse(g.addWeightedEdges("A",new String[]{},new int[]{}));
    assertEquals("A",g.getStringWeightedGraph());
    //test 1
    assertTrue(g.addWeightedEdges("A",new String[]{"E"},new int[]{10}));
    assertEquals("A 10 E\nE",g.getStringWeightedGraph());
    //test 1 again
    assertFalse(g.addWeightedEdges("A",new String[]{"E"},new int[]{30}));
    assertEquals("A 10 E\nE",g.getStringWeightedGraph());
    //test many
    assertTrue(g.addWeightedEdges("A",new String[]{"C","D","B"},new int[]{20,30,40}));
    assertEquals("A 10 E 20 C 30 D 40 B\nB\nC\nD\nE",g.getStringWeightedGraph());
  }
  
  @Test
  public void testReadWeighted() throws Exception{
    WeightedGraph g = new WeightedGraph();
    //test 0
    WeightedGraph graph = g.readWeighted("graph0.txt");
    assertTrue(graph.getStringWeightedGraph().equals("")); 
    
    //test 1
    graph = g.readWeighted("graph1.txt");
    assertTrue(graph.getStringGraph().equals("One"));
    
    //test many
    graph = g.readWeighted("wgraph.txt");
    assertTrue(graph.getStringWeightedGraph().equals("A 1 D 2 B\nB 3 D 10 E\nC 4 A 5 F\nD 2 C 2 E 4 G 8 F\nE 6 G\nF\nG 1 F"));
    
    //test one edge
    graph = g.readWeighted("wgraphOneEdge .txt");
    assertTrue(graph.getStringWeightedGraph().equals("Cin\nCle 10 Cin"));
  }
  
  @Test
  public void testShortestPath() throws Exception{
    WeightedGraph g = new WeightedGraph();
    WeightedGraph graph = g.readWeighted("wgraph.txt");
    //test 0
    assertEquals(new String[]{}, g.shortestPath("C","E"));
    //test 1
    assertEquals(new String[]{"C"}, graph.shortestPath("C","C"));
    assertEquals(new String[]{"C","F"}, graph.shortestPath("C","F"));
    //test many
    assertEquals(new String[]{"C","A","D","E"}, graph.shortestPath("C","E"));
  }
  
  @Test
  public void testSecondShortestPath1() throws Exception{
    Graph g = new Graph();
    Graph s = g.read("graph.txt");
    assertEquals(new String[]{"Cincinnati","Columbus","Cleveland","Pittsburgh","Buffalo"},s.secondShortestPath("Cincinnati","Buffalo"));
  }
  
  @Test
  public void testSecondShortestPath2() throws Exception{
    WeightedGraph g = new WeightedGraph();
    WeightedGraph s = g.readWeighted("wgraph.txt");
    assertEquals(new String[]{"C","A","B","D","E"},s.secondShortestPath("C","E"));
  }
  
  
}