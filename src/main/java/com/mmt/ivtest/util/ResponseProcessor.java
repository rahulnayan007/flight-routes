package com.mmt.ivtest.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.mmt.ivtest.dto.FlightDto;
import com.mmt.ivtest.model.Flight;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author rahul
 *
 */
@Slf4j
@Component(value = "responseProcessor")
public class ResponseProcessor {
	
	// prepare response for the client
	public String prepareResponse(List<Flight> directFlights, List<List<Flight>> connectingFlights) {
		log.info("prepareResponse started.");
		
		JSONArray responseArray = new JSONArray();
		JSONObject obj = new JSONObject();
		// sort flights by duration
		directFlights = directFlights.stream().sorted(new FlightComparator()::compare).collect(Collectors.toList());
		for (Flight flight : directFlights) {
			obj.put(flight.getSourceAirportCode().concat(Constants.SEPERATOR)
					.concat(flight.getDestinationAirportCode()),
					Constants.SINGLE_QUOTE.concat(flight.getFlightNumber()).concat(Constants.SINGLE_QUOTE)
							.concat(Constants.COLON) + (flight.getDuration() / 1000) / 60);
			responseArray.put(obj);
		}

		List<FlightDto> flightDtos = new ArrayList<FlightDto>();
		for (List<Flight> flights : connectingFlights) {
			FlightDto dto = new FlightDto();
			for (int i = 0; i < flights.size(); i++) {
				if (dto.getAirportCodes().length() > 0) {
					dto.appendAirportCodes(Constants.SEPERATOR);
				}
				dto.appendAirportCodes(flights.get(i).getSourceAirportCode());

				if (dto.getFlightCodes().length() > 0) {
					dto.appendFlightCode(Constants.SEPERATOR);
				}
				dto.appendFlightCode(flights.get(i).getFlightNumber());

				dto.setDuration(flights.get(i).getDuration());

				// Append destination airport code for the last element
				if (i == flights.size() - 1) {
					dto.appendAirportCodes(Constants.SEPERATOR);
					dto.appendAirportCodes(flights.get(i).getDestinationAirportCode());
				}
			}
			flightDtos.add(dto);
		}
		// sort flights by duration
		flightDtos = flightDtos.stream().sorted(Comparator.comparing(FlightDto::getDuration))
				.collect(Collectors.toList());
		for (FlightDto flightDto : flightDtos) {
			JSONObject iobj = new JSONObject();
			iobj.put(flightDto.getAirportCodes().toString(),
					Constants.SINGLE_QUOTE.concat(flightDto.getFlightCodes().toString()).concat(Constants.SINGLE_QUOTE)
							.concat(Constants.COLON).concat(Long.toString((flightDto.getDuration() / 1000) / 60)));
			responseArray.put(iobj);
		}

		log.info("prepareResponse ends.");
		return responseArray.toString();
	}

}
