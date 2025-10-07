package epj2.model.vehicle;

/**
 * Represents a scooter in the vehicle rental system.
 * This class extends {@link Vehicle} and adds specific properties related to scooters.
 * It implements the {@link Cloneable} interface to allow cloning.
 * 
 * @author Jelena Maletić
 * @version 29.8.2024.
 */
public class Scooter extends Vehicle {
	/**
	 * Serial version UID for serialization.
	 * This value is used to verify the compatibility of serialized data during deserialization.
	 */
	private static final long serialVersionUID = 1L;
	 /** The maximum speed of the scooter. */
	private int maxSpeed;
	
	/**
	 * Constructs a new vehicle with the specified attributes 
	 * (ID, producer, purchase price, model, maximum speed).
	 * 
	 * @param ID the unique identifier for the vehicle.
	 * @param producer the manufacturer or producer of the vehicle.
	 * @param purchasePrice the price at which the vehicle was purchased.
	 * @param model the model of the vehicle.
	 * @param maxSpeed the maximum speed of the scooter.
	 */
	public Scooter(String ID,String producer,double purchasePrice,String  model, int maxSpeed) {
		super(ID,producer,purchasePrice,model);
		this.maxSpeed=maxSpeed;
	}
	
	/**
     * Returns the maximum speed of the scooter.
     * 
     * @return the maximum speed.
     */
	public int getMaxSpeed() {
		return maxSpeed;
	}
	
	/**
     * Sets a new maximum speed for the scooter.
     *
     * @param maxSpeed the new ID to be assigned to the scooter.
     */
	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
	/**
     * Returns a string representation of this scooter.
     * The string includes the vehicle's ID, producer, purchase price, model and maximum speed.
     * 
     * @return a string representation of the scooter.
     */
    @Override
    public String toString() {
        return super.toString() + ", max speed: " + "\"" + maxSpeed + "\"" ;
    }
    
    /**
     * Creates and returns a clone of this scooter.
     * The clone is a copy of the current scooter with the same properties.
     * 
     * @return a clone of this scooter.
     * @throws RuntimeException if cloning is not supported.
     */
    @Override
    public Scooter clone() {
        try {
            return (Scooter) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning failed", e);
        }
    }
    

    /**
     * Determines whether documents are required to operate scooter.
     * 
     * @return {@code false} because documents are not required to operate scooter.
     */
    @Override
    public boolean requiresDocuments() {
        return false;
    }
}
