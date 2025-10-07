package epj2.model.vehicle;

/**
 * Represents a bike in the vehicle rental system.
 * This class extends {@link Vehicle} and adds specific properties related to bikes.
 * 
 * @author Jelena Maletić
 * @version 1.9.2024.
 */
public class Bike extends Vehicle {
	/**
	 * Serial version UID for serialization.
	 * This value is used to verify the compatibility of serialized data during deserialization.
	 */
	private static final long serialVersionUID = 1L;
	/** The range of the bike per full charge in units of distance. */
	private int rangePerCharge;
	/** The distance covered by the bike so far in units of distance. */
	private int distanceCovered;
	
	/**
    * Constructs a new bike with the specified arguments (ID, producer, purchase price, model, 
    * and range per charge). The distance covered is initialized to zero.
    * 
    * @param ID the unique identifier of the bike.
    * @param producer the producer of the bike.
    * @param purchasePrice the purchase price of the bike.
    * @param model the model of the bike.
    * @param rangePerCharge the range of the bike per full charge.
    */
	public Bike(String ID, String producer, double purchasePrice,String  model, int rangePerCharge) {
		super(ID,producer,purchasePrice,model);
		this.rangePerCharge=rangePerCharge;
	}
	
    /**
     * Returns the range of the bike per full charge.
     * 
     * @return the range per charge in units of distance.
     */
	public int getRangePerCharge() {
		return rangePerCharge;
	}
	
	/**
     * Sets a new range of the bike per full charge.
     *
     * @param rangePerCharge the new range of the bike per full charge.
     */
	public void setRangePerCharge(int rangePerCharge) {
		this.rangePerCharge = rangePerCharge;
	}
	
	/**
     * Returns the distance covered by the bike so far.
     * 
     * @return the distance covered in units of distance.
     */
	public int getDistanceCovered() {
		return distanceCovered;
	}
	
	/**
     * Sets the distance covered by the bike so far.
     *
     * @param distanceCovered the distance covered in units of distance
     */
	public void setDistanceCovered(int distanceCovered) {
		this.distanceCovered = distanceCovered;
	}
	
	/**
	 * Updates the battery level based on the range per charge and the current distance covered. 
	 * If the distance covered is equal to or greater than the range per charge, the battery level is set to 0. 
	 * Otherwise, the battery level is adjusted proportionally to reflect the distance traveled relative to the total range.
	 */
	@Override
    public void decreaseBatteryLevel() {
		if(rangePerCharge == 0) {
			currentBatteryLevel -= 5;
		}
		else {
			if (distanceCovered >= rangePerCharge) {
	            currentBatteryLevel = 0;
	        } 
			else {
	            currentBatteryLevel = (int) (100 - (distanceCovered / rangePerCharge * 100));
	        }
		}
		if (currentBatteryLevel < 0) {
            currentBatteryLevel = 0;
        }
    }
	
	/**
     * Returns a string representation of the bike, including its ID, producer, purchase price, model,
     * and range per charge.
     * 
     * @return a string representing the bike.
     */
    @Override
    public String toString() {
        return super.toString() + ", range per charge: " + "\"" + rangePerCharge + "\"";
    }
    
    /**
     * Creates and returns a clone of this bike.
     * The clone is a copy of the current bike with the same properties.
     * 
     * @return a clone of this bike.
     * @throws RuntimeException if cloning is not supported.
     */
    @Override
    public Bike clone() {
        try {
            return (Bike) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning failed", e);
        }
    }
    
    /**
     * Determines whether documents are required to operate bike.
     * 
     * @return {@code false} because documents are not required to operate bike.
     */
    @Override
    public boolean requiresDocuments() {
        return false;
    }

}
