package com.mmt.ivtest.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mmt.ivtest.dto.FlightDto;
import com.mmt.ivtest.model.Flight;

public class ResponseProcessor {

	public String prepareResponse(List<Flight> sortedDirectFlights, List<List<Flight>> sortedConnectingFlights) {

		JSONArray responseArray = new JSONArray();
		JSONObject obj = new JSONObject();
		for (Flight flight : sortedDirectFlights) {
			obj.put(flight.getSourceAirportCode().concat(Constants.SEPERATOR)
					.concat(flight.getDestinationAirportCode()),
					Constants.SINGLE_QUOTE.concat(flight.getFlightNumber()).concat(Constants.SINGLE_QUOTE)
							.concat(Constants.COLON) + (flight.getDuration() / 1000) / 60);
			responseArray.put(obj);
		}

		List<FlightDto> flightDtos = new ArrayList<FlightDto>();
		for (List<Flight> flights : sortedConnectingFlights) {
			FlightDto dto = new FlightDto();
			for (int i=0; i<flights.size(); i++) {
				if (dto.getAirportCodes().length() > 0) {
					dto.appendAirportCodes(Constants.SEPERATOR);
				}
				dto.appendAirportCodes(flights.get(i).getSourceAirportCode());

				if (dto.getFlightCodes().length() > 0) {
					dto.appendFlightCode(Constants.SEPERATOR);
				}
				dto.appendFlightCode(flights.get(i).getFlightNumber());

				if (dto.getDuration() > 0) {
					dto.addDuration(Constants.GAP_BETWEEN_CONNECTING_FLIGHTS_MILLIS);
				}
				dto.addDuration(flights.get(i).getDuration());
				
				if(i == flights.size()-1) {
					dto.appendAirportCodes(Constants.SEPERATOR);
					dto.appendAirportCodes(flights.get(i).getDestinationAirportCode());
				}
			}
			flightDtos.add(dto);
		}
		flightDtos = flightDtos.stream().sorted(new FlightDtoComparator()::compare).collect(Collectors.toList());
		for (FlightDto flightDto : flightDtos) {
			JSONObject iobj = new JSONObject();
			iobj.put(flightDto.getAirportCodes().toString(),
					Constants.SINGLE_QUOTE.concat(flightDto.getFlightCodes().toString()).concat(Constants.SINGLE_QUOTE)
							.concat(Constants.COLON).concat(Long.toString(((flightDto.getDuration()) / 1000) / 60)));
			responseArray.put(iobj);
		}

		return responseArray.toString();
	}

}
