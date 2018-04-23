package com.company.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**This class is to translate string to date and vice versa.
 * Type of date: dd.MM.yyyy
 * @version 0.1
 * @author Rostislav Pekhovsky 2018*/
public class DateTranslation {

	/**Date format for translation*/
	private static DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

	/**Converts string to date
	 * @param stringDate string with date*/
	public static Date convertStringToDate(String stringDate) {
		try {
			Date date = format.parse(stringDate);
			return date;
		} catch (ParseException e) {
			return null;
		}
	}
	/**Converts date to string
	 * @param date date*/
	public static String convertDateToString(Date date) {

		return format.format(date);
	}
}
