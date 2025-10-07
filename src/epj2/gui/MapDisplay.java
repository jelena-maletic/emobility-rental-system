package epj2.gui;

import javax.swing.*;

import epj2.model.vehicle.*;
import epj2.util.MapUtil;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Map;
import java.util.List;

/**
 * Represents the graphical user interface for displaying and interacting with the map of the city and various reports
 * in the vehicle rental simulation system.
 * This class extends {@code JFrame} and sets up the main window for the application. 
 * It includes panels for displaying vehicle information, faults, and reports
 * and provides buttons for accessing these functionalities. 
 * The class also manages the display of vehicles on a grid representing the city map.
 * 
 * @author Jelena Maletić
 * @version 2.9.2024.
 */
public class MapDisplay extends JFrame {
	/**
	 * Serial version UID for serialization.
	 * This value is used to verify the compatibility of serialized data during deserialization.
	 */
    private static final long serialVersionUID = 1L;
    /** Size of the city map (number of rows and columns) */
    private static final int MAP_SIZE = MapUtil.getMapSize();
    /** Index of the row representing the start of the narrow part of the city */
    private static final int START_ROW = MapUtil.getNarrowStartRow();
    /** Index of the row representing the end of the narrow part of the city */
    private static final int END_ROW = MapUtil.getNarrowEndRow();
    /** Index of the column representing the start of the narrow part of the city */
    private static final int START_COL = MapUtil.getNarrowStartCol();
    /** Index of the column representing the end of the narrow part of the city */
    private static final int END_COL = MapUtil.getNarrowEndCol();;
    /** Map of the city */
    private final MapCell[][] labels = new MapCell[MAP_SIZE][MAP_SIZE];
    /** A map storing daily report data categorized by date.*/
    private Map<LocalDate, String> dailyReportData;
    /** The summary report data as a string. */
    private String summaryReportData;
    /** A map where the keys are vehicle IDs and the values are {@link Vehicle} objects. */
    private Map<String, Vehicle> vehicles;
    /** List of faulty vehicles */
    private List<Vehicle> faultyVehicles;
    /** Button to display available vehicles. */
    private JButton buttonVehicles = new JButton("<html>Show available<br>vehicles</html>");
    /** Button to display vehicle faults. */
    private JButton buttonFaults = new JButton("<html>Show<br>faults</html>");
    /** Button to display the summary report. */
    private JButton buttonSummR = new JButton("<html>Summary<br>report</html>");
    /** Button to display the daily reports. */
    private JButton buttonDailyR = new JButton("<html>Daily<br>report</html>");
    /** Button to display the top vehicle report. */
    private JButton buttonTopVR = new JButton("<html>Most profitable<br>vehicles</html>");
    
