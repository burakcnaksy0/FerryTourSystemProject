package FerryTourSystemProject;

import java.util.Random;

public class Vehicles implements Runnable {
    private String vehicleName; // Determines the type of vehicle.
    private int unitSize;  // It keeps information about how many units of space the vehicle occupies.
    private Side initialSide;  // It stores the side information where the vehicle is initially located.
    private Side currentSide; // It holds the side where the vehicle is currently located.
    private boolean returnedBack; // Keeps track of whether the vehicle has returned to its starting position.
    private static final Random random = new Random(); // Since vehicles will enter the toll booths randomly, they are kept for a random number.
    private boolean hasCrossed = false; // It keeps information about whether the vehicle has passed to the opposite side or not.


    Vehicles(String vehicleName, int unitSize, Side initialSide) {
        this.vehicleName = vehicleName;
        this.unitSize = unitSize;
        this.initialSide = initialSide;
        this.currentSide = initialSide; // Firstly, the current side of the vehicle is the starting side.
    }

    // Checks whether the vehicle has completed its round trip.
    private synchronized void checkRoundTripCompletion() {
        if (hasCrossed && currentSide == initialSide) { // currentSide == initialSide --> This status indicates that the vehicle has completed its lap.
            returnedBack = true;
            System.out.println(vehicleName + " has returned to starting side: " + initialSide.getName());
        }

    }

    // Vehicles pass through toll booths randomly.
    private void passRandomToll() {
        Toll[] tolls = currentSide.getTolls();
        Toll randomToll = tolls[random.nextInt(tolls.length)];
        randomToll.passToToll(this);
    }


    @Override
    public void run() {
        try {
            passRandomToll();
            while (!hasCrossed()) {
                checkRoundTripCompletion();
                Thread.sleep(500);
            }
            Thread.sleep(1000);
            passRandomToll();
            while (!hasReturnedToInitialSide()) {
                checkRoundTripCompletion();
                Thread.sleep(500);
            }
            checkRoundTripCompletion();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean isReturnedBack() {
        return returnedBack;
    }

    public boolean hasReturnedToInitialSide() {
        return currentSide == initialSide;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public Side getInitialSide() {
        return initialSide;
    }

    public void setInitialSide(Side initialSide) {
        this.initialSide = initialSide;
    }

    public Side getCurrentSide() {
        return currentSide;
    }

    public void setCurrentSide(Side currentSide) {
        this.currentSide = currentSide;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public int getUnitSize() {
        return unitSize;
    }

    public void setUnitSize(int unitSize) {
        this.unitSize = unitSize;
    }

    // indicates that the vehicle is crossing the road.
    public synchronized void markAsCrossed() {
        hasCrossed = true;
    }

    public synchronized boolean hasCrossed() {
        return hasCrossed;
    }
}