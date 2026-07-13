public class Route {
    private String origin;
    private String place1;
    private String place2;
    private double distance;

    public Route(String origin, String place1, String place2, double distance) {
        this.origin = origin;
        this.place1 = place1;
        this.place2 = place2;
        this.distance = distance;
    }

    public Route(String place1, String place2, double distance) {
        this.place1 = place1;
        this.place2 = place2;
        this.distance = distance;
    }

    public String getOrigin() {
        return this.origin;
    }

    public String getPlace1() {
        return this.place1;
    }

    public String getPlace2() {
        return this.place2;
    }

    public double getDistance() {
        return this.distance;
    }
}
