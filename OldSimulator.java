import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OldSimulator {
    public static void run(){
        List<Route> office = new ArrayList<>();
        List<Route> map = new ArrayList<>(); 
        Scanner keyboard = new Scanner(System.in);
        int i, mailInput = -1;
        boolean first, check, isPlace1Valid, isPlace2Valid, over = false;
        Solver solver = new Solver();
        Graph g;
        Vertex start;
        List<String> filtered = new ArrayList<>();
        ArrayDeque<String> unfiltered = new ArrayDeque<>();
        List<String> dest, rejected, post;
        String origin;
        List<Edge> toMail;
        double maildist;
        
        //gets csv input
        office = Input.readOfficeFileLocation(keyboard);
        map = Input.readMapFileLocation(keyboard);
        //starts the loop
        do{
            //gets the starting post office
            //start = new Vertex(Input.readStartingOffice(keyboard, office));
            start = new Vertex("Manila City Post Office"); //TEMP
            System.out.println("Starting office is manila post office for now");
            first = true;

            //gets destinations then puts them in an unfiltered deque
            while(first || !unfiltered.isEmpty()){
                mailInput = getMailCount(keyboard);
                dest = Input.readDestinations(keyboard, map, mailInput);
                for(String s: dest){
                    unfiltered.add(s);
                }
                dest.clear();

                
                first = false;
                origin = null;

                //gets origin for filtering
                for(i = 0; i < map.size() && origin == null; i++){
                    if(map.get(i).getPlace1().equalsIgnoreCase(start.name())){
                        origin = map.get(i).getOrigin();
                    }
                }

                //makes a list for rejected destinations for current origin
                rejected = new ArrayList<>();

                //filtering process
                while(!unfiltered.isEmpty()){
                    check = false;
                    String s = unfiltered.poll();
                    for(i = 0; i < map.size() && !check; i++){
                        Route r = map.get(i);
                        if(r.getOrigin().equalsIgnoreCase(origin)){
                            if(s.equalsIgnoreCase(r.getPlace1()) || s.equalsIgnoreCase(r.getPlace2())){
                                check = true;
                            }
                        }
                    }
                    if (check){
                        filtered.add(s);
                    } else {
                        rejected.add(s);
                    }
                }
                
                //adds rejected back to unfiltered
                for(String s: rejected){
                    unfiltered.add(s);
                }
                //clears rejected list
                rejected.clear();
                //just to see what is filtered and unfiltered
                System.out.println(filtered);
                System.out.println(unfiltered);

                
                g = new Graph();
                
                //adds route to graph if route has filtered dest in either the place1 or place2 on nonlinear map
                for (i = 0; i < map.size(); i++) {
                    Route r = map.get(i);
                    if (r.getOrigin().equalsIgnoreCase(origin)) {
                        isPlace1Valid = r.getPlace1().equalsIgnoreCase(start.name());
                        for (String f : filtered) {
                            if (f.equalsIgnoreCase(r.getPlace1())) {
                                isPlace1Valid = true;
                            }
                        }
                        isPlace2Valid = r.getPlace2().equalsIgnoreCase(start.name());
                        for (String f : filtered) {
                            if (f.equalsIgnoreCase(r.getPlace2())) {
                                isPlace2Valid = true;
                            }
                        }
                        if (isPlace1Valid && isPlace2Valid) {
                            g.addEdge(new Vertex(r.getPlace1()), new Vertex(r.getPlace2()), r.getDistance());
                            g.addEdge(new Vertex(r.getPlace2()), new Vertex(r.getPlace1()), r.getDistance());
                        }
                    }
                }

                //runs the solver
                solver.findShortestCycle(g, start);
                toMail = solver.getBestCycle();
                maildist = solver.getBestCycleDistance();

                //display
                for(i = 0; i < toMail.size(); i++){
                    System.out.println(toMail.get(i).getSource().name());
                    System.out.println(toMail.get(i).getDestination().name());
                }
                System.out.println(maildist);

                //to get the next city
                post = new ArrayList<>();
                //gets next possible post office by origin
                for(String s: unfiltered){
                    for(i = 0; i < map.size(); i++){
                        Route r = map.get(i);
                        if(s.equalsIgnoreCase(r.getPlace2())){
                            String toPostOffice = r.getOrigin() + " Post Office";
                            if(r.getPlace1().equalsIgnoreCase(toPostOffice)){
                                if(!post.contains(r.getPlace1())){
                                    post.add(r.getPlace1());
                                }   
                            }
                        }
                    }
                }
                g = new Graph();

                //adds route to graph if post office is located in either place1 or place 2 on the postoffice.csv
                for(i = 0; i < office.size(); i++){
                    Route r = office.get(i);
                    boolean is1Valid = r.getPlace1().equalsIgnoreCase(origin + " Post Office") || post.contains(r.getPlace1());
                    boolean is2Valid = r.getPlace2().equalsIgnoreCase(origin + " Post Office") || post.contains(r.getPlace2());
                    if(is1Valid && is2Valid){
                        Vertex place1 = new Vertex(r.getPlace1());
                        Vertex place2 = new Vertex(r.getPlace2());
                        g.addEdge(place1, place2, r.getDistance());
                        g.addEdge(place2, place1, r.getDistance());
                    }
                }

                //runs solver
                Vertex currentOffice = new Vertex(origin + " Post Office"); 
                solver.findShortestPath(g, currentOffice);

                //swaps cities if next city is available else no more
                if(!solver.getBestPath().isEmpty()){
                    String nextCity = solver.getBestPath().get(0).getDestination().name();
                    String justCity = nextCity.replaceAll("(?i)\\sPost\\sOffice", "").trim();
                    System.out.println("Next City: " + justCity);
                    start = new Vertex(nextCity);
                } else {
                    System.out.println("No city");
                }
                //clears filter for next use
                filtered.clear();
            }
            over = runAgain(keyboard);      
        } while(!over);
        keyboard.close();
    }
    public static boolean runAgain(Scanner sc){
        boolean over = false;
        int choice = -1;
        do{
            System.out.println("Would you like to deliver more mail?");
            System.out.print("Put 1 to restart, 0 to exit: ");
            choice = Input.readIntInput(sc, 0, 1);
            if(choice == 0){
                over = true;
            }
        } while (choice == -1);
        return over;
    }

    public static int getMailCount(Scanner sc){
        int mailInput = -1;
        do{
            System.out.print("Input number of mails: ");
            mailInput = Input.readIntInput(sc, 0, -1);
        } while (mailInput == -1);
        return mailInput;
    }
    
}

