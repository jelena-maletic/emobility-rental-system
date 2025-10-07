package epj2.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import epj2.model.user.*;
import epj2.model.vehicle.*;
import epj2.util.PropertiesManager;

/**
 * Represents an invoice generated for a rental transaction. This class handles the creation of invoices, 
 * including storing the invoices in a specified directory.
 *
 * @author Jelena Maletić
 * @version 1.9.2024.
 */
public class Invoice {
	/**
	 * Manages the properties file used by the Invoice class.
	 * This instance is used to load configurations such as file paths for storing invoices.
	 */
	private static PropertiesManager propertiesManager;
	/**
	 * Path to the directory where invoices are stored.
	 * This path is retrieved from the properties file.
	 */
	private static String invoicesDirPath;
	/**
	 * Counter for generating unique invoice numbers.
	 * This static field is incremented with each new Invoice instance to ensure each invoice has a unique number.
	 */
	private static int invoiceCounter = 0;
	/** The unique number assigned to invoice. */
	private int invoiceNumber;
    /** The rental transaction associated with this invoice. */
    private Rental rental;
    
    // Static block to initialize propertiesManager and invoicesDirPath
    static {
    	propertiesManager = new PropertiesManager("filePaths.properties");
        invoicesDirPath = propertiesManager.getProperty("INVOICES_DIR");
    }
    
