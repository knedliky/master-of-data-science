package model;

public class Standard extends Room {
	
	private static final long serialVersionUID = 1L;

	public Standard() {
		super();
	}
	
	public Standard(Integer roomId, String featureSummary, Integer numBeds) throws Exception {
		this();
		setRoomId(roomId);
		setNumBeds(numBeds);
		setFeatureSummary(featureSummary);
		setStatus(RoomStatus.Available);
		switch(getNumBeds()) {
		case 1: 
			setRentalRate(59.00);
			break;

		case 2:
			setRentalRate(99.00);
			break;

		case 4:
			setRentalRate(199.00);
			break;

		}
		setLateRate(getRentalRate()* 1.35);

	}

	@Override
	public String getDetails() {
		StringBuilder details = new StringBuilder();
		
		for(HiringRecord record : getRecords()) {
			details.append(record.getDetails());
		}
		
		return 
				super.getDetails() +
				"----------------------------------------\n" +
				"\nRENTAL RECORD\t\t" +
				(getRecords().isEmpty() ? "NONE\n" + 
				"----------------------------------------\n" : "\n" + details);
	}

	@Override
	public String toString() {
		return 
				"R_" + getRoomId() + ":"  + getClass().getSimpleName() + ":" + 
				getNumBeds()  + ":" + getFeatureSummary() + ":" + 
				getStatus() + ":" + getRentalRate() + ":" + getLateRate();

	}

}






