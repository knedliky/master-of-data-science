import java.util.ArrayList;
import util.DateTime;

public abstract class Room implements Rentable, Recordable {	
	// All Room objects have these universal fields
	String roomId;
	String featureSummary;
	int numBeds;
	double rentalRate;
	double lateRate;

	RoomStatus status;
	DateTime lastMaintenanceDate;
	ArrayList<HiringRecord> records = new ArrayList<HiringRecord>(10);
	HiringRecord currentRecord;
	
	@Override
	public boolean rentRoom(int custId, DateTime rentDate, int numOfRentDays) {

		this.currentRecord = new HiringRecord(this.roomId, custId, rentDate, numOfRentDays);
		this.records.add(0,this.currentRecord);
		if(records.size() > 10) {
			records.remove(9);
		}
		this.status = RoomStatus.Rented;
		return true;

	}

	@Override
	public boolean returnRoom(DateTime actualReturnDate) {
		this.currentRecord.updateRecord(actualReturnDate, this.rentalRate, this.lateRate);
		this.status = RoomStatus.Available;
		return true;
		
	}

	@Override
	public boolean performMaintenance() {
		System.out.println("Room " + this.roomId + " is now under maintenance...\n");
		this.status = RoomStatus.Maintenance;
		return true;
		
	}

	@Override
	public boolean completeMaintenance(DateTime maintenanceCompleteDate) {
		System.out.println("Room " + this.roomId + " has completed maintenance!\n");
		this.status = RoomStatus.Available;
		this.lastMaintenanceDate = maintenanceCompleteDate;
		return true;
		
	}

	@Override
	public abstract String toString();

	@Override
	public String getDetails() {
		return 
				"----------------------------------------\n" +
				"ROOM DETAILS\n"+
				"Room ID:\t\t" + this.roomId + "\n" +
				"Bedrooms:\t\t" + this.numBeds + "\n" +				
				"Room Type:\t\t" + this.getClass().getName() + "\n" +
				"Status:\t\t\t" + this.status + "\n" +				
				"Feature summary:\t" + this.featureSummary + "\n";
	}
}
