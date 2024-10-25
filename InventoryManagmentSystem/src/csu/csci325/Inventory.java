package csu.csci325;

import java.util.ArrayList;
import java.util.Scanner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;

/**
 *
 * @author Gabriel Tirado
 */
public class Inventory {
    private ArrayList<InventoryItem> inventory;
    private String lastOpenFile;
    
    public Inventory() {
        inventory = new ArrayList<InventoryItem>();
        lastOpenFile = "";
    }
    
    /**
     * Attempts to add an InventoryItem into the Inventory. Will fail if an item with the same SKU exists
     * 
     * @param _item The InventoryItem that will be attempted to be added into the Inventory List
     * @return Boolean that tells if the item was added into the Inventory
     */
    public boolean AddInventoryItem(InventoryItem _item) {
        boolean added = false;
        boolean uniqueItem = true;
        
        // Check if an item with the same sku exists
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getSku() == _item.getSku()) {
                uniqueItem = false;
                break;
            }
        }
        
        // If given item isn't in current inventory, add in item
        if (uniqueItem) { 
            inventory.add(_item);
            added = true;
        }
        else {
            added = false;
        }
        
        return added;
    }
    
    /**
     * Adds InventoryItems from a given file into Inventory.
     * 
     * @param fullFileDir The name of the folder and directory that the inventory file will be saved at.
     * @return Boolean that tells if the file was saved or not
     */
    public boolean AddInventoryItemsByFile(String fullFileDir) {
        boolean added = false;
        Scanner input = null;
        FileInputStream fileInput = null;
        
        String name, description;
        int sku, quantity;
        double cost;
        
        // Add all data from file to Inventory
        try {
            fileInput = new FileInputStream(fullFileDir);
            input = new Scanner(fileInput);
            
            while (input.hasNextLine()) {
                String[] fileInvItem = input.nextLine().split("[|]");
                
                sku = Integer.parseInt(fileInvItem[0]);
                name = fileInvItem[1];
                description = fileInvItem[2];
                cost = Double.parseDouble(fileInvItem[3]);
                quantity = Integer.parseInt(fileInvItem[4]);
                
                this.AddInventoryItem(new InventoryItem(name, description, sku, quantity, cost));
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File with given name wasn't found.");
        }
        finally {
            // Close all input sources
            try {
                if (fileInput != null) {
                    fileInput.close();
                    lastOpenFile = fullFileDir;
                }
                if(input != null) {
                    input.close();
                }
            }
            catch (IOException e) {
                System.out.println("Error closing streams: " + e.getMessage());
            }
        }
        
        return added;
    }
    
    
    /**
     * Saves the current Inventory into a file with the given name
     * 
     * @param fullFileDir The name of the folder and directory that the inventory file will be saved at.
     * @return Boolean that tells if the file was saved or not
     */
    public boolean SaveInventoryToFile(String folderName, String fullFileDir) {
        boolean saved = false;
        PrintWriter outFS = null;
        FileWriter fileWriter = null;
        
        // Create File Directory ONLY
        File file = new File(folderName);
        file.mkdirs();
        
        // Attempts to save all file data
        try {
            fileWriter = new FileWriter(fullFileDir);
            outFS = new PrintWriter(fileWriter);
            
            for (int i = 0; i < inventory.size(); i++) {
                InventoryItem inventoryItem = inventory.get(i);
                
                outFS.println(inventoryItem.getSku() + "|" + inventoryItem.getName() + "|" + inventoryItem.getDescription() + "|" + inventoryItem.getCost() + "|" + inventoryItem.getQuantity());
            }
            
            saved = true;
            System.out.println("File updated successfully.");
        }
        catch (FileNotFoundException e) {
            System.out.println("File was not found");
        }
        catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
        finally {
            // Closes both Print and FileWriter
            try {
                if (outFS != null) {
                    outFS.close();
                }

                if (fileWriter != null) {
                    fileWriter.close();
                }
            }
            catch (IOException e) {
                System.out.println("Error Closing File");
            }
        }
        
        return saved;
    }
    
    /**
     * Grabs and prints out a specific item in the inventory if found
     * 
     * @param _sku SKU Number for the InventoryItem to view
     */
    public void ViewSpecificItem(int _sku) {
        boolean found = false;
        InventoryItem foundItem = new InventoryItem();
        
        // Tries to find given item via SKU in Inventory
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getSku() == _sku) {
                found = true;
                foundItem = inventory.get(i);
                break;
            }
        }
        
        // Prints either the item info or that no item was found
        if (found) {
            foundItem.printInfo();
        }
        else {
            System.out.println("No item found with this SKU.");
        }
    }
    
    /**
     * Removes an item in Inventory given a SKU
     * 
     * @param _sku SKU Number for the InventoryItem to delete
     * @param scnr Scanner object for user input
     * @return Boolean that tells if the item was removed or not
     */
    public boolean RemoveInventoryItem(int _sku, Scanner scnr) {
        char input = ' ';
        boolean found = false;
        int foundIndex = 0;
        
        // Tries to find given item via SKU in Inventory
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getSku() == _sku) {
                found = true;
                foundIndex = i;
                break;
            }
        }
        
        // Prompt user if they want to remove item if found
        if (found) {
            System.out.println("~~~~~~~~~~~Inventory Item #" + _sku + "~~~~~~~~~~~");
            inventory.get(foundIndex).printInfo();
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            
            do {
                System.out.print("Do you want to delete this item from Inventory (Y/N): ");
                input = Character.toUpperCase(scnr.next().charAt(0));
            } while (input != 'Y' && input != 'N');
            
            // Checks for user confirmation before deleting or not
            if (input == 'Y') {
                inventory.remove(foundIndex);
                System.out.println("Item was removed");
                return true;
            }
            else {
                System.out.println("Item was not removed");
                return false;
            }
        }
        else {
            System.out.println("No item found with this SKU.");
            return false;
        }
    }
    
    // Grabs a specific Inventory Item by SKU Num
    public InventoryItem getItemBySku(int _sku) {
        
        // Tries to find given item via SKU in Inventory
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getSku() == _sku) {
                return inventory.get(i);
            }
        }
        
        // If item wasn't found return nothing
        return null;
    }
    
    // Returns the ArrayList of the entire Inventory
    public ArrayList<InventoryItem> getInventory() {
        return inventory;
    }
    
    // Returns the string of the last opened file
    public String getLastOpenedFile() {
        return lastOpenFile;
    }
    
    public void clearInventoryList() {
        inventory = new ArrayList<InventoryItem>();
    }
    
    // Prints out a list of items for pagination
    public void printPageList (int _page, int _itemsPerPage) {
        int i;
        int itemsPrinted = 0;
        
        try {
            // Print items until either maxs items per page or final item printed
            for (i = 0 + (_page * 10); i < inventory.size() && itemsPrinted < _itemsPerPage; i++) {
                InventoryItem tempItem = inventory.get(i);

                System.out.println(tempItem.getSku() + " -- " + tempItem.getName() + " -- " + tempItem.getQuantity());
                itemsPrinted += 1;
            }
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("Nothing In Inventory Yet.");
        }        
    }
    
    // Returns a boolean depending on if an item exists
    public boolean InventoryItemExists(int _sku) {
        
        // Tries to find given item via SKU in Inventory
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getSku() == _sku) {
                return true;
            }
        }
        
        // If item wasn't found, return false
        return false;
    }
}
