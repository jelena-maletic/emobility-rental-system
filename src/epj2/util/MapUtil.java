package epj2.util;

import java.util.List;

/**
 * A utility class for map of the city
 * City map includes both a wide area and a narrow area.
 * The narrow area is defined by specific coordinate boundaries, and the wide area
 * is everything outside of these boundaries.
 * This class provides methods to check whether a specific location or path is within
 * the wide area of the city map.
 * 
 * @author Jelena Maletić
 * @version 8.9.2024.
 */
public class MapUtil {
	/**
	 * Manages the properties file used by the MapUtil class.
	 * This instance is used to load configurations.
	 */
	private static PropertiesManager propertiesManager;
	/** Size of the city map (number of rows and columns) */
    private static int MAP_SIZE = 20;
    /** Index of the row representing the start of the narrow part of the city */
    private static int NARROW_START_ROW = 5;
    /** Index of the row representing the end of the narrow part of the city */
    private static int NARROW_END_ROW = 14;
    /** Index of the column representing the start of the narrow part of the city */
    private static int NARROW_START_COL = 5;
    /** Index of the column representing the end of the narrow part of the city */
    private static int NARROW_END_COL = 14;
    
    // Static block to initialize propertiesManager
    static {
    	propertiesManager = new PropertiesManager("mapDimensions.properties");
    	MAP_SIZE = propertiesManager.getPropertyAsInt("MAP_SIZE");
    	NARROW_START_ROW = propertiesManager.getPropertyAsInt("NARROW_START_ROW");
    	NARROW_END_ROW = propertiesManager.getPropertyAsInt("NARROW_END_ROW");
    	NARROW_START_COL = propertiesManager.getPropertyAsInt("NARROW_START_COL");
    	NARROW_END_COL = propertiesManager.getPropertyAsInt("NARROW_END_COL");
    }
    
    /**
     * Gets the size of the city map (number of rows and columns).
     * This value is loaded from the properties file.
     * 
     * @return the size of the city map.
     */
	public static int getMapSize() {
		return MAP_SIZE;
	}
	
	/**
	 * Gets the index of the row representing the start of the narrow part of the city.
	 * This value is loaded from the properties file.
	 * 
	 * @return the index of the start row of the narrow part of the city.
	 */
	public static int getNarrowStartRow() {
		return NARROW_START_ROW;
	}
	
	/**
	 * Gets the index of the row representing the end of the narrow part of the city.
	 * This value is loaded from the properties file.
	 * 
	 * @return the index of the end row of the narrow part of the city.
	 */
	public static int getNarrowEndRow() {
		return NARROW_END_ROW;
	}
	
	/**
	 * Gets the index of the column representing the start of the narrow part of the city.
	 * This value is loaded from the properties file.
	 * 
	 * @return the index of the start column of the narrow part of the city.
	 */
	public static int getNarrowStartCol() {
		return NARROW_START_COL;
	}
	

	/**
	 * Gets the index of the column representing the end of the narrow part of the city.
	 * This value is loaded from the properties file.
	 * 
	 * @return the index of the end column of the narrow part of the city.
	 */
	public static int getNarrowEndCol() {
		return NARROW_END_COL;
	}
	
	/**
     * Determines if a given location is in the wide area of the city map.
     * The wide area is defined as any location that falls outside of the narrow area boundaries.
     * 
     * @param x the x-coordinate of the location.
     * @param y the y-coordinate of the location.
     * @return {@code true} if the location is in the wide area, {@code false} otherwise.
     */
    public static boolean isInWideArea(int x, int y) {
        return x < NARROW_START_ROW || x > NARROW_END_ROW || y < NARROW_START_COL || y > NARROW_END_COL;
    }
    
    /**
     * Checks if any point on the path is in the wide area of the city.
     * 
     * @param path the path being checked to determine if it is in the wide area
     * @return {@code true} if the path is within the wide area, {@code false} otherwise
     */
    public static boolean isPathInWideArea(List<int[]> path) {
        for (int[] location : path) {
            if (isInWideArea(location[0], location[1])) {
                return true;
            }
        }
        return false;
    }
    
    
}
