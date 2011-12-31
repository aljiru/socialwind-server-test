package org.inftel.socialwind.services;

import static com.beoui.geocell.GeocellManager.proximitySearch;

import com.beoui.geocell.model.GeocellQuery;
import com.beoui.geocell.model.Point;

import org.inftel.socialwind.server.domain.EMF;
import org.inftel.socialwind.server.domain.Spot;

import java.util.List;

import javax.persistence.EntityManager;

public class SpotService {

    private static final String SELECT_ONE = "select o from Spot o";
    private final static String SELECT_COUNT = "select count(o) from Spot o";

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
            List<Spot> SpotList = em.createQuery(SELECT_ONE).getResultList();
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
            List<Spot> resultList = em.createQuery(SELECT_ONE).setFirstResult(firstResult)
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
}
