
package student;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.Edge;
import models.Node;
import student.Paths3.SFdata;

/** This class contains a modified version of a pseudo BFS and some other methods. */
public class Paths3 {

    /** Return a path from start to end using the idea of a BFS, or the empty list if a path
     * does not exist.
     * Note: The empty list is NOT "null"; it is a list with 0 elements. */
	public static List<Node> modifiedPseudoBfs(Node start, Node end) {
		/* Search the first frontier and store the nodes into a ArrayList named "old"
		 * Then search the frontier of the nodes already in "old" and 
		 * store them in ArrayList "current". Update ArrayList "old" as ArrayList "current"
		 * Repeat steps until path is found
		 * A horizontal path is allowed in this version of the pseudo BFS
		 */
		
		ArrayList<Node> old = new ArrayList<Node>();
		ArrayList<Node> current = new ArrayList<Node>();
		ArrayList<Node> visited = new ArrayList<Node>(); //store a list of visited nodes, multiple visits are allowed. 
		HashMap<Double,SFdata> solution = new HashMap<Double,SFdata>();
		ArrayList<Node> templist = new ArrayList<Node>();
		double temphostile=0;
		double counter=0;
		double maxvisit=0;
		double oldcounter=-1;

		old.add(start);
		visited.add(start);
		ArrayList<Node> first = new ArrayList<Node>();
		first.add(start);
		solution.put(counter, new SFdata(first,0));
			
		while(old!=null){	
			if(visited.size()==maxvisit){
				break;
			}
			if(visited.size()>maxvisit){
				maxvisit=visited.size();
			}	
			for(Node n:old){
				oldcounter=oldcounter+1;
				for(Edge e:n.getExits()){					
					Node w = e.getOther(n);
					if(solution.get(oldcounter).path.contains(w)==false){
						if(w==end && w.isHostile()==false && solution.get(oldcounter).hostile<2){
								solution.get(oldcounter).path.add(w);
								return solution.get(oldcounter).path;
						}
						current.add(w);
						visited.add(w);					
						templist=(ArrayList<Node>) solution.get(oldcounter).path.clone();
						templist.add(w);
						temphostile = solution.get(oldcounter).hostile;
						
						if(w.isHostile()){
							temphostile=temphostile+1;
						}
						counter=counter+1;
						solution.put(counter, new SFdata(templist,temphostile));						
					}
				}										
			}
		old.clear();
		old=(ArrayList<Node>) current.clone();
		current.clear();
		
		}
    	return new LinkedList<Node>();
    }
    

    /** An instance contains information about a node: the previous node
     *  on a shortest path from the start node to this node and the distance
     *  of this node from the start node. */
    public static class SFdata {
        private ArrayList<Node> path; // distance from start node to this one
        private double hostile;

        /** Constructor: an instance with distance d from the start node and
         *  backpointer p.*/
        private SFdata(ArrayList<Node> p, double h) {
            path= p;  // Backpointer on the path (null if start node)
            hostile=h;
        }
        
        /** return a representation of this instance. */
        public String toString() {
            return "path " + path.toString();
        }
    }
}
