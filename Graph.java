import java.util.*;
import java.io.*;
/** 
 * A class to represent the different basic unweighted un directed graph
 * @author <em>Andrew Tran</em>
 */
public class Graph{
  
  protected ArrayList<Vertex> verticesList; //  the list store all the vertices
  protected int numVertices; // track the current adding index of the list
  protected Hashtable<String,Integer> table; // store the index of each vertex for the list
  protected int realSize; // the real number of vertices
  
   /** Creates an a graph with essential fields */
  public Graph(){
    this.table = new Hashtable<String,Integer>(19);
    this.verticesList = new ArrayList<Vertex>(10);
  }
  
  /** 
 * A class to represent the vertex of the graph
 */
  protected class Vertex implements Comparator<Vertex>, Comparable<Vertex>{
     protected String id; // the name/id representation of the vertex
     protected LinkedList<Edge> edgesList; // adjacency list
     protected boolean encountered; // visiting status
     protected boolean done;
     protected String parent; // The id of parent vertex for traversal
     protected Vertex prev;
     protected int cost = 0; // initial cost
     protected int value = 0;
     
     protected Vertex(){
       this.edgesList = new LinkedList<Edge>();
     }
     
     protected Vertex(String id){
       this.id = id;
       this.edgesList = new LinkedList<Edge>();
     }
     /** 
   * Return the id/name of the vertex
   * @return id/name of the vertex
   */
     protected String getId(){
       return this.id;
     }
     
     /** 
      * Return edges in a list of a vertex
      * @return edges in a list of a vertex
      */
     protected LinkedList<Edge> getEdgesList(){
       return this.edgesList;
     }
     
     /** 
      * Return the result for comparision
      * @return the result for comparision
      */
     @Override
     public int compare(Vertex v1, Vertex v2) {
       return v1.getId().compareTo(((Vertex)v2).getId());
     }
     
     /** 
      * Return the result for comparision
      * @return the result for comparision
      */
     @Override
     public int compareTo(Vertex v){
      return this.cost - v.cost;
    }
  }
  
  /** 
 * A class to represent the Edge between each vertex of the graph
 */
  protected class Edge implements Comparator<Edge>, Comparable<Edge>{
    protected String endNode; // the endNode id of the edge
    protected int cost; // the cost to traverse through this edge
    
    /** 
      * Return the endNode id of this edge
      * @return the endNode id of this edge
      */
    protected String getEndNode(){
      return this.endNode;
    }
    
     /** 
      * Set the node id according to the input value
      */
    protected void setEndNode(String endNode){
      this.endNode = endNode;
    }
    /** 
      * Return the cost to traverse through this edge
      * @return the the cost to traverse through this edge
      */
    protected int getCost(){
      return this.cost;
    }
    
    /** 
     * Set the cost value according to the input value
     */
    protected void setCost(int cost){
      this.cost = cost;
    }
    
    /** 
      * Return the result for comparision alphabetically
      * @return the result for comparision alphabetically
      */
    @Override
    public int compare(Edge e1, Edge e2) {
      return e1.getEndNode().compareTo(e2.getEndNode());
    }
    
    /** 
      * Return the result for comparision for the cost
      * @return the result for comparision for the cost
      */
    @Override
    public int compareTo(Edge e){
      return this.getCost() - e.getCost();
    }
  }
  /**
   * Helper method to get the list that contain all vertices of the graph
   * @return the list including all the vertices of the graph
   */
  protected ArrayList<Vertex> getVerticesList(){
    return this.verticesList;
  }
  
  /**
   * Helper method to determine whether a vertex is directly connected to the other vertex
   * @param from the original node, to the destination node
   * @return the boolean result of connected or not
   */
  protected boolean isConnected(String from, String to){
    if(from == null || to == null || table.get(from) == null || table.get(to) == null)
      return false;
    ListIterator<Edge> edgesListItr = this.getVertex(from).getEdgesList().listIterator();
    while(edgesListItr.hasNext()){
      if(edgesListItr.next().getEndNode().equals(to))
        return true;
    }
    return false;
  }
  
