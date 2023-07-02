import util.DateTime;

public interface Rentable {
	boolean rentRoom(int custId, DateTime rentDate, int NumOfRentDay);
	boolean returnRoom(DateTime returnDate);
	boolean performMaintenance();
	boolean completeMaintenance(DateTime date);
}
