package epj2.gui;

import javax.swing.*;

import epj2.model.vehicle.*;
import epj2.service.report.TopVehicleReport;

import java.awt.*;
import java.util.Map;

/**
 * This is a {@code JFrame} subclass that deserializes and displays a list of the most profitable vehicles 
 * (vehicles with highest revenue) in the company.
 * This class initializes a GUI that shows the top vehicles by type (Scooter, Bike, and Car)
 * based on serialized data.
 * 
 * @author Jelena Maletić
 * @version 31.8.2024.
 */
public class TopVehicleDisplay extends JFrame {
	/**
	 * Serial version UID for serialization.
	 * This value is used to verify the compatibility of serialized data during deserialization.
	 */
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs a {@code TopVehicleDisplay} frame and initializes the user interface.
     * Sets the title, size, and location of the frame, and makes it visible.
     * It also populates the frame with vehicle data.
     */
    public TopVehicleDisplay() {
        setTitle("Most profitable vehicles");
        setSize(800, 600);
        setLocationRelativeTo(null);
        displayVehicles();
        setVisible(true);
    }
    
    /**
     * Populates the frame with vehicle data.
     * Creates a {@code JTextArea} to display the details of the most profitable vehicles,
     * formatted with specific font and color. The text area is added to a {@code JScrollPane},
     * which is then added to the frame.
     * The method retrieves vehicle data from serialized files
     * and then appends the details for each vehicle type (Scooter, Bike, Car) to the text area.
     */
    private void displayVehicles() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.BOLD, 14));
        textArea.setForeground(Color.decode("#123451")); 
        textArea.setText("Most profitable vehicles:\n\n");
        textArea.setAlignmentX(Component.CENTER_ALIGNMENT);

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
        
        Map<Class<? extends Vehicle>, Vehicle> vehicles = TopVehicleReport.deserializeAllVehicles();
        
        appendVehicleDetails(textArea, "Scooters", vehicles, Scooter.class);
        appendVehicleDetails(textArea, "Bikes", vehicles, Bike.class);
        appendVehicleDetails(textArea, "Cars", vehicles, Car.class);
    }
    
    /**
     * Appends details of vehicles of a specific type to the provided JTextArea.
     * The details are appended with a header for the vehicle type, followed by the string representation of each vehicle.
     * 
     * @param textArea the JTextArea to which vehicle details will be appended
     * @param title a string representing the header for the vehicle type section
     * @param vehicles a map where the keys are vehicle types (classes extending {@code Vehicle}) and the values are the most profitable vehicles of each type.
     * @param vehicleType a class representing the type of vehicles to be included in this section
     */
    private void appendVehicleDetails(JTextArea textArea, String title, Map<Class<? extends Vehicle>, Vehicle> vehicles, Class<? extends Vehicle> vehicleType) {
        textArea.append(title + ":\n");
        vehicles.values().stream()
            .filter(vehicleType::isInstance)
            .forEach(vehicle -> textArea.append(vehicle.toString() + "\n"));
        textArea.append("\n");
    }


}