    /**
     * Constructs an Invoice for a specified rental transaction. Initializes the price calculation instance, assigns a unique
     * invoice number, and sets up the directory for storing invoices.
     * 
     * @param rental the rental transaction for which the invoice is being created.
     */
    public Invoice(Rental rental) {
        invoiceCounter++;
        this.invoiceNumber = invoiceCounter;
        this.rental = rental;
        File folder = new File(invoicesDirPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
    
    /**
     * Generates an invoice for a rental transaction and writes it to a file.
     * The invoice file is created in the specified directory with a filename based on the user's name 
     * and rental date and time. 
     * Invoice file is in .txt format.
     *
     * Details included in the invoice:
     * -User's name and document details if required
     * -Rented vehicle type and ID
     * -Start and end locations of the rental
     * -City zone (wide or narrow area) affecting pricing
     * -Duration of the rental in seconds.
     * -Base price calculation
     * -Distance price
     * -Discounts and promotions applied
     * -Total amount due for payment
     * -Date and time of invoice issuance
     * -Invoice number
     * -Information about faults, if any have occurred
     *
     * @param isWideArea Indicates whether the rental occurred in a wide area of the city.
     *                   This affects the pricing calculations based on the area of the city.
     */
    public void generateInvoice(boolean isWideArea) {
    	Vehicle vehicle = rental.getVehicle();
    	User user = rental.getUser();
    	String userName = user.getName();
    	boolean hasDiscount = user.ishasDiscount();
    	LocalDateTime dateTime = rental.getDateTime();
    	int[] startLocation = rental.getStartLocation();
    	int[] endLocation = rental.getEndLocation();
    	double durationSeconds = rental.getDurationSeconds();
    	boolean hasPromotion = rental.isHasPromotion();
        String invoiceFileName = generateInvoiceFileName(userName, dateTime);
        File invoiceFile = new File(invoicesDirPath + File.separator + invoiceFileName);
        try {
        	PrintWriter outInvoice = new PrintWriter(new BufferedWriter(new FileWriter(invoiceFile)));
            outInvoice.println("======================================================");
            outInvoice.println("              e-mobility company ePJ2");
            outInvoice.println("                     Java City");
            outInvoice.println("------------------------------------------------------");
            outInvoice.println("			  INVOICE");
            outInvoice.println("User: " + userName);
            if (vehicle.requiresDocuments()) {
            	if(user.getDocumentType().equals(DocumentType.PASSPORT)) {
            		outInvoice.println("Passport number " + user.getDocumentNumber());
            	}
            	else if(user.getDocumentType().equals(DocumentType.ID_CARD)) {
            		outInvoice.println("ID card number " + user.getDocumentNumber());
            	}
            	outInvoice.println("Driver’s license number " + user.getDrivingLicenseNumber());
            }
            outInvoice.println("------------------------------------------------------");
            outInvoice.print("Rented vehicle: ");
            if (vehicle instanceof Car) {
                outInvoice.println("Car " + vehicle.getModel() + "," + vehicle.getID());
            } 
            else if (vehicle instanceof Bike) {
            	outInvoice.println("Bike " + vehicle.getModel() + "," + vehicle.getID());
            } 
            else if (vehicle instanceof Scooter) {
            	outInvoice.println("Scooter " + vehicle.getModel() + "," + vehicle.getID());
            }
            outInvoice.println("Start location: (" + startLocation[0] + "," + startLocation[1] + ")");
            outInvoice.println("Destination: (" + endLocation[0] + "," + endLocation[1] + ")");
            outInvoice.print("City zone: ");
            if(isWideArea) {
            	outInvoice.println("wide city area");
            }
            else outInvoice.println("narrow city area");
            outInvoice.println("Ride duration [s]: " + durationSeconds);
            outInvoice.println("------------------------------------------------------");
            double basePrice = PriceCalculation.calculateBasePrice(vehicle, durationSeconds);
            double distancePrice = PriceCalculation.calculateDistancePrice(basePrice, isWideArea);
            double discountAmount = PriceCalculation.calculateDiscount(distancePrice, hasDiscount);
            double promotionAmount = PriceCalculation.calculatePromotion(distancePrice, hasPromotion);
            outInvoice.println("Base price: " + PriceCalculation.calculateUnitPrice(vehicle) + " * " + durationSeconds + " = " + basePrice);
            if(isWideArea) {
            	outInvoice.println("Rate for wide area of the city: " + PriceCalculation.getWideAreaFactor());
            }
            else outInvoice.println("Rate for narrow area of the city: " + PriceCalculation.getNarrowAreaFactor());
            outInvoice.println("Amount: " + PriceCalculation.calculateDistancePrice(basePrice, isWideArea) + " EUR");
            if(hasDiscount) {
            	outInvoice.println("Discount: " + PriceCalculation.getDiscountPercentage() + "% (" + discountAmount + " EUR)");
            }
            if(hasPromotion) {
            	outInvoice.println("Promotion: " + PriceCalculation.getPromotionPercentage() + "% (" + promotionAmount + " EUR)");
            }
            outInvoice.println("------------------------------------------------------");
            outInvoice.println("Total price: " + PriceCalculation.calculateTotalPrice(distancePrice, discountAmount, promotionAmount) + " EUR");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy/HH-mm");
            String formattedDateTime = dateTime.format(formatter);
            outInvoice.println("Date and time: " + formattedDateTime);
            outInvoice.println("Invoice number: " + invoiceNumber);
            if(vehicle.getFault()!=null) {
            	outInvoice.println("------------------------------------------------------");
            	outInvoice.println("Fault: " + vehicle.getFault().getDescription());
            	outInvoice.println("We apologize for the inconvenience. ");
            }
            outInvoice.println("======================================================");
            outInvoice.println("           *THANK YOU FOR USING OUR SERVICE*");
            outInvoice.println("------------------------------------------------------");
            outInvoice.close();
        }
        catch(Exception ex) {
        	ex.printStackTrace();
        }
    }

    /** 
     * Generates the invoice name based on the user's name, date and time of issuance, and unique invoice number.
     * File is in .txt format.
     * 
     * @param userName the name of the user to whom this invoice is issued.
     * @param dateTime date and time of invoice issuance
     * @return name of the invoice file
     */
    private String generateInvoiceFileName(String userName, LocalDateTime dateTime) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy_HH-mm");
        String formattedDateTime = dateTime.format(formatter);
        return formattedDateTime + "_" + userName + invoiceNumber + ".txt";
    }
}

