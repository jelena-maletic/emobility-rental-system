package epj2.model.vehicle;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import epj2.util.PropertiesManager;

/**
 * The Fault class represents a fault or malfunction that can occur in a vehicle.
 * It contains a description of the fault and the date and time when the fault occurred.
 * The class provides methods to get and set the fault details, as well as a method to generate 
 * a random fault description from a predefined set of possible faults.
 * It implements the {@link Cloneable} interface to allow cloning.
 * 
 * @author Jelena Maletić
 * @version 1.9.2024.
 */
public class Fault implements Cloneable {
	/**
	 * Manages the properties file used by the Fault class.
	 * This instance is used to load fault descriptions.
	 */
	private static PropertiesManager propertiesManager;
	/** Fault descriptions */
	private static final List<String> FAULT_DESCRIPTIONS = new ArrayList<>();
	/** A brief description of the fault */
	private String description;
	/** The date and time when the fault occurred. */
	private LocalDateTime dateTime;
	

    // Static block to initialize fault descriptions from properties file.
	static{
		propertiesManager = new PropertiesManager("faultDescriptions.properties");
		int i = 1;
        while (propertiesManager.containsKey("FAULT_" + i)) {
            FAULT_DESCRIPTIONS.add(propertiesManager.getProperty("FAULT_" + i));
            i++;
        }
	}
	
	
	/**
     * Constructs a new Fault with the specified description and date and time.
     * 
     * @param description description of the fault.
     * @param dateTime the date and time when the fault occurred.
     */
	public Fault(String description, LocalDateTime dateTime) {
	    this.description = description;
	    this.dateTime = dateTime;
	}
	
	/**
     * Returns the description of the fault.
     * 
     * @return the fault description.
     */
	public String getDescription() {
		return description;
	}
	
	/**
     * Sets a description of the fault.
     *
     * @param description the new description to be assigned to the fault.
     */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
     * Returns the date and time when the fault occurred.
     * 
     * @return the date and time of the fault.
     */
	public LocalDateTime getDateTime() {
		return dateTime;
	}
	
	/**
     * Sets the date and time when the fault occurred.
     *
     * @param dateTime the date and time to set for the fault.
     */
	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}
	
	/**
	 * A list of predefined fault descriptions.
	 * These descriptions are used to simulate random faults in the system.
	 */
	
	/**
     * Generates and returns a random fault description from a predefined set of possible faults.
     * 
     * @return a random fault description.
     */
	public static String getRandomFaultDescription() {
        Random random = new Random();
        int index = random.nextInt(FAULT_DESCRIPTIONS.size());
        return FAULT_DESCRIPTIONS.get(index);
    }
    
    /**
     * Returns a string representation of the Fault object, including the fault description and 
     * the date and time when the fault occurred.
     * 
     * @return a string representation of the Fault.
     */
    @Override
    public String toString() {
        return "Fault: " + "\"" + description + "\"" + " at " + dateTime;
    }
    
    /**
     * Creates and returns a copy (clone) of this Fault object.
     * 
     * @return a clone of this Fault.
     * @throws CloneNotSupportedException if cloning is not supported.
     */
    public Fault clone() throws CloneNotSupportedException {
        return (Fault) super.clone(); 
    }
	
}