  /**
   * Helper method to reset some fields to the original value that are neccessary for traversal and path finding
   */
  protected void resetAll(){
    for(Vertex v : getVerticesList()){
      if(v != null){
        v.encountered = false;
        v.cost = 0;
        v.parent = null;
      }
    }
  }
  
  
  /**
   * Helper method to get the vertex address from its id
   * @param vertex the id/name of the vertex
   * @return the address of the vertex having the input id
   */
  protected Vertex getVertex(String vertex){
    return this.getVerticesList().get(table.get(vertex));
  }
  
  /**
   * Helper method to get the edge address from the starting id to the detination id vertices
   * @param from the id of the starting vertex to the id/name of the destination vertex
   * @return the address of the edge from the starting vetex to the ending vertex
   */
  protected Edge getEdge(String from,String to){
    Edge connectedEdge = null;
    if(table.get(from) == null || table.get(to) == null)
      return null;
    ListIterator<Edge> edgesListItr = this.getVertex(from).getEdgesList().listIterator();
    while(edgesListItr.hasNext()){
      connectedEdge = edgesListItr.next();
      if(connectedEdge.getEndNode().equals(to))
        return connectedEdge;
    }
    return connectedEdge;
  }
  
  
  /**
   * Add a new node to the graph
   * @param name the id for the adding vertex
   * @return true if a new node is successfully added
   */
  public boolean addNode(String name){
    if(name == null)
      return false;
    if(table.get(name) == null){
      table.put(name,numVertices);
      getVerticesList().add(new Vertex(name));
      numVertices++;
      realSize++;
      return true;
    }
    return false;
  }
  
  /**
   * Add a series of nodes to the graph
   * @param names store the the id for the adding vertex
   * @return true if all elements of the input array is newly added
   */
  public boolean addNodes(String[] names){
    boolean result = true;
    for(String name : names){
      if(!this.addNode(name))
        result = false;
    }
    return result;
  }
  
  /**
   * Add a edge from a starting node to the ending node based on their id
   * @param from the starting node, to the ending node
   * @return true if an edge is newly added to a vertex
   */
  public boolean addEdge(String from, String to){
    this.addNode(from);
    this.addNode(to);
    boolean found = false;
    Vertex fromNode = this.getVertex(from);
    Vertex toNode = this.getVertex(to);
    if(!isConnected(from,to) && !fromNode.getId().equals(to)){
      Edge newEdge = new Edge();
      newEdge.setEndNode(to);
      fromNode.getEdgesList().add(newEdge);
      Collections.sort(fromNode.getEdgesList() , new Edge());
      Edge newEdge2 = new Edge();
      newEdge2.setEndNode(from);
      toNode.getEdgesList().add(newEdge2);
      Collections.sort(toNode.getEdgesList(), new Edge());
      return true;
    }
    return false;
  }
  
  
  /**
   * Add a series of edges to a starting node
   * @param from the starting node, to array store the id of all ending nodes
   * @return true if all edges is newly added to a vertex
   */
  public boolean addEdges(String from, String[] to){
    boolean success = true;
    this.addNode(from);
    if(to.length == 0)
      return false;
    for(String destination : to){
      if(!this.addEdge(from,destination))
        success = false;
    }
    return success;
  }
  
  
  /**
   * Remove a node based on its input id
   * @param name refer to the id of the removing vertex
   * @return true if that input vertex is succesfully removed
   */
  public boolean removeNode(String name){
    if(table.get(name) == null)
      return false;
    ListIterator<Edge> edgesListItr = getVertex(name).getEdgesList().listIterator();
    while(edgesListItr.hasNext()){
      Edge neighbor = edgesListItr.next();
      Vertex v = getVertex(neighbor.getEndNode());
      ListIterator<Edge> temp = v.getEdgesList().listIterator();
      while(!temp.next().getEndNode().equals(name));
      temp.remove();
    }
    getVerticesList().set(table.get(name),null);
    table.remove(name);
    realSize--;
    return true;
  }
  
