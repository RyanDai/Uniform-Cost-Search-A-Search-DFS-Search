package assignment1;

import java.io.*;
import assignment1.AStarSearch;
import java.util.*;

public class UniformCostSearch {
	
	public static void startSearch(String mapFileName, String queryFileName, String outputFileName) throws IOException{
		BufferedReader mapInput = new BufferedReader(new FileReader(mapFileName));
		BufferedReader queryInput = new BufferedReader(new FileReader(queryFileName));
		String mapLine;
		String queryLine;
		int nodeNum;
		int queryNum;
		FileWriter output = new FileWriter(outputFileName);
        
		
		mapLine = mapInput.readLine();
		Scanner s = new Scanner(mapLine);
		nodeNum = s.nextInt();
		s.close();
		
		double[][] map = new double[nodeNum][nodeNum];
		
		for(int i = 0; i < nodeNum; i++){
			mapLine = mapInput.readLine();
			String[] lineContent = mapLine.split(" ");
			for(int j = 0; j < nodeNum; j++){
				map[i][j] = Double.parseDouble(lineContent[j]);
			}
		}
		
		queryLine = queryInput.readLine();
		Scanner s2 = new Scanner(queryLine);
		queryNum = s2.nextInt();
		
		for(int i = 0; i < queryNum; i++){
			queryLine = queryInput.readLine();
			String[] queryContent = queryLine.split(" ");
			if(queryContent[0].equals("Uniform")){
				int initialNode = Integer.parseInt(queryContent[1]);
				int goalNode = Integer.parseInt(queryContent[2]);
				uniformCostSearch(map, initialNode, goalNode, output);
			}
			if(queryContent[0].equals("A*")){
				int initialNode = Integer.parseInt(queryContent[1]);
				int goalNode = Integer.parseInt(queryContent[2]);
				AStarSearch.aStarSearch(map, initialNode, goalNode, output);
			}
			
		}
		output.close();
		s2.close();
		mapInput.close();
		queryInput.close();	
	}
	
	public static void uniformCostSearch(double[][] map, int start, int goal, FileWriter output) throws IOException{
		ArrayList<Node> nodes = new ArrayList<Node>();
		UniformCostSearch search = new UniformCostSearch();
		for(int i = 0; i < map.length; i++){
			String nodeName = i + 1 + "";
			Node node = search.new Node(nodeName);
			nodes.add(node);
		}
		for(int i = 0; i < nodes.size(); i++){
			for(int j = 0; j < nodes.size(); j++){
				if(map[i][j] != 0){	
					nodes.get(i).adjacencies.add(search.new Edge(nodes.get(j), map[i][j]));
				}	
			}
		}
		
		Node startNode = nodes.get(start - 1);
		Node goalNode = nodes.get(goal - 1);
		startNode.pathCost = 0;
		
		PriorityQueue<Node> queue = new PriorityQueue<Node>(20, 
	            new Comparator<Node>(){
	                //override compare method
	                public int compare(Node i, Node j){
	                    if(i.pathCost > j.pathCost){
	                        return 1;
	                    }
	                    else if (i.pathCost < j.pathCost){
	                        return -1;
	                    }
	                    else{
	                        return 0;
	                    }
	                }
	            }
	        );
		
		queue.add(startNode);
		Set<Node> explored = new HashSet<Node>();
		while(!queue.isEmpty()){
			Node current = queue.poll();
            explored.add(current);
            //reach the goal
            if(current.value.equals(goalNode.value)){   
                List<Node> path = printPath(goalNode);
                
                String ls = System.getProperty("line.separator");
                output.write(path.get(0).toString());
                for(int i = 1; i < path.size(); i++){
                	output.write("-");
                	output.write(path.get(i).toString());
                }
                output.write(ls);
                return;
            }
            if(current.adjacencies != null){         	
	            for(Edge e: current.adjacencies){	            	
	                Node child = e.target;
	                double cost = e.cost;      	
	        
	                if(!explored.contains(child) && !queue.contains(child)){
	                	child.pathCost = current.pathCost + cost;
	                    child.parent = current;
	                    queue.add(child);
	                }
	                else if((queue.contains(child))&&(child.pathCost>current.pathCost + cost)){
	                	queue.remove(child);
	                	child.pathCost = current.pathCost + cost;
	                    child.parent=current;
	                    queue.add(child);
	                }
	            }          	
            }       
		}
	}
	
		
	public static List<Node> printPath(Node target){
        List<Node> path = new ArrayList<Node>();
        for(Node node = target; node!=null; node = node.parent){
            path.add(node);
        }
        Collections.reverse(path);
        return path;
    }
	
	
	class  Node{

	    public final String value;
	    public double pathCost;
	    public ArrayList<Edge> adjacencies;
	    public Node parent;

	    public  Node(String val){
	        value = val;
	        adjacencies = new ArrayList<Edge>();
	    }

	    public String toString(){
	        return value;
	    }
	    
	    public void addEdge(Edge e){
	    	adjacencies.add(e);
	    }

	}

	class Edge{
	    public final double cost;
	    public final Node target;

	    public Edge(Node targetNode, double map){
	        cost = map;
	        target = targetNode;

	    }
	    
	    public String toString(){
	        return cost + "";
	    }
	}

}
