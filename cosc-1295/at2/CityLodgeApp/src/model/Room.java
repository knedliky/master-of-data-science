package model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import util.DateTime;

public class Room implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer roomId;
	private String featureSummary;
	private String roomType;
	private Integer numBeds;
	private Double rentalRate;
	private Double lateRate;
	private RoomStatus status;
	private DateTime lastMaintenanceDate;
	private ArrayList<HiringRecord> records = new ArrayList<HiringRecord>(10);
	private String imageURL;

	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	public static final double SINGLE = 59;
	public static final double DOUBLE = 99;
	public static final double DOUBLE_TWIN = 199;
	public static final double RATE = 1.35;

	
	public Room() {
		this.status = RoomStatus.Available;
	}
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}		
	public String getFeatureSummary() {
		return featureSummary;
	}
	public void setFeatureSummary(String featureSummary) {
		this.featureSummary = featureSummary;
	}
	public String getRoomType() {
		return roomType;
	}
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	public int getNumBeds() {
		return numBeds;
	}
	public void setNumBeds(int numBeds) {
		this.numBeds = numBeds;
		
		switch(numBeds) {
		case 1: 
			setRentalRate(SINGLE*RATE);
			break;

		case 2:
			setRentalRate(DOUBLE*RATE);
			break;

		case 4:
			setRentalRate(DOUBLE_TWIN*RATE);
			break;
			
		default:
			break;
			
		}
	}
	public double getRentalRate() {
		return rentalRate;
	}
	public void setRentalRate(double rentalRate) {
		this.rentalRate = rentalRate;
	}
	public double getLateRate() {
		return lateRate;
	}
	public void setLateRate(double lateRate) {
		this.lateRate = lateRate;
	}
	public RoomStatus getStatus() {
		return status;
	}
	public void setStatus(RoomStatus status) {
		this.status = status;
	}
	public DateTime getLastMaintenanceDate() {
		return lastMaintenanceDate;
	}
	public void setLastMaintenanceDate(DateTime dateTime) {
		this.lastMaintenanceDate = dateTime;
	}
	public ArrayList<HiringRecord> getRecords() {
		return records;
	}
	public void setRecords(ArrayList<HiringRecord> records) {
		this.records = records;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public String getDetails() {
		return null;
	}
}