  /**
   * Remove a series of nodes
   * @param name refer to the array that store all the ids of the removing vertices
   * @return true if that all vertices in the array is succesfully removed
   */
  public boolean removeNodes(String[] name){
    boolean success = true;
    if(name.length == 0)
      return false;
    for(String id : name){
      if(!this.removeNode(id))
        success = false;
    }
    return success;
  }
  
  /**
   * Construct a graph from a text file
   * @param fileName refer to the name of the file 
   * @return the graph constructed from that file
   */
  public Graph read(String fileName) throws Exception{
    Graph readGraph = new Graph();
    FileReader fr = new FileReader(fileName);
    BufferedReader br = new BufferedReader(fr);
    String st;
    while((st = br.readLine()) != null){
      String[] arrOfStr = st.replaceAll("\\p{Punct}", "").trim().split(" ");
      readGraph.addEdges(arrOfStr[0], Arrays.copyOfRange(arrOfStr,1,arrOfStr.length));
    }
    return readGraph;
  }
  
   /**
   * Print the graph that shows all vertices and edges
   */
  public void printGraph(){
    System.out.println(getStringGraph());
  }
  
  /**
   *Get the string representation of the graph displaying all vertices and edges
   *@return the String representation of the graph
   */
  protected String getStringGraph(){
    StringBuilder s = new StringBuilder();
    ArrayList<Vertex> list = new ArrayList<Vertex>();
    for(Vertex node: getVerticesList()){
      if(node != null)
        list.add(node);
    }
    Collections.sort(list, new Vertex());
    for(int i = 0; i < list.size() ; i++){
      Vertex node = list.get(i);
        s.append(node.getId());
        ListIterator<Edge> edgesListItr = node.getEdgesList().listIterator();
        if(edgesListItr.hasNext())
          s.append(" ");
        while(edgesListItr.hasNext()){
          Edge endNodeItr = edgesListItr.next();
          if(edgesListItr.hasNext())
            s.append(endNodeItr.getEndNode() + " ");
          else
            s.append(endNodeItr.getEndNode());
        }
        if(i != list.size() -1)
          s.append("\n");
    }
    return s.toString();
  }
  
  /**
   * Finding the path from a vertex to the other vertex using Depth First Search algorithm
   * @param from the root vertex, to the destination vetex, neighborOrder traversing order
   * @return array that contain the path 
   */
  public String[] DFS(String from, String to, String neighborOrder){
    if(table.get(to) == null || table.get(from) == null)
      return new String[]{};
    dfTrav(from,null,neighborOrder);
    ArrayList<String> path = new ArrayList<String>();
    String parent = to;
    while(parent != null){
      path.add(parent);
      parent = this.getVerticesList().get(table.get(parent)).parent;
    }
    String[] result = new String[path.size()];
    int index = 0;
    for(int i = path.size() - 1; i >= 0; i--)
      result[index++] = path.get(i);
    this.resetAll();
    if(!result[0].equals(from))
      return new String[]{};
    return result;
  }
  
  /**
   *Helper method where DFS is actually happening
   *@param root the root vertex, parent to trace parent, neighborOrder traversing order
   */
  protected void dfTrav(String root, String parent, String neighborOrder) { 
    Vertex rootVertex =  this.getVerticesList().get(table.get(root));
    rootVertex.encountered = true;
    rootVertex.parent = parent;
      ListIterator<Edge> edgeItr = rootVertex.getEdgesList().listIterator();
      if(neighborOrder.equals("reverse")){
        while(edgeItr.hasNext())
          edgeItr.next();
        while (edgeItr.hasPrevious()) { // iterate backward for reverse alphabetical
          Edge curEdge = edgeItr.previous();
          String nextRoot = curEdge.endNode;
          if (getVerticesList().get(table.get(nextRoot)).encountered == false)
            dfTrav(nextRoot,rootVertex.getId(),neighborOrder); // recursion the whole process again, searching for the furthest possible from an edge
        }
      }
      else{
        while (edgeItr.hasNext()) { // try all the edges until all vertices are marked encountered
          Edge curEdge = edgeItr.next();
          String nextRoot = curEdge.endNode;
          if (getVerticesList().get(table.get(nextRoot)).encountered == false)
            dfTrav(nextRoot,rootVertex.getId(),neighborOrder);
        }
      }
  }
  
