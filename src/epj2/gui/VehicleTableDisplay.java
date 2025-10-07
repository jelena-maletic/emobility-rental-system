package epj2.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import epj2.model.vehicle.*;

import java.awt.*;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is a JFrame subclass that displays tables for different types of vehicles
 * (Car, Bike, and Scooter) in a GUI. It creates a separate table for each
 * type of vehicle and arranges them in a vertical layout.
 * 
 * @author Jelena Maletić
 * @version 31.8.2024.
 */
public class VehicleTableDisplay extends JFrame {
	/**
	 * Serial version UID for serialization.
	 * This value is used to verify the compatibility of serialized data during deserialization.
	 */
	private static final long serialVersionUID = 1L;
	/** JTable displaying the details of the cars */
	private JTable carTable;
	/** JTable displaying the details of the bikes */
    private JTable bikeTable;
    /** JTable displaying the details of the scooters */
    private JTable scooterTable;
    
    /**
     * Constructs a {@code VehicleTableDisplay} frame that displays tables for different types of vehicles 
     * (Car, Bike, and Scooter).
     * This constructor initializes the GUI components, filters the provided map of vehicles by their type 
     * and creates tables to show the details of each vehicle type.
     * The GUI is set up with a title, size, and layout. It creates three separate tables, 
     * one for each type of vehicle: Car, Bike, and Scooter. 
     * Each table is displayed in a vertical stack within a JScrollPane for scrollable content. 
     * 
     * @param vehicles a map where the key is the vehicle ID and the value is the Vehicle object
     */
    public VehicleTableDisplay(Map<String, Vehicle> vehicles) {
        setTitle("Available vehicles");
        setSize(800, 600);
        setLayout(new BorderLayout());

        List<Car> cars = filterVehiclesByType(vehicles, Car.class);
        List<Bike> bikes = filterVehiclesByType(vehicles, Bike.class);
        List<Scooter> scooters = filterVehiclesByType(vehicles, Scooter.class);

        carTable = createCarTable(cars);
        bikeTable = createBikeTable(bikes);
        scooterTable = createScooterTable(scooters);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); 

        panel.add(createTablePanel(carTable, "Cars"));
        panel.add(Box.createRigidArea(new Dimension(0, 10))); 
        panel.add(createTablePanel(bikeTable, "Bikes"));
        panel.add(Box.createRigidArea(new Dimension(0, 10))); 
        panel.add(createTablePanel(scooterTable, "Scooters"));

        add(new JScrollPane(panel), BorderLayout.CENTER);

        setVisible(true);
    }
    
    /**
     * Creates a JPanel that contains a JTable and a title label.
     * The table is displayed with a scroll pane.
     *
     * @param table the JTable to be added to the panel
     * @param title the title to be displayed above the table
     * @return a JPanel containing the table and its title
     */
    private JPanel createTablePanel(JTable table, String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel label = new JLabel(title, JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(Color.decode("#123451"));
        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }
    

    /**
     * Creates a JTable to display information about Car objects.
     * The table includes columns for ID, producer, model, purchase price, purchase date and description.
     *
     * @param cars a list of Car objects to be displayed in the table
     * @return a JTable displaying the details of the cars
     */
    private JTable createCarTable(List<Car> cars) {
        String[] columnNames = {"ID", "Producer", "Model", "Purchase price", "Purchase date", "Description"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (Car car : cars) {
            Object[] rowData = {
                    car.getID(),
                    car.getProducer(),
                    car.getModel(),
                    car.getPurchasePrice(),
                    car.getPurchaseDate() ==null ? "Unknown": car.getPurchaseDate(), 
                    car.getDescription()   
            };
            model.addRow(rowData);
        }
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.BOLD, 14));
        table.setForeground(Color.decode("#123451"));
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 16));
        header.setForeground(Color.decode("#123451"));
        return table;
    }
    
    /**
     * Creates a JTable to display information about Bike objects.
     * The table includes columns for ID, producer, model, purchase price and range.
     *
     * @param bikes a list of Bike objects to be displayed in the table
     * @return a JTable displaying the details of the bikes
     */
    private JTable createBikeTable(List<Bike> bikes) {
        String[] columnNames = {"ID", "Producer", "Model", "Purchase price", "Range per charge"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (Bike bike : bikes) {
            Object[] rowData = {
                    bike.getID(),
                    bike.getProducer(),
                    bike.getModel(),
                    bike.getPurchasePrice(),
                    bike.getRangePerCharge() 
            };
            model.addRow(rowData);
        }
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.BOLD, 14));
        table.setForeground(Color.decode("#123451"));
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 16)); 
        header.setForeground(Color.decode("#123451"));
        return table;
    }
    
    /**
    * Creates a JTable to display information about Scooter objects.
    * The table includes columns for ID, producer, model, purchase price, and maximum speed.
    *
    * @param scooters a list of Scooter objects to be displayed in the table
    * @return a JTable displaying the details of the scooters
    */
    private JTable createScooterTable(List<Scooter> scooters) {
        String[] columnNames = {"ID", "Producer", "Model", "Purchase price", "Max speed"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (Scooter scooter : scooters) {
            Object[] rowData = {
                    scooter.getID(),
                    scooter.getProducer(),
                    scooter.getModel(),
                    scooter.getPurchasePrice(),
                    scooter.getMaxSpeed() 
            };
            model.addRow(rowData);
        }
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.BOLD, 14));
        table.setForeground(Color.decode("#123451"));
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 16)); 
        header.setForeground(Color.decode("#123451")); 
        return table;
    }
    
    /**
     * Filters the map of vehicles by type and returns a list of vehicles of the specified type.
     * 
     * @param <T> the type of the vehicle (extends Vehicle)
     * @param vehicles a map of vehicle IDs to Vehicle objects
     * @param type the class of the type of vehicle to filter
     * @return
     */
    private <T extends Vehicle> List<T> filterVehiclesByType(Map<String, Vehicle> vehicles, Class<T> type) {
        return vehicles.values().stream()
                .filter(type::isInstance)
                .map(type::cast)
                .collect(Collectors.toList());
    }
}
