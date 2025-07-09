package com.stopsnearme.app.ws.ui.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Repository;

import com.stopsnearme.app.ws.ui.model.entity.Trip;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class TripRepository {
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @PersistenceContext
    private EntityManager em;

    public void loadAllTrips(boolean truncate, String region) {

        if (truncate) {
            logger.log(Level.FINER, "Truncating trips %b", truncate);
            // em.createQuery("delete c from Stops c where stopId != 0", Stop.class).executeUpdate();
            // em.createQuery("truncate table if exists Stop cascade", Stop.class).executeUpdate();
            em.createNativeQuery("truncate table Trip", Trip.class).executeUpdate();
        }

        // load trips code here
        logger.log(Level.FINER, "Loading trips for {0}", region);

        if (region == "bc") {
            var regionIds = System.getenv("BC_REGION_IDS").split(",");

            if (regionIds.length > 0) {
                logger.log(Level.SEVERE, "Check if BC_REGION_IDS env var is set");

                //load for all regions
                for (String reg : regionIds) {
                    reg = reg.trim();

                    loadTrips(region);

                }
            }
        } else {
            loadTrips(region);
        }
        // return coffee;
    }

    private void loadTrips(String region) {

        int first = 0, i = 0;
        Map<String, Integer> linkMap = new TreeMap<String, Integer>();

        var staticsPath = System.getenv("STATICS");

        try (Scanner scanner = new Scanner(new File(String.format("%s/%s/trips.txt", staticsPath, region)))) {

            while (scanner.hasNextLine()) {

                String stopLine = scanner.nextLine();
                // logger.info("Line is " + stopLine);
                String[] stopArr = stopLine.split(",");

                // assign index for cols
                if (first == 0) {
                    // index row
                    for (String elem : stopArr) {
                        linkMap.put(elem, i);
                        i++;
                    }
                    first++;
                    continue;
                }

                Trip trip = new Trip();
                int val = linkMap.get("trip_id");
                trip.setTripId(Long.parseLong(stopArr[val]));
                val = linkMap.get("route_id");
                trip.setRouteId(Integer.parseInt(stopArr[val]));
                val = linkMap.get("service_id");
                trip.setServiceId(Integer.parseInt(stopArr[val]));
                val = linkMap.get("trip_headsign");
                trip.setTripHeadSign(stopArr[val]);
                val = linkMap.get("trip_short_name");
                trip.setTripShortName(stopArr[val]);
                val = linkMap.get("direction_id");
                trip.setDirectionId(Integer.parseInt(stopArr[val]));
                val = linkMap.get("block_id");
                trip.setBlockId(stopArr[val]);
                val = linkMap.get("shape_id");
                trip.setShapeId(Integer.parseInt(stopArr[val]));
                val = linkMap.get("wheelchair_accessible");
                trip.setWheelChairAccessible(Integer.parseInt(stopArr[val]));
                val = linkMap.get("bikes_allowed");
                trip.setBikesAllowed(Integer.parseInt(stopArr[val]));
                /*
                val = linkMap.get("zone_id");
                if ("".equals(stopArr[val])) {
                }
                else {
                    trip.setZoneId(Integer.parseInt(stopArr[val]));
                }*/

                // logger.log(Level.WARNING, "Loading trip {0}", trip);
                em.persist(trip);
            }

        } catch (FileNotFoundException e) {
            logger.info("(FileNotFoundException is ");
            e.printStackTrace();
        } catch (Exception e) {
            logger.info("Exception is ");
            e.printStackTrace();
        }

        logger.log(Level.FINER, "Finished loading trips for {0}", region);
    }

    public List<Trip> findAll() {
        logger.info("Getting all trips");
        return em.createQuery("SELECT c FROM Trip c", Trip.class)
        .setMaxResults(3)
        .getResultList();
    }

    public List<Trip> getTripInfoByRouteId(String routeId) {
        logger.log(Level.FINER, "Getting trips by routeId - {0}", routeId);
        return em.createQuery("SELECT c FROM Trip c where routeId = ?1 order by routeId", Trip.class)
                .setParameter(1, routeId)
                .getResultList();
    }

    public List<Trip> getTripInfoByTripId(String tripId) {
        logger.log(Level.FINER, "Getting trips by tripId - {0}", tripId);
        return em.createQuery("SELECT c FROM Trip c where tripId = ?1 order by tripId", Trip.class)
                .setParameter(1, tripId)
                .getResultList();
    }
}
