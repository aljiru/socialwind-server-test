package org.inftel.socialwind.services;

import org.inftel.socialwind.server.domain.Session;
import org.inftel.socialwind.server.domain.Spot;
import org.inftel.socialwind.server.domain.Surfer;
import org.inftel.socialwind.server.domain.ThreadLocalEntityManager;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Servicio encargado de gestionar las tareas relacionadas con las sesiones de windsurf.<br>
 * 
 * @author ibaca
 * @see Session
 * 
 */
public class SessionService {

    private static final String SELECT_SESSIONS_BY_SURFER = "select o from Session o where o.surfer = :surfer";
    private static final String SELECT_ALL = "select o from Session o";

    public static final EntityManager entityManager() {
        return ThreadLocalEntityManager.get();
    }

    public static List<Session> findAllSessions() {
        EntityManager em = entityManager();
        @SuppressWarnings("unchecked")
        List<Session> SessionList = em.createQuery(SELECT_ALL).getResultList();
        SessionList.size(); // forzar materializar resultados
        return SessionList;
    }

    public static Session findSession(Long id) {
        if (id == null) {
            return null;
        }
        EntityManager em = entityManager();
        return em.find(Session.class, id);
    }

    public static void persist(Session instance) {
        EntityManager em = entityManager();
        em.persist(instance);
        em.refresh(instance);
    }

    public static void remove(Session instance) {
        EntityManager em = entityManager();
        Session attached = em.find(Session.class, instance.getId());
        em.remove(attached);
    }

    /**
     * Es el encargado de iniciar una sesion de surf del surfero.
     * 
     * @param surfer
     *            surfero al que se quiere crear una nueva sesion
     * @param spot
     *            playa donde se quiere iniciar la sesion del surfero
     * @throws RuntimeException
     *             Si surfer esta actualmente en un spot, es decir, tiene una sesion activa
     */
    static Session beginSession(Surfer surfer, Spot spot) {
        if (hasActiveSession(surfer)) {
            throw new RuntimeException("No puede iniciar una sesion mintras haya otra activa");
        }

        // Create session and add data
        Session session = new Session();
        session.setSurferId(surfer.getId());
        session.setSpotId(spot.getId());
        session.setStart(new Date());
        persist(session);

        // Update spot data
        SpotService.addSurfer(spot, surfer);

        // Update surfer data
        surfer.setActiveSessionId(session.getId());

        return session;
    }

    /**
     * Es el encargado de finalizar una sesion de surf del surfero.
     * 
     * @param surfer
     *            surfero al que se quiere finalizar su sesion actual
     * @throws RuntimeException
     *             si surfer no esta actualmente en un spot, es decir, no tiene sesion activa
     */
    static Session endSession(Surfer surfer) {
        if (!hasActiveSession(surfer)) {
            throw new RuntimeException("No puede finalizar una sesion si no hay ninguna activa");
        }

        // Find session and finalize
        Session session = findActiveSession(surfer);
        session.setEnd(new Date());

        // Update spot data
        SpotService.removeSurfer(session.getSpot(), surfer);

        // Update surfer data
        surfer.setActiveSessionId(null);

        return session;
    }

    /**
     * Busca la sesion activa si existiese
     * 
     * @param surfer
     *            surfero del que se busca su sesion activa
     * @return la sesion activa actual del surfero o <code>null</code> si no hay session activa
     */
    public static Session findActiveSession(Surfer surfer) {
        if (surfer.getActiveSessionId() == null) {
            return null;
        }
        return SessionService.findSession(surfer.getActiveSessionId());
    }

    /**
     * Comprueba si existe una sesion activa para un surfero
     * 
     * @param surfer
     *            surfero al que se quiere comprobar si tiene sesion activa
     * @return <code>true</code> si el surfero tiene sesion activa, <code>false</code> en caso
     *         contrario
     */
    public static boolean hasActiveSession(Surfer surfer) {
        return findActiveSession(surfer) != null;
    }

    public static List<Session> findSessionsBySurfer(Surfer surfer) {
        EntityManager em = entityManager();
        Query query = em.createQuery(SELECT_SESSIONS_BY_SURFER);
        query.setParameter("surfer", surfer);
        @SuppressWarnings("unchecked")
        List<Session> resultList = query.getResultList();
        resultList.size(); // force it to materialize
        return resultList;
    }

}
