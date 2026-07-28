import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;

public class Simulator {
    private List<Route> mapRoutes;
    private List<Route> officeRoutes;
    private Solver solver;
    private boolean isOver;

    public Simulator() {
        this.mapRoutes = new ArrayList<>();
        this.officeRoutes = new ArrayList<>();
        this.solver = new Solver();
        this.isOver = false;
    }

    public void runSimulation(Scanner sc) {
        mapRoutes = Input.readMapFileLocation(sc); // gets csv inputs
        officeRoutes = Input.readOfficeFileLocation(sc);

        while(!isOver) {
            List<String> offices = new ArrayList<>();
            List<String> localDestinations = new ArrayList<>(); // filtered destinations
            Deque<String> pendingDestinations = new ArrayDeque<>(); // unfiltered destinations

            System.out.println("List of Post Offices:");

            for(Route r : officeRoutes) { // gets unique post offices
                String place1 = r.getPlace1();
                String place2 = r.getPlace2();

                if(!offices.contains(place1))
                    offices.add(place1);

                if(!offices.contains(place2))
                    offices.add(place2);
            }

            for(int i = 0; i < offices.size(); i++) // shows unique post offices to choose from
                System.out.println((i + 1) + " - " + offices.get(i));

            System.out.print("Select the number of the post office to start: ");
            int choice = Input.readIntInput(sc, 1, offices.size());

            Vertex startOffice = new Vertex(offices.get(choice - 1)); // gets starting post office

            do {
                System.out.print("Enter the amount of mails: ");
                int mailCount = Input.readIntInput(sc, 1, -1);

                List<String> destinations = new ArrayList<>();
                destinations = Input.readDestinations(sc, mapRoutes, mailCount, pendingDestinations);

                for(String s : destinations) // puts destinations in an unfiltered deque
                    pendingDestinations.add(s);

                String originCity = null;

                for(Route r : mapRoutes) { // gets origin city for filtering
                    if(originCity == null && r.getPlace1().equals(startOffice.name()))
                        originCity = r.getOrigin();
                }

                List<String> tempDestinations = new ArrayList<>(); // a list of rejected destinations for origin city
                boolean hasRoute;

                while(!pendingDestinations.isEmpty()) { // filtering process
                    hasRoute = false;
                    String s = pendingDestinations.poll();

                    for(Route r : mapRoutes) {
                        if(!hasRoute && r.getOrigin().equals(originCity)) {
                            if(s.equals(r.getPlace1()) || s.equals(r.getPlace2()))
                                hasRoute = true;
                        }
                    }

                    if(hasRoute)
                        localDestinations.add(s);
                    else tempDestinations.add(s);
                }

                for(String s : tempDestinations) // adds rejected back to unfiltered
                    pendingDestinations.add(s);

                System.out.println();
                Graph routeGraph = new Graph();

                for(Route r : mapRoutes) { // adds route to graph if it has filtered dest in place1 or 2
                    if(r.getOrigin().equals(originCity)) {
                        boolean isPlace1Valid = r.getPlace1().equals(startOffice.name());
                        boolean isPlace2Valid = r.getPlace2().equals(startOffice.name());

                        for(String s : localDestinations) {
                            if(s.equals(r.getPlace1()))
                                isPlace1Valid = true;

                            if(s.equals(r.getPlace2()))
                                isPlace2Valid = true;
                        }

                        if(isPlace1Valid && isPlace2Valid) {
                            Vertex place1 = new Vertex(r.getPlace1());
                            Vertex place2 = new Vertex(r.getPlace2());
                            routeGraph.addEdge(place1, place2, r.getDistance());
                        }
                    }
                }

                solver.findShortestCycle(routeGraph, startOffice); // runs solver for local deliveries
                List<Edge> deliveryRoute = new ArrayList<>();
                deliveryRoute = solver.getBestCycle();
                double deliveryDistance = solver.getBestCycleDistance();

                for(Edge e : deliveryRoute) // temp route display
                    System.out.printf("%s -> %s %.1f km%n",
                            e.getSource().name(),
                            e.getDestination().name(),
                            e.getWeight());

                System.out.println();
                System.out.printf("Total Distance Covered: %.1f km%n", deliveryDistance);

                List<String> nextOffices = new ArrayList<>(); // to get the next city

                for(String s : pendingDestinations) { // gets next possible post offices by origin city
                    for(Route r : mapRoutes) {
                        if(s.equals(r.getPlace2())) {
                            String toPostOffice = r.getOrigin() + " Post Office";

                            if(r.getPlace1().equals(toPostOffice)) {
                                if(!nextOffices.contains(r.getPlace1()))
                                    nextOffices.add(r.getPlace1());
                            }
                        }
                    }
                }

                Graph postOfficeGraph = new Graph();

                for(Route r : officeRoutes) { // adds post office to graph if it has filtered dest in place1 or 2
                    boolean isPlace1Valid = r.getPlace1().equals(originCity + " Post Office") ||
                            nextOffices.contains(r.getPlace1());
                    boolean isPlace2Valid = r.getPlace2().equals(originCity + " Post Office") ||
                            nextOffices.contains(r.getPlace2());

                    if(isPlace1Valid && isPlace2Valid) {
                        Vertex place1 = new Vertex(r.getPlace1());
                        Vertex place2 = new Vertex(r.getPlace2());
                        postOfficeGraph.addEdge(place1, place2, r.getDistance());
                    }
                }

                Vertex currOffice = new Vertex(originCity + " Post Office"); // runs solver for office travel
                solver.findShortestPath(postOfficeGraph, currOffice);

                if(!solver.getBestPath().isEmpty()) { // swaps post offices if next office is available
                    String nextOffice = solver.getBestPath().get(0).getDestination().name();
                    System.out.println("Next Post Office: " + nextOffice);
                    startOffice = new Vertex(nextOffice);
                }
                else System.out.println("No more post offices to explore...");

                System.out.println();
                localDestinations.clear(); // clears filtered destinations for next use

            } while(!pendingDestinations.isEmpty());

            System.out.print("Would you like to simulate again? (1 for Yes, 2 for No): ");
            int choice2 = Input.readIntInput(sc, 1, 2);

            if(choice2 == 2)
                isOver = true;
        }
    }
}
