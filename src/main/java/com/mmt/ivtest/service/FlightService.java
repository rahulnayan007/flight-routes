package com.mmt.ivtest.service;

import java.text.ParseException;

/**
 * 
 * @author rahul
 *
 */
public interface FlightService {

	String getFlights(String source, String destination) throws ParseException;

}
