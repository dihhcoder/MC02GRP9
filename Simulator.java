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

            System.out.print("Select the number of the city to start: ");
            int choice = Input.readIntInput(sc, 1, offices.size());

            Vertex startingOffice = new Vertex(offices.get(choice - 1)); // gets starting post office

            do {
                System.out.print("Enter the amount of mails: ");
                int mailCount = Input.readIntInput(sc, 1, -1);

                List<String> destinations = new ArrayList<>();
                destinations = Input.readDestinations(sc, mapRoutes, mailCount);

                for(String s : destinations) // puts destinations in an unfiltered deque
                    pendingDestinations.add(s);

                String originCity = null;

                for(Route r : mapRoutes) { // gets origin city for filtering
                    if(originCity == null && r.getPlace1().equals(startingOffice.name()))
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

                System.out.println("Local Destinations: " + localDestinations); // temp destinations for display
                System.out.println("Pending Destinations: " + pendingDestinations);
                System.out.println();

                Graph routeGraph = new Graph();

                for(Route r : mapRoutes) { // adds route to graph if it has filtered dest in place1 or 2
                    if(r.getOrigin().equals(originCity)) {
                        boolean isPlace1Valid = r.getPlace1().equals(startingOffice.name());
                        boolean isPlace2Valid = r.getPlace2().equals(startingOffice.name());

                        for(String s : localDestinations) {
                            if (s.equals(r.getPlace1()))
                                isPlace1Valid = true;

                            if (s.equals(r.getPlace2()))
                                isPlace2Valid = true;
                        }

                        if (isPlace1Valid && isPlace2Valid) {
                            routeGraph.addEdge(new Vertex(r.getPlace1()),
                                    new Vertex(r.getPlace2()), r.getDistance());
                            routeGraph.addEdge(new Vertex(r.getPlace2()),
                                    new Vertex(r.getPlace1()), r.getDistance());
                        }
                    }
                }

                solver.findShortestCycle(routeGraph, startingOffice); // runs solver for local deliveries
                List<Edge> deliveryRoute = new ArrayList<>();
                deliveryRoute = solver.getBestCycle();
                double deliveryDistance = solver.getBestCycleDistance();

                for(Edge e : deliveryRoute) // temp route display
                    System.out.println(e.getSource().name() + " -> " +
                            e.getDestination().name() + " " +
                            e.getWeight() + " km");

                System.out.println();
                System.out.printf("Total Distance Covered: %.1f km%n", deliveryDistance);

                List<String> nextPostOffices = new ArrayList<>(); // to get the next city

                for(String s : pendingDestinations) { // gets next possible post offices by origin city
                    for(Route r : mapRoutes) {
                        if(s.equals(r.getPlace2())) {
                            String toPostOffice = r.getOrigin() + " Post Office";

                            if(r.getPlace1().equals(toPostOffice)) {
                                if(!nextPostOffices.contains(r.getPlace1()))
                                    nextPostOffices.add(r.getPlace1());
                            }
                        }
                    }
                }

                Graph postOfficeGraph = new Graph();

                for(Route r : officeRoutes) { // adds post office to graph if it has filtered dest in place1 or 2
                    boolean isPlace1Valid = r.getPlace1().equals(originCity + " Post Office") ||
                            nextPostOffices.contains(r.getPlace1());
                    boolean isPlace2Valid = r.getPlace2().equals(originCity + " Post Office") ||
                            nextPostOffices.contains(r.getPlace2());

                    if(isPlace1Valid && isPlace2Valid) {
                        Vertex place1 = new Vertex(r.getPlace1());
                        Vertex place2 = new Vertex(r.getPlace2());
                        postOfficeGraph.addEdge(place1, place2, r.getDistance());
                        postOfficeGraph.addEdge(place2, place1, r.getDistance());
                    }
                }

                Vertex currentOffice = new Vertex(originCity + " Post Office"); // runs solver for office travel
                solver.findShortestPath(postOfficeGraph, currentOffice);

                if(!solver.getBestPath().isEmpty()) { // swaps cities if next city is available
                    String nextCity = solver.getBestPath().get(0).getDestination().name();
                    String justCity = nextCity.replaceAll("(?i)\\sPost\\sOffice", "").trim();
                    System.out.println("Next City: " + justCity);
                    startingOffice = new Vertex(nextCity);
                }
                else System.out.println("No more cities to explore...");

                System.out.println();
                localDestinations.clear(); // clears filtered for next use

            } while(!pendingDestinations.isEmpty());

            System.out.print("Would you like to simulate again? (1 for Yes, 2 for No): ");
            int choice2 = Input.readIntInput(sc, 1, 2);

            if(choice2 == 2)
                isOver = true;
        }
    }
}
