package com.mmt.ivtest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mmt.ivtest.loader.FlightDataLoader;

import lombok.extern.slf4j.Slf4j;

/**
 * Configuration class to load data
 * 
 * @author rahul
 *
 */
@Slf4j
@Configuration
public class IvtestConfig {

	@Bean(name = "flightDataLoader")
	public FlightDataLoader getFlightsMap() {
		log.info("IvtestConfig called, loading getFlightsMap.");
		return new FlightDataLoader();
	}

}
