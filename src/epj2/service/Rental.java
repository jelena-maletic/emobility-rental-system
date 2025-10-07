package epj2.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import epj2.gui.MapDisplay;
import epj2.model.user.*;
import epj2.model.vehicle.*;

/**
 * Represents a rental transaction in which a user rents a vehicle for a specified duration and route within the city.
 * This class handles the details of the rental, including the user, vehicle, 
 * start and end locations, duration, and any promotions applied.
 * This class extends {@link Thread} to allow the rental process to be executed concurrently in real-time.
 *
 * @author Jelena Maletić
 * @version 2.9.2024.
 */
public class Rental extends Thread {
	/** The date and time when the rental begins. */
	private LocalDateTime dateTime;
	/** The user who has rented a vehicle */
    private User user;
    /** The vehicle being rented. */
    private Vehicle vehicle;
    /** The starting location of the rental, represented as an array of two integers [x, y]. */
    private int[] startLocation;
    /** The ending location of the rental, represented as an array of two integers [x, y]. */
    private int[] endLocation;
    /** The duration of the rental in seconds. */
    private double durationSeconds;
    /** Indicates whether the rental includes a promotion. */
    private boolean hasPromotion;
    /** The display map used to visualize the rental route. */
    private MapDisplay mapDisplay;
    /** Indicates whether a fault occurred during the rental. */
    private boolean faultOccurred = false;
    /** Vehicle that had a fault*/
    private Vehicle faultyVehicle;
    
    /**
     * Constructs a Rental object with the specified parameters.
     * 
     * @param dateTime the date and time when the rental begins.
     * @param user the user who has rented a vehicle.
     * @param vehicle the vehicle being rented.
     * @param startLocation the starting location of the rental, represented as an array of two integers [x, y].
     * @param endLocation the ending location of the rental, represented as an array of two integers [x, y].
     * @param durationSeconds the duration of the rental in seconds.
     * @param hasPromotion indicates whether the rental includes a promotion.
     */
    public Rental(LocalDateTime dateTime, User user, Vehicle vehicle, int[] startLocation, int[] endLocation, double durationSeconds, boolean hasPromotion) {
        this.dateTime = dateTime;
        this.user = user;
        this.vehicle = vehicle;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.durationSeconds = durationSeconds;
        this.hasPromotion = hasPromotion;
    }
    
    /**
     * Returns the date and time when the rental begins.
     * 
     * @return the date and time when the rental begins.
     */
	public LocalDateTime getDateTime() {
		return dateTime;
	}
	
