package epj2.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import epj2.model.vehicle.*;

/**
 * A utility class for loading vehicles from a CSV file.
 * This class provides methods to read vehicle data from a CSV file and create
 * {@link Vehicle} objects. The CSV file should have specific columns for vehicle attributes.
 * 
 * @author Jelena Maletić
 * @version 27.8.2024.
 */
public class VehicleLoader {
	/**
	 * Manages the properties file used by the VehicleLoader class.
	 * This instance is used to load  vehicles file path.
	 */
	private static PropertiesManager propertiesManager;
	/** Date format for parsing dates in the CSV file. */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d.M.yyyy.");
    
    // Static block to initialize propertiesManager
    static {
    	propertiesManager = new PropertiesManager("filePaths.properties");
    }
    
    /**
     * Loads vehicles from a CSV file into a map 
     * where the keys are vehicle IDs and the values are {@link Vehicle} objects..
     * 
     * The CSV file should have the following columns:
     *-ID
     *-Producer
     *-Model
     *-Purchase Date
     *-Purchase Price
     *-Range (for Bike)
     *-Max Speed (for Scooter)
     *-Description (for Car)
     *-Type (car, bike, scooter)
	 *
     * If a line in the CSV file is not in the expected format or if the vehicle type
     * is unknown, it is skipped. Duplicate IDs are not accepted and lines containing such IDs are also skipped.
     *
     * @return a map where the keys are vehicle IDs and the values are {@link Vehicle} objects.
     */
    public static Map<String, Vehicle> loadVehiclesFromCSV() {
        Map<String, Vehicle> vehicles = new HashMap<>();
        boolean isFirstLine = true;
        try (BufferedReader br = new BufferedReader(new FileReader(propertiesManager.getProperty("VEHICLES_FILE_PATH")))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length < 8) {
                    System.out.println("Skipped line (incorrect number of columns): " + line);
                    continue;
                }
                String id = parts[0].trim();
                if (vehicles.containsKey(id)) {
                    System.out.println("Skipped line (duplicate ID): " + id);
                    continue;
                }
                String producer = parts[1].trim();
                String model = parts[2].trim();
                double purchasePrice;
                try {
                    String purchasePriceStr = parts[4].trim();
                    if (purchasePriceStr.isEmpty()) {
                        System.out.println("Skipped line (purchase price not specified): " + line);
                        continue;
                    }
                    purchasePrice = Double.parseDouble(purchasePriceStr);
                } 
                catch (NumberFormatException e) {
                    System.out.println("Skipped line (invalid purchase price): " + line);
                    continue;
                }
                Vehicle vehicle = null;
                switch (parts[8].trim().toLowerCase()) {
                    case "car":
                        LocalDate purchaseDate = parseDate(parts[3].trim());
                        String description = parts[7].trim().isEmpty()? "No description available":parts[7].trim();
                        vehicle = new Car(id, producer, purchasePrice, model, purchaseDate, description);
                        break;
                    case "bike":
                    	int range;
                        try {
                            String maxSpeedStr = parts[5].trim();
                            if (maxSpeedStr.isEmpty()) {
                                System.out.println("Range not specified (set to 0): " + line);
                                range = 0;
                            } else {
                                range = Integer.parseInt(maxSpeedStr);
                            }
                        } 
                        catch (NumberFormatException e) {
                            System.out.println("Invalid range (set to 0): " + line);
                            range = 0;
                        }
                        vehicle = new Bike(id, producer, purchasePrice, model, range);
                        break;
                    case "scooter":
                        int maxSpeed;
                        try {
                            String maxSpeedStr = parts[6].trim();
                            if (maxSpeedStr.isEmpty()) {
                                System.out.println("Maximum speed not specified (set to 0): " + line);
                                maxSpeed = 0;
                            } else {
                                maxSpeed = Integer.parseInt(maxSpeedStr);
                            }
                        } 
                        catch (NumberFormatException e) {
                            System.out.println("Invalid maximum speed (set to 0): " + line);
                            maxSpeed = 0;
                        }
                        vehicle = new Scooter(id, producer, purchasePrice, model, maxSpeed);
                        break;
                    default:
                        System.out.println("Skipped line (unknown vehicle type): " + line);
                        continue;
                }
                vehicles.put(id, vehicle);
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    /**
     * Parses a date string into a {@link LocalDate} object.
     * The date string should match the format specified by {@link #DATE_FORMATTER}.
     * If the date format is invalid, an error message is printed, and the method returns null.
     *
     * @param dateStr the date string to parse.
     * @return the parsed {@link LocalDate} object, or null if the date format is invalid.
     */
    private static LocalDate parseDate(String dateStr) {
        try {
        	 return LocalDate.parse(dateStr.trim(), DATE_FORMATTER);
        } 
        catch (Exception e) {
            return null;
        }
    }
}



