import java.util.Random;

public class Node
{
    private static int count = 0;

    // Each node will store a costs vector, its own distance vector
    // and a distance vector for each of its neighbors
    private int[] neighbors;
    private int[] cost = new int[DVSimulator.NUMNODES];
    private int[] myDV = new int[DVSimulator.NUMNODES];
    private int[][] neighborDV = new int[DVSimulator.NUMNODES][DVSimulator.NUMNODES];
    private int id;
    // fwd table specifies for reaching each destination (index) from current node
    // which neighbor node we should visit first
    private int[] fwdTable = new int[DVSimulator.NUMNODES];
    // bestPath is a temporary changing forwarding table
    private int[] bestPath = new int[DVSimulator.NUMNODES];
    private int numUpdates = 0;

    public Node() {
        this.id = count++;
        for (int i = 0; i<DVSimulator.NUMNODES; i++) {
            // reading from the DVSimulator variables,
            // for each node:
            // 1. initialize its cost and myDV value
            // 2. specify the neighbors
            // 3. Initialize the forwarding table (bestPath variable)

            // BestPath to any node should be initialized as follows:
            // If node has id = this node's id, use id
            // Else if node is a direct neighbor, use the neighbor id
            // Otherwise, choose a random neighbor (see randomNeighbor method)

            // WRITE YOUR CODE HERE
        	//Initialize cost of this node to i
        	this.cost[i] = DVSimulator.cost[id][i];
        	//Initialize myDV.
        	//Because the node does not have any other information about other nodes initially, myDV equals cost.
        	this.myDV = this.cost;
//        	System.out.println("myDV to "+i+ " is "+this.myDV[i]);
//        	System.out.println("Costs are: "+i+ " = "+this.cost[i]);
        }
        //Initialize neighbors from DVSimulator
        this.neighbors = DVSimulator.neighbors[id];
//        for(int a=0; a<this.neighbors.length; a++){
//        	System.out.println("Neighbors are: "+ this.neighbors[a]);
//        }
        //Initialization of bestPath. Index = destination node, value = next node
        for(int i = 0; i<DVSimulator.NUMNODES; i++){
        	
        	if(this.getId() == i){
        		//destination node is  itself
        		bestPath[i]=this.getId();        		
        	}else{
        		//if destination node is a neighbor of the node
        		boolean isNeighbor = false;
        		for(int a : this.neighbors){
        			if(a==i){
        				isNeighbor = true;
        			}
        		}
        		if(isNeighbor){
        			//destination node is a neighbor of the node
        			bestPath[i] = i;
        		}else{
        			//destination node is not a neighbor of the node, so a random node is selected
        			bestPath[i] = randomNeighbor();
        		}
        		
        	}
        }
//        for(int i=0; i<this.bestPath.length; i++){
//        	System.out.println("Best path to "+i+" is "+ this.bestPath[i]);
//        }
        
        

        // send initial DV to neighbors
        notifyNeighbors();
    }

    public int getId() {
        return id;
    }


    public void printDV() {
        System.out.print("i            " );
        for (int i = 0; i<DVSimulator.NUMNODES; i++) {
            System.out.print(i + "      ");
        }
        System.out.println();
        System.out.print("cost         " );
        for (int i = 0; i<DVSimulator.NUMNODES; i++) {
            System.out.print(myDV[i] + "      ");
        }
        System.out.println();
    }

    public void printFwdTable() {
        System.out.println("dest         next Node" );
        for (int i = 0; i<DVSimulator.NUMNODES; i++) {
            System.out.println(i + "            " + fwdTable[i]);
        }
    }

    public int randomNeighbor() {
        int rnd = new Random().nextInt(neighbors.length);
        return neighbors[rnd];
    }

    public void notifyNeighbors() {
        // for each neighbor, create a new packet (see Packet class)
        // with current node id as source, neighbor id as destination
        // and current node's DV as the dv
        // then send packet using helper method sendPacket in DVSimulator

        // WRITE YOUR CODE HERE
    	//Send myDV to all of neighbors of the node
    	for(int a : this.neighbors){
    		Packet packet = new Packet(this.getId(), a, this.myDV);
    		DVSimulator.sendPacket(packet);
    	}
    	
    	
    }

    public void updateDV(Packet p) {
        // this method is called by the simulator each time a packet is received from a neighbor
        int neighbor_id = p.getSource();
        neighborDV[neighbor_id] = p.getDV();

        // for each value in the DV received from neighbor, see if it provides a cheaper path to
        // the corresponding node. If it does, update myDV and bestPath accordingly
        // current DV of i is min { current DV of i, cost to neighbor + neighbor's DV to i  }

        // If you do any changes to myDV:
        // 1. Notify neighbors about the new myDV using notifyNeighbors() method
        // 2. Increment the convergence measure numUpdates variable once

        // WRITE YOUR CODE HERE
        for(int nextNode=0; nextNode<this.neighborDV[neighbor_id].length; nextNode++){
        	int cost = this.neighborDV[neighbor_id][nextNode];
        	
        	if(this.myDV[nextNode]>this.cost[neighbor_id]+ this.neighborDV[neighbor_id][nextNode]){
        		//A smaller cost found in neighborDV between the node and nextNode
        		//Update myDV
        		this.myDV[nextNode] = this.cost[neighbor_id]+ this.neighborDV[neighbor_id][nextNode];
        		//Update bestPath
        		this.bestPath[nextNode] = neighbor_id;
        		
//        		System.out.println("Neighbor id: "+neighbor_id + " changed: "+ this.getId() + " to "+nextNode);
        		
        		//Notify neighbors about the change in myDV
        		notifyNeighbors();
        		//Increment update time
        		this.numUpdates++;
        		

        	}
        }
        
        
    }

    public void buildFwdTable() {
        // just copy the final values of bestPath vector
        for (int i = 0; i < DVSimulator.NUMNODES; i++) {
            fwdTable[i] = bestPath[i];
        }
    }

    public int getNumUpdates() {
        return numUpdates;
    }
}
