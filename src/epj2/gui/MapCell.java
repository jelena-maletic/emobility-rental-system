package epj2.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Represents a cell in a grid-based map display.
 * The {@code MapCell} class extends {@code JLabel} to provide a visual representation of a single map cell.
 * Each cell is capable of displaying vehicle information and changing its color based on its location within a
 * specific section of the map. It also supports tooltips and mouse interactions.
 * 
 * @author Jelena Maletić
 * @version 2.9.2024.
 */
public class MapCell extends JLabel {

    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs a {@code MapCell} with the specified row and column position.
     * Initializes the cell with a green background, a black border, and a tooltip displaying its row and column coordinates.
     * The cell's size is set to a preferred dimension of 20x20 pixels. 
     * Mouse listeners are added to handle tooltip visibility on mouse events (this helps users view detailed information easily)
     *
     * @param row the row index of the cell in the map.
     * @param col the column index of the cell in the map.
     */
    public MapCell(int row, int col) {
        setOpaque(true);
        setBackground(Color.GREEN);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setPreferredSize(new Dimension(20, 20));
        setToolTipText("(" + row + ", " + col + ")");
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setToolTipText(getText());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setToolTipText(null);
            }
        });
    }
    
    /**
     * Updates the cell to include information about a new vehicle.
     * Appends the specified vehicle ID and battery level to the existing text in the cell. If the cell already contains text,
     * the new vehicle information is added to the end of the existing content, preserving the previous information about other
     * vehicles located in the same cell.
     *
     * @param vehicleId the ID of the vehicle to be displayed in the cell.
     * @param vehicleBattery the battery level of the vehicle to be displayed in the cell.
     */
    public void updateVehicle(String vehicleId, int vehicleBattery) {
        String existingText = getText();
        String newText = existingText.isEmpty() ? vehicleId + " (" + vehicleBattery + ")" : existingText + " " + vehicleId + " (" + vehicleBattery + ")";
        setBackground(Color.GREEN);
        setText(newText);
        setFont(new Font("Arial", Font.BOLD, 8));
    }
    

    /**
     * Clears the vehicle information from the cell.
     */
    public void clearVehicle() {
        setBackground(Color.GREEN);
        setText("");
    }
    
    /**
     * Sets the background color of the cell based on its location within a specific section of the map.
     *
     * @param isInsideNarrowSection {@code true} if the cell is within the narrow section of the map; {@code false} otherwise.
     */
    public void setMapColor(boolean isInsideNarrowSection) {
        setBackground(isInsideNarrowSection ? Color.decode("#8EB5D7") : Color.decode("#B0CCE5"));
    }
}
