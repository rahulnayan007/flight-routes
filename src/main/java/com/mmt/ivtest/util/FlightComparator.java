package com.mmt.ivtest.util;

import java.util.Comparator;

import com.mmt.ivtest.model.Flight;

public class FlightComparator implements Comparator<Flight> {

	@Override
	public int compare(Flight flight1, Flight flight2) {
		if (flight1.getDuration() > flight2.getDuration())
			return 1;
		else if (flight1.getDuration() < flight2.getDuration())
			return -1;
		else
			return 0;
	}

}
