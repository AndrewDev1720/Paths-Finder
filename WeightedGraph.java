import java.util.*;
import java.io.*;
/** 
 * A class to represent the different weighted and directed graph
 * @author <em>Andrew Tran</em>
 */
public class WeightedGraph extends Graph{
  
  /**
   * Add a edge from a starting node to the ending node based on their id and cost
   * @param from the starting node, to the ending node
   * @return true if an edge is newly added to a vertex
   */
  public boolean addWeightedEdge(String from, String to, int weight){
    this.addNode(from);
    this.addNode(to);
    if(!isConnected(from,to) && !this.getVertex(from).getId().equals(to)){
      Edge newEdge = new Edge();
      newEdge.setEndNode(to);
      newEdge.setCost(weight);
      this.getVertex(from).getEdgesList().add(newEdge);
      Collections.sort(this.getVertex(from).getEdgesList());
      return true;
    }
    return false;
  }
  
  /**
   * Add a series of edges to a starting node with the respecting cost
   * @param from the starting node, to array store the id of all ending nodes
   * @return true if all edges is newly added to a vertex
   */
  public boolean addWeightedEdges(String from, String[] toList, int[] weightList){
    boolean result = true;
    this.addNode(from);
    if(toList.length == 0)
      result = false;
    for (int i = 0; i < toList.length; i++){
      if(!this.addWeightedEdge(from,toList[i],weightList[i]))
        result = false;
    }
    return result;
  }
  
