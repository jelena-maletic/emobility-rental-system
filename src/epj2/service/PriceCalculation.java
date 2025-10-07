package epj2.service;

import epj2.model.vehicle.*;
import epj2.util.PropertiesManager;

/**
 * This class handles the financial calculations associated with the rental process
 * This class calculates rental prices for different types of vehicles, including the amounts for 
 * discounts and promotions.
 * 
 * @author Jelena Maletić
 * @version 1.9.2024.
 */
public class PriceCalculation {
	/**
	 * Manages the properties configuration. This instance of {@link PropertiesManager}
	 * is responsible for accessing the configuration settings stored in the properties file.
	 */
	private static PropertiesManager propertiesManager;
	
	 // Static block to initialize propertiesManager
	static {
		 propertiesManager = new PropertiesManager("rentalPricing.properties");
	}
	
    /**
     * Determines the unit rental price of a vehicle based on its type using data from the properties file.
     * 
     * @param vehicle the vehicle for which the unit price is being calculated
     * @return the unit rental price for the specified vehicle type
     * @throws IllegalArgumentException if the vehicle type is unknown
     */
    public static double calculateUnitPrice(Vehicle vehicle) {
        if (vehicle instanceof Car) {
            return propertiesManager.getPropertyAsDouble("CAR_UNIT_PRICE");
        } 
        else if (vehicle instanceof Bike) {
            return propertiesManager.getPropertyAsDouble("BIKE_UNIT_PRICE");
        } 
        else if (vehicle instanceof Scooter) {
            return propertiesManager.getPropertyAsDouble("SCOOTER_UNIT_PRICE");
        } 
        else {
            throw new IllegalArgumentException("Unknown vehicle type");
        }
    }
    
    /**
     * Calculates the base rental price of a vehicle by multiplying the unit price by the rental duration (in seconds).
     * If the vehicle has a fault, the base price is set to 0.0.
     * 
     * @param vehicle the vehicle for which the base rental price is being calculated
     * @param durationSeconds the duration of the rental in seconds
     * @return the base rental price for the specified vehicle
     */
    public static double calculateBasePrice(Vehicle vehicle, double durationSeconds) {
    	if(vehicle.getFault()==null) {
    		return calculateUnitPrice(vehicle)*durationSeconds;
    	}
    	else return 0.0;
    }
    
    /**
     * Returns the value of the price factor for the wide part of the city using data from the properties file.
     * 
     * @return the price factor for the wide area
     */
    public static double getWideAreaFactor() {
    	return propertiesManager.getPropertyAsDouble("DISTANCE_WIDE");
    }
    
    /**
     * Returns the value of the price factor for the narrow part of the city using data from the properties file.
     * 
     * @return the price factor for the narrow area
     */
    public static double getNarrowAreaFactor() {
    	return propertiesManager.getPropertyAsDouble("DISTANCE_NARROW");
    }
    
    /**
     * Calculates the distance-based rental price by applying a distance factor to the base price.
     * The distance factor is determined based on whether the rental area is wide or narrow.
     * 
     * @param basePrice the base rental price of the vehicle
     * @param isWideArea a boolean indicating whether the rental area is wide or narrow
     * @return the distance-based rental price for the vehicle
     */
    public static double calculateDistancePrice(double basePrice, boolean isWideArea) {
    	double distanceFactor = isWideArea ? getWideAreaFactor() : getNarrowAreaFactor();
    	return basePrice * distanceFactor;
    }
    
    /**
     * Returns the discount percentage using data from the properties file.
     * 
     * @return the discount percentage
     */
    public static double getDiscountPercentage() {
    	return propertiesManager.getPropertyAsDouble("DISCOUNT");
    }
    
    /**
     * Calculates the discount amount based on the distance price and whether a discount is applicable.
     * 
     * @param distancePrice the base price after considering the distance factor.
     * @param hasDiscount a boolean indicating whether a discount should be applied.
     * @return the discount amount if applicable; otherwise, returns 0.0.
     */
    public static double calculateDiscount(double distancePrice, boolean hasDiscount) {
    	if(hasDiscount) {
    		return distancePrice * getDiscountPercentage() / 100.0;
    	}
    	else return 0.0;
    }
    
    /**
     * Returns the promotion percentage using data from the properties file.
     * 
     * @return the promotion percentage
     */
    public static double getPromotionPercentage() {
    	return propertiesManager.getPropertyAsDouble("DISCOUNT_PROM");
    }
    
    /**
     * Calculates the promotion amount based on the distance price and whether a promotion is applicable.
     * 
     * @param distancePrice the base price after considering the distance factor.
     * @param hasPromotion a boolean indicating whether a discount should be applied.
     * @return the promotion amount if applicable; otherwise, returns 0.0.
     */
    public static double calculatePromotion(double distancePrice, boolean hasPromotion) {
    	if(hasPromotion) {
    		return distancePrice * getPromotionPercentage() / 100.0;
    	}
    	else return 0.0;
    }
    
    /**
     * Calculates the total rental price by subtracting the discount and promotion amounts from the distance price.
     * 
     * @param distancePrice the base price after applying the distance factor.
     * @param discountAmount the amount of discount applied.
     * @param promotionAmount the amount of promotion applied.
     * @return the total rental price after applying the discount and promotion.
     */
    public static double calculateTotalPrice(double distancePrice, double discountAmount, double promotionAmount) {
        return distancePrice - discountAmount - promotionAmount;
    }
    
    
}
