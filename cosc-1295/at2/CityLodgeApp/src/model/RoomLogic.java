package model;

import java.io.*;
import java.sql.*;
import java.util.List;
import hsqldb.Database;
import javafx.collections.ObservableList;

public class RoomLogic {

	public ObservableList<Room> rooms;
	
	public ObservableList<Room> getRooms() {
		return rooms;
	}
	
	public void setRooms(ObservableList<Room> rooms) {
		this.rooms = rooms;
	}

	public static void addRoom(Room room) {
		String query = "INSERT INTO ROOMS (room_id, room) VALUES (?, ?)";

		try (Connection con = Database.connect()) {
			PreparedStatement statement = con.prepareStatement(query);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();                 
			ObjectOutputStream objOstream = new ObjectOutputStream(baos);                 
			objOstream.writeObject(room);                   
			objOstream.flush();                 
			objOstream.close();                   
			byte[] bArray = baos.toByteArray(); 

			// Executing prepared statement to add room
			statement.setInt(1, room.getRoomId());
			statement.setBytes(2, bArray);
			statement.execute();

			System.out.println("Room added");
			
		} catch (SQLException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	public static Room getRoom(int roomId) throws Exception {

		String query = "SELECT * FROM ROOMS WHERE room_id = " + roomId;
		Room room = null;
		
		try (Connection con = Database.connect()){

			ResultSet rs = Database.selectQuery(query);

			if(rs.next()) {
				ByteArrayInputStream bais;
				ObjectInputStream ins;

				try {
					bais = new ByteArrayInputStream(rs.getBytes("javaObject"));
					ins = new ObjectInputStream(bais);

					room = (Room)ins.readObject();
					System.out.println("Object in value ::"+ room.getClass().getSimpleName());
					ins.close();

					Blob blob = rs.getBlob("Room");
					InputStream input = blob.getBinaryStream();
					ObjectInputStream ois = new ObjectInputStream(input);
					room = (Room)ois.readObject();

				} catch (SQLException | IOException | ClassNotFoundException e) {
					System.out.println("While searching room number " + roomId + ", an error occurred: " + e);

				}
			}
			return room;
		
		}
	}

	public void serialize() throws IOException {

		try (FileOutputStream fout= new FileOutputStream("database/database.ser", true);
				ObjectOutputStream oos  = new ObjectOutputStream(fout);){

			oos.writeObject(rooms);

		} catch (Exception ex) {
			ex.printStackTrace();

		}

	}

	@SuppressWarnings("unchecked")
	public ObservableList<Room> deserialize() {

		ObservableList<Room> rooms = null;

		try (FileInputStream streamIn = new FileInputStream("database/database.ser");
				ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);){

			rooms = (ObservableList<Room>)objectinputstream.readObject();

		} catch (Exception e) {
			e.printStackTrace();

		} 
		return rooms;

	}

	public void writeToFile(String filepath) {
	
		File file = null; 
		FileWriter fw = null;
		
		try {
			file = new File(filepath);
			fw = new FileWriter(file);
		
		fw.write(rooms.getClass().getSimpleName());
		fw.close();
		
		} catch (IOException e) {
			e.printStackTrace();
			
		}
	}
	
	public void readFromFile() {

	}

	public static Room getRoomFromResultSet(ResultSet rs) {

		Room room = null;
		ByteArrayInputStream bais = null;
		ObjectInputStream input = null;

		try {

			if(rs.next()) {

				room = (Room)input.readObject();
				System.out.println("Object in value ::" + room.getClass().getSimpleName());
				input.close();
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();

		} catch (SQLException e) {
			e.printStackTrace();

		}
		return room;

	}		

	public ObservableList<Room> getAllRooms() {

		String query = "SELECT * FROM rooms";

		try {
			ResultSet rs = Database.selectQuery(query);
			ObservableList<Room> rooms = getRoomList(rs);

			return rooms;

		} catch (SQLException e) {
			System.out.println("SQL select query failed: " + e);
			throw e;

		}

	}

	public void insertRoom(Room room) {

		ObjectOutputStream oos = new ObjectOutputStream();
		ByeArrayOutputStream bos = new ByteArrayOutputStream();
		
		oos.writeObject(room);
		oos.flush();
		oos.close();

		byte[] data = bos.toByteArray();
		String query = "INSERT INTO ROOMS VALUES(" + data + ")";
		Database.updateQuery(query);

	}

	public void updateRoom(Room room) {

	}

	public void deleteRoom(int roomId) {
		String query =
				"DELETE FROM room WHERE room ID = "+ roomId + ";";

		Database.updateQuery(query);
	}

}
