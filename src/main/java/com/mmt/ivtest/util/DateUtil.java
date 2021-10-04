package com.mmt.ivtest.util;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component(value = "dateUtil")
public class DateUtil {

	public Date getDateValue(int dateValue, boolean isDayChangeApplicable) {
		int minute = dateValue % 100;
		dateValue = dateValue / 100;
		int hour = dateValue % 100;

		Calendar date = Calendar.getInstance();
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
		if (isDayChangeApplicable) {
			date.add(Calendar.HOUR_OF_DAY, Constants.TWENTY_FOUR_HOURS);
		}
		date.add(Calendar.HOUR_OF_DAY, hour);
		date.add(Calendar.MINUTE, minute);
		return date.getTime();
	}

	public Date increaseOneDay(Date inputDate) {
		Calendar date = Calendar.getInstance();
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
		date.add(Calendar.HOUR_OF_DAY, Constants.TWENTY_FOUR_HOURS);
		date.add(Calendar.HOUR_OF_DAY, inputDate.getHours());
		date.add(Calendar.MINUTE, inputDate.getMinutes());
		return date.getTime();
	}

}
