package database.Database2;

import java.util.*;
/**
 * This interface creates two abstract methods, one returns a List of type String,
 * and the other writes information into a file
 *
 * @author Shawn Shu
 * @version November 7, 2025
 */
public interface Fileable {
    /**
     * This method reads a file and returns a List of type String
     * @param fileName A String representing the file name
     * @return A List of type String representing all reservation information
     */
    List<String> readFile(String fileName);

    /**
     * This method writes to a file of all the reservation info retrieved from readFile method
     * @param fileName A String representing the file name
     * @param info A List of type String representing all reservation information
     */
    void writeFile(String fileName, List<String> info);
}
