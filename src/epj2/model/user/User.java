package epj2.model.user;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Represents a user in the electric vehicle rental simulation system.
 * This is an abstract class that holds common properties and methods
 * for different types of users
 * It implements the {@link Cloneable} interface to allow cloning.
 * 
 * @author Jelena Maletić
 * @version 1.9.2024.
 */

public abstract class User implements Cloneable{
	/** The name of the user. */
	protected String name;
	/** The unique document number for the user. */
    protected String documentNumber;
    /** The type of document held by the user. */
    protected DocumentType documentType;
    /** The driving license number of the user. */
    protected String drivingLicenseNumber;
    /** The number of rentals made by the user. */
    protected int numberOfRentals;
    /** A set to ensure the uniqueness of document numbers across users. */
    protected static Set<String> uniqueDocumentNumbers = new HashSet<>();

    /**
     * Constructs a new user with the specified name.
     * The user's document number and driving license number are both generated uniquely and randomly.
     * The number of rentals is initialized to zero.
     *
     * @param name the name of the user.
     */
    public User(String name) {
        this.name = name;
        this.documentNumber=generateUniqueDocumentNumber();
        this.drivingLicenseNumber=generateUniqueDocumentNumber();
        this.numberOfRentals = 0;
    }
    
    /**
     * Returns the name of the user.
     *
     * @return the name of the user.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets a new name for the user.
     *
     * @param name the new name to be assigned to the user.
     */
    public void setName(String name) {
        this.name = name;
    }
    

    /**
     * Returns the number of rentals made by the user.
     *
     * @return the number of rentals made by the user.
     */
    public int getNumberOfRentals() {
    	return numberOfRentals;
    }
    
    /**
     * Sets the number of rentals made by the user.
     *
     * @param numberOfRentals the number of rentals to be assigned to the user.
     */
    public void setNumberOfRentals(int numberOfRentals) {
    	this.numberOfRentals = numberOfRentals;
    }
    
    /**
     * Returns the document number of the user.
     *
     * @return the document number of the user.
     */
    public String getDocumentNumber() {
        return documentNumber;
    }
    
    /**
     * Returns the driving license number of the user.
     *
     * @return the driving license number of the user.
     */
    public String getDrivingLicenseNumber() {
    	return drivingLicenseNumber;
    }
    
    /**
     * Generates a unique document number that is not already in use. This method creates a random 
     * 9-digit document number and ensures its uniqueness by checking it against a set of previously 
     * used numbers. If the generated number is not unique, the method will continue to generate new 
     * numbers until a unique one is found. Once a unique number is generated, it is added to the 
     * static set of used document numbers to maintain uniqueness within the class.ument numbers to maintain uniqueness within the class.
     * 
     * @return a unique document number.
     */
    private String generateUniqueDocumentNumber() {
        Random random = new Random();
        String newDocumentNumber;
        int numberLength = 9; 
        do {
            StringBuilder number = new StringBuilder();
            for (int i = 0; i < numberLength; i++) {
                number.append(random.nextInt(10)); 
            }
            newDocumentNumber = number.toString();
        } while (uniqueDocumentNumbers.contains(newDocumentNumber));
        uniqueDocumentNumbers.add(newDocumentNumber);
        return newDocumentNumber;
    }
    
    /**
     * Determines if the user is eligible for a discount.
     * A discount is given if the number of rentals is a multiple of 10.
     *
     * @return true if the user is eligible for a discount, false otherwise.
     */
    public boolean ishasDiscount () {
    	if(numberOfRentals % 10 == 0) {
    		return true;
    	}
    	else return false;
    }
    
    /**
     * Creates and returns a clone of this user.
     *
     * @return a clone of this user.
     * @throws CloneNotSupportedException if the cloning is not supported.
     */
    @Override
    public User clone() throws CloneNotSupportedException{
    	return (User) super.clone();
    }
    
    /**
     * Returns the type of document held by the user.
     * This method must be implemented by subclasses.
     *
     * @return the document type of the user.
     */
    public abstract DocumentType getDocumentType();

}
