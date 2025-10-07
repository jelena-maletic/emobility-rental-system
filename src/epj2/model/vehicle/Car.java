package epj2.model.vehicle;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Represents a car in the vehicle rental system.
 * This class extends {@link Vehicle} and adds specific properties related to cars.
 * 
 * @author Jelena Maletić
 * @version 29.8.2024.
 */
public class Car extends Vehicle {
	/**
	 * Serial version UID for serialization.
	 * This value is used to verify the compatibility of serialized data during deserialization.
	 */
	private static final long serialVersionUID = 1L;
	/** The date when the car was purchased. */
	private LocalDate purchaseDate;
	/** A description of the car. */
	private String description;
	/** The maximum number of passengers the car can accommodate. */
    private int maxPassengers;
	
    /**
     * Constructs a new car with the specified arguments (ID, producer, purchase price, model, 
     * purchase date, and description). The maximum number of passengers is randomly generated.
     * 
     * @param ID the unique identifier of the car.
     * @param producer the producer of the car.
     * @param purchasePrice the purchase price of the car.
     * @param model the model of the car.
     * @param purchaseDate the date when the car was purchased.
     * @param description a description of the car.
     */
	public Car(String ID,String producer, double purchasePrice, String  model, LocalDate purchaseDate, String description) {
		super(ID,producer,purchasePrice,model);
		this.purchaseDate=purchaseDate;
		this.description=description;
		this.maxPassengers = generateRandomPassengerCount();
	}
	
	/**
     * Generates a random number of passengers the car can accommodate, between 2 and 6.
     * 
     * @return a random number of passengers (between 2 and 6).
     */
	private int generateRandomPassengerCount() {
		Random rand = new Random();
        return rand.nextInt(5) + 2; 
    }
	
	/**
     * Returns the maximum number of passengers the car can accommodate.
     * 
     * @return the maximum number of passengers.
     */
    public int getMaxPassengers() {
        return maxPassengers;
    }
    
    /**
     * Sets a new maximum speed number of passengers the car can accommodate.
     *
     * @param maxPassengers the new maximum number of passengers to set.
     */
    public void setMaxPassengers(int maxPassengers) {
        this.maxPassengers = maxPassengers;
    }
	
    /**
     * Returns the description of the car.
     * 
     * @return the description of the car.
     */
	public String getDescription() {
		return description;
	}
	
	/**
     * Sets a new description of the car.
     *
     * @param description the new description of the car.
     */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
     * Returns the date when the car was purchased.
     * 
     * @return the purchase date.
     */
	public LocalDate getPurchaseDate() {
		return purchaseDate;
	}
	
	/**
     * Sets the date when the car was purchased.
     * 
     * @param purchaseDate the purchase date
     */
	public void setPurchaseDate(LocalDate purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	
	/**
     * Returns a string representation of the car, including its ID, producer, purchase price, model,
     * description, purchase date, and maximum number of passengers. The purchase date is formatted 
     * as "dd.MM.yyyy".
     * 
     * @return a string representing the car.
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate;
        if(purchaseDate!=null) {
        	formattedDate = purchaseDate.format(formatter);
        }
        else {
        	formattedDate = "Unknown";
        }
        return super.toString() + ", description: \"" + description + "\"" + ", purchase date: \"" + formattedDate + "\"" + ", maximum passengers: \"" + maxPassengers + "\"";
    }
    
    /**
     * Creates and returns a clone of this car.
     * The clone is a copy of the current car with the same properties.
     * 
     * @return a clone of this car.
     * @throws RuntimeException if cloning is not supported.
     */
    @Override
    public Car clone() {
        try {
            return (Car) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning failed", e);
        }
    }
	
    /**
     * Determines whether documents are required to operate car.
     * 
     * @return {@code true} because documents are required to operate car.
     */
    @Override
    public boolean requiresDocuments() {
        return true;
    }
	
}
