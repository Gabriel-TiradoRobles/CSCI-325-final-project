package csu.csci325;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.File;

/**
 * @author Gabriel Tirado, Evan Hughes, Andrew Taylor
 */
public class InventoryManagment {
    public final static int PAGE_SIZE = 10;
    public final static String INVENTORY_FILE_PATH = "./InventoryFiles/";
    
    public static void main (String[] args) {
        Scanner scnr = new Scanner(System.in);
        char input = ' ';
        
        Inventory inventory = new Inventory();
        GenerateReport generateReport = new GenerateReport();
        Shipments shipmentList = new Shipments();
        
        // TESTING PURPOSES ONLY (REMOVE ON RELEASE)
//        for (int i = 0; i < 11; i++) {
//            inventory.AddInventoryItem(new InventoryItem("TestItem" + i, "TestDesc", i + 10, 15, 2.99));
//        }
        
        // Welcome User
        System.out.println("Welcome to Inventory Management\n");
        
        displayMainMenu();
        System.out.print("Choose Menu Option: ");
        input = Character.toUpperCase(scnr.next().charAt(0));
        
        // Loop until user quits
        while (input != 'Q') {
            
            // Main Menu Options
            switch (input) {
                // View Inventory
                case 'V':
                    viewInventoryMenu(scnr, inventory, false);
                    
                    // Display Main menu again when done
                    displayMainMenu();
                    System.out.print("Choose Menu Option: ");
                    input = Character.toUpperCase(scnr.next().charAt(0));
                    break;
                
                // Modify Inventory
                case 'M':
                    modifyInventoryMenu(scnr, inventory);
                    
                    // Display Main menu again when done
                    displayMainMenu();
                    System.out.print("Choose Menu Option: ");
                    input = Character.toUpperCase(scnr.next().charAt(0));
                    break;
                    
                // Track Shipments
                case 'T':
                    
                    // Initialize List of shipments for user to view
                    shipmentList.initializeShipments();
                    
                    // Display shipment menu for user
                    viewShipments(scnr, shipmentList);
                    
                    // Update the shipment file with any changes the user made
                    shipmentList.updateShipmentFile();
                    
                    
                    // Display Main menu again when done
                    displayMainMenu();
                    System.out.print("Choose Menu Option: ");
                    input = Character.toUpperCase(scnr.next().charAt(0));
                    break;
                    
                // Generate Reports
                case 'G':
                     GenerateReport.generateReport(scnr);
                    
                    
                    // Display Main menu again when done
                    displayMainMenu();
                    System.out.print("Choose Menu Option: ");
                    input = Character.toUpperCase(scnr.next().charAt(0));
                    break;
                    
                // Invalid User Input
                default:
                    System.out.print("Choose Menu Option: ");
                    input = Character.toUpperCase(scnr.next().charAt(0));
                    break;
            }
            
            
        }
    }
    
    // Extra Methods Under Here
    
    public static void viewSpecificShipment (Scanner scnr, int _trackNum, Shipments _shipments) {
        char input = ' ';
        char verify = ' ';
        
        _shipments.viewSpecificShipment(_trackNum);

        while (input != 'R' && input!= 'X') {
            System.out.println("Press (X) to remove Shipment or (R) to Return to Previous Menu: ");
            input = Character.toUpperCase(scnr.next().charAt(0));
        }
        
        if (input == 'X'){
            while(verify!= 'Y' && verify != 'N'){
                System.out.println("Are you sure you want to delte this shipment? Yes (Y) No (N): ");
                verify = Character.toUpperCase(scnr.next().charAt(0));
                
            }
            if(verify == 'Y') {
                _shipments.removeShipment(_trackNum);
            }
        }
    }
    
