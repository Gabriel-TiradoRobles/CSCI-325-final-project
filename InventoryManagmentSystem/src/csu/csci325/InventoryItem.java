package csu.csci325;

/**
 * Class for a generic inventory item that will be stored in the store's inventory
 * 
 * @author Gabriel Tirado
 */
public class InventoryItem {
    protected String name;
    protected String description;
    protected int sku;
    protected boolean inStock;
    protected int quantity;
    protected double cost;
    
    // Default Constructor
    public InventoryItem() {
        this.name = "none";
        this.description = "none";
        this.sku = -1;
        this.inStock = false;
        this.quantity = -1;
        this.cost = 0.00;
    }
    
    // Constructor Overload: All Parameters
    public InventoryItem(String _name, String _desc, int _sku, int _qty, double _cost) {
        this.name = _name;
        this.description = _desc;
        this.sku = _sku;
        this.quantity = _qty;
        this.cost = _cost;
        
        if (this.quantity > 0) {
            this.inStock = true;
        }
        else {
            this.inStock = false;
        }
    }
    
    // Setters
    public void setName (String _name) {
        this.name = _name;
    }
    
    public void setDescription (String _desc) {
        this.description = _desc;
    }
    
    public void setSku (int _sku) {
        this.sku = _sku;
    }
    
    public void setQuantity (int _qty) {
        this.quantity = _qty;
        // Update inStock var
        updateInStock();
    }
    
    public void setCost (double _cost) {
        this.cost = _cost;
    }
    
    // Getters
    public String getName () {
        return this.name;
    }
    
    public String getDescription () {
        return this.description;
    }
    
    public int getSku () {
        return this.sku;
    }
    
    public int getQuantity () {
        return this.quantity;
    }
    
    public double getCost () {
        return this.cost;
    }
    
    // Other Methods
    
    // Updates the InStock value if quantity is > 0
    private void updateInStock() {
        if (this.quantity > 0) {
            this.inStock = true;
        }
        else {
            this.inStock = false;
        }
    }
    
    // Prints the information about given inventory item
    public void printInfo() {
        System.out.println("SKU: " + this.sku);
        System.out.println("Item Name: " + this.name);
        System.out.println("Item Description: " + this.description);
        
        System.out.println("In Stock: " + this.inStock);
        System.out.println("Quantity: " + this.quantity);
        System.out.printf("Cost: $%.2f\n", this.cost);
    }
}
