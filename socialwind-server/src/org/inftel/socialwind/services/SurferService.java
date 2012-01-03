package org.inftel.socialwind.services;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.Point;

import static org.inftel.socialwind.services.SessionService.findActiveSession;

import org.inftel.socialwind.server.domain.Session;
import org.inftel.socialwind.server.domain.Spot;
import org.inftel.socialwind.server.domain.Surfer;
import org.inftel.socialwind.server.domain.ThreadLocalEntityManager;

import java.util.List;

import javax.persistence.EntityManager;

public class SurferService {

    private static final String SELECT_ALL = "select o from Surfer o";
    private static final String SELECT_COUNT = "select count(o) from Surfer o";

    public static final EntityManager entityManager() {
        return ThreadLocalEntityManager.get();
    }

    public static Long countSurfers() {
        EntityManager em = entityManager();
        return ((Number) em.createQuery(SELECT_COUNT).getSingleResult()).longValue();
    }

    public static List<Surfer> findAllSurfers() {
        EntityManager em = entityManager();
        @SuppressWarnings("unchecked")
        List<Surfer> surferList = em.createQuery(SELECT_ALL).getResultList();
        surferList.size(); // forzar materializar resultados
        return surferList;
    }

    public static Surfer findSurfer(Long id) {
        if (id == null) {
            return null;
        }
        EntityManager em = entityManager();
        return em.find(Surfer.class, id);
    }

    public static List<Surfer> findSurferEntries(int firstResult, int maxResults) {
        EntityManager em = entityManager();
        @SuppressWarnings("unchecked")
        List<Surfer> resultList = em.createQuery(SELECT_ALL).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
        resultList.size(); // forzar materializar resultados
        return resultList;
    }

    public static void persist(Surfer instance) {
        EntityManager em = entityManager();
        em.persist(instance);
        em.refresh(instance);
    }

    public static void remove(Surfer instance) {
        EntityManager em = entityManager();
        Surfer attached = em.find(Surfer.class, instance.getId());
        em.remove(attached);
    }

    /**
     * Actualiza la posición del surfero. En caso de que el surfero entre o salga de una playa se
     * actualizara el estado de estas playas.
     * 
     * @param surfer
     * @param latitude
     * @param longitude
     */
    public static Session updateSurferLocation(Surfer surfer, double latitude, double longitude) {
        EntityManager em = entityManager();
        Session session = null;

        if (!em.contains(surfer)) {
            throw new IllegalStateException("El surfero debe ser una untidad gestionada");
            // Si no es gestionada no se reflejan los cambios!, asi que gestionala!
            // TODO esto podria cambiarse por un assert, improved production performance
        }

        // Location as a Point
        Point location = new Point(latitude, longitude);

        // Generate Geocells
        List<String> cells = GeocellManager.generateGeoCell(location);

        // Save instance
        surfer.setLatitude(latitude);
        surfer.setLongitude(longitude);
        surfer.setGeoCellsData(cells);

        // Se busca si la posicion forma parte de una playa
        Spot spot = SpotService.findSpotByLocation(latitude, longitude);
        Session actual = findActiveSession(surfer);

        // Esta el surfer en una playa?
        if (SessionService.hasActiveSession(surfer)) {
            // Ha salido o cambiado de playa el surfer?
            if (spot == null || spot.getId() != actual.getSpotId()) {
                SessionService.endSession(surfer);
            }
        }

        // Ha cambiado o entrado en una nueva playa?
        if (spot != null && (actual == null || spot.getId() != actual.getSpotId())) {
            session = SessionService.beginSession(surfer, spot);
        }

        return session;
    }

    public static List<Session> sessions(Surfer surfer) {
        return SessionService.findSessionsBySurfer(surfer);
    }

}
