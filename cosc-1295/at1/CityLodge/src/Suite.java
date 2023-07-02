import util.DateTime;

public class Suite extends Room {
	
	public Suite(int roomId, String featureSummary, DateTime lastMaintenanceDate) {
		super.roomId = "S_" + roomId;
		super.numBeds = 6;
		super.rentalRate = 999;
		super.lateRate = 1099;
		super.status = RoomStatus.Available;
		this.lastMaintenanceDate = lastMaintenanceDate;
		this.featureSummary = featureSummary;		
	}
	
	@Override
	public String getDetails() {
		StringBuilder details = new StringBuilder();
		
		for(HiringRecord record : records) {
			details.append(record.getDetails());
		}
		
		return
				super.getDetails() +  
				"Last Maintenance Date:\t" + this.lastMaintenanceDate.getFormattedDate() + "\n" +
				"----------------------------------------\n" +
				"\nRENTAL RECORD\t\t" +			
				(this.records.isEmpty() ? "NONE\n" +
				"----------------------------------------\n" : "\n" +
				"----------------------------------------\n" +
				details);			
	}
	
	@Override
	public String toString() {
		return 
				this.roomId + ":" + this.numBeds + ":" + 
				this.getClass().getName() + ":" + this.status + ":" + 
				this.lastMaintenanceDate.getEightDigitDate() + ":" + 
				this.featureSummary;
		
		}
}
