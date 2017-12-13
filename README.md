### Distance Vector Routing Simulator
# Deniz Toprak – 36235

# Mert Kýray - 29202

# COMP416 – Computer Networks Project #3 Report

In this project we were asked to implement a Distance Vector Routing Simulator.

We changed Node.java class in order to implement a functional Distance Vector Routing Simulator.
These are the methods we changed:

```
1) public Node()
2) public void notifyNeighbors()
3) public void updateDV(Packet p)
```
public Node():

We first initialized an array named “cost” which is cost of this node between other nodes. Non-neighbor
nodes have a cost of 999. Then we initialized myDV variable, which contains Distance Vectors to the
other nodes sourcing from the specific node. In the public Node() constructor myDV is initially set to the
costs we initialized earlier. We initialized a neighbor array that shows which nodes are neighboring.

Then for the part 2 we initialized bestPath array that will store the optimal nodes, the node should visit
in order to reach the destination node with the optimal route. Index = destination node, value = next
node. If the destination is the node itself or neighboring node then bestPath is its id initially, else a
randomly chosen neighbor node’s id is the bestPath, initially.

Public void notifyNeighbors():

This function briefly sends myDV array to all neighbor nodes, with the information of the source(the
node itself) and destination. We are using the Packets.java class to send this information. We first create
a new Packet object and send it to all neighbor nodes.

Structure of the packet object: Packet(int source, int destination, int[] dv)

public void updateDV(Packet p):

For each packet received from the neighbor, we check if the neighbor provides a cheaper path. If true,
we update myDV and the bestPath accordingly.

The algorithm to check if the neighbor provides a cheaper path is: current DV of i is min { current DV of i,
cost to neighbor + neighbor's DV to i } (given us in the project pdf).
