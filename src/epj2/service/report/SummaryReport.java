package epj2.service.report;

import java.io.*;
import java.util.List;
import java.util.Map;

import epj2.model.vehicle.*;
import epj2.util.InvoiceParser;
import epj2.util.PropertiesManager;

/**
 * A concrete implementation of the {@link Report} class that generates a summary report.
 * This class collects various financial and operational metrics from the invoices and vehicles,
 * calculates relevant values, and generates a summary report in the form of a text file.
 * The report also can be displayed on a graphical user interface (GUI), as there are methods available
 * to retrieve and format report data.
 * 
 * @author Jelena Maletić
 * @version 1.9.2024. 
 */
public class SummaryReport extends Report {
	/** The collected report data as a string. */
    private String reportData;
    /**
	 * Manages the properties file used by the SummaryReport class.
	 * This instance is used to load directory path for storing file with summary report.
	 */
    private PropertiesManager propertiesManagerSR;
    
    /**
     * Constructs a new {@link SummaryReport} instance, initializes the properties manager 
     * (used to load directory path for storing summary report), collects report data, and generates the report.
     *
     * @param vehicles a map containing vehicle data, where the key is the vehicle ID and the value is the {@link Vehicle} object.
     * @param invoices a list of {@link InvoiceParser} objects representing the parsed invoice data.
     */
    public SummaryReport(Map<String, Vehicle> vehicles, List<InvoiceParser> invoices) {
        super(vehicles, invoices);
        propertiesManagerSR = new PropertiesManager("filePaths.properties");
        reportData = collectReportData();  
        generateReport();  
    }
    
    /**
     * Calculates the total company expenses.
     * 
     * @param invoiceList a list of {@link InvoiceParser} objects representing the parsed invoice data.
     * @return the total company expenses.
     */
    private double calculateCompanyExpenses(List<InvoiceParser> invoiceList) {
        return calculateTotalRevenue(invoiceList) * (propertiesManager.getPropertyAsDouble("EXPENSES_PERCENTAGE")/100.0);
    }
    
    /**
     * Calculates the total tax based on the total revenue minus maintenance cost,
     * repair cost, and company expenses.
     * 
     * @param invoiceList a list of {@link InvoiceParser} objects representing the parsed invoice data.
     * @return the total tax.
     */
    private double calculateTotalTax(List<InvoiceParser> invoiceList) {
        return (calculateTotalRevenue(invoiceList) - calculateMaintenanceCost(invoiceList) - calculateRepairCost(invoiceList) - calculateCompanyExpenses(invoiceList))* (propertiesManager.getPropertyAsDouble("TAX_PERCENTAGE")/100.0);
    }
    
    /**
     * Generates the summary report and writes it to a text file in the specified directory.
     * The report file is created in the directory specified by the "summaryReportDir" property.
     * If the directory does not exist, it will be created. The report data is written to
     * "summary_report.txt".
     * In case of an I/O error during file writing, the exception is caught and its
     * stack trace is printed to the console. 
     */
    @Override
    protected void generateReport() {
    	String reportDirPath = propertiesManagerSR.getProperty("SUMMARY_REPORT_DIR");
        File reportsDir = new File(reportDirPath);
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }
        File reportFile = new File(reportsDir, "summary_report.txt");
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(reportFile)))) {
            writer.println(reportData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Collects and formats the summary report data into a string.
     * 
     * @return a string containing the formatted summary report data.
     */
    private String collectReportData() {
        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("Total revenue: ").append(calculateTotalRevenue(invoices)).append(" EUR\n");
        reportBuilder.append("Total discount: ").append(calculateTotalDiscount(invoices)).append(" EUR\n");
        reportBuilder.append("Total promotion amount: ").append(calculateTotalPromotion(invoices)).append(" EUR\n");
        reportBuilder.append("Total amount for wide city area: ").append(calculateCityAreaTotals(invoices, "wide city area")).append(" EUR\n");
        reportBuilder.append("Total amount for narrow city area: ").append(calculateCityAreaTotals(invoices, "narrow city area")).append(" EUR\n");
        reportBuilder.append("Total maintenance cost: ").append(calculateMaintenanceCost(invoices)).append(" EUR\n");
        reportBuilder.append("Total repair cost: ").append(calculateRepairCost(invoices)).append(" EUR\n");
        reportBuilder.append("Total company expenses: ").append(calculateCompanyExpenses(invoices)).append(" EUR\n");
        reportBuilder.append("Total tax: ").append(calculateTotalTax(invoices)).append(" EUR\n");

        return reportBuilder.toString();
    }
    
    /**
     * Returns the collected report data as a string.
     * 
     * @return the report data.
     */
    public String getReportData() {
        return reportData;
    }
}
