package com.mmt.ivtest.loader;

import java.io.FileReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import com.mmt.ivtest.model.Flight;
import com.mmt.ivtest.util.Constants;

import lombok.extern.slf4j.Slf4j;

/**
 * load csv data and store as map.
 * 
 * @author rahul
 *
 */
@Slf4j
public class FlightDataLoader {

	Map<String, List<Flight>> flightsMap;

	public FlightDataLoader() {
		loadCSVData();
	}

	public void loadCSVData() {
		try {
			log.info("loadCSVData started.");
			Reader reader = new FileReader("ivtest-sched.csv");
			Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(reader);

			flightsMap = new HashMap<String, List<Flight>>();
			for (CSVRecord record : records) {
				Flight flight = getFlight(record);
				List<Flight> flightList = getFlightList(flightsMap, flight);
				flightList.add(flight);
				flightsMap.put(flight.getSourceAirportCode(), flightList);
			}
			System.out.println(flightsMap);
			log.info("loadCSVData ends.");
		} catch (Exception exception) {
			log.error("******************Exception in FlightDataLoader loadCSVData********************\n{}",
					exception.getMessage());
		}
	}

	private Flight getFlight(CSVRecord record) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.SCHEDULE_FORMAT);
		Date startTime = simpleDateFormat.parse(checkDateValue(record.get(3)));
		Date endTime = simpleDateFormat.parse(checkDateValue(record.get(4)));
		long duration = getDuration(startTime, endTime);
		return new Flight(record.get(0), record.get(1), record.get(2), startTime, endTime, duration);
	}

	private String checkDateValue(String value) {
		int shortLength = Constants.DATE_INPUT_LENGTH - value.length();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < shortLength; i++) {
			sb.append("0");
		}
		sb.append(value);
		return sb.toString();
	}

	@SuppressWarnings("deprecation")
	private long getDuration(Date startTime, Date endTime) {
		if (endTime.before(startTime)) {
			endTime.setHours(endTime.getHours() + Constants.TWENTY_FOUR_HOURS);
		}
		return endTime.getTime() - startTime.getTime();
	}

	private List<Flight> getFlightList(Map<String, List<Flight>> flightsMap, Flight flight) {
		List<Flight> flightList = flightsMap.get(flight.getSourceAirportCode());
		if (null == flightList || flightList.isEmpty()) {
			flightList = new ArrayList<Flight>();
		}
		return flightList;
	}

	public Map<String, List<Flight>> getFlightsMap() {
		return flightsMap;
	}

}
