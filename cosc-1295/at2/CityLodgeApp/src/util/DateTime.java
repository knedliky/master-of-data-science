package util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;

public class DateTime {

	private long advance;
	private long time;

	public DateTime() {
		time = System.currentTimeMillis();
	}
	
	public DateTime(Date date) {
		int day = date.toLocalDate().getDayOfMonth();
		int month = date.toLocalDate().getMonthValue();
		int year = date.toLocalDate().getYear();
		setDate(day, month, year);
	}
	
	public DateTime(int setClockForwardInDays) {
		advance = ((setClockForwardInDays * 24L + 0) * 60L) * 60000L;
		time = System.currentTimeMillis() + advance;
	}

	public DateTime(DateTime startDate, int setClockForwardInDays) {
		advance = ((setClockForwardInDays * 24L + 0) * 60L) * 60000L;
		time = startDate.getTime() + advance;
	}

	public DateTime(int day, int month, int year) {
		setDate(day, month, year);
	}

	public long getTime() {
		return time;
	}

	// get the name of the day of this DateTime object
	public String getNameOfDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		return sdf.format(time);
	}

	// Added method to returns true if DateTime object is a weekend, for 
	// the purposes of checking which minimum days for  
	public boolean isWeekend() {
		return (this.getNameOfDay().equals("Saturday") || (this.getNameOfDay().equals("Sunday")));

	}

	public String toString() {
		return getFormattedDate();
	}

	public static String getCurrentTime() {
		Date date = new Date(System.currentTimeMillis()); // returns current Date/Time
		return date.toString();
	}

	public String getFormattedDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		long currentTime = getTime();
		Date gct = new Date(currentTime);

		return sdf.format(gct);
	}

	public String getEightDigitDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		long currentTime = getTime();
		Date gct = new Date(currentTime);

		return sdf.format(gct);
	}

	// Returns difference in days
	public static int diffDays(DateTime endDate, DateTime startDate) {
		final long HOURS_IN_DAY = 24L;
		final int MINUTES_IN_HOUR = 60;
		final int SECONDS_IN_MINUTES = 60;
		final int MILLISECONDS_IN_SECOND = 1000;
		long convertToDays = HOURS_IN_DAY * MINUTES_IN_HOUR * SECONDS_IN_MINUTES * MILLISECONDS_IN_SECOND;
		long hirePeriod = endDate.getTime() - startDate.getTime();
		double difference = (double) hirePeriod / (double) convertToDays;
		int round = (int) Math.round(difference);
		return round;
	}

	private void setDate(int day, int month, int year) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, day, 0, 0);

		java.util.Date date = calendar.getTime();

		time = date.getTime();
	}

	// Advances date/time by specified days, hours and mins for testing purposes
	public void setAdvance(int days, int hours, int mins) {
		advance = ((days * 24L + hours) * 60L) * 60000L;
	}

	// Added a method to check the user input of a date, comparing it to a regex
	public static DateTime inputDate(DateTime restrictedDate) throws IOException {

		while(true) {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("DD/MM/YYYY:\n");

			String input = br.readLine();

			Pattern datePattern = Pattern.compile("^([0-2][0-9]||3[0-1])/(0[0-9]||1[0-2])/([0-9][0-9][0-9][0-9])$");
			Matcher dateMatcher = datePattern.matcher(input);

			if (!(dateMatcher.find())) {
				System.out.println("Enter valid date in the format below!");
				continue;
			}

			int day = Integer.parseInt(dateMatcher.group(1));
			int month = Integer.parseInt(dateMatcher.group(2));
			int year = Integer.parseInt(dateMatcher.group(3));

			DateTime dt = new DateTime(day, month, year);

			if(diffDays(dt, restrictedDate) < 0) {
				System.out.println("Hmmm, that doesn't seem right... try a date after: " + restrictedDate.getFormattedDate());
				continue;
				
			} 

			return dt;

		}
	}
}













