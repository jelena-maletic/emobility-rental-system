package epj2.model.vehicle;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import epj2.util.MapUtil;

import java.io.Serializable;

/**
 * Represents a electric vehicle in vehicle rental system
 * This is an abstract class that holds common properties and methods
 * for different types of vehicles
 * It implements the {@link Cloneable} interface to allow cloning and
 * the {@link java.io.Serializable} interface to support serialization.
 * 
 * @author Jelena Maletić
 * @version 1.9.2024.
 */
public abstract class Vehicle implements Serializable, Cloneable {
	/**
	 * Serial version UID for serialization.
	 * This value is used to verify the compatibility of serialized data during deserialization.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The unique identifier for vehicle.
	 * This ID is used to differentiate between different vehicles in the system.
	 */
	protected String ID;
	/**
	 * The manufacturer of the vehicle.
	 * This represents the company that produced the vehicle.
	 */
	protected String producer;
	/** The purchase price of the vehicle. */
	protected double purchasePrice;
	/** The model of the vehicle. */
	protected String model;
	/**
	 * The current battery level of the electric vehicle.
	 * This represents the percentage of battery charge remaining.
	 */
	protected int currentBatteryLevel;
	/**
	 * The fault associated with the vehicle, if any.
	 * This holds information about any issues or defects the vehicle may have.
	 */
	protected Fault fault;
	
	/**
	 * Constructs a new vehicle with the specified attributes.
	 * 
	 * @param ID the unique identifier for the vehicle.
	 * @param producer the manufacturer or producer of the vehicle.
	 * @param purchasePrice the price at which the vehicle was purchased.
	 * @param model the model of the vehicle.
	 * 
	 * The {@code currentBatteryLevel} is initialized to 100, indicating a fully charged battery.
	 * The {@code fault} is initialized to {@code null}, indicating that there is no fault associated with the vehicle at creation.
	 */
	public Vehicle(String ID,String producer,double purchasePrice,String model) {
		this.ID=ID;
		this.producer=producer;
		this.purchasePrice=purchasePrice;
		this.model=model;
		this.currentBatteryLevel = 100;
		this.fault=null;
	}
	
	/**
	 * Returns the unique identifier for the vehicle.
	 * 
	 * @return the ID of the vehicle.
	 */
	public String getID() {
		return ID;
	}
	
	/**
     * Sets a new identifier for the vehicle.
     *
     * @param iD the new ID to be assigned to the vehicle.
     */
	public void setID(String iD) {
		ID = iD;
	}
	
	/**
	 * Returns the producer for the vehicle.
	 * 
	 * @return the producer of the vehicle.
	 */
	public String getProducer() {
		return producer;
	}
	
	/**
     * Sets a new producer for the vehicle.
     *
     * @param producer the new producer to be assigned to the vehicle.
     */
	public void setProducer(String producer) {
		this.producer = producer;
	}
	
	/**
	 * Returns the price at which the vehicle was purchased.
	 * 
	 * @return the purchase price of the vehicle.
	 */
	public double getPurchasePrice() {
		return purchasePrice;
	}

	/**
     * Sets a new purchase price for the vehicle.
     *
     * @param purchasePrice the new purchase price to be assigned to the vehicle.
     */
	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	
	/**
	 * Returns the model of the vehicle
	 * 
	 * @return the model of the vehicle.
	 */
	public String getModel() {
		return model;
	}
	
	/**
     * Sets a new model for the vehicle.
     *
     * @param model the new model to be assigned to the vehicle.
     */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * Returns the current battery level of the electric vehicle (the percentage of battery charge remaining).
	 * 
	 * @return the current battery level of the electric vehicle.
	 */
	public int getCurrentBatteryLevel() {
		return currentBatteryLevel;
	}
	
	/**
     * Sets a new current battery level for the vehicle.
     *
     * @param currentBatteryLevel the new current battery level to be assigned to the vehicle.
     */
	public void setCurrentBatteryLevel(int currentBatteryLevel) {
		this.currentBatteryLevel = currentBatteryLevel;
	}
	
	/**
	 * Returns the fault associated with the vehicle, if any.
	 * 
	 * @return the fault associated with the vehicle.
	 */
	public Fault getFault() {
		return fault;
	}
	
	/**
     * Sets a new fault for the vehicle.
     *
     * @param fault the new fault to be assigned to the vehicle.
     */
	public void setFault(Fault fault) {
		this.fault = fault;
	}
	
	/**
	 * Charges the vehicle's battery to its full capacity.
	 * This method sets the battery level to 100%, regardless of its current state.
	 */
	public void chargingBattery() {
		currentBatteryLevel=100;
	}
	