    /**
     * Constructs a {@code MapDisplay} instance, setting up the main window and initializing its components.
     * The constructor configures the layout of the frame, creates and arranges panels for the title, vehicle information,
     * report information, city name and map display.
     */
    public MapDisplay() {
        setTitle("ePJ2 e-mobility");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel1 = createTitlePanel();
        JPanel panel2 = createVehicleInfoPanel();
        JPanel panel3 = createReportInfoPanel();
        JPanel panel4 = createCityNamePanel();
        JPanel panel5 = createMapPanel();
        
        panel1.setPreferredSize(new Dimension(100, 100));
        panel2.setPreferredSize(new Dimension(150, 100));
        panel3.setPreferredSize(new Dimension(150, 100));
        panel4.setPreferredSize(new Dimension(100, 100));
        panel5.setPreferredSize(new Dimension(400, 400));

        add(panel1, BorderLayout.NORTH);
        add(panel2, BorderLayout.WEST);
        add(panel3, BorderLayout.EAST);
        add(panel4, BorderLayout.SOUTH);
        add(panel5, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }
    
    /**
     * Creates and returns the panel displaying the title of the application.
     * The title panel includes the company's name and application name, styled with specific fonts and colors.
     *
     * @return the panel for the title
     */
    private JPanel createTitlePanel() {
        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.setBackground(Color.decode("#436F95"));
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        Color textColor = Color.decode("#06133b");
        JLabel text1 = new JLabel("e-mobility company for electric vehicle rental in Java city", SwingConstants.CENTER);
        text1.setForeground(textColor);
        text1.setFont(new Font("Arial", Font.BOLD, 16));
        text1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel text2 = new JLabel("ePJ2", SwingConstants.CENTER);
        text2.setForeground(textColor);
        text2.setFont(new Font("Arial", Font.BOLD, 20));
        text2.setAlignmentX(Component.CENTER_ALIGNMENT);

        textPanel.add(Box.createVerticalGlue());
        textPanel.add(text1);
        textPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        textPanel.add(text2);
        textPanel.add(Box.createVerticalGlue());

        panel1.add(textPanel, BorderLayout.CENTER);
        return panel1;
    }
    
    /**
     * Creates and returns the panel for displaying vehicle information and associated buttons.
     * The panel includes buttons for viewing available vehicles and faults, with action listeners to handle button clicks.
     *
     * @return the panel for vehicle information
     */
    private JPanel createVehicleInfoPanel() {
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        panel2.setOpaque(true);
        panel2.setBackground(Color.decode("#123451"));

        JTextArea label2 = new JTextArea("View vehicle information");
        label2.setWrapStyleWord(true);
        label2.setLineWrap(true);
        label2.setEditable(false);
        label2.setOpaque(false);
        label2.setFocusable(false);
        label2.setFont(new Font("Arial", Font.BOLD, 14));
        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
        label2.setMaximumSize(new Dimension(150, 50));
        label2.setPreferredSize(new Dimension(150, 50));
        label2.setForeground(Color.decode("#7d7976"));

        Dimension buttonSize = new Dimension(150, 150);
        buttonVehicles.setPreferredSize(buttonSize);
        buttonFaults.setPreferredSize(buttonSize);

        buttonVehicles.setBackground(Color.decode("#7d7976"));
        buttonVehicles.setForeground(Color.decode("#06133b"));
        buttonFaults.setBackground(Color.decode("#7d7976"));
        buttonFaults.setForeground(Color.decode("#06133b"));

        buttonVehicles.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonFaults.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel2.add(label2);
        panel2.add(Box.createRigidArea(new Dimension(0, 20)));
        panel2.add(buttonVehicles);
        panel2.add(Box.createRigidArea(new Dimension(0, 20)));
        panel2.add(buttonFaults);

        buttonVehicles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VehicleTableDisplay vehicleTD = new VehicleTableDisplay(vehicles);
                vehicleTD.setVisible(true);
            }
        });

        buttonFaults.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FaultTableDisplay faultTD = new FaultTableDisplay(faultyVehicles);
                faultTD.setVisible(true);
            }
        });

        return panel2;
    }
    
    /**
     * Creates and returns the panel for displaying report related buttons.
     * The panel includes buttons for viewing summary reports, daily reports and top vehicles, with action listeners
     * to handle button clicks.
     *
     * @return the panel for report information
     */
    private JPanel createReportInfoPanel() {
        JPanel panel3 = new JPanel();
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));
        panel3.setOpaque(true);
        panel3.setBackground(Color.decode("#123451"));

        JTextArea labelText = new JTextArea("View business performance results");
        labelText.setWrapStyleWord(true);
        labelText.setLineWrap(true);
        labelText.setEditable(false);
        labelText.setOpaque(false);
        labelText.setFocusable(false);
        labelText.setFont(new Font("Arial", Font.BOLD, 14));
        labelText.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelText.setMaximumSize(new Dimension(150, 50));
        labelText.setPreferredSize(new Dimension(150, 50));
        labelText.setForeground(Color.decode("#7d7976"));

        Dimension buttonSize = new Dimension(150, 150);
        buttonSummR.setPreferredSize(buttonSize);
        buttonDailyR.setPreferredSize(buttonSize);
        buttonTopVR.setPreferredSize(buttonSize);

        buttonSummR.setBackground(Color.decode("#7d7976"));
        buttonSummR.setForeground(Color.decode("#06133b"));
        buttonDailyR.setBackground(Color.decode("#7d7976"));
        buttonDailyR.setForeground(Color.decode("#06133b"));
        buttonTopVR.setBackground(Color.decode("#7d7976"));
        buttonTopVR.setForeground(Color.decode("#06133b"));

        buttonSummR.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonDailyR.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonTopVR.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel3.add(labelText);
        panel3.add(Box.createRigidArea(new Dimension(0, 20)));
        panel3.add(buttonSummR);
        panel3.add(Box.createRigidArea(new Dimension(0, 20)));
        panel3.add(buttonDailyR);
        panel3.add(Box.createRigidArea(new Dimension(0, 20)));
        panel3.add(buttonTopVR);

        buttonSummR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SummaryReportDisplay summaryReportDisplay = new SummaryReportDisplay(summaryReportData);
                summaryReportDisplay.setVisible(true);
            }
        });

        buttonDailyR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DailyReportDisplay dailyReportDisplay = new DailyReportDisplay(dailyReportData);
                dailyReportDisplay.setVisible(true);
            }
        });

        buttonTopVR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TopVehicleDisplay topVehicleDisplay = new TopVehicleDisplay();
                topVehicleDisplay.setVisible(true);
            }
        });

        return panel3;
    }
    
    /**
     * Creates and returns the panel displaying the name of the city.
     * The panel includes a label showing the city name, styled with specific fonts and colors.
     *
     * @return the panel with the city name
     */
    private JPanel createCityNamePanel() {
        JPanel panel4 = new JPanel(new BorderLayout());
        panel4.setBackground(Color.decode("#436F95"));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel textLabel = new JLabel("Map of Java city");
        textLabel.setForeground(Color.decode("#06133b"));
        textLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(textLabel);

        panel4.add(textPanel, BorderLayout.NORTH); 

        return panel4;
    }
    
    /**
     * Creates and returns the panel displaying the city map as a grid of {@code MapCell} instances.
     * Each cell represents a location on the map and can display vehicle information.
     * City map has narrow and wide area. 
     *
     * @return the panel displaying the city map
     */
    private JPanel createMapPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(MAP_SIZE, MAP_SIZE));
        centerPanel.setBackground(Color.WHITE);
        for (int row = 0; row < MAP_SIZE; row++) {
            for (int col = 0; col < MAP_SIZE; col++) {
                labels[row][col] = new MapCell(row, col);
                labels[row][col].setMapColor(row >= START_ROW && row <= END_ROW && col >= START_COL && col <= END_COL);
                centerPanel.add(labels[row][col]);
            }
        }
        return centerPanel;
    }
    
    /**
     * Updates the display of a vehicle at the specified position on the map.
     * The cell at the given position is updated with the vehicle's information(ID and battery level) and new color.
     *
     * @param x the row index of the cell to update
     * @param y the column index of the cell to update
     * @param vehicleId the ID of the vehicle to display
     * @param vehicleBattery the battery level of the vehicle to display
     */
    public void updateVehiclePosition(int x, int y, String vehicleId, int vehicleBattery) {
        MapCell cell = labels[x][y];
        cell.updateVehicle(vehicleId, vehicleBattery);
    }
    
    /**
     * Clears the display of a vehicle at the specified position on the map.
     * The cell at the given position is cleared of vehicle information and reset to its default color based on its
     * position on the map.
     *
     * @param x the row index of the cell to clear
     * @param y the column index of the cell to clear
     */
    public void clearVehiclePosition(int x, int y) {
        MapCell cell = labels[x][y];
        cell.clearVehicle();
        cell.setMapColor(x >= START_ROW && x <= END_ROW && y >= START_COL && y <= END_COL);
    }
    
    /**
     * Enables or disables the buttons in the display.
     *
     * @param enable {@code true} to enable the buttons, {@code false} to disable them
     */
    public void enableButtons(boolean enable) {
        buttonFaults.setEnabled(enable);
        buttonSummR.setEnabled(enable);
        buttonDailyR.setEnabled(enable);
        buttonTopVR.setEnabled(enable);
    }
    
    /**
    * Sets the data for the daily reports.
    * 
    * @param dailyReportData a map of dates to daily report data strings to set
    */
    public void setDailyReportData(Map<LocalDate, String> dailyReportData) {
        this.dailyReportData = dailyReportData;
    }
    

    /**
     * Sets the data for the summary report.
     * 
     * @param summaryReportData the summary report data string to set
     */
    public void setSummaryReportData(String summaryReportData) {
    	this.summaryReportData = summaryReportData;
    }
    
    /**
    * Sets the vehicles to be displayed on the map.
    * 
    * @param vehicles a map of vehicle IDs to {@code Vehicle} objects to set
    */
	public void setVehicles(Map<String, Vehicle> vehicles) {
		this.vehicles = vehicles;
	}
	

    /**
     * Sets the list of faulty vehicles to be displayed.
     * 
     * @param faultyVehicles a list of {@code Vehicle} objects that are faulty to set
     */
	public void setFaultyVehicles(List<Vehicle> faultyVehicles) {
		this.faultyVehicles = faultyVehicles;
	}
	
}

