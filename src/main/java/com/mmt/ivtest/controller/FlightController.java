package com.mmt.ivtest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mmt.ivtest.service.FlightService;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller exposing REST endpoints for flight services.
 * 
 * @author rahul
 *
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/flights")
public class FlightController {

	@Autowired
	@Qualifier(value = "flightServiceImpl")
	private FlightService flightService;

	@GetMapping(value = "/source/{source}/destination/{destination}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getFlights(@PathVariable("source") String source,
			@PathVariable("destination") String destination) {
		log.info("getFlights api called for source={} and destination={}", source, destination);
		try {
			return ResponseEntity.ok().body(flightService.getFlights(source, destination));
		} catch (Exception e) {
			log.error("Exception caught: {}", e.getMessage());
			return ResponseEntity.internalServerError().build();
		}
	}

}
