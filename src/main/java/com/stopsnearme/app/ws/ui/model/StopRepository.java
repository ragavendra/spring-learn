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

import com.stopsnearme.app.ws.ui.model.entity.Stop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class StopRepository {
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @PersistenceContext
    private EntityManager em;

    public void loadAllStops(boolean truncate, String region) {

        if (truncate) {
            logger.log(Level.FINER, "Truncating stops %b", truncate);
            // em.createQuery("delete c from Stops c where stopId != 0", Stop.class).executeUpdate();
            // em.createQuery("truncate table if exists Stop cascade", Stop.class).executeUpdate();
            em.createNativeQuery("truncate table Stop", Stop.class).executeUpdate();
        }

        // load stops code here
        logger.log(Level.FINER, "Loading stops for {0}", region);

        if (region == "bc") {
            var regionIds = System.getenv("BC_REGION_IDS").split(",");

            if (regionIds.length > 0) {

                //load for all regions
                for (String reg : regionIds) {
                    reg = reg.trim();

                    loadStops(region);

                }
            }
            else {
                logger.log(Level.SEVERE, "Check if BC_REGION_IDS env var is set");
            }
        } else {
            loadStops(region);
        }
        // return coffee;
    }

    private void loadStops(String region) {

        int first = 0, i = 0;
        Map<String, Integer> linkMap = new TreeMap<String, Integer>();

        var staticsPath = System.getenv("STATICS");

        try (Scanner scanner = new Scanner(new File(String.format("%s/%s/stops.txt", staticsPath, region)))) {

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

                Stop stop = new Stop();
                int val = linkMap.get("stop_id");
                stop.setStopId(Long.parseLong(stopArr[val]));
                val = linkMap.get("stop_code");
                stop.setStopCode(Integer.parseInt(stopArr[val]));
                val = linkMap.get("stop_name");
                stop.setStopName(stopArr[val]);
                val = linkMap.get("stop_desc");
                stop.setStopDescription(stopArr[val]);
                val = linkMap.get("stop_lat");
                stop.setLatitude(Double.parseDouble(stopArr[val]));
                val = linkMap.get("stop_lon");
                stop.setLongitude(Double.parseDouble(stopArr[val]));
                /*
                val = linkMap.get("zone_id");
                if ("".equals(stopArr[val])) {
                }
                else {
                    stop.setZoneId(Integer.parseInt(stopArr[val]));
                }*/
                val = linkMap.get("stop_url");
                stop.setStopUrl(stopArr[val]);
                val = linkMap.get("location_type");
                stop.setLocationType(Integer.parseInt(stopArr[val]));
                val = linkMap.get("parent_station");
                if ("".equals(stopArr[val])) {
                }
                else {
                    stop.setParentStation(Integer.parseInt(stopArr[val]));
                }


                // logger.info("Stop is " + stop);

                /* limiter
                if(first++ > 10)
                    break;*/

                // Stop stop = (Stop) scanner.nextLine();
                em.persist(stop);
            }

        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "FileNotFoundException is ");
            e.printStackTrace();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception is ");
            e.printStackTrace();
        }

        logger.log(Level.FINER, "Finished loading stops for {0}", region);
    }

    public List<Stop> findAll() {
        logger.log(Level.FINER, "Getting all stops");
        return em.createQuery("SELECT c FROM Stop c", Stop.class)
        .setMaxResults(3)
        .getResultList();
    }


    public List<Stop> findByLatLon(Double lt, Double  ln) {
        logger.log(Level.FINER, String.format("Getting stops by latitude %10.3f and longitude %10.3f", lt, ln));
        var latr = 0.008;
        var lonr = 0.0025;
        return em.createQuery("SELECT c FROM Stop c where latitude > ?1 AND latitude < ?2 AND longitude > ?3 AND longitude < ?4", Stop.class)
                .setParameter(1, lt - latr).setParameter(2, lt + latr)
                .setParameter(3, ln - lonr).setParameter(4, ln + lonr)
                .getResultList();
    }

    public List<Stop> findByCodeOrName(String stopCode, String stopName) {
        logger.log(Level.FINER, String.format("Getting stops by stopCode and/ or stopName", stopCode, stopName));
        return em.createQuery("SELECT c.stopCode, c.stopName FROM Stop c where stopName LIKE '?1' AND stopCode LIKE < '?2' ORDER by stopCode", Stop.class)
                .setParameter(1, stopName).setParameter(2, stopCode)
                .getResultList();
    }
}
