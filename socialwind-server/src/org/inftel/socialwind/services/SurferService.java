package org.inftel.socialwind.services;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.Point;

import static org.inftel.socialwind.services.SessionService.findActiveSession;

import org.inftel.socialwind.server.domain.EMF;
import org.inftel.socialwind.server.domain.Session;
import org.inftel.socialwind.server.domain.Spot;
import org.inftel.socialwind.server.domain.Surfer;

import java.util.List;

import javax.persistence.EntityManager;

public class SurferService {

    private static final String SELECT_ALL = "select o from Surfer o";
    private static final String SELECT_COUNT = "select count(o) from Surfer o";

    public static final EntityManager entityManager() {
        return EMF.get().createEntityManager();
    }

    public static Long countSurfers() {
        EntityManager em = entityManager();
        try {
            return ((Number) em.createQuery(SELECT_COUNT).getSingleResult()).longValue();
        } finally {
            em.close();
        }
    }

    public static List<Surfer> findAllSurfers() {
        EntityManager em = entityManager();
        try {
            @SuppressWarnings("unchecked")
            List<Surfer> surferList = em.createQuery(SELECT_ALL).getResultList();
            surferList.size(); // forzar materializar resultados
            return surferList;
        } finally {
            em.close();
        }
    }

    public static Surfer findSurfer(Long id) {
        if (id == null) {
            return null;
        }
        EntityManager em = entityManager();
        try {
            return em.find(Surfer.class, id);
        } finally {
            em.close();
        }
    }

    public static List<Surfer> findSurferEntries(int firstResult, int maxResults) {
        EntityManager em = entityManager();
        try {
            @SuppressWarnings("unchecked")
            List<Surfer> resultList = em.createQuery(SELECT_ALL).setFirstResult(firstResult)
                    .setMaxResults(maxResults).getResultList();
            resultList.size(); // forzar materializar resultados
            return resultList;
        } finally {
            em.close();
        }
    }

    public static void persist(Surfer instance) {
        EntityManager em = entityManager();
        try {
            em.persist(instance);
        } finally {
            em.close();
        }
    }

    public static void remove(Surfer instance) {
        EntityManager em = entityManager();
        try {
            Surfer attached = em.find(Surfer.class, instance.getId());
            em.remove(attached);
        } finally {
            em.close();
        }
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
        try {

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

        } finally {
            em.close();
        }
        return session;
    }

    public static List<Session> sessions(Surfer surfer) {
        return SessionService.findSessionsBySurfer(surfer);
    }

}
