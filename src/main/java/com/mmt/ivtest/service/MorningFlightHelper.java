package com.mmt.ivtest.service;

import java.text.ParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmt.ivtest.model.Flight;
import com.mmt.ivtest.util.Constants;
import com.mmt.ivtest.util.DateUtil;

/**
 * 
 * @author rahul
 *
 */
@Component(value = "morningFlightHelper")
public class MorningFlightHelper {

	@Autowired
	private DateUtil dateUtil;

	// club last flight of the day with the first one in the morning
	protected Flight getFlightForMorningScenario(Map<String, List<Flight>> flightsMap, Flight flightFromSource,
			String destination) throws ParseException {

		Flight connectingFlight = null;
		if (isEveningFlight(flightFromSource)) {
			// get connecting flights i.e. from destination
			List<Flight> connectingFlights = flightsMap.get(flightFromSource.getDestinationAirportCode());
			if (null != connectingFlights && connectingFlights.size() > 0) {
				// filter the flights for that connects to specified destination
				connectingFlights = connectingFlights.stream()
						.filter(flight -> flight.getDestinationAirportCode().equalsIgnoreCase(destination))
						.sorted(Comparator.comparing(Flight::getStartTime)).collect(Collectors.toList());
				connectingFlight = connectingFlights.get(0);
				if (isMorningFlight(connectingFlight)) {
					long transitTime = (dateUtil.increaseOneDay(connectingFlight.getStartTime()).getTime()
							- flightFromSource.getEndTime().getTime());
					if (transitTime > Constants.MIN_TRANSIT_MILLIS) {
						connectingFlight.setStartTime(dateUtil.increaseOneDay(connectingFlight.getStartTime()));
						connectingFlight.setEndTime(dateUtil.increaseOneDay(connectingFlight.getEndTime()));
						connectingFlight.setDuration(dateUtil.increaseOneDay(connectingFlight.getEndTime()).getTime()
								- flightFromSource.getStartTime().getTime());
						return connectingFlight;
					}
				}
				connectingFlight = null;
			}
		}
		return connectingFlight;
	}

	// compares against configured value
	private boolean isEveningFlight(Flight flight) throws ParseException {
		if (flight.getEndTime().after(dateUtil.getDateValue(Integer.parseInt(Constants.EVENING_VALUE), false))) {
			return true;
		} else {
			return false;
		}
	}

	// compares against configured value
	private boolean isMorningFlight(Flight flight) throws ParseException {
		if (flight.getEndTime().before(dateUtil.getDateValue(Integer.parseInt(Constants.MORNING_VALUE), false))) {
			return true;
		} else {
			return false;
		}
	}

}
