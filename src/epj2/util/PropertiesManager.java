package epj2.util;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.io.InputStream;

/**
 * A utility class for managing properties files.
 * This class provides methods to read and write properties from a properties file.
 * 
 * @author Jelena Maletić
 * @version 24.8.2024.
 */
public class PropertiesManager {
	private Properties properties = new Properties();
	
	/**
     * Constructs a `PropertiesManager` and loads properties from the specified file.
     * 
     * @param fileName The name of the properties file to load. 
     */
	public PropertiesManager(String fileName) {
		InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
	    try {
			properties.load(input);
		} 
	    catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	    catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
     * Retrieves the property value associated with the specified key.
     * 
     * @param key The key of the property to retrieve.
     * @return The property value as a string, or {@code null} if the key does not exist.
     */
	public String getProperty(String key) {
	    return properties.getProperty(key);
	}
	
	/**
     * Retrieves the property value associated with the specified key and converts it to {@code double}.
     * 
     * @param key The key of the property to retrieve.
     * @return The property value as a {@code double}. Returns {@code 0.0} if the key does not exist or if the value cannot be parsed.
     */
	public double getPropertyAsDouble(String key) {
        String value = properties.getProperty(key);
        if (value != null) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid format: " + key);
                return 0.0; 
            }
        }
        return 0.0; 
    }
	
	/**
     * Retrieves the property value associated with the specified key and converts it to int.
     * 
     * @param key The key of the property to retrieve.
     * @return The property value as int. Returns {@code 0} if the key does not exist or if the value cannot be parsed.
     */
	public int getPropertyAsInt(String key) {
        String value = properties.getProperty(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } 
            catch (NumberFormatException e) {
                System.err.println("Invalid format: " + key);
                return 0; 
            }
        }
        return 0; 
    }
	
	/**
     * Sets the value of a property with the specified key.
     * 
     * @param key   The key of the property to set.
     * @param value The value to set for the property.
     */
	public void setProperty(String key, String value) {
	    properties.setProperty(key, value);
	}
	
	/**
     * Checks if the properties contain the specified key.
     * 
     * @param key The key to check.
     * @return {@code true} if the properties contain the key; {@code false} otherwise.
     */
    public boolean containsKey(String key) {
        return properties.containsKey(key);
    }

}
