package epj2.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * This is a {@code JFrame} subclass that displays the summary report content in a table format.
 * The report content is provided as a string and is parsed into a table with two columns: 
 * Description and "Amount (EUR) (amount + currency)". 
 * 
 * @author Jelena Maletić
 * @version 29.8.2024.
 */
public class SummaryReportDisplay extends JFrame {
	/**
	 * Serial version UID for serialization.
	 * This value is used to verify the compatibility of serialized data during deserialization.
	 */
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs a {@code SummaryReportDisplay} frame to display a summary report.
     * This constructor initializes a JTable to show a summary report in a table format.
     * The frame's title is set to "Summary Report" and its size is set to 600x400 pixels. The content of the report is
     * parsed and displayed in a table with two columns: "Description" and "Amount (EUR).
     * The table is placed inside a scroll pane to allow for scrolling if the content exceeds the visible area of the frame.
     * 
     * @param reportContent a {@code String} containing the content of the summary report. The string should be formatted
     *                      such that each line represents a report entry. Each entry should include a description followed 
     *                      by an amount, separated by a colon (":").
     */
    public SummaryReportDisplay(String reportContent) {
        setTitle("Summary report");
        setSize(600, 400);
        setLocationRelativeTo(null);

        Object[][] tableData = parseReportContent(reportContent);
        String[] columns = {"Description", "Amount (EUR)"};

        DefaultTableModel model = new DefaultTableModel(tableData, columns);
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.BOLD, 14)); 
        table.setForeground(Color.decode("#123451")); 

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
    }
    
    /**
     * Parses the provided report content into a 2D array of objects suitable for use in a {@code JTable}.
     * Each line of the report content is split into two columns: description (" and amount.
     * If a line does not contain a colon, it is treated as a description with an empty amount.
     *
     * @param reportContent a {@code String} containing the raw content of the report. Each line should be formatted
     *                      as "description: amount".
     * @return a 2D {@code Object} array where each sub-array represents a line from the report content. The first
     *         element in each sub-array is the description, and the second element is the amount.
     */
    private Object[][] parseReportContent(String reportContent) {
        String[] lines = reportContent.split("\n");
        Object[][] data = new Object[lines.length][2];
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.contains(":")) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    data[i][0] = parts[0].trim();
                    data[i][1] = parts[1].trim();
                }
            } 
            else {
                data[i][0] = line;
                data[i][1] = "";
            }
        }
        return data;
    }

}