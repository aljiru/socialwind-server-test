package org.inftel.socialwind.services;

import static com.beoui.geocell.GeocellManager.proximitySearch;

import com.beoui.geocell.model.GeocellQuery;
import com.beoui.geocell.model.Point;

import org.inftel.socialwind.server.domain.EMF;
import org.inftel.socialwind.server.domain.Spot;
import org.inftel.socialwind.server.domain.Surfer;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

public class SpotService {

    private static final Logger log = Logger.getLogger(SpotService.class.getName());

    private static final String SELECT_ALL = "select o from Spot o";
    private final static String SELECT_COUNT = "select count(o) from Spot o";
    private static final Integer HOTSPOT_SURFERS_LIMIT = 5;

    public static final EntityManager entityManager() {
        return EMF.get().createEntityManager();
    }

    public static Long countSpots() {
        EntityManager em = entityManager();
        try {
            return ((Number) em.createQuery(SELECT_COUNT).getSingleResult()).longValue();
        } finally {
            em.close();
        }
    }

    public static List<Spot> findAllSpots() {
        EntityManager em = entityManager();
        try {
            @SuppressWarnings("unchecked")
            List<Spot> SpotList = em.createQuery(SELECT_ALL).getResultList();
            SpotList.size(); // forzar materializar resultados
            return SpotList;
        } finally {
            em.close();
        }
    }

    public static Spot findSpot(Long id) {
        if (id == null) {
            return null;
        }
        EntityManager em = entityManager();
        try {
            return em.find(Spot.class, id);
        } finally {
            em.close();
        }
    }

    public static List<Spot> findSpotEntries(int firstResult, int maxResults) {
        EntityManager em = entityManager();
        try {
            @SuppressWarnings("unchecked")
            List<Spot> resultList = em.createQuery(SELECT_ALL).setFirstResult(firstResult)
                    .setMaxResults(maxResults).getResultList();
            resultList.size(); // forzar materializar resultados
            return resultList;
        } finally {
            em.close();
        }
    }

    public static void persist(Spot instance) {
        EntityManager em = entityManager();
        try {
            em.persist(instance);
        } finally {
            em.close();
        }
    }

    public static void remove(Spot instance) {
        EntityManager em = entityManager();
        try {
            Spot attached = em.find(Spot.class, instance.getId());
            em.remove(attached);
        } finally {
            em.close();
        }
    }

    public static Spot findSpotByLocation(double latitude, double longitude) {
        EntityManager em = entityManager();
        try {
            Point center = new Point(latitude, longitude);
            GeocellQuery baseQuery = new GeocellQuery();
            List<Spot> spots = proximitySearch(center, 1, 500, Spot.class, baseQuery, em);
            if (spots.size() == 1) {
                return spots.get(0);
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }

    public static List<Spot> findNearbySpots(double latitude, double longitude) {
        EntityManager em = entityManager();
        try {
            Point center = new Point(latitude, longitude);
            GeocellQuery baseQuery = new GeocellQuery();
            List<Spot> spots = proximitySearch(center, 100, 0, Spot.class, baseQuery, em);
            spots.size(); // materializar resultados
            return spots;
        } finally {
            em.close();
        }
    }

    public static List<Spot> findNearbyHotSpots(double latitude, double longitude) {
        EntityManager em = entityManager();
        try {
            Point center = new Point(latitude, longitude);
            GeocellQuery baseQuery = new GeocellQuery("hot == true");
            List<Spot> spots = proximitySearch(center, 100, 0, Spot.class, baseQuery, em);
            spots.size(); // materializar resultados
            return spots;
        } finally {
            em.close();
        }
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
        if (!surfer.getActiveSession().getSpot().equals(spot)) {
            throw new IllegalStateException("No se puede añadir un surfero que no esta en la playa");
        }
        spot.setSurferCount(spot.getSurferCount() + 1);
        spot.setSurferCurrentCount(spot.getSurferCurrentCount() + 1);
        updateHotState(spot);
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
        } else if (!spot.getHot() && spot.getSurferCurrentCount() < HOTSPOT_SURFERS_LIMIT) {
            spot.setHot(true);
        }
    }
}
