
package student;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.Edge;
import models.Node;
import student.Paths.SFdata;

/** This class contains Dijkstra's shortest-path algorithm and some other methods. */
public class Paths {

    /** Return the shortest path from start to end, or the empty list if a path
     * does not exist.
     * Note: The empty list is NOT "null"; it is a list with 0 elements. */
	public static List<Node> shortestPath(Node start, Node end) {
		
    	HashMap<Node,SFdata> S = new HashMap<Node,SFdata>();
    	//The HashMap S stores where the path program has visited. 
    	//The first entry of an element in HashMap stores the Node, or a city on a map
    	//The second entry of an element in HashMap stores the which node the shortestPath
    	//method has traveled one step before the node we are currently interested in. 
    	Heap<Node> F = new Heap<Node>();
    	//F is a heap as described in the abstract version of the algorithm in the lecture notes
    	
    	F.add(start, 0);
    	SFdata wgt = new SFdata(0, null,1,0);
    	S.put(start, wgt);
    	
    	while (F.size>0){
     		
    		Node f = F.poll();
    		
    		if(f==end){
    			
    			if(S.get(f).time>99999999){
    				return new LinkedList<Node>();
    			}
    			
    			return constructPath(end,S);
    		}
    		for(Edge e:f.getExits()){    			
    			Node w = e.getOther(f);
    			double time=S.get(f).time+(double)(e.length/S.get(f).speed);// specify time for the specific node
    			int hostilenumber=0;
    			double currentspeed = 0;
				if(w.isHostile()==false && w.hasSpeedUpgrade()==false){
					hostilenumber=S.get(f).hostile;
					currentspeed=S.get(f).speed;
				}
				if(w.isHostile()==false && w.hasSpeedUpgrade()==true){
					hostilenumber=S.get(f).hostile;
					currentspeed=S.get(f).speed+0.2;
				}
				if(w.isHostile()==true && w.hasSpeedUpgrade()==false){
					hostilenumber=S.get(f).hostile+1;
					if(S.get(f).speed>=1.2){
						currentspeed=S.get(f).speed-0.2;
						
					}
					currentspeed=S.get(f).speed;
				}
				if(w.isHostile()==true && w.hasSpeedUpgrade()==true){
					hostilenumber=S.get(f).hostile+1;
					currentspeed = S.get(f).speed;
				}
    			if(!S.containsKey(w)){    				
    				if(hostilenumber> 1){ 
    					F.add(w,999999999);
    					S.put(w, new SFdata(999999999,f,currentspeed,hostilenumber)); 
    				}	    				
    				else{
    					F.add(w,time);
    					S.put(w, new SFdata(time,f,currentspeed,hostilenumber));
    				}    				
    			}    			
    			else if(time<S.get(w).time){
    				if(S.get(w).hostile>= 1){ //error is thought to be
    					S.get(w).time=999999999;
        				S.get(w).backPointer=f; 
        				S.get(w).speed=currentspeed;
        				S.get(w).hostile=hostilenumber;
        				F.updatePriority(w, 999999999);
    				} 
    				else{
	    				S.get(w).time=time;
	    				S.get(w).backPointer=f; 
	    				S.get(w).speed=currentspeed;
	    				S.get(w).hostile=hostilenumber;
	    				F.updatePriority(w, time);
    				}
    			}
    		}	
    	}
    	return new LinkedList<Node>();
    }


    /** Return the path from the start node to node end.
     *  Precondition: nData contains all the necessary information about
     *  the path. */
    public static List<Node> constructPath(Node end, HashMap<Node, SFdata> nData) {
        LinkedList<Node> path= new LinkedList<Node>();
        Node p= end;
        // invariant: All the nodes from p's successor to the end are in
        //            path, in reverse order.
        while (p != null) {
            path.addFirst(p);
            p= nData.get(p).backPointer;
        }
        return path;
    }

    /** Return the sum of the weights of the edges on path path. */
    public static int pathDistance(List<Node> path) {
        if (path.size() == 0) return 0;
        synchronized(path) {
            Iterator<Node> iter= path.iterator();
            Node p= iter.next();  // First node on path
            int s= 0;
            // invariant: s = sum of weights of edges from start to p
            while (iter.hasNext()) {
                Node q= iter.next();
                s= s + p.getConnect(q).length;
                p= q;
            }
            return s;
        }
    }

    /** An instance contains information about a node: the previous node
     *  on a shortest path from the start node to this node and the time 
     *  taken from start to this node and the speed of the spaceship and 
     *  number of hostile planets
     */
    public static class SFdata {
        private Node backPointer; // backpointer on path from start node to this one
        private double time; // distance from start node to this one
        private double speed; // speed of the spaceship
        private int hostile; // number of hostile planet visited

        /** Constructor: an instance with distance d from the start node and
         *  backpointer p.*/
        private SFdata(double t, Node p, double s, int h) {
            time= t;     // Distance from start node to this one.
            backPointer= p;  // Backpointer on the path (null if start node)
            speed=s; // speed of the spaceship
            hostile=h; // speed of the spaceship
        }

        /** return a representation of this instance. */
        public String toString() {
            return "time " + time + ", bckptr " + backPointer;
        }
    }
}
