package com.mmt.ivtest.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmt.ivtest.model.Flight;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author rahul
 *
 */
@Slf4j
@Service(value = "connectedFlightService")
public class ConnectedFlightService {

	@Autowired
	private RepeatingFlightResolver repeatingFlightResolver;

	@Autowired
	private MorningFlightHelper morningFlightHelper;

	@Autowired
	private FastestConnectingFlightHelper fastestConnectingFlightHelper;

	@Autowired
	private ConnectingFlightHelper connectingFlightHelper;

	// get connecting flights for each flight leaving from the source airport
	// destination in source airport flight is the connecting airport
	protected List<List<Flight>> getConnectingFlights(Map<String, List<Flight>> flightListMap,
			List<Flight> flightsFromSource, String destination) throws ParseException {
		log.info("getConnectingFlights started.");

		List<List<Flight>> connectingFlights = new ArrayList<List<Flight>>();
		Iterator<Flight> iterator = flightsFromSource.iterator();
		while (iterator.hasNext()) {
			Flight flightFromSource = iterator.next();
			List<Flight> tempConnectingFlights = new ArrayList<Flight>();
			// get connecting flights for the flight from origin
			tempConnectingFlights.addAll(connectingFlightHelper.getConnectingFlightsOrderedByStartTime(flightListMap,
					flightFromSource, destination));
			log.debug("connected flights checked.");
			Flight lastSourceAndFirstConnFlight = morningFlightHelper.getFlightForMorningScenario(flightListMap,
					flightFromSource, destination);
			log.debug("morning flight scenario checked.");
			if (null != lastSourceAndFirstConnFlight) {
				tempConnectingFlights.add(lastSourceAndFirstConnFlight);
			}
			if (tempConnectingFlights.size() > 0) {
				// get connecting flight with minimum duration from source flight
				Flight fastestConnectingFlight = fastestConnectingFlightHelper
						.getFastestConnectingFlightForSource(tempConnectingFlights, flightFromSource);
				tempConnectingFlights.clear();
				tempConnectingFlights.add(flightFromSource);
				tempConnectingFlights.add(fastestConnectingFlight);
				connectingFlights.add(tempConnectingFlights);
			}
		}

		log.info("getConnectingFlights started.");
		// return list of fastest routes per connecting airport
		return repeatingFlightResolver.getFastestFromRepeatingRoutes(connectingFlights);
	}

}
