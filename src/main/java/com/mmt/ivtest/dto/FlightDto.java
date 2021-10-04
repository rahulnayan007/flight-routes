package com.mmt.ivtest.dto;

import lombok.Data;
import lombok.Getter;

/**
 * 
 * @author rahul
 *
 */
@Data
@Getter
public class FlightDto {

	private StringBuilder airportCodes;
	private StringBuilder flightCodes;
	private long duration;

	public FlightDto() {
		this.airportCodes = new StringBuilder();
		this.flightCodes = new StringBuilder();
		this.duration = 0L;
	}

	public FlightDto(String airportCode, String flightCode, long duration) {
		this.airportCodes.append(airportCode);
		this.flightCodes.append(flightCode);
		this.duration += duration;
	}

	public void appendAirportCodes(String airportCode) {
		this.airportCodes.append(airportCode);
	}

	public void appendFlightCode(String flightCode) {
		this.flightCodes.append(flightCode);
	}

	public void addDuration(long duration) {
		this.duration += duration;
	}

}
