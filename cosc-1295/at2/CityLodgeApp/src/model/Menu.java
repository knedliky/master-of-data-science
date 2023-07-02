package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import hsqldb.Database;
import util.DateTime;

public class Menu {

	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	public void addRoom(Integer roomId, String features, Integer numBeds) throws Exception {
		System.out.println("Creating Standard Room...");
		Room room = new Standard(roomId, features, numBeds);
	}

	public void addRoom(Integer roomId, String features, DateTime lastMaintenanced) throws Exception{
		System.out.println("Creating Suite...");
		Room room = new Suite(roomId, features, lastMaintenanced);

	}

}
