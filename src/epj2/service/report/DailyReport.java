package epj2.service.report;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import epj2.model.vehicle.*;
import epj2.util.*;

/**
 * A concrete implementation of the {@link Report} class that generates the daily reports.
 * This class collects various financial and operational metrics from the invoices and vehicles,
 * calculates relevant values, and generates daily reports in the form of a text file.
 * The reports also can be displayed on a graphical user interface (GUI), as there are methods available
 * to retrieve and format report data.
 * 
 * @author Jelena Maletić
 * @version 31.8.2024. 
 */
public class DailyReport extends Report {
	/**
	 * A map storing report data categorized by date.
	 * This map uses {@link LocalDate} as the key to represent the date of the report entry,
	 * and {@link String} as the value to store the report data for that date.
	 */
    private Map<LocalDate, String> reportData;
    /**
   	 * Manages the properties file used by the DailyReport class.
   	 * This instance is used to load directory path for storing files with daily reports.
   	 */
    private PropertiesManager propertiesManagerDR;
    
    /**
     * Constructs a new {@link DailyReport} instance, initializes the properties manager 
     * (used to load directory path for storing daily reports), collects reports data, and generates the reports.
     *
     * @param vehicles a map containing vehicle data, where the key is the vehicle ID and the value is the {@link Vehicle} object.
     * @param invoices a list of {@link InvoiceParser} objects representing the parsed invoice data.
     */
    public DailyReport(Map<String, Vehicle> vehicles, List<InvoiceParser> invoices) {
        super(vehicles, invoices);
        propertiesManagerDR = new PropertiesManager("filePaths.properties");
        reportData = collectReportData();  
        generateReport();  
    }

    /**
     * Generates the daily reports and writes it to a text file in the specified directory.
     * The report files are created in the directory specified by the "dailyReportDir" property.
     * If the directory does not exist, it will be created. For each entry in the {@link #reportData} map, which contains
     * report content categorized by date, it creates a new text file named according
     * to the date of the report (in the format "daily_report_dd.MM.yyyy.txt").
     * In case of an I/O error during file writing, the exception is caught and its
     * stack trace is printed to the console.
     */
    @Override
    protected void generateReport() {
    	String dailyReportsPath = propertiesManagerDR.getProperty("DAILY_REPORTS_DIR");
        File reportsDir = new File(dailyReportsPath);
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }
        for (Map.Entry<LocalDate, String> entry : reportData.entrySet()) {
            LocalDate date = entry.getKey();
            String reportContent = entry.getValue();

            File reportFile = new File(reportsDir, "daily_report_" + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + ".txt");

            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(reportFile)))) {
                writer.println(reportContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Collects and formats the daily reports data into a map that uses {@link LocalDate} as the key 
     * to represent the date of the report entry and {@link String} as the value to store the report data for that date.
     * The function first categorizes all parsed invoices by date and then generates a report for each date based on this categorization.
     * 
     * @return a map containing the formatted report data categorized by date.
     */
    private Map<LocalDate, String> collectReportData() {
        Map<LocalDate, List<InvoiceParser>> invoicesByDate = invoices.stream()
            .collect(Collectors.groupingBy(invoice -> invoice.getIssueDate().toLocalDate()));
        
        Map<LocalDate, String> reportData = new LinkedHashMap<>();

        for (LocalDate date : invoicesByDate.keySet()) {
            List<InvoiceParser> dailyInvoices = invoicesByDate.get(date);

            StringBuilder reportContent = new StringBuilder();
            reportContent.append("Date: ").append(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))).append("\n");
            reportContent.append("Total revenue: ").append(calculateTotalRevenue(dailyInvoices)).append(" EUR\n");
            reportContent.append("Total discount: ").append(calculateTotalDiscount(dailyInvoices)).append(" EUR\n");
            reportContent.append("Total promotion amount: ").append(calculateTotalPromotion(dailyInvoices)).append(" EUR\n");
            reportContent.append("Total amount for wide city area: ").append(calculateCityAreaTotals(dailyInvoices, "wide city area")).append(" EUR\n");
            reportContent.append("Total amount for narrow city area: ").append(calculateCityAreaTotals(dailyInvoices, "narrow city area")).append(" EUR\n");
            reportContent.append("Total maintenance cost: ").append(calculateMaintenanceCost(dailyInvoices)).append(" EUR\n");
            reportContent.append("Total repair cost: ").append(calculateRepairCost(dailyInvoices)).append(" EUR\n");

            reportData.put(date, reportContent.toString());
        }

        return reportData;
    }
    
    /**
     * Returns the collected report data as a map that uses {@link LocalDate} as the key 
     * to represent the date of the report entry and {@link String} as the value to store the report data for that date.
     * 
     * @return the report data categorized by date.
     */
    public Map<LocalDate, String> getReportData() {
        return reportData;
    }
}
