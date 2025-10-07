package epj2.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import epj2.model.vehicle.*;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * This is a {@code JFrame} subclass that displays a list of vehicles with recorded faults.
 * The information is displayed in a table format.
 * 
 * @author Jelena Maletić
 * @version 31.8.2024.
 */
public class FaultTableDisplay extends JFrame {
	/**
	 * Serial version UID for serialization.
	 * This value is used to verify the compatibility of serialized data during deserialization.
	 */
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs a {@code FaultTableDisplay} frame to display faulty vehicles in table format.
     * This constructor initializes the frame with a title "Faults Report" and a size of 800x400 pixels. 
     * This constructor initializes a JTable to show faulty vehicles in a table format.
     * The table is populated with data from the provided list of vehicles, which includes vehicle IDs, 
     * fault descriptions, fault date and time, and vehicle types. 
     * The table is placed inside a scroll pane to allow for scrolling if the content exceeds the visible area of the frame.
     *
     * @param faultyVehicles a list of {@code Vehicle} objects representing vehicles with recorded faults. Each
     *                       {@code Vehicle} should have an associated {@code Fault} object containing details such as 
     *                       description and date and time of the fault.
     */
    public FaultTableDisplay(List<Vehicle> faultyVehicles) {
        setTitle("Faults Report");
        setSize(800, 400);
        setLocationRelativeTo(null);
        String[] columns = {"Vehicle ID", "Fault Description", "Fault Date and Time", "Vehicle Type"};
        Object[][] tableData = parseFaultyVehicles(faultyVehicles);
        DefaultTableModel model = new DefaultTableModel(tableData, columns);
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.BOLD, 14));
        table.setForeground(Color.decode("#123451"));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
        setVisible(true);
    }
    
    /**
     * Converts a list of faulty vehicles into a 2D array suitable for populating a table.
     * This method processes the provided list of faulty vehicles and extracts relevant fault information to populate a 2D array of
     * {@code Object}. In this array each sub-array corresponds to a vehicle and includes its ID, fault description, fault date and
     * time (formatted as "dd.MM.yyyy. HH:mm:ss") and vehicle type.
     *
     * @param faultyVehicles a list of {@code Vehicle} objects that have a fault.
     * @return a 2D {@code Object} array where each sub-arry represents a vehicle with columns for ID, fault description, date and
     *         time, and vehicle type.
     */
    private Object[][] parseFaultyVehicles(List<Vehicle> faultyVehicles) {
        Object[][] data = new Object[faultyVehicles.size()][4];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
        for (int i = 0; i < faultyVehicles.size(); i++) {
            Vehicle vehicle = faultyVehicles.get(i);
            Fault fault = vehicle.getFault();
            data[i][0] = vehicle.getID();
            data[i][1] = fault != null ? fault.getDescription() : "No description";
            data[i][2] = fault != null ? fault.getDateTime().format(formatter) : "No date";
            data[i][3] = getVehicleType(vehicle); 
        }
        return data;
    }
    
    /**
     * Determines the type of vehicle based on its class.
     *
     * @param vehicle the {@code Vehicle} object whose type is to be determined.
     * @return a string indicating the type of the vehicle.
     */
    private String getVehicleType(Vehicle vehicle) {
        if (vehicle == null) 
        	return "Unknown vehicle";
        return vehicle.getClass().getSimpleName();
    }
}
