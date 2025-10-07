package epj2.service.report;

import java.util.List;
import java.util.Map;

import epj2.model.vehicle.*;
import epj2.util.*;

/**
 * This abstract class serves as a base for generating various types of reports related to vehicle rentals.
 * It provides attributes and methods that are shared across different report types.
 * 
 * @author Jelena Maletić
 * @version 29.8.2024.
 */
public abstract class Report {
	/**
	 * Manages the properties file used by the Report class.
	 * This instance is used to load configurations.
	 */
	protected static PropertiesManager propertiesManager;
	/**
     * A map containing vehicle data, where the key is the vehicle ID and the value is the {@link Vehicle} object.
     */
    protected Map<String, Vehicle> vehicles;
    /**
     * A list of {@link InvoiceParser} objects representing the parsed invoice data.
     * This list is used to analyze and generate reports based on invoice information.
     */
    protected List<InvoiceParser> invoices;
    
    // Static block to initialize propertiesManager
    static {
    	propertiesManager = new PropertiesManager("reportConfig.properties");
    }
    
    /**
     * Constructs a new report with the specified vehicle data and invoice information. 
     * Initializes the {@link PropertiesManager} with the specified properties file for reports (tax percentage,repair costs...)
     *
     * @param vehicles a map containing vehicle data, where the key is the vehicle ID and the value is the {@link Vehicle} object.
     * @param invoices a list of {@link InvoiceParser} objects representing the parsed invoice data.
     */
    public Report(Map<String, Vehicle> vehicles, List<InvoiceParser> invoices) {
        this.vehicles = vehicles;
        this.invoices = invoices;
    }
    
    /**
     * Calculates the total revenue by summing up the total amounts from all invoices
     * 
     * @param invoiceList a list of {@link InvoiceParser} objects representing the parsed invoice data.
     * @return the total revenue
     */
    protected double calculateTotalRevenue(List<InvoiceParser> invoiceList) {
        return invoiceList.stream().mapToDouble(InvoiceParser::getTotalAmount).sum();
    }
    
    /**
     * Calculates the total discount by summing up the discounts from all invoices
     * 
     * @param invoiceList a list of {@link InvoiceParser} objects representing the parsed invoice data.
     * @return the total discount amount
     */
    protected double calculateTotalDiscount(List<InvoiceParser> invoiceList) {
        return invoiceList.stream().mapToDouble(InvoiceParser::getDiscountAmount).sum();
    }
    
    /**
     * Calculates the total promotion by summing up the promotions from all invoices
     * 
     * @param invoiceList a list of {@link InvoiceParser} objects representing the parsed invoice data.
     * @return the total promotion amount
     */
    protected double calculateTotalPromotion(List<InvoiceParser> invoiceList) {
        return invoiceList.stream().mapToDouble(InvoiceParser::getPromotionAmount).sum();
    }
    
    /**
     * Calculates the total revenue for a specified city zone by summing the total amounts of all invoices
     * within that city zone. 
     * 
     * @param invoiceList a list of {@link InvoiceParser} objects representing the parsed invoice data.
     * @param cityZone the city zone for which the total revenue is to be calculated
     * @return the total revenue for the specified city zone
     */
    protected double calculateCityAreaTotals(List<InvoiceParser> invoiceList, String cityZone) {
        return invoiceList.stream().filter(invoice -> invoice.getCityZone().equals(cityZone)).mapToDouble(InvoiceParser::getTotalAmount).sum();
    }
    
    /**
     * Calculates the maintenance cost as 20% of the total revenue from all invoices.
     *
     * @param invoiceList a list of {@link InvoiceParser} objects representing the parsed invoice data.
     * @return the total maintenance cost
     */
    protected double calculateMaintenanceCost(List<InvoiceParser> invoiceList) {
    	return calculateTotalRevenue(invoiceList) * propertiesManager.getPropertyAsDouble("MAINTENANCE_COEF");
    }
    
    /**
     * Calculates the total repair cost.
     * This method filters the list of parsed invoices to include only those where the vehicle had a fault.
     * For each faulty vehicle, it retrieves the purchase price and calculates the repair cost using
     * a repair cost factor specific to the type of vehicle. The total repair cost is the sum of these
     * individual repair costs.
     *
     * @param invoiceList a list of {@link InvoiceParser} objects representing the parsed invoice data.
     * @return the total repair cost for all vehicles with reported faults.
     */
    protected double calculateRepairCost(List<InvoiceParser> invoiceList) {
        return invoiceList.stream()
                .filter(InvoiceParser::getHasFault)
                .mapToDouble(invoice -> {
                    Vehicle vehicle = vehicles.get(invoice.getVehicleID());
                    if (vehicle != null) {
                        double vehicleCost = vehicle.getPurchasePrice();
                        return getRepairCostFactor(vehicle) * vehicleCost;
                    }
                    return 0.0;
                })
                .sum();
    }
    
    /**
     * Returns a repair cost factor specific to the type of vehicle.
     * 
     * @param vehicle vehicle for which the repair cost factor is determined
     * @return repair cost factor specific to the type of vehicle
     */
    protected double getRepairCostFactor(Vehicle vehicle) {
        if (vehicle instanceof Car) {
            return propertiesManager.getPropertyAsDouble("CAR_REPAIR_COEF");
        } else if (vehicle instanceof Bike) {
            return propertiesManager.getPropertyAsDouble("BIKE_REPAIR_COEF");
        } else if (vehicle instanceof Scooter) {
            return propertiesManager.getPropertyAsDouble("SCOOTER_REPAIR_COEF");
        }
        return 0.0;
    }
    
    /**
     * Generates the report based on the collected data.
     * This is an abstract method that must be implemented by subclasses to provide specific report
     * generation logic.
     */
    protected abstract void generateReport();

}
