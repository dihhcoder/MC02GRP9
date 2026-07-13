import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Input {
    public static int readIntChoice(Scanner sc, int min, int max) {
        int n = -1;
        boolean isValid = false;

        do {
            try {
                n = sc.nextInt();
                sc.nextLine();

                if(!(n >= min && n <= max))
                    System.out.println("Invalid option! Please try again.");
                else isValid = true;
            } catch(InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                sc.nextLine();
            }
        } while(!isValid);

        return n;
    }

    public static List<Route> readMapFileLocation(Scanner sc) {
        List<Route> map = new ArrayList<>();
        Reader r = new Reader();
        String filePath;

        boolean isRead = false;

        do {
            try {
                System.out.print("Enter file location of map routes: ");
                filePath = sc.nextLine();
                map = r.readMap(filePath);
                isRead = true;
            } catch(FileNotFoundException e) {
                System.out.println("Could not locate file. Please try again.");
            } catch(NumberFormatException e) {
                System.out.println("Could not read number format. Please try again.");
            } catch(IOException e) {
                System.out.println("Could not read file. Please try again.");
            }
        } while(!isRead);

        return map;
    }

    public static List<Route> readOfficeFileLocation(Scanner sc) {
        List<Route> offices = new ArrayList<>();
        Reader r = new Reader();
        String filePath;

        boolean isRead = false;

        do {
            try {
                System.out.print("Enter file location of office routes: ");
                filePath = sc.nextLine();
                offices = r.readOffices(filePath);
                isRead = true;
            } catch(FileNotFoundException e) {
                System.out.println("Could not locate file. Please try again.");
            } catch(NumberFormatException e) {
                System.out.println("Could not read number format. Please try again.");
            } catch(IOException e) {
                System.out.println("Could not read file. Please try again.");
            }
        } while(!isRead);

        return offices;
    }

    public static String readDestination(Scanner sc, int mailCount, List<Route> routes) {
        String s = "";
        boolean isFound;

        for(int i = 0; i < mailCount; i++) {
            do {
                isFound = false;
                System.out.print("Destination of Mail " + (i + 1) + ": ");
                s = sc.nextLine();

                for(int j = 0; j < routes.size(); j++) {
                    if(!(s.equals(routes.get(j).getOrigin())) && s.equals(routes.get(j).getPlace1())) {
                        isFound = true;
                        j = routes.size();
                    }
                }

                if(!isFound)
                    System.out.println("Invalid input. Please try again.");
            } while(!isFound);
        }

        return s;
    }
}