    public static void viewShipments (Scanner scnr, Shipments _shipments) {
        String stringInput = "";
        char charInput = ' ';
        int page = 0;
        int maxPage = (_shipments.getShipmentList().size()) / PAGE_SIZE;
        boolean inputIsNum = false;
        
        while (charInput != 'R') {
            
            System.out.println();
            System.out.println("Enter Tracking Number of shipment you wish to view or delete");
            System.out.println("~~~~~~~~~View Shipments~~~~~~~~~~");
            System.out.println("          PAGE " + (page + 1) + " OF " + (maxPage + 1));
            System.out.println("Track Num --- Status --- Location");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            _shipments.printList(page, PAGE_SIZE);
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("<--- Prev (P) --- Next (N) --->");
            System.out.println("      Return to Menu (R)\n");
            System.out.println();
                    
            do {
                System.out.print("Choose Menu Option or Enter Tracking Number to Select: ");
                
                stringInput = scnr.next();
                
                
                inputIsNum = isNumber(stringInput);
                
                // If input isn't a number, treat it as a char
                if (!inputIsNum) {
                    charInput = Character.toUpperCase(stringInput.charAt(0));
                }     
            } while (charInput != 'R' && charInput != 'P' && charInput != 'N' && inputIsNum == false);

            if (inputIsNum) {
                viewSpecificShipment(scnr, Integer.parseInt(stringInput), _shipments);
            }
            else if (charInput == 'P') {
                if (page > 0) {
                    page--;
                }
            }
            else if (charInput == 'N') {
                if (page < maxPage) {
                    page++;
                }
            }
        }
        System.out.println();
    }
    
    // Main Menu Display
    public static void displayMainMenu () {
        System.out.println("~~~~~~~~~~~MAIN MENU~~~~~~~~~~~");
        System.out.println("V. View Inventory");
        System.out.println("M. Modify Inventory");
        System.out.println("T. Track Shipment(s)");
        System.out.println("G. Generate Report(s)");
        System.out.println("Q. Quit Program\n");
    }
                 
    
    // Modify Main Menu Display
    public static void displayModifyInvMenu (Inventory inventory) {
        System.out.println("~~~~~~~~~~Modify Inventory~~~~~~~~~~");
        System.out.println("Current Opened File: " + inventory.getLastOpenedFile());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("V. View List");
        System.out.println("A. Add Inventory Item");
        System.out.println("F. Add Inventory Item(s) From File");
        if (inventory.getInventory().size() > 0) {
            System.out.println("C. Clear Inventory");
            System.out.println("S. Save Inventory to File");
        }
        System.out.println("R. Return to Menu\n");
    }
    
    // Modify Specific Item Display
    public static void displayModifyItemMenu() {
        System.out.println("~~~~~~~~~~~~~~~~~Modify Item~~~~~~~~~~~~~~~~~");
        System.out.println("N. Change Name");
        System.out.println("D. Change Description");
        System.out.println("C. Change Cost");
        System.out.println("Q. Change Quantity");
        System.out.println("X. Delete Item From Inventory");
        System.out.println("R. Return to Previous Menu\n");
    }
    
    // View Inventory Menu Stuff
    public static void viewInventoryMenu (Scanner scnr, Inventory inventory, boolean modifyMode) {
        String stringInput = "";
        char charInput = ' ';
        boolean inputIsNum;
        int page = 0;
        int maxPage = (int)(Math.ceil(inventory.getInventory().size() / (PAGE_SIZE * 1.0)) - 1);
        
        // Loop until return to menu option selected
        while (charInput != 'R') {
            inputIsNum = false;
            
            // Sends user to max page if they somehow pass it
            if (page >= maxPage) {
                page = maxPage;
            }
            
            // Used to help differentiate between modify and view mode
            if (modifyMode) {
                System.out.println("~~~~~~~~~Modify Inventory~~~~~~~~~");
            }
            else {
                System.out.println("~~~~~~~~~~View Inventory~~~~~~~~~~");
            }
            
            System.out.println("           PAGE " + (page + 1) + " OF " + (maxPage + 1));
            System.out.println("SKU --- Name --- Quantity");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            inventory.printPageList(page, PAGE_SIZE);
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("<--- Prev (P) --- Next (N) --->");
            System.out.println("Return to Menu (R)\n");
            
            // Keep asking for input until valid input given
            do {
                System.out.print("Choose Menu Option or Enter SKU Number to Select: ");
                stringInput = scnr.next();
                
                // Checks to see if user input is a number or menu option
                inputIsNum = isNumber(stringInput);
                
                // If input isn't a number, treat it as a char
                if (!inputIsNum) {
                    charInput = Character.toUpperCase(stringInput.charAt(0));
                }
                
            } while (charInput != 'R' && charInput != 'P' && charInput != 'N' && inputIsNum == false);
            
            // Choose action based on input (View Specific, Prev Page, Next Page)
            if (inputIsNum) {
                viewSpecificItem(scnr, Integer.parseInt(stringInput), inventory, modifyMode);
                maxPage = (int)(Math.ceil(inventory.getInventory().size() / (PAGE_SIZE * 1.0)) - 1);
                System.out.println(inventory.getInventory().size());
            }
            else if (charInput == 'P') {
                if (page > 0) {
                    page--;
                }
            }
            else if (charInput == 'N') {
                if (page < maxPage) {
                    page++;
                }
            }
        }
        System.out.println();
    }
    
