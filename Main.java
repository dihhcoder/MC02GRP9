import java.util.*;
public class Main {
    public static void main(String[] args) {
        List<Route> office = new ArrayList<>();
        List<Route> map = new ArrayList<>(); 
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Welcome To Arrow Mail");
        office = Input.readOfficeFileLocation(keyboard);
        map = Input.readMapFileLocation(keyboard);
        int mailCount = 0, choice = -1;
        int mailInput = -1;
        boolean over = false;
        Solver solver = new Solver();
        Graph g;
        Vertex start, next;
        List<String> filtered = new ArrayList<>();
        ArrayDeque<String> unfiltered = new ArrayDeque<>();
        List<String> dest;
        String origin;
        
        System.out.println("Welcome To Arrow Mail");
        do{
            start = new Vertex(Input.readStartingOffice(keyboard, office));
            boolean first = true;
            while(first || !unfiltered.isEmpty()){
                do{
                    System.out.print("Input number of mails: ");
                    mailInput = Input.readIntInput(keyboard, 0, -1);
                } while (mailInput == -1);
                dest = Input.readDestinations(keyboard, map, mailInput);
                for(String s: dest){
                    unfiltered.add(s);
                }

                dest.clear();
                first = false;
                origin = null;

                for(int i = 0; i < map.size() && origin == null; i++){
                    if(map.get(i).getPlace1().equals(start.getName())){
                        origin = map.get(i).getOrigin();
                    }
                }
                List<String> rejected = new ArrayList<>();

                while(!unfiltered.isEmpty()){
                    boolean check = false;
                    String s = unfiltered.poll();
                    for(int i = 0; i < map.size() && !check; i++){
                        Route r = map.get(i);
                        if(r.getOrigin().equals(origin)){
                            if(s.equalsIgnoreCase(r.getPlace1()) || s.equalsIgnoreCase(r.getPlace2())){
                                check = true;
                            }
                        }
                    }
                    if (check){
                        filtered.add(s);
                        System.out.println("1");
                    } else {
                        rejected.add(s);
                    }
                }
                for(String s: rejected){
                    unfiltered.add(s);
                }
                dest.clear();   
                
            }
            System.out.println("Job done");
            do{
                System.out.print("Input 1 to restart else input 0 to end: ");
                choice = Input.readIntInput(keyboard, 0, 1);
                if(choice == 0){
                    over = true;
                }
            } while (choice == -1);            
        } while(!over);
        keyboard.close();
        
    }
}