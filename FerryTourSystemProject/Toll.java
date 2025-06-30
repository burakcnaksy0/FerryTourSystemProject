package FerryTourSystemProject;

public class Toll {
    private Side side;  // Here, the side information to which the ticket office belongs is kept.

    Toll(Side side) {
        this.side = side;
    }

    // This method randomly enters the waiting vehicles into one of the toll booths, one by one.
    public synchronized void passToToll(Vehicles vehicles) {
        System.out.println(vehicles.getVehicleName() + " is passing through toll on side " + side.getName());

        try {
            Thread.sleep((int) (Math.random() * 500));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        side.getWaitingArea().addVehicles(vehicles);
        System.out.println(vehicles.getVehicleName() + " has entered waiting area on side " + side.getName());
    }


}