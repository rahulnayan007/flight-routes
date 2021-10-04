package com.mmt.ivtest.model;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 
 * @author rahul
 *
 */
@Data
@ToString
@EqualsAndHashCode
public class Flight {

	private String flightNumber;

	private String sourceAirportCode;

	private String destinationAirportCode;

	private Date startTime;

	private Date endTime;

	private long duration;

	public Flight(String flightNumber, String sourceAirportCode, String destinationAirportCode, Date startTime,
			Date endTime, long duration) {
		this.flightNumber = flightNumber;
		this.sourceAirportCode = sourceAirportCode;
		this.destinationAirportCode = destinationAirportCode;
		this.startTime = startTime;
		this.endTime = endTime;
		this.duration = duration;
	}

}