  /**
   * Print the graph that shows all vertices, edges, and their respected costs
   */
  public void printWeightedGraph(){
    System.out.println(getStringWeightedGraph().toString());
  }
  /**
   * Helper method to get the String representation of the graph that shows all vertices, edges, and their respected costs
   * Return String representation of the graph
   */
  protected String getStringWeightedGraph(){
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
            s.append(endNodeItr.getCost() + " " + endNodeItr.getEndNode() + " ");
          else
            s.append(endNodeItr.getCost() + " " + endNodeItr.getEndNode());
        }
        if(i != list.size() -1)
          s.append("\n");
    }
    return s.toString();
  }
  
    /**
   * Construct a  weighted graph from a text file
   * @param fileName refer to the name of the file 
   * @return the graph constructed from that file
   */
  public WeightedGraph readWeighted(String fileName) throws Exception{
    WeightedGraph readGraph = new WeightedGraph();
    FileReader fr = new FileReader(fileName);
    BufferedReader br = new BufferedReader(fr);
    String st;
    while((st = br.readLine()) != null){
      String[] arrOfStr = st.replaceAll("\\p{Punct}", "").trim().replaceAll(" +", " ").split(" ");
      String[] arrOfNodes = st.replaceAll("\\p{Digit}", "").trim().replaceAll(" +", " ").split(" ");
      arrOfNodes = Arrays.copyOfRange(arrOfNodes,1,arrOfNodes.length);
      String[] arrOfWeight = st.replaceAll("\\p{Alpha}", "").trim().replaceAll(" +", " ").split(" ");
      if(arrOfWeight[0].equals(""))
        arrOfWeight = new String[0];
      int[] weightArr = new int[arrOfWeight.length];
      for(int i = 0; i < arrOfWeight.length; i++)
        weightArr[i] = Integer.parseInt(arrOfWeight[i]);
      readGraph.addWeightedEdges(arrOfStr[0], arrOfNodes, weightArr);
    }
    return readGraph;
  }
  
  /**
   *Helper method where dijkstra algorithm is actually implementing to find the shortest path from a root 
   *@param root the root vertex 
   */
  private void dijkstra(String root){
    PriorityQueue<Vertex> queue = new PriorityQueue<Vertex>();
    Vertex rootVertex = this.getVertex(root);
    rootVertex.cost = 0;
    queue.add(rootVertex);
     while(!queue.isEmpty()){ // Similar to BFS, but adding comparing the cost after each queue removal
       Vertex v = queue.peek();
       queue.remove().encountered = true;
      ListIterator<Edge> edgeItr = v.getEdgesList().listIterator();
        while(edgeItr.hasNext()){
          Edge curEdge = edgeItr.next();
          String nextRoot = curEdge.endNode;
          Vertex nextNode = getVerticesList().get(table.get(nextRoot));
          if(!nextNode.encountered){
            if(isConnected(v.getId(),nextRoot) && (nextNode.parent == null || curEdge.cost + v.cost < nextNode.cost)){
              nextNode.parent = v.getId();
              nextNode.cost = curEdge.cost + this.getVertex(nextNode.parent).cost;
          }
            queue.add(nextNode);
          }
        }
     }  
  }
  
  /**
   * Find the shortest path based on the total cost for that path using dijkstra algorithm
   * @param from the starting node, to the destination node
   * @return the String array that contain the shortest path
   */
  public String[] shortestPath(String from, String to){
    if(table.get(from) == null || table.get(to) == null)
      return new String[]{};
    dijkstra(from);
    ArrayList<String> path = new ArrayList<String>();
    String parent = to;
    while(parent != null){
      path.add(parent);
      parent = this.getVertex(parent).parent;
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
  
    protected int shortestDistance(String from, String to){
      dijkstra(from);
      int distance = this.getVertex(to).cost;
      this.resetAll();
      return distance;
    }
    
    
    @Override
    public String[] secondShortestPath(String root, String to){
      if(table.get(root) == null || table.get(to) == null)
        return new String[]{};
      int min = this.shortestDistance(root,to);
      int nextMin = Integer.MAX_VALUE;
      Vertex second = null;
      LinkedList<Vertex> queue = new LinkedList<Vertex>();
      LinkedList<Vertex> queue2 = new LinkedList<Vertex>(); // two queue so that one can remove until empty and then switch to other queue
      LinkedList<Vertex> list = new LinkedList<Vertex>();
      queue.add(this.getVertex(root));
      this.getVertex(root).value = 1;
      while(!queue.isEmpty()){ // similar to BFS
        for(Vertex node : queue){
          ListIterator<Edge> edgeItr = node.getEdgesList().listIterator();
          while(edgeItr.hasNext()){
            Edge curEdge = edgeItr.next();
            String nextRoot = curEdge.endNode;
            Vertex temp = new Vertex(nextRoot);
            Vertex nextNode = getVertex(nextRoot);
            temp.prev= node;
            if(nextRoot.equals(to))
              list.add(temp);
            temp.cost = temp.prev.cost + curEdge.cost;
            temp.value = temp.prev.value + 1;
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
        if(nextMin == Integer.MAX_VALUE) // condition if the second shortest path cannot be found
          return new String[]{};
        String[] secondPath = new String[second.value];
        for(int i = second.value - 1; i >= 0; i--){
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
  public static void main(String args[]) throws Exception{
    WeightedGraph wg = new WeightedGraph();
    System.out.println("This is the demonstration for class WeightedGraph");
    System.out.println("When the graph is empty: ");
    System.out.println("Result of calling addWeightedEdge(A,B,2): " + wg.addWeightedEdge("A","B",2));
    System.out.println("Result of calling addWeightedEdge(A,D,1): " + wg.addWeightedEdge("A","D",1));
    System.out.println("The graph after adding the two first weighted edges above: ");
    wg.printWeightedGraph();
    System.out.println("Result of calling addWeightedEdges(C,[A,F],[4,5]): " + wg.addWeightedEdges("C",new String[]{"A","F"},new int[]{4,5}));
    System.out.println("The graph after adding weighted edges above: ");
    wg.printWeightedGraph();
    System.out.println("Result of calling addWeightedEdges(D,[C,E,F,G],[2,2,8,4]): " + wg.addWeightedEdges("D",new String[]{"C","E","F","G"},new int[]{2,2,8,4}));
    System.out.println("Result of calling addWeightedEdges(E,[G],[6]): " + wg.addWeightedEdges("E",new String[]{"G"},new int[]{6}));
    System.out.println("Result of calling addWeightedEdges(G,[F],[1]): " + wg.addWeightedEdges("G",new String[]{"F"},new int[]{1}));
    System.out.println("Result of calling addWeightedEdges(B,[D,E],[3,10]): " + wg.addWeightedEdges("B",new String[]{"D","E"},new int[]{3,10}));
    System.out.println("The graph after adding weighted edges above: ");
    wg.printWeightedGraph();
    System.out.println("\nCreat the same graph using readWeighted() and then printGraph() method (graph from project prompt): ");
    WeightedGraph read = wg.readWeighted("wgraph.txt");
    read.printWeightedGraph();
    System.out.println("Shortest Path from C to E: " + Arrays.toString(read.shortestPath("C","E")));
    System.out.println("Second shortest Path from C to E: " + Arrays.toString(read.secondShortestPath("C","E")));
  }
  
}