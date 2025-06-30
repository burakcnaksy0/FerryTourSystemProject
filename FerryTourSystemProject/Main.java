package FerryTourSystemProject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {

        // First, create the two sides (Side A and Side B)
        Side sideA = new Side("Side A");
        Side sideB = new Side("Side B");

        // Randomly determine which side the ferry starts from
        boolean startFromA = new Random().nextBoolean();

        // List to store the threads for each vehicle
        List<Thread> vehicleThreads = new ArrayList<>();

        // List to store all vehicles in the system
        List<Vehicles> allVehicles = new ArrayList<>();

        // Create and start the ferry
        Ferry ferry = new Ferry(sideA, sideB, startFromA, allVehicles);
        Thread ferryThread = new Thread(ferry);
        ferryThread.start();

        // Create different types of vehicles and add them to the system
        createVehicles("Car", 12, sideA, sideB, allVehicles, vehicleThreads);
        createVehicles("Minibus", 10, sideA, sideB, allVehicles, vehicleThreads);
        createVehicles("Truck", 8, sideA, sideB, allVehicles, vehicleThreads);

        // Start all vehicle threads
        for (Thread thread : vehicleThreads) {
            thread.start();
        }

        // Wait until all vehicles return to their initial side (i.e., complete a round trip)
        waitForVehiclesToReturn(allVehicles, ferry);

        // Wait for all vehicle threads to finish
        for (Thread thread : vehicleThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    // This method creates the specified number of vehicles of a given type
    // and randomly assigns them to one of the two sides (Side A or Side B)
    private static void createVehicles(String type, int count, Side sideA, Side sideB,
                                       List<Vehicles> allVehicles, List<Thread> vehicleThreads) {
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            Side initialSide = random.nextBoolean() ? sideA : sideB;
            Vehicles vehicle;

            switch (type) {
                case "Car":
                    vehicle = new Car("Car-" + (i + 1), initialSide);
                    break;
                case "Minibus":
                    vehicle = new Minibus("Minibus-" + (i + 1), initialSide);
                    break;
                case "Truck":
                    vehicle = new Truck("Truck-" + (i + 1), initialSide);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown vehicle type: " + type);
            }

            allVehicles.add(vehicle);
            vehicleThreads.add(new Thread(vehicle));
        }
    }

    // This method continuously checks whether all vehicles have completed
    // their round trip and returned to their initial side
    private static void waitForVehiclesToReturn(List<Vehicles> vehicles, Ferry ferry) {
        while (true) {
            boolean allReturned = vehicles.stream().allMatch(Vehicles::isReturnedBack);

            if (allReturned) {
                ferry.setTourCompleted(true);
                System.out.println("All vehicles have completed round trips and returned.");
                break;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

}
