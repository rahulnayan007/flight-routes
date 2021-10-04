package com.mmt.ivtest.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import com.mmt.ivtest.model.Flight;
import com.mmt.ivtest.util.Constants;

/**
 * 
 * @author rahul
 *
 */
@Service(value = "repeatingFlightResolver")
public class RepeatingFlightResolver {

	// get flight with least duration from multiple flights on same route
	protected List<List<Flight>> getFastestFromRepeatingRoutes(List<List<Flight>> connectingFlightsList)
			throws ParseException {
		// one key will have one list, first element in list is source flight and second
		// element is connecting flight
		Map<String, List<List<Flight>>> fastestFlightMap = new HashMap<String, List<List<Flight>>>();
		for (List<Flight> connectingFlights : connectingFlightsList) {
			// use destination code of flight from source to compare connecting airport
			Flight flightFromSource = connectingFlights.get(Constants.SOURCE_FLIGHT_INDEX);

			List<List<Flight>> fastestFlights = fastestFlightMap.get(flightFromSource.getDestinationAirportCode());
			if (null == fastestFlights) {
				fastestFlights = new ArrayList<List<Flight>>();
				fastestFlights.add(connectingFlights);
			} else {
				Flight currentSourceFlight = connectingFlights.get(Constants.SOURCE_FLIGHT_INDEX);
				Flight currentConnectingFlight = connectingFlights.get(Constants.CONNECTING_FLIGHT_INDEX);
				List<Flight> fastestFlightList = fastestFlights.get(0);
				Flight fastestSourceFlight = fastestFlightList.get(Constants.SOURCE_FLIGHT_INDEX);
				Flight fastestConnectingFlight = fastestFlightList.get(Constants.CONNECTING_FLIGHT_INDEX);
				if ((currentConnectingFlight.getEndTime().getTime()
						- currentSourceFlight.getStartTime().getTime()) < fastestConnectingFlight.getEndTime().getTime()
								- fastestSourceFlight.getStartTime().getTime()) {
					List<Flight> fastestEntry = new ArrayList<Flight>();
					fastestEntry.add(currentSourceFlight);
					fastestEntry.add(currentConnectingFlight);
					fastestFlights.clear();
					fastestFlights.add(fastestEntry);
				}
			}
			fastestFlightMap.put(flightFromSource.getDestinationAirportCode(), fastestFlights);
		}
		List<List<Flight>> resultList = new ArrayList<List<Flight>>();
		for(Entry<String, List<List<Flight>>> entry : fastestFlightMap.entrySet()) {
			resultList.addAll(entry.getValue());
		}
		return resultList;
	}

}
