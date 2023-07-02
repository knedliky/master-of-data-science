
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.DateTime;

// Instantiating a new City Lodge object with 50 Room objects in a HashMap
// and a global custId variable to keep check of customers
public class CityLodge {
	public int custId = 1;
	HashMap<Integer, Room> rooms = new HashMap<Integer, Room>(50);
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	// Menu items
	public void menu() {

		System.out.println("######################################");
		System.out.println("  ##\t##\t##\t##\t##");
		System.out.println("######################################\n");
		System.out.println("Hello and Welcome to City Lodge Melbourne!\n");
		System.out.println("1: Add New Room");
		System.out.println("2: Rent Room");
		System.out.println("3: Return room");
		System.out.println("4: Perform Room Maintenance");
		System.out.println("5: Complete Room Maintenance");
		System.out.println("6: Display All Rooms");
		System.out.println("7: Exit Program\n");
		System.out.println("Enter Your Choice: ");

		try {
			int input = Integer.parseInt(br.readLine());

			if (input < 0 || input > 7) {
				System.out.println("Please select one of the options again\n");
				menu();

			} else if (input == 7) {
				System.out.println("You have quit the program\n");
				System.exit(1);

			} else {
				switch (input) {
				case 1:
					addRoom();
					break;

				case 2:
					rentRoom();
					break;

				case 3:
					returnRoom();
					break;

				case 4:
					performMaintenance();
					break;

				case 5:
					completeMaintenance();
					break;

				case 6:
					displayAllRooms();
					break;

				default:
					break;

				}

				menu();
			}
		} catch (IOException ioe) {
			System.out.println("Error trying to read your input!\n");
			menu();

		} catch (NumberFormatException e) {
			System.out.println("Error! Please enter a valid integer!\n");
			menu();

		}
	}

	public void addRoom() throws IOException {
		System.out.println("----------------------------------------");
		System.out.println("ADDING NEW ROOM");
		System.out.println("Please create a 3 digit room ID");
		System.out.println("Enter '0' to return to Menu");

		try {
			int roomId = addRoomId();

			if(roomId == 0) {
				return;
			}

			String featureSummary = createFeatureSummary();
			createRoom(roomId, featureSummary);

		} catch (NumberFormatException nfe) {
			System.out.println("Error! Please enter an integer!");
			addRoom();

		}
	}

	public void rentRoom() throws IOException {		
		System.out.println("----------------------------------------");
		System.out.println("RENTING A ROOM");
		displayRoomByStatus(RoomStatus.Available);

		try {
			System.out.println("Which room would you like to rent?");
			System.out.println("Enter '0' to return to Menu");

			int roomId = selectRoomByStatus(RoomStatus.Available);

			if(roomId == 0) {
				return;
			}
			
			// Checks this isn't being rented before the return date of previous 
			DateTime rentDate;
			if(rooms.get(roomId).records.size() > 0) {
				rentDate = DateTime.inputDate(rooms.get(roomId).currentRecord.actualReturnDate);
				
			} else {
				rentDate = DateTime.inputDate(new DateTime());

			}
			int numOfRentDays = numOfRentDays(roomId, rentDate);

			rooms.get(roomId).rentRoom(custId, rentDate, numOfRentDays);
			custId++;

			System.out.println("Room rented for " +rentDate.getNameOfDay() +", " + 
					rentDate.getFormattedDate() + " for " + numOfRentDays + " days!\n");

		} catch (NumberFormatException nfe) {
			System.out.println("Error! Please enter an integer!");
			rentRoom();

		} catch (NullPointerException e) {
			System.out.println("That room doesn't yet exist!");
			rentRoom();

		}
	}

