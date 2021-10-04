package com.mmt.ivtest.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmt.ivtest.model.Flight;
import com.mmt.ivtest.util.FlightIntervalValidator;

/**
 * 
 * @author rahul
 *
 */
@Component(value = "connectingFlightHelper")
public class ConnectingFlightHelper {

	@Autowired
	private FlightIntervalValidator flightIntervalValidator;

	// get connecting flight list ordered by start time
	protected List<Flight> getConnectingFlightsOrderedByStartTime(Map<String, List<Flight>> flightsMap,
			Flight flightFromSource, String destination) throws ParseException {

		List<Flight> result = new ArrayList<Flight>();
		// get connecting flights i.e. from destination
		List<Flight> connectingFlights = flightsMap.get(flightFromSource.getDestinationAirportCode());
		if (null != connectingFlights && connectingFlights.size() > 0) {
			// filter the flights for that connects to specified destination
			connectingFlights = connectingFlights.stream()
					.filter(flight -> flight.getDestinationAirportCode().equalsIgnoreCase(destination))
					.sorted(Comparator.comparing(Flight::getStartTime)).collect(Collectors.toList());
			if (null != connectingFlights && connectingFlights.size() > 0) {
				connectingFlights = flightIntervalValidator.filterByWaitTime(connectingFlights, flightFromSource);
			}
			if (connectingFlights.size() > 0) {
				result.addAll(connectingFlights);
			}
		}
		return result;
	}

}
