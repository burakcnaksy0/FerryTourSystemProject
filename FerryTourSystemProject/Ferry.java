package FerryTourSystemProject;

import java.util.ArrayList;
import java.util.List;


public class Ferry implements Runnable {
    private static final int MAX_CAPACITY = 20;
    private List<Vehicles> loadedVehicles = new ArrayList<>();  // Maintains a list of vehicles currently on the ferry.
    private Side currentSide; // Here, information on which side the ferry is currently located will be kept.
    private Side oppositeSide; //The ferry's opposite information will be kept here.
    private volatile boolean isTourCompleted; // Here you will find information about whether the ferry has completed its tour or not.
    private final List<Vehicles> allVehicles; // Maintains a list of all vehicles in the ferry system.

    Ferry(Side sideA, Side sideB, boolean startFromA, List<Vehicles> allVehicles) {
        this.currentSide = startFromA ? sideA : sideB; // If startFromA is true then currentSide will be sideA, otherwise it will be sideB.
        this.oppositeSide = startFromA ? sideB : sideA; // here it supports what was done above. If startFromA is true, sideB will be the opposite side.
        this.allVehicles = allVehicles;
    }

    // Here, vehicles waiting in the waiting area fill the ferry.
    private synchronized void load() {
        System.out.println("Ferry attempting to load from " + currentSide.getName() + "...");
        WaitingArea currentSideWaitingArea = currentSide.getWaitingArea();  // Gets the waiting area of the current side.

        if (currentSideWaitingArea.isEmpty()) {
            System.out.println("No vehicles in waiting area : " + currentSide.getName());
            return;
        }

        int usedTotalCapacity = 0;  // Total capacity of vehicles boarding the ferry so far

        while (usedTotalCapacity < MAX_CAPACITY) {
            Vehicles vehicles = currentSideWaitingArea.poll();
            if (vehicles == null) {
                break;
            }
            if (usedTotalCapacity + vehicles.getUnitSize() <= MAX_CAPACITY) {
                loadedVehicles.add(vehicles);
                usedTotalCapacity += vehicles.getUnitSize();
                int remainingCapacity = MAX_CAPACITY - usedTotalCapacity;

                System.out.println("Ferry loaded : " + vehicles.getVehicleName() +
                        " (" + vehicles.getUnitSize() + " units). " +
                        "Remaining ferry capacity: " + remainingCapacity + " units." +
                        "  Number of vehicles waiting in the queue in the waiting area : " + currentSideWaitingArea.size() + " vehicles" +
                        " -- " + currentSide.getName());

            } else {
                currentSideWaitingArea.addVehicles(vehicles);
                break;
            }
        }
        if (loadedVehicles.isEmpty()) {
            System.out.println("No vehicles to load at " + currentSide.getName());
        }
    }

    // Here, when the vehicles taken onto the ferry arrive at the opposite side, they will be removed from the ferry and added to the waitinig area belonging to that side.
    private synchronized void unloaded() {
        WaitingArea oppositeSideWaitingArea = oppositeSide.getWaitingArea();
        for (Vehicles v : loadedVehicles) {
            System.out.println("Ferry unloading: " + v.getVehicleName() + " at " + oppositeSide.getName());
            v.setCurrentSide(oppositeSide);
            v.markAsCrossed();
            oppositeSideWaitingArea.addVehicles(v);
        }
        loadedVehicles.clear();
    }

    // In this method we perform a switch operation. Since we have passed to the opposite side, the current Side is now the oppositeSide. The oppositeSide becomes the currentSide.
    private synchronized void moveToOtherSide() {
        System.out.println("Ferry departing from " + currentSide.getName() + " to " + oppositeSide.getName());

        try {
            Thread.sleep((int) (Math.random() * 500));
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        Side temp = currentSide;
        currentSide = oppositeSide;
        oppositeSide = temp;

        System.out.println("Ferry arrived at : " + currentSide.getName());
    }

    // This method checks whether all vehicles have returned to the same side they started on.
    private synchronized void checkIfAllVehiclesReturned() {
        for (Vehicles v : allVehicles) { // circulates vehicles on the ferry
            if (!v.isReturnedBack()) {
                return;
            }
        }
        isTourCompleted = true; // If it's tied, the round is complete.
        System.out.println("All vehicles have returned to their starting side. Tour completed.");
    }


    @Override
    public void run() {
        while (!isTourCompleted) {  // if the tour is completed
            load(); // First we fill the ferry.
            if (!loadedVehicles.isEmpty()) {  // if the ferry is not empty
                moveToOtherSide(); // We move on to the opposite side.
                unloaded(); // Then we unload the vehicles on the ferry.
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            checkIfAllVehiclesReturned();

        }
    }

    public List<Vehicles> getLoadedVehicles() {
        synchronized (this) {
            return new ArrayList<>(loadedVehicles);
        }
    }

    public void setLoadedVehicles(List<Vehicles> loadedVehicles) {
        this.loadedVehicles = loadedVehicles;
    }

    public Side getCurrentSide() {
        return currentSide;
    }

    public void setCurrentSide(Side currentSide) {
        this.currentSide = currentSide;
    }

    public Side getOppositeSide() {
        return oppositeSide;
    }

    public void setOppositeSide(Side oppositeSide) {
        this.oppositeSide = oppositeSide;
    }

    public boolean isTourCompleted() {
        return isTourCompleted;
    }

    public void setTourCompleted(boolean tourCompleted) {
        isTourCompleted = tourCompleted;
    }
}