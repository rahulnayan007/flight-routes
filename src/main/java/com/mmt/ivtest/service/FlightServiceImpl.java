package com.mmt.ivtest.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmt.ivtest.loader.FlightDataLoader;
import com.mmt.ivtest.model.Flight;
import com.mmt.ivtest.util.Constants;
import com.mmt.ivtest.util.FlightComparator;
import com.mmt.ivtest.util.ResponseProcessor;

@Service(value = "flightServiceImpl")
public class FlightServiceImpl implements FlightService {

	@Autowired
	private FlightDataLoader flightDataLoader;
	private Map<String, List<Flight>> flightsMap;
	private List<String> usedAirports;

	@Override
	public String getFlights(String source, String destination) {
		flightsMap = flightDataLoader.getFlightsMap();
		usedAirports = new ArrayList<String>();
		// get list of flights for given source
		List<Flight> flightList = flightsMap.get(source);
		// find direct or connecting flights to destination from flightList
		List<Flight> directFlightList = getDirectFlights(new ArrayList<Flight>(flightList), destination);
		directFlightList = directFlightList.stream().sorted(new FlightComparator()::compare).collect(Collectors.toList());
		// Traverse only on remaining flight objects in the list
		List<Flight> filteredFlightList = new ArrayList<Flight>(flightList);
		filteredFlightList.removeAll(directFlightList);
		List<List<Flight>> sortedConnectingFlightList = getConnectingFlights(filteredFlightList, destination);
		
		// return prepared response
		return new ResponseProcessor().prepareResponse(directFlightList, sortedConnectingFlightList);
	}

	private List<Flight> getDirectFlights(List<Flight> flightList, String destination) {
		List<Flight> result = new ArrayList<Flight>();
		for (Flight flight : flightList) {
			if (destination.equalsIgnoreCase(flight.getDestinationAirportCode())) {
				result.add(flight);
			}
		}
		return result;
	}

	// get indirect flights via traversal
	private List<List<Flight>> getConnectingFlights(List<Flight> flightList, String destination) {
		List<List<Flight>> flights = new ArrayList<List<Flight>>();
		Iterator<Flight> iterator = flightList.iterator();
		while (iterator.hasNext()) {
			Flight flight = iterator.next();
			List<Flight> subflights = new ArrayList<Flight>();
			subflights.add(flight);
			subflights.addAll(getConnectingFlightsRoute(flight, destination));
			if(subflights.size()>1) {
				flights.add(subflights);
			}
		}
		return flights;
	}

	private List<Flight> getConnectingFlightsRoute(Flight source, String destination) {
		List<Flight> flights = flightsMap.get(source.getDestinationAirportCode());
		List<Flight> result = new ArrayList<Flight>();
		if (null != flights && !flights.isEmpty()) {
			List<Flight> filteredFlights = filterByWaitTime(getDirectFlights(flights, destination), source);
			filteredFlights = filteredFlights.stream().sorted(new FlightComparator()::compare)
					.collect(Collectors.toList());
			for (Flight flight : filteredFlights) {
				if (!usedAirports.contains(flight.getSourceAirportCode())) {
					result.add(flight);
					usedAirports.add(flight.getSourceAirportCode());
				}
			}

		}
		return result;
	}

	private List<Flight> filterByWaitTime(List<Flight> flights, Flight source) {
		List<Flight> filteredFlights = new ArrayList<Flight>();
		Iterator<Flight> itrFlights = flights.iterator();
		while (itrFlights.hasNext()) {
			Flight flight = itrFlights.next();
			if ((flight.getStartTime().getTime()
					- source.getEndTime().getTime()) > Constants.GAP_BETWEEN_CONNECTING_FLIGHTS_MILLIS) {
				filteredFlights.add(flight);
			}
		}
		return filteredFlights;
	}

}
