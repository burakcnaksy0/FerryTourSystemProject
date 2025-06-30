package FerryTourSystemProject;

import java.util.LinkedList;
import java.util.Queue;

public class WaitingArea {
    private Queue<Vehicles> vehiclesList = new LinkedList<>();

    // We add vehicles to the waiting area.
    public synchronized void addVehicles(Vehicles vehicles) {
        vehiclesList.add(vehicles);
    }

    // selects the first vehicle in the waiting area queue and removes it.
    public synchronized Vehicles poll() {
        return vehiclesList.poll();
    }

    // Determines whether the vehicle queue in the waiting area is empty.
    public synchronized boolean isEmpty() {
        return vehiclesList.isEmpty();
    }

    // Keeps track of how many vehicles are in the waiting area.
    public synchronized int size() {
        return vehiclesList.size();
    }


}