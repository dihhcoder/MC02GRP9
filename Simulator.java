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
        findPath(g, start, start, visited, currentRoute, 0.0);
    }

    public void findPath(Graph g, Vertex start, Vertex current, Set<Vertex> visited,
                         List<Edge> currentRoute, double currentDistance) {
        if(visited.size() == g.getVertexCount()) {

            for(Edge e : g.getNeighbors(current)) {
                if(e.getDestination().equals(start)) {
                    double totalDistance = currentDistance + e.getWeight();

                    if(totalDistance < shortestDistance) {
                        shortestDistance = totalDistance;
                        bestRoute = new ArrayList<>(currentRoute);//bestRoute = (List<Edge>) currentRoute.clone();
                        bestRoute.add(e);
                    }
                }
            }
        }
        else {
            for(Edge e : g.getNeighbors(current)) {
                Vertex next = e.getDestination();

                if(!visited.contains(next)) {
                    double totalDistance = currentDistance + e.getWeight();

                    visited.add(next);
                    currentRoute.add(e);

                    findPath(g, start, next, visited, currentRoute, totalDistance);

                    currentRoute.remove(currentRoute.size() - 1);
                    visited.remove(next);
                }
            }
        }
    }
}
