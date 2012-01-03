package org.inftel.socialwind.services;

import static com.beoui.geocell.GeocellManager.proximitySearch;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.GeocellQuery;
import com.beoui.geocell.model.Point;

import org.inftel.socialwind.server.domain.Spot;
import org.inftel.socialwind.server.domain.Surfer;
import org.inftel.socialwind.server.domain.ThreadLocalEntityManager;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

public class SpotService {

    private static final Logger log = Logger.getLogger(SpotService.class.getName());

    private static final String SELECT_ALL = "select o from Spot o";
    private final static String SELECT_COUNT = "select count(o) from Spot o";
    private static final Integer HOTSPOT_SURFERS_LIMIT = 5;

    public static final EntityManager entityManager() {
        return ThreadLocalEntityManager.get();
    }

    public static Long countSpots() {
        EntityManager em = entityManager();
        return ((Number) em.createQuery(SELECT_COUNT).getSingleResult()).longValue();
    }

    public static List<Spot> findAllSpots() {
        EntityManager em = entityManager();
        @SuppressWarnings("unchecked")
        List<Spot> SpotList = em.createQuery(SELECT_ALL).getResultList();
        SpotList.size(); // forzar materializar resultados
        return SpotList;
    }

    public static Spot findSpot(Long id) {
        if (id == null) {
            return null;
        }
        EntityManager em = entityManager();
        return em.find(Spot.class, id);
    }

    public static List<Spot> findSpotEntries(int firstResult, int maxResults) {
        EntityManager em = entityManager();
        @SuppressWarnings("unchecked")
        List<Spot> resultList = em.createQuery(SELECT_ALL).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
        resultList.size(); // forzar materializar resultados
        return resultList;
    }

    public static void persist(Spot instance) {
        EntityManager em = entityManager();
        generateGeoCells(instance); // TODO actualizar geocells solo cuando se necesite
        em.persist(instance);
        em.refresh(instance);
    }

    private static void generateGeoCells(Spot instance) {
        Point location = new Point(instance.getLatitude(), instance.getLongitude());
        List<String> cells = GeocellManager.generateGeoCell(location);
        instance.setGeoCellsData(cells);
    }

    public static void remove(Spot instance) {
        EntityManager em = entityManager();
        Spot attached = em.find(Spot.class, instance.getId());
        em.remove(attached);
    }

    public static Spot findSpotByLocation(double latitude, double longitude) {
        EntityManager em = entityManager();
        Point center = new Point(latitude, longitude);
        GeocellQuery baseQuery = new GeocellQuery(SELECT_ALL);
        List<Spot> spots = proximitySearch(center, 1, 500, Spot.class, baseQuery, em);
        if (spots.size() == 1) {
            return spots.get(0);
        } else {
            return null;
        }
    }

    public static List<Spot> findNearbySpots(double latitude, double longitude) {
        EntityManager em = entityManager();
        Point center = new Point(latitude, longitude);
        GeocellQuery baseQuery = new GeocellQuery();
        List<Spot> spots = proximitySearch(center, 100, 0, Spot.class, baseQuery, em);
        spots.size(); // materializar resultados
        return spots;
    }

    public static List<Spot> findNearbyHotSpots(double latitude, double longitude) {
        EntityManager em = entityManager();
        Point center = new Point(latitude, longitude);
        GeocellQuery baseQuery = new GeocellQuery("hot == true");
        List<Spot> spots = proximitySearch(center, 100, 0, Spot.class, baseQuery, em);
        spots.size(); // materializar resultados
        return spots;
    }

    /**
     * Se encarga de añadir surfer a la playa, acutaliza los contadores y el estado de playa
     * caliente.
     * 
     * @param spot
     *            playa donde se quiere añadir un surfero
     * @param surfer
     *            surfero al que se quiere añadir
     */
    static void addSurfer(Spot spot, Surfer surfer) {
        EntityManager em = entityManager();
        spot.setSurferCount(spot.getSurferCount() + 1);
        spot.setSurferCurrentCount(spot.getSurferCurrentCount() + 1);
        updateHotState(spot);
        em.persist(spot);
    }

    /**
     * Se encarga de eliminar un surfero de una playa, actualiza los contadores y el estado de playa
     * caliente.
     * 
     * @param spot
     *            playa donde se quiere eliminar un surfero
     * @param surfer
     *            surfero al que se quiere eliminar
     */
    static void removeSurfer(Spot spot, Surfer surfer) {
        spot.setSurferCurrentCount(spot.getSurferCurrentCount() - 1);
        updateHotState(spot);
    }

    /**
     * @param spot
     */
    private static void updateHotState(Spot spot) {
        // Esta sorprendente situacion podria llegar a darse si no se usa un sistema transaccional
        // y de hecho, el sistema no tiene porque serlo ya que no es requisito y gasta recursos
        if (spot.getSurferCurrentCount() < 0) {
            spot.setSurferCurrentCount(0); // asi ningun usuario vera numero negativos
            log.warning("El contador del spot " + spot.getName() + " ha bajado de 0");
        }

        // Actualizar estado hotspot
        if (spot.getHot() && spot.getSurferCurrentCount() < HOTSPOT_SURFERS_LIMIT) {
            spot.setHot(false);
        } else if (!spot.getHot() && spot.getSurferCurrentCount() > HOTSPOT_SURFERS_LIMIT) {
            spot.setHot(true);
        }
    }
}
