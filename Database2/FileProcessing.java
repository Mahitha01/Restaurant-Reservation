package database.Database2;

import database.Database2.Fileable;

import java.io.*;
import java.util.*;

/**
 * This program implements the interface Fileable and have readFile & writeFile methods to read
 * information from the customer files and write them into another file
 *
 * @author Shawn Shu, lab sec 02
 * @version November 7, 2025
 */
public class FileProcessing implements Fileable {
    /**
     * This method reads a file and returns a List of type String
     * @param fileName A String representing the file name
     * @return A List of type String representing all reservation information
     */
    @Override
    public List<String> readFile(String fileName) {
        List<String> allReservations = new ArrayList<>();
        try (BufferedReader bfr = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                allReservations.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allReservations;
    }

    /**
     * This method writes to a file of all the reservation info retrieved from readFile method
     * @param fileName A String representing the file name
     * @param info A List of type String representing all reservation information
     */
    @Override
    public void writeFile(String fileName, List<String> info) {
        try (BufferedWriter bfw = new BufferedWriter(new FileWriter(fileName))) {
            for (String line: info) {
                bfw.write(line);
                bfw.newLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
