package csu.csci325;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Andrew Taylor
 */
public class Shipments {
    public ArrayList<ShipmentItem> shipmentList;
    
    // Constructor
    public Shipments () {
        shipmentList = new ArrayList<>();
    }
    
    // User can add a shipment to the array
    public void addShipment (ShipmentItem addShipment) {
	shipmentList.add(addShipment);
    }
    
    // Calls the array list
    public ArrayList<ShipmentItem> getShipmentList () {
        return shipmentList;
    }
    
    //Returns the size of the shipment list
    public int getShipmentListSize () {
        return shipmentList.size();
    }

    // Initialize the array with data from the .txt file containing shipment info
    public void initializeShipments() {
	int trackNum;
        String stat;
        String location;
        
        Scanner input = null;
        // TODO: Add Try Catch/Throw
	FileInputStream fileInput = null;
	
        try {
            fileInput = new FileInputStream("shipmentData.txt");
            input = new Scanner(fileInput);

            while (input.hasNext()) {
                //if (input.hasNextInt()){
                //    trackNum = input.nextInt();
                  //  System.out.print(trackNum + " ");
                //}
                //else {
                  //  stat = input.next();
                    //System.out.print(stat + " ");
                    //location = input.next();
                    //System.out.print(location + " ");
                //}
                //}
                trackNum = input.nextInt();
                stat = input.next();
                location = input.next();
                ShipmentItem newShipment = new ShipmentItem(trackNum, stat, location);
                shipmentList.add(newShipment);
            }
            
    } catch (FileNotFoundException e) {
        System.out.println("File not found: " + e.getMessage());
    //} catch (IOException e) {
       // System.out.println("error reading file: " + e.getMessage());
    } finally {
        try {
            if (fileInput != null) {
                fileInput.close();
            }
            if(input != null) {
                input.close();
            }
            
        } catch (IOException e) {
            System.out.println("Error closing streams: " + e.getMessage());
        }
        }
    }

    // The .txt file is updated after any changes are made to the array.
    public void updateShipmentFile() {
        FileOutputStream output = null;
        PrintWriter outFS = null;
        FileWriter fileWrite = null;
        try {
            fileWrite = new FileWriter("shipmentData.txt");
            outFS = new PrintWriter(fileWrite);
       
            for (int i = 0; i < shipmentList.size(); i++) {
                ShipmentItem shipmentItem = shipmentList.get(i);
                //System.out.println(shipmentItem.getTrackingNum());
                outFS.println(shipmentItem.getTrackingNum() + " " + shipmentItem.getStatus() + " " + shipmentItem.getLocation());
            }
            
            System.out.println("File updated successfully.");
            System.out.println();
            
        }catch ( FileNotFoundException e) {
            System.out.println("problem: " + e.getMessage());
        }catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        
        }finally {
            try { 
                if (output != null) {
                    output.close();
                }
                if (outFS != null) {
                    outFS.close();
                }
            }catch (IOException e) {
                System.out.println("problem: " + e.getMessage());
            }
        }
    }
    
    // Display information relating to a specific shipment
    public void viewSpecificShipment(int _trackNum) {
        boolean foundShipment = false;
        int index = 0;
        
        
        for (int i = 0; i < shipmentList.size(); i++) {
            if (shipmentList.get(i).trackingNum == _trackNum) {
                foundShipment = true;
                index = i;        
            }
            
        }
        
        // Prints either the item info or that no item was found
        if (foundShipment) {
            shipmentList.get(index).printInfo();
        }
        else {
            System.out.println("No item found with this SKU.");
        }

    }
    
    // Remove a shipment from the list
    public void removeShipment(int voidShipment) {
        boolean foundShipment = false;
        
        for (int i = 0; i < shipmentList.size(); i++) {
            if (shipmentList.get(i).trackingNum == voidShipment) {
                foundShipment = true;
                shipmentList.remove(i);
            }
        }
        if (!foundShipment) {
            System.out.println("Shipment not found, nothing removed.");
        }
    }

    // Prints the shipment list in menu format
    public void printList(int _page, int _itemsPerPage) {
        int itemsPrinted = 0;
        
        for (int i = 0 + (_page * 10); i < shipmentList.size() && itemsPrinted < _itemsPerPage; i++) {
            ShipmentItem tempShipmentItem = shipmentList.get(i);
            
            System.out.println("    " + tempShipmentItem.getTrackingNum() + " ---- " + tempShipmentItem.getStatus() + " ---- " + tempShipmentItem.getLocation());
            itemsPrinted += 1;
        }

    }
    
}