    // Attempts to view a specific inventory item via sku number
    public static void viewSpecificItem (Scanner scnr, int _sku, Inventory _inventory, boolean modifyMode) {
        char input = ' ';
        boolean itemFound;
        
        System.out.println("~~~~~~~~~~~Inventory Item #" + _sku + "~~~~~~~~~~~");
        _inventory.ViewSpecificItem(_sku);

        // Loop until return to Menu option selected
        while (input != 'R') {
            itemFound = _inventory.InventoryItemExists(_sku);
            
            // Menu options based on if viewing or planning to modify an item
            if (modifyMode == true && itemFound) {
                displayModifyItemMenu();
                
                // Keep asking for input until valid input given
                do {
                    System.out.print("Choose Menu Option: ");
                    input = Character.toUpperCase(scnr.next().charAt(0));
                } while (input != 'R' && input != 'X' && input != 'N' && input != 'D' && input != 'C' && input != 'Q');
                
                // Modify Certain aspect based on input
                switch (input) {
                    // Name
                    case ('N'):
                        modifyItemName(scnr, _sku, _inventory);
                        break;
                    // Description
                    case ('D'):
                        modifyItemDesc(scnr, _sku, _inventory);
                        break;
                    // Cost
                    case ('C'):
                        modifyItemCost(scnr, _sku, _inventory);
                        break;
                    // Quantity
                    case ('Q'):
                        modifyItemQty(scnr, _sku, _inventory);
                        break;
                    // Delete Item
                    case ('X'):
                        System.out.println();
                        _inventory.RemoveInventoryItem(_sku, scnr);
                        System.out.println();
                        break;
                    // Return to menu
                    case ('R'):
                        return;
                }
                
                System.out.println("~~~~~~~~~~~Inventory Item #" + _sku + "~~~~~~~~~~~");
                _inventory.ViewSpecificItem(_sku);
            }
            else {
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.print("Press (R) to Return to Previous Menu: ");
                input = Character.toUpperCase(scnr.next().charAt(0));
            }
        }
    }
    
    // Modify Inventory Menu Stuff
    public static void modifyInventoryMenu (Scanner scnr, Inventory _inventory) {
        char input = ' ';
        
        // Loop until return to Main Menu option selected
        while (input != 'R') {
            displayModifyInvMenu(_inventory);
            
            // Keep asking for input until valid input given
            do {
                System.out.print("Choose Menu Option: ");
                input = Character.toUpperCase(scnr.next().charAt(0));
            } while (input != 'V' && input != 'A' && input != 'F' && input != 'R' && input != 'C' && input != 'S');
            
            switch (input) {
                // View list of items to modify
                case ('V'):
                    viewInventoryMenu(scnr, _inventory, true);
                    break;
                    
                // Add one inventory item
                case ('A'):
                    addSingleItem(scnr, _inventory);
                    break;
                    
                // Add inventory item(s) from a file
                case ('F'):
                    addItemsFromFile(scnr, _inventory);
                    break;
                    
                // Clear Inventory
                case ('C'):
                    clearInventory(scnr, _inventory);
                    break;
                    
                // Save inventory to a file
                case ('S'):
                    saveInventoryToFile(scnr, _inventory);
                    break;
            }
        }
    }
    
    // Adds a single Inventory Item into Inventory given inputted data
    public static void addSingleItem (Scanner scnr, Inventory _inventory) {
        char input = ' ';
        boolean added;
        
        int sku = askForSKU(scnr);
        String name = askForName(scnr);
        String description = askForDesc(scnr);
        double cost = askForCost(scnr);
        int quantity = askForQty(scnr);
        
        System.out.println("~~~~~~~~~~~~~New Item~~~~~~~~~~~~~");
        System.out.println("SKU: " + sku);
        System.out.println("Name: " + name);
        System.out.println("Description: " + description);
        System.out.println("Cost: " + cost);
        System.out.println("Quantity: " + quantity);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("Are you sure you wish to add this item into the inventory? (Y/N)");
        
        // Keep asking for input until valid input given
        do {
            System.out.print("Choice: ");
            input = Character.toUpperCase(scnr.next().charAt(0));
        } while (input != 'Y' && input != 'N');
        
        // Ask for confirmation for adding item
        if (input == 'Y') {
            added = _inventory.AddInventoryItem(new InventoryItem(name, description, sku, quantity, cost));
            
            // Outputs certain statement based on item added
            if (added) {
                System.out.println("Item was added into the inventory.");
            }
            else {
                System.out.println("Item was not added into the inventory. Item with same SKU exists");
            }
        }
        else {
            System.out.println("Item was not added into the inventory.");
        }
    }
    
