package assignment1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class AStarSearch {
	
	public static void aStarSearch(double[][] map, int start, int goal, FileWriter output) throws IOException{
		int NodeNum = map.length;
		
		ArrayList<Node> nodes = new ArrayList<Node>();
		AStarSearch search = new AStarSearch();
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
		
		for(Node node : nodes){
			node.heuristic = get_heuristic(map, nodes, node, goalNode);
		}
		
		
		startNode.pathCost = 0;
		//startNode.heuristic = get_heuristic(map, nodes, startNode, goalNode);
		//System.out.println("result:" + startNode.heuristic);
		startNode.fValue = get_fValue(startNode);
		
		
		PriorityQueue<Node> queue = new PriorityQueue<Node>(20, 
	            new Comparator<Node>(){
	                //override compare method
	                public int compare(Node i, Node j){
	                    if(i.fValue > j.fValue){
	                        return 1;
	                    }
	                    else if (i.fValue < j.fValue){
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
	                	child.fValue = get_fValue(child);
	                    child.parent = current;
	                    queue.add(child);
	                }
	                else if((queue.contains(child))&&(child.pathCost>current.pathCost + cost)){
	                	queue.remove(child);
	                	child.pathCost = current.pathCost + cost;
	                	child.fValue = get_fValue(child);
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
	
	
	public static double get_heuristic(double[][] map, ArrayList<Node> nodes, Node startNode, Node goalNode){
		HashMap<Node, Boolean> visited = new HashMap<Node, Boolean>();
		HashMap<Node, Node> child = new HashMap<Node, Node>();
		
		List<Node> path = new LinkedList<Node>();
		Queue<Node> queue = new LinkedList<Node>();
		Node current = startNode;
		queue.add(current);
		visited.put(current, true);
		
		while(!queue.isEmpty()){
			current = queue.poll();
			if(current.equals(goalNode)){
				break;
			} else {
				for(Edge e: current.adjacencies){
					Node childNode = e.target;
					if(!visited.containsKey(childNode)){
						queue.add(childNode);
						visited.put(childNode, true);
						child.put(childNode, current);
					}
				}
			}
		}
		for(Node node = goalNode; node != null; node = child.get(node)){
			path.add(node);
		}
		int pathSize = path.size() - 1;
		
		double shortestLength = 999999999;
		for(int i = 0; i < map.length; i++){
			for(int j = 0; j < map[i].length; j++){
				if(map[i][j] != 0){
					if(map[i][j] < shortestLength){
						shortestLength = map[i][j];
					}
				}
			}
		}
		//System.out.println("length:" + shortestLength);
		
		return pathSize * shortestLength;
	}
	
	public static double get_fValue(Node node){
		return node.pathCost + node.heuristic;
	}
	
	
	
	
	
	
	class  Node{

	    public final String value;
	    public double pathCost;
	    public double heuristic;
	    double fValue;
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
