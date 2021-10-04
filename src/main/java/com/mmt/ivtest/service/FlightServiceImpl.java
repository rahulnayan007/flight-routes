package com.mmt.ivtest.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmt.ivtest.loader.FlightDataLoader;
import com.mmt.ivtest.model.Flight;
import com.mmt.ivtest.util.ResponseProcessor;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author rahul
 *
 */
@Slf4j
@Service(value = "flightServiceImpl")
public class FlightServiceImpl implements FlightService {

	@Autowired
	private FlightDataLoader flightDataLoader;

	@Autowired
	private DirectFlightService directFlightService;

	@Autowired
	private ConnectedFlightService connectedFlightService;
	
	@Autowired
	private ResponseProcessor responseProcessor;

	private Map<String, List<Flight>> flightsMap;

	@Override
	public String getFlights(String source, String destination) throws ParseException {
		log.info("getFlights service started.");
		
		flightsMap = flightDataLoader.getFlightsMap();
		log.info("flight data loaded.");
		
		// get list of flights the source
		List<Flight> flightList = flightsMap.get(source);
		log.info("list of flights from source airport fetched.");
		
		// find direct flights from above list
		List<Flight> directFlightList = directFlightService.getDirectFlights(new ArrayList<Flight>(flightList),
				destination);
		log.info("direct flights list fetched.");

		// remove processed flights from the list
		List<Flight> filteredFlightList = new ArrayList<Flight>(flightList);
		filteredFlightList.removeAll(directFlightList);

		// get list of connecting flights from remaining items
		List<List<Flight>> connectingFlightList = connectedFlightService.getConnectingFlights(flightsMap,
				filteredFlightList, destination);
		log.info("connected flights list fetched.");

		log.info("getFlights service ends.");
		// response
		return responseProcessor.prepareResponse(directFlightList, connectingFlightList);
	}

}
