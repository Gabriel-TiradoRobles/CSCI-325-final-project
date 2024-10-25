package csu.csci325;


/**
 *
 * @author Andrew Taylor
 */
public class ShipmentItem {
    public int trackingNum;
    public String status;
    public String location;

    //public ShipmentItem() {

    //}
    // Constructor
    public ShipmentItem(int initTrackingNum, String initStatus, String initLocation) {
	trackingNum = initTrackingNum;
	status = initStatus;
	location = initLocation;
    }
    
    // Setters
    public void setTrackingNum(int _trackingNum) {
	trackingNum = _trackingNum;
    }

    public void setStatus(String _status) {
	status = _status;
    }

    public void setLocation(String _location) {
	location = _location;
    }
    
    //Getters
    public int getTrackingNum() {
	return trackingNum;
    }

    public String getStatus() {
	return status;
    }

    public String getLocation() {
	return location;
    }

    // Print information on shipment item
    public void printInfo() {
        System.out.println();
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Tracking Number: " + this.trackingNum);
        System.out.println("Status: " + this.status);
        System.out.println("Location: " + this.location);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println();
    }
}