    // Adds information from a given file into Inventory
    public static void addItemsFromFile (Scanner scnr, Inventory _inventory) {
        File inventoryList = new File(INVENTORY_FILE_PATH);
        inventoryList.mkdirs();
        String[] fileList = inventoryList.list();
        
        char charInput;
        String stringInput = "";
        int page = 0;
        int maxPage = (int)(Math.ceil(fileList.length / (PAGE_SIZE * 1.0)) - 1);
        int itemsPrinted;
        
        // Loop until return to Modify Menu option selected
        while (!stringInput.equals("R")) {
            itemsPrinted = 0;
            
            System.out.println("~~~~~~~~~~~~~File List~~~~~~~~~~~~~");
            System.out.println("           PAGE " + (page + 1) + " OF " + (maxPage + 1));
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            // Print items until either maxs items per page or final item printed
            for (int i = 0 + (page * 10); i < fileList.length && itemsPrinted < PAGE_SIZE; i++) {
                System.out.println(fileList[i]);
                itemsPrinted++;
            }
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("<--- Prev (P) --- Next (N) --->");
            System.out.println("Return to Menu (R)\n");
            

            // Keep asking for input until valid input given
            do {
                System.out.print("Choose Menu Option or Enter File Name to Select: ");
                stringInput = scnr.next();
            } while (stringInput.equals(""));
            
            // Decide what option to do
            if (stringInput.length() == 1) {
                stringInput = stringInput.toUpperCase();
                
                switch (stringInput) {
                    case ("N"):
                        if (page < maxPage) {
                            page++;
                        }
                        break;
                        
                    case ("P"):
                        if (page > 0) {
                            page--;
                        }
                        break;
                }
            }
            else {
                System.out.println("Note: This will overwrite ALL previously inputted data.");
                System.out.println("Are you sure you want to import this file? (Y/N)");
                System.out.println("File: " + stringInput);
                
                do {
                    System.out.print("Input: ");
                    charInput = Character.toUpperCase(scnr.next().charAt(0));
                } while (charInput != 'Y' && charInput != 'N');
                
                if (charInput == 'Y') {
                    _inventory.clearInventoryList();
                    _inventory.AddInventoryItemsByFile(INVENTORY_FILE_PATH + stringInput);
                    stringInput = "R";
                }
                else {
                    System.out.println("File Not Imported.");
                }
            }
        }
    }
    
    ///////////////////////
    //// OTHER METHODS ////
    ///////////////////////
    
    // Clears the current Inventory after confirmation. Doesn't automatically save
    public static void clearInventory (Scanner scnr, Inventory _inventory) {
        char input = ' ';
        
        System.out.println("Note: This will not save to a file, only remove it from the program");
        System.out.println("Are you sure you wish to clear the current inventory?");
        System.out.println("This action can not be undone. (Y/N)");
        
        do {
            System.out.print("Input: ");
            input = Character.toUpperCase(scnr.next().charAt(0));
        } while (input != 'Y' && input != 'N');
        
        if (input == 'Y') {
            _inventory.clearInventoryList();
            System.out.println("Inventory Has Been Cleared");
        }
        else {
            System.out.println("Inventory Not Cleared");
        }
    }
    
    // Will save the current Inventory to a file. Currently only does file last opened
    public static void saveInventoryToFile (Scanner scnr, Inventory _inventory) {
        char charInput = ' ';
        
        System.out.println("How would you like to save this file?");
        if (!_inventory.getLastOpenedFile().equals("")) {
            System.out.println("O. Save over last opened file? (" + _inventory.getLastOpenedFile() + ")");
        }
        System.out.println("S. Save to new file");
        System.out.println("R. Return to Previous Menu");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        
        do {
            System.out.print("Input: ");
            charInput = Character.toUpperCase(scnr.next().charAt(0));
        } while (charInput != 'O' && charInput != 'S' && charInput != 'R');
        
        switch (charInput) {
            case ('O'):
                if (!_inventory.getLastOpenedFile().equals("")) {
                    confirmFileOverwrite(scnr, _inventory, "", false);
                }
                break;
                
            case ('S'):
                saveToNewFile(scnr, _inventory);
                break;
                
            case ('R'):
                System.out.println("Save Aborted, Returning to previous menu.");
                break;
        }
    }
    
