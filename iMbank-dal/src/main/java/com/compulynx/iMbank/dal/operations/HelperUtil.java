package com.compulynx.iMbank.dal.operations;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class HelperUtil {

	public static void main(String[] args) {
		//int year = 1989;
		//int month = 9;
		//int day = 11;

		//String date = year + "/" + month + "/" + day;
		//java.util.Date utilDate = null;

		try {
			System.out.println(String.format("%4s", Integer.toBinaryString(1))
					.replace(' ', '0'));

			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyyMMddHHmmssSSS");
			// utilDate = formatter.parse(date);
			System.out.println(formatter.format(new Date()));
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	public static int CalculateAge(Date dob) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(dob);
		Calendar now = new GregorianCalendar();
		int res = now.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
		if ((cal.get(Calendar.MONTH) > now.get(Calendar.MONTH))
				|| (cal.get(Calendar.MONTH) == now.get(Calendar.MONTH) && cal
						.get(Calendar.DAY_OF_MONTH) > now
						.get(Calendar.DAY_OF_MONTH))) {
			res--;
		}
		return res;
	}
}
