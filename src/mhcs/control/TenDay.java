package mhcs.control;

import java.util.Date;

import com.google.gwt.storage.client.Storage;

/**
 * Creates a ten day alert and stores it in the local storage.
 *
 * @author Carlos Wolle
 *
 */
public class TenDay {

	/**
	 * Creates a ten day date 
	 * @param none
	 * @return Returns the date that is 10 days away
	 */
	public static Date createTenDay() {
		Storage moduleStore = Storage.getLocalStorageIfSupported();
		Date end = null;
		if (moduleStore != null) {
			String item = moduleStore.getItem("month");
			if (item == null) {
				tenDayUpdate();
			}
			end = getTenDay();
		}
		return end;
	}
	
	/**
	 * Retrieves the ten day alert date
	 * @param none
	 * @return The ten day alert date
	 */
	public static Date getTenDay() {
		Storage moduleStore = Storage.getLocalStorageIfSupported();
		Date e = new Date();
		String sItem = moduleStore.getItem("day");
		int day = Integer.parseInt(sItem);
		e.setDate(day);
		
		sItem = moduleStore.getItem("month");
		int month = Integer.parseInt(sItem);
		e.setMonth(month);
				
		sItem = moduleStore.getItem("hours");
		int hours = Integer.parseInt(sItem);
		e.setHours(hours);
		
		sItem = moduleStore.getItem("minutes");
		int minutes = Integer.parseInt(sItem);
		e.setMinutes(minutes);
		
		sItem = moduleStore.getItem("seconds");
		int seconds = Integer.parseInt(sItem);
		e.setSeconds(seconds);
		
		sItem = moduleStore.getItem("year");
		int year = Integer.parseInt(sItem);
		e.setYear(year);
		
		return e;
	}
	
	
	/**
	 * Updates the ten day alert and stores in local storage
	 * @param none
	 * @return none
	 */
	public static void tenDayUpdate() {
		Storage moduleStore = Storage.getLocalStorageIfSupported();
		Date currentDate = new Date();
		Date end = new Date();
		int endDate = currentDate.getDate() + 10;
		end.setDate(endDate);
		
		int end_MONTH_I = end.getMonth();
		int end_DAY_I = end.getDate();
		int end_HOURS_I = end.getHours();
		int end_MINUTES_I = end.getMinutes();
		int end_SECONDS_I = end.getSeconds();
		int end_YEAR_I = end.getYear();
		
		String YEAR = Integer.toString(end_YEAR_I);
		String MONTH = Integer.toString(end_MONTH_I);
		String DAY = Integer.toString(end_DAY_I);
		String HOURS =Integer.toString(end_HOURS_I);
		String MINUTES = Integer.toString(end_MINUTES_I);
		String SECONDS =Integer.toString(end_SECONDS_I); 
		
		moduleStore.setItem("month", MONTH);
		moduleStore.setItem("day", DAY);
		moduleStore.setItem("hours", HOURS);
		moduleStore.setItem("minutes", MINUTES);
		moduleStore.setItem("seconds", SECONDS);
		moduleStore.setItem("year", YEAR);
	}
}
