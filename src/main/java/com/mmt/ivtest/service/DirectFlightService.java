package com.mmt.ivtest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mmt.ivtest.model.Flight;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author rahul
 *
 */
@Slf4j
@Service(value = "DirectFlightService")
public class DirectFlightService {

	// get direct flights between source and destination
	protected List<Flight> getDirectFlights(List<Flight> flightList, String destination) {
		List<Flight> result = new ArrayList<Flight>();
		for (Flight flight : flightList) {
			if (destination.equalsIgnoreCase(flight.getDestinationAirportCode())) {
				result.add(flight);
			}
		}
		return result;
	}

}