	public void returnRoom() throws IOException {

		System.out.println("----------------------------------------");
		System.out.println("RETURNING A ROOM");
		displayRoomByStatus(RoomStatus.Rented);

		try {
			System.out.println("Which room is being returned?");
			int roomId = selectRoomByStatus(RoomStatus.Rented);

			if(roomId == 0) {
				return;
			}

			System.out.println("What date was the room been returned?");
			DateTime actualReturnDate = DateTime.inputDate(rooms.get(roomId).currentRecord.rentDate);

			rooms.get(roomId).returnRoom(actualReturnDate);
			System.out.println("UPDATING RECORDS...");
			System.out.println(rooms.get(roomId).getDetails());

		} catch (NumberFormatException nfe) {
			System.out.println("Error! Please enter an integer!");
			returnRoom();

		} catch (NullPointerException e) {
			System.out.println("That room doesn't yet exist!");
			returnRoom();

		}

	}

	public void performMaintenance() throws IOException {

		try {
			System.out.println("----------------------------------------");
			System.out.println("PERFORMING ROOM MAINTENANCE");
			displayRoomByStatus(RoomStatus.Available);

			System.out.println("Enter the room to be maintainenced");
			System.out.println("Enter '0' to return to Menu");
			int roomId = selectRoomByStatus(RoomStatus.Available);

			if(roomId == 0) {
				return;
			}

			rooms.get(roomId).performMaintenance();

		} catch (NullPointerException e) {
			System.out.println("Error! Please enter an integer!");
			performMaintenance();

		} catch (NumberFormatException nfe) {
			System.out.println("That room doesn't yet exist!");
			performMaintenance();

		}

	} 

	public void completeMaintenance() throws IOException {

		try {
			System.out.println("----------------------------------------");
			System.out.println("COMPLETING ROOM MAINTENANCE");
			displayRoomByStatus(RoomStatus.Maintenance);

			System.out.println("Enter the 3 digit room to complete maintenace:");
			int roomId = selectRoomByStatus(RoomStatus.Maintenance);

			if(roomId == 0) {
				return;
			}

			// For the purpose of being able to maintain a room in the future, 
			// allowing user input for completion date 
			System.out.println("When was the maintenance completed?");
			DateTime maintenanceCompleteDate = DateTime.inputDate(new DateTime());

			rooms.get(roomId).completeMaintenance(maintenanceCompleteDate);

		} catch (NullPointerException e) {
			System.out.println("Please select a valid option!\n");
			completeMaintenance();
		}

	} 

	public void displayAllRooms() {
		rooms.forEach((key,value) -> System.out.println(value.getDetails()));
	}

	// Displays rooms depending on if they are Rented, Maintenance or Available
	public void displayRoomByStatus(RoomStatus status) {
		System.out.println(status.toString().toUpperCase() + " ROOMS\n");
		rooms.forEach((key,value) -> {
			if(value.status == status) {
				System.out.println(value.getDetails());
			}
		});
	}

	// Modular method of the Add Room menu item, creates the feature summary portion of a room
	public String createFeatureSummary() throws IOException {

		while (true) {
			System.out.println("Please create a 20 word feature summary for the room:");
			String input = br.readLine();

			if(input.split(" ").length > 20) {
				System.out.println("Your summary is " + (input.split(" ").length - 20) +
						" words too long. Try again!");
				continue;

			} else if(input.split(" ").length < 1) {
				System.out.println("Your summary is too short. Try again!");
				continue;

			}

			return input;
		}
	}

	// Modular method of the Add Room menu item, creates a Suite from 
	// roomId and featureSummary, asking for last maintenance date
	public void createSuite(int roomId, String featureSummary) throws IOException {

		System.out.println("Please enter last maintenance date of the Suite");
		System.out.println("If unknown, enter today's date: " + new DateTime().getFormattedDate());

		DateTime lastMaintenanceDate = DateTime.inputDate(new DateTime(-10));

		if(DateTime.diffDays(lastMaintenanceDate, new DateTime()) > 0) {
			System.out.println("The last maintenance date can't be in the future, please try again");
			createSuite(roomId, featureSummary);
			return;
		}

		System.out.println("New Suite created!\n");
		rooms.put(roomId, new Suite(roomId, featureSummary, lastMaintenanceDate));
	}

