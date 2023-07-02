package model;

import util.DateTime;

public class Suite extends Room {
	
	private static final long serialVersionUID = 1L;

	public Suite() {
		super();
		setNumBeds(6);
		setRentalRate(999);
		setLateRate(1099);
		setStatus(RoomStatus.Available);
	}
	
	public Suite(int roomId, String featureSummary, DateTime lastMaintenanceDate) throws Exception {
		this();
		setRoomId(roomId);
		setLastMaintenanceDate(lastMaintenanceDate);
		setFeatureSummary(featureSummary);
	}
	
	@Override
	public String getDetails() {
		StringBuilder details = new StringBuilder();
		
		for(HiringRecord record : getRecords()) {
			details.append(record.getDetails());
		}
		
		return
				super.getDetails() +  
				"Last Maintenance Date:\t" + getLastMaintenanceDate().getFormattedDate() + "\n" +
				"----------------------------------------\n" +
				"\nRENTAL RECORD\t\t" +			
				(getRecords().isEmpty() ? "NONE\n" +
				"----------------------------------------\n" : "\n" +
				"----------------------------------------\n" +
				details);			
	}
	
	@Override
	public String toString() {
		return
				"S_" + getRoomId() + ":" + getClass().getSimpleName() + ":" + 
				getNumBeds() + ":" + getFeatureSummary() + ":" + getStatus() + ":" + 
				getRentalRate() + ":" + getLateRate() + ":" + 
				getLastMaintenanceDate().getEightDigitDate();
		}
}
