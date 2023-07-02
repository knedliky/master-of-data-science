import util.DateTime;

public class HiringRecord implements Recordable {
	String recordId;
	String custId = "CUS_";
	
	// Decided to instantiate actualReturnDate to the current time so that 
	// it's possible to test different cases in other methods
	DateTime rentDate;
	DateTime estimatedReturnDate;
	DateTime actualReturnDate;
	
	double rentalFee = 0;
	double lateFee = 0;
	boolean returned = false;

	// Creates a Hiring Record object while creating record id
	public HiringRecord(String roomId, int custId, DateTime rentDate, int numOfRentDay) {
		this.custId += custId + "_";
		this.rentDate = rentDate;
		this.recordId = roomId + "_"+ this.custId + rentDate.getEightDigitDate();
		this.estimatedReturnDate = new DateTime(rentDate, numOfRentDay);

	}

	// Updates the hiring record once a Room has been returned with relevant information
	public void updateRecord(DateTime actualReturnDate, double rentalRate, double lateRate) {
		this.actualReturnDate = actualReturnDate;

		if ((DateTime.diffDays(actualReturnDate, estimatedReturnDate)) > 0) {
			this.lateFee = lateRate * (DateTime.diffDays(actualReturnDate, estimatedReturnDate));
			this.rentalFee = rentalRate * (DateTime.diffDays(estimatedReturnDate, rentDate));
			returned = true;

		} else {
			this.rentalFee = rentalRate * (DateTime.diffDays(actualReturnDate, rentDate));
			returned = true;
			
		}
	}

	@Override
	public String getDetails() {
		return 
				"Record ID:\t\t" + this.recordId + "\n" +
				"Rent Date:\t\t" + this.rentDate + "\n" +
				"Estimated Return Date:\t" + this.estimatedReturnDate + "\n" +

				(returned ?
						"Actual Return Date:\t" + this.actualReturnDate + "\n" +
						"Rental Fee:\t\t$" + String.format("%.2f",this.rentalFee) + "\n" +
						"Late Fee:\t\t$" + String.format("%.2f", this.lateFee) +"\n":
						"\n") +

				"----------------------------------------\n";
	}

	@Override
	public String toString() {
		return 
				this.recordId +":" + this.rentDate + ":" + 
				this.estimatedReturnDate + ":" + this.actualReturnDate + ":" + 
				this.rentalFee + ":" + this.lateFee +
				(!(returned) ? "NONE:NONE:NONE" : this.actualReturnDate.getFormattedDate()+ ":" + 
						String.format("%.2f",this.rentalFee) + ":" + String.format("%.2f", this.lateFee)); 
	}
}