    // Shows a confirmation screen before allowing the user to save their data (Overwrite or Save to new file)
    public static void confirmFileOverwrite(Scanner scnr, Inventory _inventory, String fileName, boolean overwriteMode) {
        char input = ' ';
        
        if (overwriteMode) {
            System.out.println("Are you sure you want to overwrite saved data? (Y/N)");
        }
        else {
            System.out.println("Are you sure you want to save to " + fileName + "? (Y/N)");
        }
        
        // Keep asking until valid input given
        do {
            System.out.print("Input: ");
            input = Character.toUpperCase(scnr.next().charAt(0));
        } while (input != 'Y' && input != 'N');
        
        if (input == 'Y') {
            if (overwriteMode) {
                _inventory.SaveInventoryToFile(INVENTORY_FILE_PATH, _inventory.getLastOpenedFile());
            }
            else {
                _inventory.SaveInventoryToFile(INVENTORY_FILE_PATH, INVENTORY_FILE_PATH + fileName);
            }
        }
        else {
            System.out.println("Save Aborted, Returning to previous menu.");
        }
    }
    
    // Asks the user for the name of the file they wish to save to
    public static void saveToNewFile(Scanner scnr, Inventory _inventory) {
        String fileName = "";
        
        System.out.println("Enter the name of file to save to (Ex: Inventory01.txt OR Inventory01)");
        // Keep asking until valid input given
        do {
            System.out.print("File Name: ");
            fileName = scnr.next();
        } while (fileName.equals(""));
        
        // Automatically add .txt if none provided
        if (fileName.contains(".txt")) {
            confirmFileOverwrite(scnr, _inventory, fileName, false);
        }
        else {
            confirmFileOverwrite(scnr, _inventory, fileName + ".txt", false);
        }
    }
    
    // Changes an Inventory Item's name after confirmation
    public static void modifyItemName (Scanner scnr, int _sku, Inventory _inventory) {
        char input = ' ';
        String newName = "";
        
        // Clear Any extra input
        scnr.nextLine();
        
        System.out.println("~~~~~~~~~~~~~~~Change Name~~~~~~~~~~~~~~~");
        System.out.println("Previous Name: " + _inventory.getItemBySku(_sku).getName());
        
        do {
            System.out.print("Enter New Name: ");
            newName = scnr.nextLine();
        } while (newName.equals(""));
        
        do {
            System.out.print("Are you sure you want to change name (Y/N):");
            input = Character.toUpperCase(scnr.next().charAt(0));
        } while (input != 'Y' && input != 'N');
        
        if (input == 'Y') {
            _inventory.getItemBySku(_sku).setName(newName);
            System.out.println("Name Successfully Changed");
        }
        else {
            System.out.println("Name Not Changed");
        }
    }
    
    // Changes an Inventory Item's description after confirmation
    public static void modifyItemDesc (Scanner scnr, int _sku, Inventory _inventory) {
        char input = ' ';
        String newDesc = "";
        
        // Clear Any extra input
        scnr.nextLine();
        
        System.out.println("~~~~~~~~~~~~Change Description~~~~~~~~~~~~");
        System.out.println("Previous Description:");
        System.out.println(_inventory.getItemBySku(_sku).getDescription());
        
        do {
            System.out.println("Enter New Description:");
            newDesc = scnr.nextLine();
        } while (newDesc.equals(""));
        
        do {
            System.out.print("Are you sure you want to change the description (Y/N):");
            input = Character.toUpperCase(scnr.next().charAt(0));
        } while (input != 'Y' && input != 'N');
        
        if (input == 'Y') {
            _inventory.getItemBySku(_sku).setDescription(newDesc);
            System.out.println("Description Successfully Changed");
        }
        else {
            System.out.println("Description Not Changed");
        }
    }
    
