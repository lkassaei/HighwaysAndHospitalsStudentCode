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

        // Edge case: Hospitals are less than highways so just give every city a hospital
        if (hospitalCost <= highwayCost) {
            return (long) n * hospitalCost;
        }

        // Each index equals its associated city. Each index saves its father root.
        // The father root saves the number of cities below it (order) in its negative value for differentiation.
        int[] roots = new int[n + 1];

        // Each city starts as its own root with a size of 1 (represented by -1)
        for (int i = 0; i < n; i++) {
            roots[i] = -1;
        }

        // At first, the cities are all disconnected, so the clusters equals the number of cities
        int numClusters = n;

        // Go through each possible connection
        for (int[] edge : cities) {
            // Find the ultimate roots of each city
            int ultimateParent1 = findUltimateParent(roots, edge[0]);
            int ultimateParent2 = findUltimateParent(roots, edge[1]);

            // If the cities are not yet connected
            if (ultimateParent1 != ultimateParent2) {
                // Find the orders of the two clusters by changing it from its negative value to positive
                int order1 = roots[ultimateParent1] * -1;
                int order2 = roots[ultimateParent2] * -1;

                // If the first order is greater, compress the second tree into the first
                if (order1 >= order2) {
                    // Update the new parent root
                    roots[ultimateParent2] = ultimateParent1;
                    // Update the new size of the new parent root by changing the orders of the two trees to negative
                    roots[ultimateParent1] = -1 * (order1 + order2);
                }
                // If the second order is greater, compress the first tree into the second
                else {
                    // Update the new parent root
                    roots[ultimateParent1] = ultimateParent2;
                    // Update the new size of the new parent root by changing the orders of the two trees to negative
                    roots[ultimateParent2] = -1 * (order1 + order2);
                }
                // A new connection has been made, meaning there is one less cluster
                numClusters-=1;
            }
        }

        // Find the cost of all hospitals by giving each cluster a hospital
        // Find the cost of all highways by subtracting hospitals from total cities and giving highways to the rest
        // Add up costs and return total
        return (long) numClusters * hospitalCost + (long) (n - numClusters) * highwayCost;
    }

    // Find the ultimate parent of a given node by tracing up parent by parent until we find a negative value
    // If a value is negative, we know it is a parent root because it is storing its order
    public static int findUltimateParent(int[] roots, int node) {
        // While we haven't found the ultimate parent
        while (roots[node] > 0) {
            // Reassign to its parent
            node = roots[node];
        }

        // Path compression: make all nodes on the path point directly to the root
        int current = node;
        // While we haven't assigned yet to the ultimate parent
        while (roots[current] > 0) {
            // Go up
            int next = roots[current];
            roots[current] = node;
            current = next;
        }
        // Return
        return node;
    }
}
