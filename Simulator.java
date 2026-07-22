import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Simulator {
    private List<Edge> bestRoute;
    private double shortestDistance;
    //other attributes

    public Simulator() {
        this.bestRoute = new ArrayList<>();
        this.shortestDistance = Double.POSITIVE_INFINITY;
        //other attribute instantiations
    }

    //other getters and setters

    public List<Edge> getBestRoute() {
        return this.bestRoute;
    }

    public double getShortestDistance() {
        return this.shortestDistance;
    }

    //other methods

    public void findShortestPath(Graph g, Vertex start) {
        bestRoute.clear();
        shortestDistance = Double.POSITIVE_INFINITY;

        Set<Vertex> visited = new HashSet<>();
        visited.add(start);

        List<Edge> currentRoute = new ArrayList<>();
        findPath(g, start, visited, currentRoute);
    }

    public void findPath(Graph g, Vertex current, Set<Vertex> visited, List<Edge> currentRoute) {
        if(visited.size() == g.getVertexCount()) {
            double totalDistance = 0.0;

            for(Edge e : currentRoute)
                totalDistance += e.getWeight();

            if(totalDistance < shortestDistance) {
                shortestDistance = totalDistance;
                bestRoute = new ArrayList<>(currentRoute);//bestRoute = (List<Edge>) currentRoute.clone();
            }
        }
        else {
            for(Edge e : g.getNeighbors(current)) {
                Vertex next = e.getDestination();

                if(!visited.contains(next)) {
                    visited.add(next);
                    currentRoute.add(e);

                    findPath(g, next, visited, currentRoute);

                    currentRoute.remove(currentRoute.size() - 1);
                    visited.remove(next);
                }
            }
        }
    }
}