    /**
   * Finding the path from a vertex to the other vertex using Breadth First Search algorithm
   * @param from the root vertex, to the destination vetex, neighborOrder traversing order
   * @return array that contain the path 
   */
  public String[] BFS(String from, String to, String neighborOrder){
    if(table.get(to) == null || table.get(from) == null)
      return new String[]{};
    bfTrav(from,neighborOrder);
    ArrayList<String> path = new ArrayList<String>();
    String parent = to;
    while(parent != null){ 
      path.add(parent); 
      parent = this.getVerticesList().get(table.get(parent)).parent;
    }
    String[] result = new String[path.size()];
    int index = 0;
    for(int i = path.size() - 1; i >= 0; i--)
      result[index++] = path.get(i);
    this.resetAll();
    if(!result[0].equals(from))
      return new String[]{};
    return result;
  }
  
  /**
   *Helper method where BFS is actually happening
   *@param root the root vertex, parent to trace parent, neighborOrder traversing order
   */
  protected void bfTrav(String root, String order){
    LinkedList<String> queue = new LinkedList<String>();
    queue.add(root);
    while(!queue.isEmpty()){ // Traverese through each level of the spanning tree until the queue is empty
      String v = queue.remove();
      Vertex node = this.getVertex(v);
      node.encountered = true;
      ListIterator<Edge> edgeItr = node.getEdgesList().listIterator();
      if(order.equals("reverse")){
        while(edgeItr.hasNext())
          edgeItr.next();
        while(edgeItr.hasPrevious()){ // iterate backward for reverse alphabetical order
          Edge curEdge = edgeItr.previous();
          String nextRoot = curEdge.endNode;
          Vertex nextNode = getVerticesList().get(table.get(nextRoot));
          if (!nextNode.encountered){
            nextNode.encountered = true; //marked as encountered after finished adding all it edges to the queue
            nextNode.parent = v; // set parent after reaching a new level
            queue.add(nextRoot);
          }
        }
      }
      else{
        while(edgeItr.hasNext()){
          Edge curEdge = edgeItr.next();
          String nextRoot = curEdge.endNode;
          Vertex nextNode = getVerticesList().get(table.get(nextRoot));
          if (!nextNode.encountered){
            nextNode.encountered = true;
            nextNode.parent = v;
            queue.add(nextRoot);
          }
        }
      }
    }
  }
  
