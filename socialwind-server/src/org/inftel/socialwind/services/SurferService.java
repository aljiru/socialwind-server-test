package org.inftel.socialwind.services;

import org.inftel.socialwind.server.domain.EMF;
import org.inftel.socialwind.server.domain.Surfer;

import java.util.List;

import javax.persistence.EntityManager;

public class SurferService {

    public static final EntityManager entityManager() {
        return EMF.get().createEntityManager();
    }

    public static Long countSurfers() {
        EntityManager em = entityManager();
        try {
            return ((Number) em.createQuery("select count(o) from Surfer o").getSingleResult())
                    .longValue();
        } finally {
            em.close();
        }
    }

    public static List<Surfer> findAllSurfers() {
        EntityManager em = entityManager();
        try {
            @SuppressWarnings("unchecked")
            List<Surfer> surferList = em.createQuery("select o from Surfer o").getResultList();
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
            List<Surfer> resultList = em.createQuery("select o from Surfer o")
                    .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
}
