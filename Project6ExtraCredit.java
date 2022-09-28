import java.util.*;
import java.io.*;
public class Project6ExtraCredit {
  public static void main(String args[])throws Exception{
    WeightedGraph map = new WeightedGraph();
    WeightedGraph northVn = map.readWeighted("ExtraCredit.txt");
    northVn.printWeightedGraph();
    System.out.println("Shortest path from PhuTho to QuangNinh: ");
    System.out.println(Arrays.toString(northVn.shortestPath("PhuTho","QuangNinh")));
    System.out.println(northVn.shortestDistance("PhuTho","QuangNinh"));
    System.out.println("Second shortest path from PhuTho to QuangNinh: ");
    System.out.println(Arrays.toString(northVn.secondShortestPath("PhuTho","QuangNinh")));
    System.out.println("If BacNinh cannot be visited in every way due to Covid 19 lockdown: ");
    northVn.removeNode("BacNinh");
    System.out.println("New shortest path from PhuTho to QuangNinh: " + Arrays.toString(northVn.shortestPath("PhuTho","QuangNinh")));
    System.out.println("New second shortest path from PhuTho to QuangNinh: " + Arrays.toString(northVn.secondShortestPath("PhuTho","QuangNinh")));
    System.out.println("Shortest path from LangSon to HaiDuong: " +  Arrays.toString(northVn.shortestPath("LangSon","HaiDuong")));
    System.out.println("Second shortest path from LangSon to HaiDuong: " +  Arrays.toString(northVn.secondShortestPath("LangSon","HaiDuong")));
  }
}