  /**
   * Finding the second shortest path path from a vertex to the other vertex using Depth First Search algorithm
   * @param from the root vertex, to the destination vetex
   * @return array that contain the path 
   */
  public String[] secondShortestPath(String root, String to){
    if(table.get(root) == null || table.get(to) == null)
        return new String[]{};
    int min = this.BFS(root,to,"alphabetical").length;
      int nextMin = Integer.MAX_VALUE;
      Vertex second = null;
      LinkedList<Vertex> queue = new LinkedList<Vertex>();
      LinkedList<Vertex> queue2 = new LinkedList<Vertex>();
      LinkedList<Vertex> list = new LinkedList<Vertex>();
      queue.add(this.getVertex(root));
      this.getVertex(root).cost = 1;
      this.getVertex(root).encountered = true;
      while(!queue.isEmpty()){ // Very similar to BFS but will discover all the edges before set parents
        for(Vertex node : queue){
          ListIterator<Edge> edgeItr = node.getEdgesList().listIterator();
          while(edgeItr.hasNext()){
            String nextRoot = edgeItr.next().endNode;
            Vertex temp = new Vertex(nextRoot);
            Vertex nextNode = getVertex(nextRoot); // creat the new node that will store the data for each path
            temp.prev= node;
            if(nextRoot.equals(to))
              list.add(temp);
            temp.cost = temp.prev.cost + 1;
            if (!nextNode.encountered){
              temp.edgesList = nextNode.getEdgesList();
              queue2.add(temp);
            }
          }
        }
        while(!queue.isEmpty())
          this.getVertex(queue.remove().getId()).encountered = true;
        LinkedList<Vertex> temp = queue;
        queue = queue2;
        queue2 = temp;
      }
    for(Vertex v : list){
      if(v.cost > min && v.cost < nextMin){
        nextMin = v.cost;
        second = v;
      }
    }
    if(nextMin == Integer.MAX_VALUE) // if the second shortest path cannot be found
      return new String[]{};
    String[] secondPath = new String[nextMin];
    for(int i = nextMin - 1; i >= 0; i--){
      secondPath[i] = second.getId();
      second = second.prev;
    }
    this.resetAll();
    return secondPath;
  }
  
  
  /** 
   * The main method portrays the function of all methods in NumLinkedList class
   * @param args command line arguments of the main method
   */
  public static void main (String args[]) throws Exception{
    Graph graph = new Graph();
    System.out.println("This is the demonstration for class Graph");
    System.out.print("When the graph is empty: ");
    graph.printGraph();
    System.out.println("Result of adding node A using addNode(): " + graph.addNode("A"));
    System.out.println("Result of adding nodes D, C, B using addNodes(): " + graph.addNodes(new String[]{"D","C","B"}));
    System.out.println("Result of adding nodes D, E, F using addNodes(): " + graph.addNodes(new String[]{"D","E","F"}) + " because node D is already inside the graph");
    System.out.println("Result of the graph now with only vertices and no edges: ");
    graph.printGraph();
    System.out.println("Result of removing node E using removeNode(): " + graph.removeNode("E"));
    System.out.println("Result of removing nodes F, E, D using removeNodes(): " + graph.removeNodes(new String[]{"F","B","D"}));
    System.out.println("Remaining nodes of the graph using printGraph(): ");
    graph.printGraph();
    System.out.println("Result of removing nodes A, B, D using removeNodes(): " + graph.removeNodes(new String[]{"A","B","D"}) + " because node B and D are not in the graph, but not A is still removed");
    System.out.println("Remaining nodes of the graph using printGraph(): ");
    graph.printGraph();
    System.out.println("Result of adding a new edge to node C using addEdge(): " + graph.addEdge("C","A"));
    System.out.println("Result of adding a edges D, B to node A using addEdges(): " + graph.addEdges("A",new String[]{"B","D"}));
    System.out.println("Result of adding a edges C, A to node B using addEdges(): " + graph.addEdges("B",new String[]{"C","A"}) + " as node B is already connected with node A, but edge C is still be added to node B");
    System.out.println("The resulting graph using printGraph(): ");
    graph.printGraph();
    System.out.println("Creat the same graph using read() and then printGraph() method: ");
    Graph read = graph.read("graphMany.txt");
    read.printGraph();
    System.out.println("Using DFS() alphabetically to find path from C to D: " + Arrays.toString(graph.DFS("C","D","alphabetically")));
    System.out.println("Using DFS() reverse alphabetically to find path from C to D: " + Arrays.toString(graph.DFS("C","D","reverse")));
    System.out.println("Using BFS() alphabetically to find path from C to D: " + Arrays.toString(graph.BFS("C","D","alphabetically")));
    System.out.println("Using BFS() reverse alphabetically to find path from C to D: " + Arrays.toString(graph.BFS("C","D","reverse")) + " as BFS will find the shortest path");
    System.out.println("Demonstration for secondShortestPath(): ");
    read = graph.read("graph.txt");
    System.out.println("The graph from lecture : ");
    read.printGraph();
    System.out.println("Shortest path from Cincinnati to Buffalo by using BFS: ");
    System.out.println(Arrays.toString(read.BFS("Cincinnati","Buffalo","reverse")));
    System.out.println("Second Shortest path from Cincinnati to Buffalo by calling secondShortestPath(): ");
    System.out.println(Arrays.toString(read.secondShortestPath("Cincinnati","Buffalo")));
  }
  
  
  
}