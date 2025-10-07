package epj2.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for parsing invoice data from txt files.
 * It extracts and processes only the information relevant for analyzing business performance.
 * 
 * @author Jelena Maletić
 * @version 29.8.2024.
 */
public class InvoiceParser {
	/**
	 * Manages the properties file used by the InvoiceParser class.
	 * This instance is used to load invoices file path.
	 */
	private static PropertiesManager propertiesManager;
	/**
     * The total amount to be paid, as calculated from the invoice.
     */
	private double totalAmount;
	/**
     * The ID of the vehicle related to this invoice.
     */
    private String vehicleID;
    /**
     * The promotion amount applied to the total amount. 
     * Initialized to 0.0 if no discount is applied.
     */
    private double promotionAmount = 0.0;
    /**
     * The discount amount applied to the total amount. 
     * Initialized to 0.0 if no discount is applied.
     */
    private double discountAmount = 0.0;
    /**
     * The city zone in which the vehicle was used.
     */
    private String cityZone;
    /**
     * The date and time when the invoice was issued.
     */
    private LocalDateTime issueDate;
    /**
     * A flag indicating whether there was a fault related to the vehicle during the rental period.
     * Initialized to false if no fault occurred.
     */
    private boolean hasFault = false;
    
    // Static block to initialize propertiesManager
    static {
    	propertiesManager = new PropertiesManager("filePaths.properties");
    }
    
    /**
     * Constructs an InvoiceParser instance and parses the invoice data from the specified file.
     *
     * @param filePath the path to the file containing the invoice data.
     */
    public InvoiceParser(String filePath) {
        parseInvoiceData(filePath);
    }
    
    /**
     * Parses the invoice data from the specified file.
     * The method reads the file line by line and extracts relevant information for business analysis.
     * It looks for specific keywords to identify and parse the required data
     * 
     * @param filePath the path to the file containing the invoice data.
     */
    private void parseInvoiceData(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Total price:")) {
                    this.totalAmount = Double.parseDouble(line.split(":")[1].trim().replace("EUR", ""));
                } 
                else if (line.contains("Rented vehicle:")) {
                	this.vehicleID = line.split(",")[1].trim();
                } 
                else if (line.contains("Promotion:")) {
                    this.promotionAmount = Double.parseDouble(line.split("\\(")[1].replace("EUR)", "").trim());
                } 
                else if (line.contains("Discount:")) {
                    this.discountAmount = Double.parseDouble(line.split("\\(")[1].replace("EUR)", "").trim());
                } 
                else if (line.contains("City zone:")) {
                    this.cityZone = line.split(":")[1].trim();
                } 
                else if (line.contains("Date and time:")) {
                    String dateStr = line.split(":")[1].trim();
                    this.issueDate = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("dd.MM.yyyy/HH-mm"));
                }
                else if (line.contains("Fault:")) {
                	this.hasFault = true;
                }
            }
        }
        catch(Exception ex) {
        	ex.printStackTrace();
        }
    }
    
    /**
      * Parses all invoice files in the specified directory and returns a list of {@link InvoiceParser} objects.
      * 
      * The method searches the specified directory for all text files (files with a ".txt" extension),
      * then creates an {@link InvoiceParser} instance for each file and adds it to a list.
      * If no files are found in the directory, a message is printed to the console. 
      * @return A list of {@link InvoiceParser} objects, each representing an invoice parsed from a file.
      */
    @SuppressWarnings("unused")
	public static List<InvoiceParser> parseAllInvoices() {
        List<InvoiceParser> invoices = new ArrayList<>();
        File folder = new File(propertiesManager.getProperty("INVOICES_DIR"));
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files != null) {
            for (File file : files) {
                InvoiceParser parser = new InvoiceParser(file.getPath());
                invoices.add(parser);
            }
        } else {
            System.out.println("No files in the directory " + propertiesManager.getProperty("INVOICES_DIR"));
        }
        return invoices;
    }
    
    /**
     * Returns the total amount to be paid, as parsed from the invoice.
     *
     * @return the total amount.
     */
    public double getTotalAmount() { 
    	return totalAmount; 
    }
    
    /**
     * Returns the ID of the vehicle associated with this invoice.
     *
     * @return the vehicle ID.
     */
    public String getVehicleID() { 
    	return vehicleID; 
    }
    
    /**
     * Returns the promotion amount applied to the invoice.
     *
     * @return the promotion amount.
     */
    public double getPromotionAmount() { 
    	return promotionAmount; 
    }
    
    /**
     * Returns the discount amount applied to the invoice.
     *
     * @return the discount amount.
     */
    public double getDiscountAmount() { 
    	return discountAmount; 
    }
    
    /**
     * Returns the city zone in which the vehicle was used.
     *
     * @return the city zone.
     */
    public String getCityZone() { 
    	return cityZone; 
    }
    
    /**
     * Returns the date and time when the invoice was issued.
     *
     * @return the issue date and time of invoice.
     */
    public LocalDateTime getIssueDate() { 
    	return issueDate; 
    }
    
    /**
     * Returns whether the vehicle had a fault during the rental period.
     *
     * @return {@code true} if the vehicle had a fault, {@code false} otherwise.
     */
    public boolean getHasFault() {
    	return hasFault;
    }

}
