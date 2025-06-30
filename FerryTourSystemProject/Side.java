package FerryTourSystemProject;

public class Side {
    private String name; // holds the name of the side.
    private Toll[] tolls; // holds the toll booths located in Side.
    private WaitingArea waitingArea; // holds the waiting area located on that side.


    Side(String name) {
        this.name = name;
        this.waitingArea = new WaitingArea();
        this.tolls = new Toll[]{new Toll(this), new Toll(this)}; // bir side'da iki tane gişe olduğu için
    }


    public WaitingArea getWaitingArea() {
        return waitingArea;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Toll[] getTolls() {
        return tolls;
    }

    public void setTolls(Toll[] tolls) {
        this.tolls = tolls;
    }
}