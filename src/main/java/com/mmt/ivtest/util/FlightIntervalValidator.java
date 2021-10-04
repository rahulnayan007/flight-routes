package com.mmt.ivtest.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.mmt.ivtest.model.Flight;

/**
 * 
 * @author rahul
 *
 */
@Component(value = "flightIntervalValidator")
public class FlightIntervalValidator {

	// Validate for the time difference between source flight and connecting flights
	public List<Flight> filterByWaitTime(List<Flight> connectingFlights, Flight flightFromSource)
			throws ParseException {
		List<Flight> filteredFlights = new ArrayList<Flight>();
		Iterator<Flight> connectingFlightsItr = connectingFlights.iterator();
		while (connectingFlightsItr.hasNext()) {
			Flight connectingFlight = connectingFlightsItr.next();
			long transitTime = (connectingFlight.getStartTime().getTime() - flightFromSource.getEndTime().getTime());
			if (transitTime > Constants.MIN_TRANSIT_MILLIS) {
				connectingFlight.setDuration(
						connectingFlight.getEndTime().getTime() - flightFromSource.getStartTime().getTime());
				filteredFlights.add(connectingFlight);
			}
		}
		return filteredFlights;
	}

}