	/**
	 * Decreases the current battery level of the vehicle by 5 units.
	 * If the battery level goes below 0, it is set to 0.
	 */
	public void decreaseBatteryLevel() {
	    currentBatteryLevel -= 5;
	    if (currentBatteryLevel < 0) {
	        currentBatteryLevel = 0; 
	    }
	}
	
	/**
	 * Simulates a fault by assigning a random fault description to the vehicle.
	 * The fault's description is generated by {@link Fault#getRandomFaultDescription()},
	 * and the fault occurrence time is initially set to the current date and time.
	 */
    public void simulateFault() {
        String description = Fault.getRandomFaultDescription();
        fault = new Fault(description, LocalDateTime.now());
    }
    
    /**
     * Finds a path from the start coordinates to the end coordinates.
     * The path is represented as a list of integer arrays, where each array
     * contains the x and y coordinates of a point in the path.
     * The method calculates the path by first moving horizontally from the starting x-coordinate 
     * to the ending x-coordinate, and then vertically from the starting y-coordinate to the 
     * ending y-coordinate. The resulting path is a series of points that represent the straight 
     * line traversal between the start and end coordinates. 
     * 
     * @param start the starting coordinates as an array of two integers [x, y].
     * @param end the ending coordinates as an array of two integers [x, y].
     * @return a list of integer arrays representing the path from start to end location.
     */
    public List<int[]> findPath(int[] start, int[] end) {
        List<int[]> path = new ArrayList<>();
        int x1 = start[0];
        int y1 = start[1];
        int x2 = end[0];
        int y2 = end[1];
        if (x1 != x2) {
            int stepX = x1 < x2 ? 1 : -1;
            for (int x = x1; x != x2; x += stepX) {
                path.add(new int[]{x, y1});
            }
        }
        if (y1 != y2) {
            int stepY = y1 < y2 ? 1 : -1;
            for (int y = y1; y != y2; y += stepY) {
                path.add(new int[]{x2, y});
            }
        }
        path.add(new int[]{x2, y2});
        return path;
    }
    
    /**
     * Checks if the path from start to end coordinates is within the wide area of the city.
     * 
     * @param start the starting coordinates as an array of two integers [x, y].
     * @param end the ending coordinates as an array of two integers [x, y].
     * @return {@code true} if the path is within the wide area, {@code false} otherwise.
     */
    public boolean isPathInWideArea(int[] start, int[] end) {
        List<int[]> path = findPath(start, end);
        return MapUtil.isPathInWideArea(path);
    }
    
    /**
     * Calculates the time per cell for a given path based on the total duration in seconds.
     * The total duration, which represents the total time of vehicle movement along the path,
     * is divided by the number of cells in the path to determine the time spent per cell.
     * 
     * @param path the list of coordinates representing the path.
     * @param durationSeconds the total duration in seconds, which corresponds to the total time of vehicle movement.
     * @return the time in seconds per cell, or 0 if the path is empty.
     */
    public double calculateTimePerCell(List<int[]> path, double durationSeconds) {
        int numberOfCells = path.size();
        if (numberOfCells == 0) {
            System.out.println("Warning: The path is empty. Returning 0.");
            return 0;
        }
        return durationSeconds / numberOfCells;
    }
    
    /**
     * Returns a string representation of this vehicle.
     * The string includes the vehicle's ID, producer, purchase price, and model.
     * 
     * @return a string representation of the vehicle.
     */
    public String toString() {
        return "ID: \"" + ID + "\", producer: \"" + producer + "\", purchase price: \"" + purchasePrice + 
               "\", model: \"" + model;
    }
    
    /**
     * Creates and returns a clone of this vehicle.
     * The cloning process ensures that the {@link Fault} is also cloned,
     * provided they implement the {@link Cloneable} interface.
     * 
     * @return a clone of this vehicle.
     * @throws CloneNotSupportedException if the cloning of this vehicle is not supported.
     */
    @Override
    public Vehicle clone() throws CloneNotSupportedException {
        Vehicle cloned = (Vehicle) super.clone();
        if (this.fault != null) {
            cloned.fault = (Fault) this.fault.clone(); //Fault implements Cloneable
        }
        return cloned;
    }
    
    /**
     * Determines whether personal identification documents and a driver's license are required 
     * to operate this vehicle.. 
     * This method must be implemented by subclasses.
     * 
     * @return {@code true} if personal identification documents and a driver's license are required; 
     *         {@code false} otherwise.
     */
    public abstract boolean requiresDocuments(); 
}
