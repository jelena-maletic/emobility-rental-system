package epj2.util;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import epj2.model.user.*;
import epj2.model.vehicle.*;
import epj2.service.Rental;

/**
 * A utility class for loading rentals from a CSV file.
 * This class provides methods to read rental data from a CSV file and create
 * {@link Rental} objects. The CSV file should have specific columns for rental attributes.
 * 
 * @author Jelena Maletić
 * @version 1.9.2024.
 */
public class RentalLoader {
	/**
	 * Manages the properties file used by the RentalLoader class.
	 * This instance is used to load rentals file path.
	 */
	private static PropertiesManager propertiesManager;
	/** Formatter for parsing and formatting date and time in the format "d.M.yyyy HH:mm". */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d.M.yyyy HH:mm");
    /**
     * Minimum value for coordinate validation.
     * Coordinates must be within the range of 0 to 19.
     */
    private static final int COORDINATE_MIN = 0;
    /**
     * Maximum value for coordinate validation.
     * Coordinates must be within the range of 0 to 19.
     */
    private static final int COORDINATE_MAX = MapUtil.getMapSize() - 1;
    /** Random number generator used for creating random users. */
    private static final Random RANDOM = new Random();
    
    // Static block to initialize propertiesManager
    static {
    	propertiesManager = new PropertiesManager("filePaths.properties");
    }
    
    /** 
     * Loads vehicles from a CSV file into a list
     * This method processes each line in the file, extracts the necessary details, 
     * and constructs `Rental` objects.
     * 
     * The CSV file should have the following columns:
     * -Rental date and time
     * -Name of the user who rented a vehicle
     * -Vehicle ID
     * -Start location
     * -End location
     * -Duration of the rental in seconds
     * -Information about whether a fault has occurred
     * -Information about whether a promotion exists
     * 
     * This method performs the following:
     * - Initializes data structures for users, rentals, and processed rentals.
     * - Reads and parses each line from the CSV file, skipping incorrect lines 
     *  (those with incorrect arguments, non-existent vehicles, already rented vehicles, 
     *  invalid start and end location coordinates).
     * - Validates and processes each component, including date, username, vehicle ID, locations, duration, fault presence, and promotion applicability.
     * - Creates vehicle copies and simulates faults as needed.
     * - Retrieves or creates users.
     * - Adds valid rentals to the list, sorts them by date and time, and updates user rental counts.
 	 * 
     * 
     * @param vehicles A map of vehicle IDs to Vehicle objects used for looking up vehicles based on 
     *                 their ID.
     * @return A list of Rental objects parsed from the file. The list is sorted by rental 
     *         date and time.
     */
    public static List<Rental> loadRentals(Map<String, Vehicle> vehicles) {
    	Map<String, User> users = new HashMap<>();
    	List<Rental> rentals = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(propertiesManager.getProperty("RENTALS_FILE_PATH")))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (parts.length != 8) {
                    System.out.println("Skipped line (incorrect number of columns): " + line);
                    continue;
                }
                try {
                    LocalDateTime dateTime = LocalDateTime.parse(parts[0], DATE_TIME_FORMATTER);
                    String username = parts[1];
                    String vehicleId = parts[2];
                    int[] startLocation = parseCoordinate(parts[3]);
                    int[] endLocation = parseCoordinate(parts[4]);
                    double duration = Double.parseDouble(parts[5]);
                    boolean hasFault = "yes".equalsIgnoreCase(parts[6]);
                    boolean hasPromotion = "yes".equalsIgnoreCase(parts[7]);
                    if (startLocation == null || endLocation == null) {
                        System.out.println("Skipped line (invalid coordinates): " + line);
                        continue;
                    }
                    Vehicle vehicle = vehicles.get(vehicleId);
                    if (vehicle == null) {
                        System.out.println("Skipped line (vehicle not found): " + line);
                        continue;
                    }
                    
                    boolean isRented = rentals.stream().anyMatch(rental -> rental.getVehicle().getID().equals(vehicleId) && rental.getDateTime().equals(dateTime));
                    if (isRented) {
                        System.out.println("Skipped line (vehicle is already rented): " + line);
                        continue;
                    }
                    
                    Vehicle vehicleCopy = vehicle.clone();
                    if (hasFault) { 
                        vehicleCopy.simulateFault();
                    }
                    User user = users.computeIfAbsent(username, RentalLoader::createRandomUser);
                    Rental rental = new Rental(dateTime, user, vehicleCopy, startLocation, endLocation, duration, hasPromotion);
                    rentals.add(rental);
                } 
                catch (CloneNotSupportedException e) {
                	System.out.println("Cloning failed " + line);
                    e.printStackTrace(); 
                }
                catch (Exception e) {
                    System.out.println("Skipped line (data parsing error): " + line);
                    e.printStackTrace();
                }
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        rentals.sort(Comparator.comparing(Rental::getDateTime));
        updateNumberOfRentals(rentals, users);
        return rentals;
    }
    
    /**
     * Updates the number of rentals for each user and replaces the user in the rental with a cloned copy.
     * This method iterates over the list of rentals, increments the rental count for each user, updates
     * the user information in the map, and sets a cloned user object in each rental.
     * 
     * @param rentals A list of Rental objects that needs to be processed.
     * @param users A map of user names to `User` objects.
     */
    private static void updateNumberOfRentals(List<Rental> rentals, Map<String, User> users) {
        for (Rental rental : rentals) {
            User user = rental.getUser();
            user.setNumberOfRentals(user.getNumberOfRentals()+1);
            users.put(user.getName(), user);
            try {
                User userCopy = user.clone();
                rental.setUser(userCopy);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace(); 
                throw new RuntimeException("Cloning failed", e);
            }
        }
    }
    
    /**
     * Parses a coordinate string and converts it to an integer array.
     * The coordinate string should be in the format "x,y". The method trims any quotes and spaces,
     * splits the string by the comma, and parses the x and y values. The coordinate values are checked
     * to ensure they are within the defined bounds.
     * 
     * @param coordinate A string representing the coordinate in the format "x,y".
     * @return An integer array containing the x and y values if the format is correct and values are within bounds,
     *         or null if the format is invalid or values are out of bounds.
     */
    private static int[] parseCoordinate(String coordinate) {
        try {
        	if (!coordinate.startsWith("\"") || !coordinate.endsWith("\"")) {
                return null;
            }
            coordinate = coordinate.replace("\"", "").trim();
            String[] parts = coordinate.split(",");
            if (parts.length != 2) {
                return null;
            }
            int x = Integer.parseInt(parts[0].trim());
            int y = Integer.parseInt(parts[1].trim());
            if (x >= COORDINATE_MIN && x <= COORDINATE_MAX && y >= COORDINATE_MIN && y <= COORDINATE_MAX) {
                return new int[]{x, y};
            } 
            else {
                return null;
            }
        } 
        catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * Creates a random `User` object based on the provided username.
     * The method randomly decides whether to create a `DomesticUser` or a `ForeignUser`.
     * 
     * @param username The name of the user to be created.
     * @return A `User` object which can be either a `DomesticUser` or a `ForeignUser`.
     */
    private static User createRandomUser(String username) {
        if (RANDOM.nextBoolean()) {
            return new DomesticUser(username);
        } else {
            return new ForeignUser(username);
        }
    }
}


