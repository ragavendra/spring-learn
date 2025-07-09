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

import com.stopsnearme.app.ws.ui.model.entity.Route;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class RouteRepository {
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @PersistenceContext
    private EntityManager em;

    public void loadAllRoutes(boolean truncate, String region) {

        if (truncate) {
            logger.log(Level.FINER, "Truncating routes %b", truncate);
            em.createNativeQuery("truncate table Route", Route.class).executeUpdate();
        }

        // load routes code here
        logger.log(Level.FINER, "Loading routes for {0}", region);

        if (region == "bc") {
            var regionIds = System.getenv("BC_REGION_IDS").split(",");

            if (regionIds.length > 0) {
                logger.log(Level.SEVERE, "Check if BC_REGION_IDS env var is set");

                //load for all regions
                for (String reg : regionIds) {
                    reg = reg.trim();

                    loadRoutes(region);

                }
            }
        } else {
            loadRoutes(region);
        }
        // return coffee;
    }

    private void loadRoutes(String region) {

        int first = 0, i = 0;
        Map<String, Integer> linkMap = new TreeMap<String, Integer>();

        var staticsPath = System.getenv("STATICS");

        try (Scanner scanner = new Scanner(new File(String.format("%s/%s/routes.txt", staticsPath, region)))) {

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

                Route route = new Route();
                int val = linkMap.get("route_id");
                route.setRouteId(Long.parseLong(stopArr[val]));
                val = linkMap.get("route_type");
                route.setRouteType(Integer.parseInt(stopArr[val]));
                val = linkMap.get("route_short_name");
                route.setRouteShortName(stopArr[val]);
                val = linkMap.get("route_long_name");
                route.setRouteLongName(stopArr[val]);

                em.persist(route);
            }

        } catch (FileNotFoundException e) {
            logger.info("(FileNotFoundException is ");
            e.printStackTrace();
        } catch (Exception e) {
            logger.info("Exception is ");
            e.printStackTrace();
        }

        logger.log(Level.FINER, "Finished loading routes for {0}", region);
    }

    public List<Route> findAll() {
        logger.log(Level.FINER, "Getting all routes");
        return em.createQuery("SELECT c FROM Route c", Route.class)
        .setMaxResults(3)
        .getResultList();
    }

    public List<Route> getRouteInfoByRouteId(String routeId) {
        logger.log(Level.FINER, "Getting route info by routeId - {0}", routeId);
        return em.createQuery("SELECT c FROM Route c where routeId = ?1 order by routeId", Route.class)
                .setParameter(1, routeId)
                .getResultList();
    }
}
