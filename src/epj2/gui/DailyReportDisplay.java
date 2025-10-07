package epj2.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * This is a {@code JFrame} subclass that displays the daily reports in a tabbed format.
 * Each tab contains a table displaying the report data for specific date
 * 
 * @author Jelena Maletić
 * @version 29.8.2024.
 */
public class DailyReportDisplay extends JFrame {
	/**
	 * Serial version UID for serialization.
	 * This value is used to verify the compatibility of serialized data during deserialization.
	 */
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs a {@code DailyReportDisplay} frame to display daily reports in a tabbed format.
     * This constructor initializes a JFrame with a tabbed pane where each tab represents a different day. 
     * Each tab contains a table displaying the report data for that specific date. 
     * The report data is provided as a map where each key is a {@code LocalDate} representing the date of the report
     * and each value is a string containing the report content for that date. 
     * Each line in the report string should follow the format: "description: amount".
     * Each table is enclosed in a {@code JScrollPane} to ensure that it is scrollable if its content exceeds the visible area of the panel.
     * The frame's title is set to "Daily reports", its size is set to 800x600 pixels.
     * 
     * @param reportData a map where each key is a {@code LocalDate} representing the date of the report, and each
     *                   value is a string containing the report content for that date. The string should be formatted
     *                   such that each line represents a report entry, with descriptions and amounts separated by a colon (":").
     */
    public DailyReportDisplay(Map<LocalDate, String> reportData) {
        setTitle("Daily Reports");
        setSize(800, 600);
        setLocationRelativeTo(null);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.decode("#436F95")); 
        for (LocalDate date : reportData.keySet()) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(Color.decode("#A7C9E4"));

            String[] columns = {"Description", "Amount (EUR)"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);

            String[] lines = reportData.get(date).split("\n");
            for (String line : lines) {
                if (line.contains(":")) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        model.addRow(new Object[]{parts[0].trim(), parts[1].trim()});
                    }
                }
            }
            JTable table = new JTable(model);
            table.setFont(new Font("Arial", Font.BOLD, 14));
            table.setForeground(Color.decode("#123451"));
            DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
            renderer.setForeground(Color.decode("#123451"));
            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);

            tabbedPane.addTab(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), panel);
        }
        getContentPane().setBackground(Color.decode("#A7C9E4")); 
        add(tabbedPane);
        setVisible(true);
    }
}