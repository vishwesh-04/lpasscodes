import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
//		RingNode node1 = new RingNode(1);
//      RingNode node2 = new RingNode(2);
//      RingNode node3 = new RingNode(3);
//      RingNode node4 = new RingNode(4);
//      
//      // Connect the nodes in a circular fashion (ring)
//      node1.setNextNode(node2);
//      node2.setNextNode(node3);
//      node3.setNextNode(node4);
//      node4.setNextNode(node1); // Closing the loop
//
//      // Start the election from node 1
//      node1.startElection();
      
      BullyNode node1 = new BullyNode(1, new ArrayList<>());
    BullyNode node2 = new BullyNode(2, new ArrayList<>());
    BullyNode node3 = new BullyNode(3, new ArrayList<>());
    BullyNode node4 = new BullyNode(4, new ArrayList<>());

    // Add nodes to each other's lists
    node1.nodes.add(node2);
    node1.nodes.add(node3);
    node1.nodes.add(node4);
    
    node2.nodes.add(node1);
    node2.nodes.add(node3);
    node2.nodes.add(node4);
    
    node3.nodes.add(node1);
    node3.nodes.add(node2);
    node3.nodes.add(node4);
    
    node4.nodes.add(node1);
    node4.nodes.add(node2);
    node4.nodes.add(node3);

    // Start the election from node 1
    node2.startElection();

	}

}

class RingNode {
    int id;
    RingNode nextNode;
    
    // Constructor
    public RingNode(int id) {
        this.id = id;
        this.nextNode = null;
    }

    // Starts the election
    public void startElection() {
        System.out.println("Node " + id + " starts an election.");
        sendMessage(id);
    }

    // Sends a message with its ID to the next node
    private void sendMessage(int msg) {
        if (msg < this.id) {
            System.out.println("Node " + this.id + " forwards message: " + msg);
            this.nextNode.sendMessage(msg);
        } else if (msg > this.id) {
            System.out.println("Node " + this.id + " replaces message with its ID: " + this.id);
            this.nextNode.sendMessage(this.id);
        } else {
            declareLeader();
        }
    }

    // Declares the node as the leader once the message comes back with the largest ID
    private void declareLeader() {
        System.out.println("Node " + id + " is the leader.");
    }

    // Set the next node in the ring
    public void setNextNode(RingNode nextNode) {
        this.nextNode = nextNode;
    }

}


class BullyNode {
    int id;
    boolean isLeader;
    List<BullyNode> nodes;

    // Constructor
    public BullyNode(int id, List<BullyNode> nodes) {
        this.id = id;
        this.nodes = nodes;
        this.isLeader = false;
    }

    // Starts the election process
    public void startElection() {
        System.out.println("Node " + id + " starts an election.");
        boolean hasHigherNode = false;

        // Inform higher ID nodes of the election
        for (BullyNode node : nodes) {
            if (node.id > this.id) {
                node.respondToElection(this);
                hasHigherNode = true;
            }
        }

        // If no higher node exists, this node becomes the leader
        if (!hasHigherNode) {
            declareLeader();
        }
    }

    // Responds to an election message (higher ID nodes respond)
    public void respondToElection(BullyNode caller) {
        System.out.println("Node " + id + " with higher ID responds to election from Node " + caller.id);
        // The higher node starts its own election process
        startElection();
    }

    // Declares this node as the leader
    public void declareLeader() {
        this.isLeader = true;
        System.out.println("Node " + id + " is the leader.");
    }

    // Optional: For debugging purposes, you can add this method to check the current state of nodes.
    public void printStatus() {
        System.out.println("Node " + id + " is " + (isLeader ? "the leader." : "not the leader."));
    }
}