	// Modular method of Add Room menu item, creates a Standard room
	// asking for either 1,2 or 4 bedrooms
	public void createStandardRoom(int roomId, String featureSummary) throws NumberFormatException, IOException {
		System.out.println("Please enter number of bedrooms to add to the room");

		int numBeds = Integer.parseInt(br.readLine());

		if ((numBeds != 1) ^ (numBeds != 2) ^ (numBeds != 4)) {
			System.out.println("Please enter 1, 2 or 4 bedrooms\n");
			createStandardRoom(roomId, featureSummary);
		}

		System.out.println("New Standard Room created!\n");
		rooms.put(roomId, new Standard(roomId, featureSummary, numBeds));
	}

	// Modular method of Add Room menu item, separates Standard and Suite objects
	public void createRoom(int roomId, String featureSummary) throws NumberFormatException, IOException {
		System.out.println("Would you like to add a Standard Room or a Suite?");
		System.out.println("1: Standard Room");
		System.out.println("2: Suite");

		int input = Integer.parseInt(br.readLine());

		if (input == 1) {
			System.out.println("Creating Standard Room...");
			createStandardRoom(roomId, featureSummary);
			menu();

		} else if (input == 2) {
			System.out.println("Creating Suite...");
			createSuite(roomId, featureSummary);
			menu();

		} else {
			System.out.println("Please select a valid option\n");
			createRoom(roomId, featureSummary);
		} 

	} 

	// Creates a roomId making sure it is unique and valid
	public int addRoomId() throws NumberFormatException, IOException {

		while (true) {
			System.out.println("Room ID:");
			int input = Integer.parseInt(br.readLine());

			if(input == 0) {
				return input;

			} else if (input < 100 || input > 999) {
				System.out.println("Please enter a valid room number between 100 and 999 or enter '0' to exit to Menu");
				continue;

			} else if (rooms.containsKey(input)) {
				System.out.println("This ID has already been allocated to a room!");
				continue;

			}

			return input;
		}	

	}

	// Takes user input for selecting a room and ensures that the selection is valid
	public int selectRoomByStatus(RoomStatus status) throws NullPointerException, IOException {

		while (true) {
			System.out.println("Room ID:");

			String input = br.readLine();

			if(Integer.parseInt(input) == 0) {
				return 0;
			}

			Pattern roomIdPattern = Pattern.compile(".*([1-9][0-9][0-9])$");
			Matcher roomIdMatcher = roomIdPattern.matcher(input);

			if (!(roomIdMatcher.find())) {
				System.out.println("Enter a room from the options above!");
				continue;
			}

			int roomId = Integer.parseInt(roomIdMatcher.group(1));

			if(!(rooms.containsKey(roomId)) && (rooms.get(roomId).status == status)) {
				System.out.println("There are no rooms to perform this action on!");
				return 0;
			}

			return roomId;

		}

	}

	// Takes user input for number of rental days and ensures that Suites are not rented more than 10
	// days after last maintenance and weekends have higher minimum rental period
	public int numOfRentDays(int roomId, DateTime rentDate) throws NumberFormatException, IOException {

		int minimum = 2;
		int maximum = 10;

		if(rooms.get(roomId) instanceof Suite) {
			maximum = DateTime.diffDays(rooms.get(roomId).lastMaintenanceDate, rentDate) + 10;

		}

		if(rentDate.isWeekend()) {
			System.out.println("It's finally the weekend!");
			minimum = 3;

		}

		while (true) {
			System.out.println("How many days would you like to rent this room?" );
			int input = Integer.parseInt(br.readLine());

			if (input > maximum) {
				System.out.println("You can only rent this room for " + maximum + " days");
				continue;

			} else if (input < minimum) {
				System.out.println("You must rent this room for at least " + minimum + " days");
				continue;
			}

			return input;

		}
	}
}