	/**
     * Sets the date and time of the rental.
     * 
     * @param dateTime the date and time of the rental.
     */
	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}
	
	/**
     * Returns the user who has rented a vehicle.
     * 
     * @return the user who has rented a vehicle.
     */
	public User getUser() {
		return user;
	}
	
	/**
	 * Sets the user who has rented a vehicle.
	 * 
	 * @param user the user who is renting the vehicle.
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * Returns the vehicle that is being rented.
	 * 
	 * @return the vehicle that is being rented.
	 */
	public Vehicle getVehicle() {
		return vehicle;
	}
	
	/**
	 * Sets the vehicle that is being rented.
	 * 
	 * @param vehicle the vehicle to be rented.
	 */
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	
	/**
	 * Returns the starting location of the rental route.
	 * 
	 * @return the starting location as an array of two integers [x, y].
	 */
	public int[] getStartLocation() {
		return startLocation;
	}
	
	/**
	 * Sets the starting location of the rental route.
	 * 
	 * @param startLocation the starting location as an array of two integers [x, y].
	 */
	public void setStartLocation(int[] startLocation) {
		this.startLocation = startLocation;
	}
	
	/**
	 * Returns the ending location of the rental route.
	 * 
	 * @return the ending location as an array of two integers [x, y].
	 */
	public int[] getEndLocation() {
		return endLocation;
	}
	
	/**
	 * Sets the ending location of the rental route.
	 * 
	 * @param endLocation the ending location as an array of two integers [x, y].
	 */
	public void setEndLocation(int[] endLocation) {
		this.endLocation = endLocation;
	}
	
	/**
	 * Returns the duration of the rental in seconds.
	 * 
	 * @return the duration of the rental in seconds.
	 */
	public double getDurationSeconds() {
		return durationSeconds;
	}
	
	/**
	 * Sets the duration of the rental in seconds.
	 * 
	 * @param durationSeconds the duration of the rental in seconds.
	 */
	public void setDurationSeconds(double durationSeconds) {
		this.durationSeconds = durationSeconds;
	}
	
	/**
	 * Returns whether a promotion was applied to the rental.
	 * 
	 * @return {@code true} if a promotion was applied; {@code false} otherwise.
	 */
	public boolean isHasPromotion() {
		return hasPromotion;
	}
	
	/**
	 * Sets whether a promotion was applied to the rental.
	 * 
	 * @param hasPromotion {@code true} if a promotion was applied; {@code false} otherwise.
	 */
	public void setHasPromotion(boolean hasPromotion) {
		this.hasPromotion = hasPromotion;
	}
	
	/**
	 * Returns the map display associated with the rental.
	 * 
	 * @return the map display for the rental.
	 */
	public MapDisplay getMapDisplay() {
		return mapDisplay;
	}
	
	/**
	 * Sets the map display associated with the rental.
	 * 
	 * @param mapDisplay the map display for the rental.
	 */
	public void setMapDisplay(MapDisplay mapDisplay) {
		this.mapDisplay = mapDisplay;
	}
	
	/**
	 * Returns whether a fault occurred during the rental.
	 * 
	 * @return {@code true} if a fault occurred; {@code false} otherwise.
	 */
	public boolean hasFaultOccurred() {
		return faultOccurred;
	}
	
	/**
	 * Returns the vehicle that broke down during the rental.
	 * 
	 * @return the vehicle that had a fault.
	 */
	public Vehicle getFaultyVehicle() {
		return faultyVehicle;
	}
	
	/**
	 * Defines the code to be executed when the thread starts.
	 * This method includes the logic for the rental process simulation, which will
	 * run concurrently with other threads, managing tasks as specified in the implementation.
	 * It executes the rental simulation by moving the vehicle along the path from start to end location.
	 * This method performs the following actions:
	 * -Calculates the path the vehicle will take from the starting location to the ending location.
	 * -Determines the time required to traverse each cell in the path.
	 * -Randomly selects a point in the path where a fault may occur.
	 * -Iterates through the path, updating the vehicle's position on the map display.
	 * -Simulates the vehicle's movement, adjusting the battery level and handling the charging process if necessary.
	 * -Checks if the vehicle encounters a fault at any point in the path and updates the fault information.
	 * -After the vehicle completes its movement, it checks whether it has traveled through the wider area of the city. 
	 * -A bill is then generated and delivered to the user. Additionally, a message is printed to the console indicating that 
	 *  the vehicle has finished its movement.
	 *  
	 * During execution:
	 * -If the vehicle's battery level drops below or equals 20%, it pauses for a charging period before continuing.
	 * -The simulation takes into account the distance covered for bikes to properly update their battery level.
	 * -The method captures the last position of the vehicle for proper display updates and fault handling.
	 * 
	 */
	@Override
	public void run() {
	    List<int[]> path = vehicle.findPath(startLocation, endLocation);
	    double timePerCell = vehicle.calculateTimePerCell(path, durationSeconds);
	    Random random = new Random();
	    int faultIndex = random.nextInt(path.size() - 1) + 1;
	    int[] lastPosition = null; 
	    for (int i = 0; i < path.size(); i++) {
	        int[] position = path.get(i);
	        int x = position[0];
	        int y = position[1];
	        mapDisplay.updateVehiclePosition(x, y, vehicle.getID(), vehicle.getCurrentBatteryLevel());
	        lastPosition = position;
	        if(vehicle instanceof Bike) {
	        	((Bike) vehicle).setDistanceCovered(((Bike) vehicle).getDistanceCovered() + 1);
	        }
	        if (vehicle.getCurrentBatteryLevel() <= 20) {
	        	System.out.println("Vehicle " + vehicle.getID() + " stopped to charge the battery");
	            try {
	                Thread.sleep((long) (timePerCell * 1000) + 2000); 
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	            vehicle.chargingBattery();
	        } 
	        else {
	        	try {
	                Thread.sleep((long) (timePerCell * 1000)); 
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	        vehicle.decreaseBatteryLevel();
	        if (vehicle.getFault()!=null && (i + 1 == faultIndex)) {
	            LocalDateTime faultTime = dateTime.plusSeconds((long) ((i + 1) * timePerCell));
	            vehicle.getFault().setDateTime(faultTime);
	            System.out.println("Vehicle " + vehicle.getID() + " broke down at position (" + x + ", " + y + ").");
	            faultOccurred = true;
	            faultyVehicle = vehicle;
	            break; 
	        }
	        mapDisplay.clearVehiclePosition(x, y); 
	    }
	    if (faultOccurred && lastPosition != null) {
	        mapDisplay.clearVehiclePosition(lastPosition[0], lastPosition[1]);
	    }
	    boolean isWideArea = vehicle.isPathInWideArea(startLocation, endLocation);
	    Invoice invoice = new Invoice(this);
	    invoice.generateInvoice(isWideArea);
	    if (!faultOccurred) {
	        System.out.println("Vehicle " + vehicle.getID() + " has reached the final position.");
	    }
	}
	
}
