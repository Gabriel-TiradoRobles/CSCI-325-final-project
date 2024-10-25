package csu.csci325;

/**
 *
 * @author Gabriel Tirado
 */
public class ProduceItem extends InventoryItem {
    protected boolean canExpire;
    protected int avgShelfLife;
    
    // Default Constructor
    public ProduceItem() {
        this.name = "";
        this.description = "";
        this.sku = -1;
        this.inStock = false;
        this.quantity = -1;
        this.cost = 0.0;
        this.canExpire = false;
        this.avgShelfLife = -1;
    }
    
    // Constructor all params
    public ProduceItem(String _name, String _desc, int _sku, boolean _inStock, int _qty, double _cost, boolean _canExp, int _shelfLife) {
        this.name = _name;
        this.description = _desc;
        this.sku = _sku;
        this.inStock = _inStock;
        this.quantity = _qty;
        this.cost = _cost;
        this.canExpire = _canExp;
        this.avgShelfLife = _shelfLife;
    }
    
    // Setters
    public void setAvgShelfLife(int _avgShelfLife) {
        this.avgShelfLife = _avgShelfLife;
        updateCanExpire();
    }
    
    // Getters
    public boolean getCanExpire() {
        return this.canExpire;
    }
    
    public int getAvgShelfLife() {
        return this.avgShelfLife;
    }
    
    // Other Methods
    
    // Updates the canExpire value if the average shelf life is > 0
    private void updateCanExpire() {
        if (this.avgShelfLife > 0) {
            this.canExpire = true;
        }
        else {
            this.canExpire = false;
        }
    }
    
    // Prints the information about given inventory item
    @Override
    public void printInfo() {
        System.out.println("SKU: " + this.sku);
        System.out.println("Item Name: " + this.name);
        System.out.println("Item Description: " + this.description);
        
        System.out.println("Can Expire: " + this.canExpire);
        System.out.println("Average Shelf Life: " + this.avgShelfLife);
        
        System.out.println("In Stock: " + this.inStock);
        System.out.println("Quantity: " + this.quantity);
        System.out.printf("Cost: $%.2f\n", this.cost);
    }
}
