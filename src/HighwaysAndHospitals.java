import java.util.ArrayList;
import java.util.Stack;

/**
 * Highways & Hospitals
 * A puzzle created by Zach Blick
 * for Adventures in Algorithms
 * at Menlo School in Atherton, CA
 *
 * Completed by: Lily Kassaei
 *
 */

public class HighwaysAndHospitals {

    /**
     * TODO: Complete this function, cost(), to return the minimum cost to provide
     *  hospital access for all citizens in Menlo County.
     */
    public static long cost(int n, int hospitalCost, int highwayCost, int cities[][]) {
        // Main Question: How many clusters do we have (because we only need one hospital per cluster)
        // Use DFS to count chunks on each layer

        // Edge case: Hospitals are less than highways so just give every city a hospital
        if (hospitalCost <= highwayCost) {
            return (long) n * hospitalCost;
        }

        // Create a graph for quick lookups during DFS
        ArrayList<Integer>[] graph = new ArrayList[n + 1];

        // Initialize each ArrayList in graph, representing all possible connections at graph[city]
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }

        // Populate graph by putting each connection in the graph at the index that corresponds with the city
        for (int[] edge : cities) {
            int cityOne = edge[0];
            int cityTwo = edge[1];
            graph[cityOne].add(cityTwo);
            graph[cityTwo].add(cityOne);
        }

        // Create array that marks what has been visited
        boolean[] visited = new boolean[n + 1];

        // Create count for clusters of cities (AKA where the connections stop)
        int numClusters = 0;

        // Go through each city, performing DFS on unvisited ones
        for (int i = 1; i <= n; i++) {
            // If a city is not visited, it has no before seen connections so it is a new cluster
            // Perform DFS with this city to mark all of its connections in the new cluster
            if (!visited[i]) {
                performDFS(i, visited, graph);
                numClusters++;
            }
        }
        // Find the cost of all hospitals by giving each cluster a hospital
        long finalHospitalCost = (long) numClusters * hospitalCost;
        // Find the cost of all highways by subtracting hospitals from total cities and giving highways to the rest
        long finalHighwayCost = (long) (n - numClusters) * highwayCost;
        // Add up costs and return total
        return finalHospitalCost + finalHighwayCost;
    }

    // Performs Depth-First-Search to find all connections between cities
    public static void performDFS(int start, boolean visited[], ArrayList<Integer>[] graph) {
        // Create stack and explore our starting value
        Stack<Integer> stack = new Stack<>();
        stack.push(start);
        visited[start] = true;

        // While we still have connections to search
        while(!stack.isEmpty()) {
            // Find the next connection
            int current = stack.pop();
            // Find the connections of the connections and mark those as visited
            for (int neighbor: graph[current]) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    // Eventually find the connections of the new connection
                    stack.push(neighbor);
                }
            }
        }
    }
}
