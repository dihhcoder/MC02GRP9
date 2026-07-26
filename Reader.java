import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Reader {
    public List<Route> readMap(String filePath) throws FileNotFoundException, IOException, NumberFormatException {
        List<Route> map = new ArrayList<>();
        boolean isValid = true;

        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String header = br.readLine();
            String line;

            while((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length != 4)
                    isValid = false;
                else {
                    String origin = data[0].trim() + " Post Office";
                    String place1 = data[1].trim();
                    String place2 = data[2].trim();
                    double distance = Double.parseDouble(data[3].trim());

                    Route r = new Route(origin, place1, place2, distance);
                    map.add(r);
                }
            }
        }

        if(!isValid)
            throw new IOException();

        return map;
    }

    public List<Route> readOffices(String filePath) throws FileNotFoundException, IOException, NumberFormatException {
        List<Route> offices = new ArrayList<>();
        boolean isValid = true;

        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String header = br.readLine();
            String line;

            while((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length != 3)
                    isValid = false;
                else {
                    String place1 = data[0].trim();
                    String place2 = data[1].trim();
                    double distance = Double.parseDouble(data[2].trim());

                    Route r = new Route(place1, place2, distance);
                    offices.add(r);
                }
            }
        }

        if(!isValid)
            throw new IOException();

        return offices;
    }
}
