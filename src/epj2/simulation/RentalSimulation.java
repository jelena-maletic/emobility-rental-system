package epj2.simulation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import epj2.gui.MapDisplay;
import epj2.model.vehicle.Vehicle;
import epj2.service.Rental;
import epj2.service.report.*;
import epj2.util.*;

/**
 * This class serves as the entry point for running the simulation of a rental company.
 * It is responsible for initializing and starting the entire simulation process, which models the operations 
 * of a vehicle rental company.
 * 
 * @author Jelena Maletić
 * @version 2.9.2024. 
 */
public class RentalSimulation {
	/** List of faulty vehicles */
	private static List<Vehicle> faultyVehicles = new ArrayList<>();;
	
	/**
	 * Initializes the application, processes command-line arguments
	 * and starts the main functionality of the program.
	 * This method performs the following steps:
	 * -Loads vehicle data from a CSV file and rental data from a specified file.
	 * -Initializes and configures the map display with the loaded vehicle and rental data.
	 * -Disables map display buttons, runs the simulation, and then re-enables the buttons.
	 * -Parses invoice data from the specified directory.
	 * -Updates the map display with the generated summary and daily reports, as well as the list of faulty vehicles.
	 * 
	 * @param args an array of {@code String} arguments passed from the command line during the application's execution.
	 */
	public static void main(String[] args) {
	    Map<String, Vehicle> vehicles = VehicleLoader.loadVehiclesFromCSV();
	    List<Rental> rentals = RentalLoader.loadRentals(vehicles);
	    MapDisplay mapDisplay = new MapDisplay();
	    mapDisplay.setVehicles(vehicles);
	    for (Rental rental : rentals) {
	    	rental.setMapDisplay(mapDisplay);
	    }    
	    
	    mapDisplay.enableButtons(false);
	    runSimulation(rentals);
	    mapDisplay.enableButtons(true);
	    
	    List<InvoiceParser> invoices = InvoiceParser.parseAllInvoices();
	    new TopVehicleReport(vehicles, invoices);
	    SummaryReport summaryReport = new SummaryReport(vehicles, invoices);
	    String summaryReportData = summaryReport.getReportData();
	    mapDisplay.setSummaryReportData(summaryReportData);
	    DailyReport dailyReport = new DailyReport(vehicles, invoices);
	    Map<LocalDate, String> dailyReportData = dailyReport.getReportData();
	    mapDisplay.setDailyReportData(dailyReportData);
	    mapDisplay.setFaultyVehicles(faultyVehicles);
	        
	 }
	
	/**
	 * This function initiates the vehicle rental simulation. 
	 * First, rentals are grouped and sorted by date and time. 
	 * Rentals scheduled for the same date and time are processed concurrently,
	 * with each rental running in its own thread. 
	 * After all rentals for the current date and time are complete, the system records any 
	 * vehicle faults that occurred during these rentals 
	 * and then waits for 5 seconds before starting the next round of rentals. 
	 * Once all rentals have been processed, a message is displayed on the console indicating that the simulation is over.
	 * 
	 * @param rentals list of all rentals
	 */
    public static void runSimulation(List<Rental> rentals) {
        Map<LocalDateTime, List<Rental>> rentalsByDateTime = rentals.stream()
                .collect(Collectors.groupingBy(Rental::getDateTime));
        
        List<LocalDateTime> sortedDateTimes = new ArrayList<>(rentalsByDateTime.keySet());
        Collections.sort(sortedDateTimes);

        for (LocalDateTime dateTime : sortedDateTimes) {
            List<Rental> currentRentals = rentalsByDateTime.get(dateTime);
            for (Rental rental : currentRentals) {
                rental.start();
            }
            for (Rental rental : currentRentals) {
                try {
                    rental.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }           
            for (Rental rental : currentRentals) {
                if (rental.hasFaultOccurred()) {
                    faultyVehicles.add(rental.getFaultyVehicle());
                }
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("The rental simulation has finished.");
    }

    /**
	 * Returns the list of the faulty vehicles.
	 * 
	 * @return the list of the faulty vehicles.
	 */
	public List<Vehicle> getFaultyVehicles() {
		return faultyVehicles;
	}

	/**
	 * Sets the list of the faulty vehicles.
	 * 
	 * @param faultyVehicles list of the faulty vehicles to be set
	 */
	public void setFaultyVehicles(List<Vehicle> faultyVehicles) {
		RentalSimulation.faultyVehicles = faultyVehicles;
	}
}

