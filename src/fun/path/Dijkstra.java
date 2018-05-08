package fun.path;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Dijkstra {
	
	class Connection {
		Node toNode;
		int distance;
		
		Connection(Node to, int distance) {
			this.toNode = to;
			this.distance = distance;
		}
	}
	
	class Node implements Comparable<Node> {
		String name;
		Node previous;
		int realDistance;

		List<Connection> connections = new ArrayList<Connection>();
		
		Node(String name) {
			this.name = name;
			this.realDistance = Integer.MAX_VALUE;
		}

		@Override
		public int compareTo(Node other) {
			return Integer.compare(realDistance, other.realDistance);
		}
	}
	
	Node a,b,c,d,e;
	
	public void setupGraph() {
		a = new Node("A");
		b = new Node("B");
		c = new Node("C");
		d = new Node("D");
		e = new Node("E");
		
		a.connections.add(new Connection(d, 2));
		a.connections.add(new Connection(c, 3));
		a.connections.add(new Connection(b, 1));
		
		b.connections.add(new Connection(c, 1));
		b.connections.add(new Connection(a, 1));
		
		c.connections.add(new Connection(b, 1));
		c.connections.add(new Connection(a, 3));
		c.connections.add(new Connection(d, 2));
		c.connections.add(new Connection(e, 8));
		
		d.connections.add(new Connection(a, 2));
		d.connections.add(new Connection(c, 2));
		d.connections.add(new Connection(e, 8));
		
		e.connections.add(new Connection(d, 8));
		e.connections.add(new Connection(c, 8));
	}

	public void solveGraph(Node start, Node end) {
		PriorityQueue<Node> nodesQueue = new PriorityQueue<Node>();
		
		start.realDistance = 0;
		nodesQueue.add(start);
		
		Node n = null;
		
		while(!nodesQueue.isEmpty()) {
			if (start == end) {
				System.out.println("You are here !");
				break;
			}
			
			n = nodesQueue.poll();
			for (Connection c : n.connections) {
				Node actualNode = c.toNode;
								
				if (actualNode.realDistance >= n.realDistance + c.distance) {
					actualNode.realDistance = n.realDistance + c.distance;
					actualNode.previous = n;
					nodesQueue.add(actualNode);
				}
			}
		}
		
		this.showPath(end);
	}
	
	public void showPath(Node n) {
		System.out.println(n.name);
		if (n.previous != null) showPath(n.previous);
	}
	
	public void run() {
		this.solveGraph(a, e);
	}
}
