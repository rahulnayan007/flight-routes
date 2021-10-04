package com.mmt.ivtest.util;

import java.util.Comparator;

import com.mmt.ivtest.dto.FlightDto;

/**
 * 
 * @author rahul
 *
 */
public class FlightDtoComparator implements Comparator<FlightDto> {

	@Override
	public int compare(FlightDto flight1, FlightDto flight2) {
		if (flight1.getDuration() > flight2.getDuration())
			return 1;
		else if (flight1.getDuration() < flight2.getDuration())
			return -1;
		else
			return 0;
	}

}
