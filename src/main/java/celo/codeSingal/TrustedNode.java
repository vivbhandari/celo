package celo.codeSingal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class TrustedNode {

	public static boolean IsTrusted(int node, int[][] trustGraph,
			int[] pretrustedPeers, int trustThreshold) {
		HashMap<Integer, ShortestPath> paths = new HashMap<Integer, ShortestPath>();
		paths.put(node, new ShortestPath(0, node));
		HashSet<Integer> preTrustedSet = new HashSet<Integer>();
		for (int i : pretrustedPeers)
			preTrustedSet.add(i);

		List<Integer> toBeProcessed = new ArrayList<Integer>();
		toBeProcessed.add(node);

		while (!toBeProcessed.isEmpty()) {
			Integer currentNode = toBeProcessed.remove(0);

			for (int neighbor = 0; neighbor < trustGraph.length; neighbor++) {
				if (neighbor == currentNode
						|| trustGraph[currentNode][neighbor] != 0) {
					paths.putIfAbsent(neighbor,
							new ShortestPath(Integer.MAX_VALUE, -1));
					ShortestPath path = paths.get(neighbor);
					int newDistance = paths.get(currentNode).distance
							+ trustGraph[currentNode][neighbor];
					if (newDistance < path.distance) {
						path.distance = newDistance;
						path.fromNode = currentNode;
						toBeProcessed.add(neighbor);
					}
					if (preTrustedSet.contains(neighbor)
							&& paths.get(neighbor).distance < trustThreshold)
						return true;
				}
			}
		}
		return false;
	}

	public static void main(String[] args) {
		System.out.println(
				IsTrusted(0, new int[][] { { 0 } }, new int[] { 0 }, 1));

		System.out.println(
				IsTrusted(0, new int[][] { { 0, 1 }, {1, 0} }, new int[] { 1 }, 1));

		System.out.println(
				IsTrusted(0, new int[][] { { 0, 1, 0 }, {1, 0, 2}, {0, 2, 0 } }, new int[] { 2 }, 4));
	}
}

class ShortestPath {
	int distance;
	int fromNode;

	public ShortestPath(int distance, int fromNode) {
		this.distance = distance;
		this.fromNode = fromNode;
	}
}