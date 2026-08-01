import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Input {
    public static int readIntInput(Scanner sc, String prompt, int min, int max) {
        int n = -1;
        boolean isValid = false;

        do {
            try {
                System.out.print(prompt);
                n = sc.nextInt();
                sc.nextLine();

                if(max == -1) {
                    if (n < min)
                        System.out.println("Invalid option! Value must be at least " + min + ".");
                    else isValid = true;
                }
                else {
                    if(!(n >= min && n <= max))
                        System.out.println("Invalid option! Please enter a value between " +
                                min + " and " + max + ".");
                    else isValid = true;
                }
            } catch(InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                sc.nextLine();
            }
        } while(!isValid);

        return n;
    }

    public static List<Route> readMapFileLocation(Scanner sc) {
        List<Route> mapRoutes = new ArrayList<>();
        Reader r = new Reader();
        boolean isRead = false;
        String filePath;

        do {
            try {
                System.out.print("Enter file location of map routes: ");
                filePath = sc.nextLine();
                mapRoutes = r.readMap(filePath);
                isRead = true;
            } catch(FileNotFoundException e) {
                System.out.println("Could not locate file! Please try again.");
            } catch(IOException e) {
                System.out.println("Could not read file! Please try again.");
            } catch(NumberFormatException e) {
                System.out.println("Could not read number format! Please try again.");
            }
        } while(!isRead);

        return mapRoutes;
    }

    public static List<Route> readOfficeFileLocation(Scanner sc) {
        List<Route> officeRoutes = new ArrayList<>();
        Reader r = new Reader();
        boolean isRead = false;
        String filePath;

        do {
            try {
                System.out.print("Enter file location of office routes: ");
                filePath = sc.nextLine();
                officeRoutes = r.readOffices(filePath);
                isRead = true;
            } catch(FileNotFoundException e) {
                System.out.println("Could not locate file! Please try again.");
            } catch(IOException e) {
                System.out.println("Could not read file! Please try again.");
            } catch(NumberFormatException e) {
                System.out.println("Could not read number format! Please try again.");
            }
        } while(!isRead);

        return officeRoutes;
    }

    public static List<String> readDestinations(Scanner sc, List<Route> map, int mailCount,
                                                Deque<String> pendingDestinations) {
        List<String> destinations = new ArrayList<>();
        boolean isFound;
        String s;

        for(int i = 0; i < mailCount; i++) {
            do {
                isFound = false;
                System.out.print("Destination of Mail " + (i + 1) + ": ");
                s = sc.nextLine();

                for(Route r : map) {
                    if(!isFound && s.equals(r.getPlace2()) &&
                            !destinations.contains(s) &&
                            !pendingDestinations.contains(s)) {
                        isFound = true;
                        destinations.add(s);
                    }
                }

                if(!isFound)
                    System.out.println("Invalid input! Please enter a valid destination.");
            } while(!isFound);
        }

        return destinations;
    }
}
