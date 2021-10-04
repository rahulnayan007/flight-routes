package com.mmt.ivtest.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.mmt.ivtest.model.Flight;

/**
 * 
 * @author rahul
 *
 */
@Component(value = "fastestConnectingFlightHelper")
public class FastestConnectingFlightHelper {

	// list will have source flight as first element and one or many elements of
	// connecting flights, so, logic required only if more than 2 entries present
	// It will handle morning connecting flights also
	protected Flight getFastestConnectingFlightForSource(List<Flight> flightList, Flight flightFromSource) {
		Flight connectingFlight = null;
		Flight nextConnectingFlight = null;
		for (Flight flight : flightList) {
			if (null == connectingFlight) {
				connectingFlight = flight;
			} else {
				nextConnectingFlight = flight;
				if ((nextConnectingFlight.getEndTime().getTime()
						- flightFromSource.getStartTime().getTime()) < (connectingFlight.getEndTime().getTime()
								- flightFromSource.getStartTime().getTime())) {
					connectingFlight = nextConnectingFlight;
				}
			}
		}
		return connectingFlight;
	}

}
