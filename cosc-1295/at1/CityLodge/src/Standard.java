
public class Standard extends Room {

	public Standard(int roomId, String featureSummary, int numBeds) {
		super.roomId = "R_" + roomId;
		this.numBeds = numBeds;
		this.featureSummary = featureSummary;
		super.status = RoomStatus.Available;

		switch(this.numBeds) {
		case 1: 
			super.rentalRate = 59.00;
			break;

		case 2:
			super.rentalRate = 99.00;
			break;

		case 4:
			super.rentalRate = 199.00;
			break;

		}

		super.lateRate = super.rentalRate * 1.35;

	}

	@Override
	public String getDetails() {
		StringBuilder details = new StringBuilder();
		
		for(HiringRecord record : records) {
			details.append(record.getDetails());
		}
		
		return 
				super.getDetails() +
				"----------------------------------------\n" +
				"\nRENTAL RECORD\t\t" +
				(this.records.isEmpty() ? "NONE\n" + 
				"----------------------------------------\n" : "\n" + details);
	}

	@Override
	public String toString() {
		return this.roomId + ":" + this.numBeds + ":" + this.getClass().getName() + ":" + this.status + ":" + this.featureSummary;

	}

}






