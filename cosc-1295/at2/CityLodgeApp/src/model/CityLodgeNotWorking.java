package model;

import hsqldb.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DateTime;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CityLodgeNotWorking {

	private List<Room> cityLodgeRooms = null;
	private List<HiringRecords> cityLodgeRecords = null;

	// Selects and returns a Room object from the Database
	public static Room selectByRoomId(int roomId) throws SQLException {
		String query = "SELECT * FROM ROOM WHERE room_id = " + roomId;

		try {
			ResultSet rs = Database.selectQuery(query);
			if(rs.next()) {
				Blob blob = rs.getBlob("Room");
			}
			Room room = getRoomFromResultSet();

			return room;

		} catch (SQLException e) {
			System.out.println("While searching an room with " + roomId + " id, an error occurred: " + e);
			throw e;

		}
	}

	// Gets Room from Result set and applies the properties to a Room object
	private static Room getRoomFromResultSet(ResultSet rs) throws SQLException {
		Room room = null;

		if(rs.next()) {
			Blob blob = rs.getBlob("ROOM");
			InputStream input = blob.getBinaryStream();
			cityLodgeRooms
			/*room = new Room();
			room.setRoomId(rs.getInt("room_id"));
			room.setNumBeds((rs.getInt("bedrooms")));
			room.setFeatureSummary((rs.getString("features")));
			room.setStatus(RoomStatus.valueOf((rs.getString("room_status"))));
			room.setRentalRate(rs.getFloat("rental_rate"));
			room.setLateRate((rs.getFloat("late_rate")));
			room.setImageURL((rs.getString("image_url")));
			room.setLastMaintenanceDate(new DateTime((rs.getDate(("last_maintenanced")).toLocalDate())));
		*/
		}
		return room;

	}

	// Selects all Rooms from the Database
	public static List<Room> searchRooms() throws SQLException, ClassNotFoundException {
		String query = "SELECT * FROM ROOMS";

		try {
			ResultSet rs = Database.selectQuery(query);
			List<Room> roomList = getRoomList(rs);

			return roomList;

		} catch (SQLException e) {
			System.out.println("SQL select operation has been failed: " + e);
			throw e;

		}
	}

	// Selects all from Rooms operation
	/*private static List<Room> getRoomList(ResultSet rs) throws SQLException, ClassNotFoundException {
		
		List<Room> roomList = FXCollections.observableArrayList();

		while (rs.next()) {
			Room room = new Room();
			room.setRoomId(rs.getInt("room_id"));
			room.setNumBeds((rs.getInt("bedrooms")));
			room.setFeatureSummary((rs.getString("features")));
			room.setStatus(RoomStatus.valueOf((rs.getString("room_status"))));
			room.setRentalRate(rs.getFloat("rental_rate"));
			room.setLateRate((rs.getFloat("late_rate")));
			room.setImageURL((rs.getString("image_url")));
			//room.setLastMaintenanceDate((new DateTime(rs.getDate(("last_maintenanced")))));
			roomList.add(room);

		}
		
		return roomList;

	}*/

	// Deletes room from Database by ID
	public static void deleteRoomId (Integer roomId) throws ClassNotFoundException, SQLException {
		String query = "DELETE FROM room WHERE room_id ="+ roomId;
		Database.updateQuery(query);

	}

	public List<Room> getRooms() {
		return cityLodgeRooms;
	}
	
	public HashMap<String, HiringRecord> getRecords() {
		return cityLodgeRecords;
	}
	
	public void setRoom(List<Room> rooms) {
		this.cityLodgeRooms = rooms;
	}
	
	public void setRecords(HashMap<String, HiringRecord> records) {
		this.cityLodgeRecords = records;
	}
		
	@Override
	public void addRoom(Room room) {
		cityLodgeRooms.add(room);

	}

	@Override
	public void editRoom(Room room) {
		cityLodgeRooms.get(room.getRoomId()).setFeatureSummary(room.getFeatureSummary());
	}

	@Override
	public void deleteRoom(Room room) {
		cityLodgeRooms.remove(room);
	}

	public void addRecord(HiringRecord record) {
		cityLodgeRecords.put(record.getRecordId(), record);
	}

	@Override
	public void updateRecord(HiringRecord record) {
		cityLodgeRecords.get(record.getRecordId()).setActualReturnDate(record.getActualReturnDate());
		cityLodgeRecords.get(record.getRecordId()).setRentalFee((record.getRentalFee()));
		cityLodgeRecords.get(record.getRecordId()).setLateFee((record.getLateFee()));

	}

	@Override
	public void deleteRecord(HiringRecord record) {
		cityLodgeRecords.remove(record.getRecordId(), record);
	}

	public HashMap<String, HiringRecord> selectAllRecords() {
		return cityLodgeRecords;
	}

}