    // Changes an Inventory Item's cost after confirmation
    public static void modifyItemCost (Scanner scnr, int _sku, Inventory _inventory) {
        char input = ' ';
        double newCost = 0.0;
        
        // Clear Any extra input
        scnr.nextLine();
        
        System.out.println("~~~~~~~~~~~~~~~Change Cost~~~~~~~~~~~~~~~");
        System.out.println("Previous Cost: " + _inventory.getItemBySku(_sku).getCost());
        
        do {
            try {
                System.out.print("Enter New Cost: ");
                newCost = scnr.nextDouble();
            }
            catch (InputMismatchException e) {
                System.out.println("Please enter a price (EX: 1.99)");
                scnr.nextLine();
            }
        } while (newCost <= 0.0);
        
        do {
            System.out.print("Are you sure you want to change cost (Y/N):");
            input = Character.toUpperCase(scnr.next().charAt(0));
        } while (input != 'Y' && input != 'N');
        
        if (input == 'Y') {
            _inventory.getItemBySku(_sku).setCost(newCost);
            System.out.println("Cost Successfully Changed");
        }
        else {
            System.out.println("Cost Not Changed");
        }
    }
    
    // Changes an Inventory Item's quantity after confirmation
    public static void modifyItemQty (Scanner scnr, int _sku, Inventory _inventory) {
        char input = ' ';
        int newQty = 0;
        
        // Clear Any extra input
        scnr.nextLine();
        
        System.out.println("~~~~~~~~~~~~~~~Change Quantity~~~~~~~~~~~~~~~");
        System.out.println("Previous Quantity: " + _inventory.getItemBySku(_sku).getQuantity());
        
        do {
            try {
                System.out.print("Enter New Quantity: ");
                newQty = scnr.nextInt();
            }
            catch (InputMismatchException e) {
                System.out.println("Please enter an integer (EX: 3)");
                scnr.nextLine();
            }
        } while (newQty < 0);
        
        do {
            System.out.print("Are you sure you want to change quantity (Y/N):");
            input = Character.toUpperCase(scnr.next().charAt(0));
        } while (input != 'Y' && input != 'N');
        
        if (input == 'Y') {
            _inventory.getItemBySku(_sku).setQuantity(newQty);
            System.out.println("Quantity Successfully Changed");
        }
        else {
            System.out.println("Quantity Not Changed");
        }
    }
    
    // Ask for a sku number for new item. Randomize if none given
    public static int askForSKU (Scanner scnr) {
        int sku = -2;
        
        System.out.println("Note: Don't use random a SKU if you have a large inventory.");
        System.out.println("Enter SKU number of new item. Input -1 for a random SKU.");
        
        // Keep asking for input until valid input given
        do {
            try {
                System.out.print("SKU: ");
                sku = scnr.nextInt();
            }
            catch (InputMismatchException e) {
                System.out.println("Please enter an integer. (EX: 3)");
                scnr.nextLine();
            }
        } while (sku < -1);
        
        
        // Returns either inputted SKU or a random SKU number if none given
        if (sku == -1) {
            return (int)(Math.random() * 999999999) + 1;
        }
        else {
            return sku;
        }
    }
    
    // Ask for a name for new item. Not optional
    public static String askForName (Scanner scnr) {
        String name = "";
        
        System.out.println("Enter a Name for the item you're adding.");
        
        // Asks for a name until one is given
        do {
            System.out.print("Name: ");
            name = scnr.nextLine();
        } while (name.equals(""));
        
        return name;
    }
    
    // Ask for a description for new item. Optional
    public static String askForDesc (Scanner scnr) {
        String desc = "";
        
        System.out.println("Enter a Description for the item you're adding. (Optional)");
        
        // Asks for a name until one is given
        System.out.println("Description:");
        desc = scnr.nextLine();
        
        if (desc.equals("")) {
            return "No Description";
        }
        else { 
            return desc;
        }
    }
    
    // Ask for a cost for new item. Not optional
    public static double askForCost (Scanner scnr) {
        double cost = 0.0;
        
        System.out.println("Enter the Cost for the item you're adding.");
        
        do {
            try {
                System.out.print("Cost: ");
                cost = scnr.nextDouble();
            }
            catch (InputMismatchException e) {
                System.out.println("Please enter a price. (EX: 2.99)");
                scnr.nextLine();
            }
        } while (cost == 0.0);
        
        return cost;
    }
    
    // Ask for a quantity for new item. Requires non-negative number
    public static int askForQty (Scanner scnr) {
        int qty = 0;
        
        System.out.println("Enter the Quantity for the item you're adding.");
        
        do {
            try {
                System.out.print("Quantity: ");
                qty = scnr.nextInt();
            }
            catch (InputMismatchException e) {
                System.out.println("Please enter an integer. (EX: 3)");
                scnr.nextLine();
            }
        } while (qty < 0);
        
        return qty;
    }
    
    // Checks to see if the given string is a number
    public static boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
