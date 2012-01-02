package org.inftel.socialwind.services;

import org.inftel.socialwind.server.domain.EMF;
import org.inftel.socialwind.server.domain.Session;
import org.inftel.socialwind.server.domain.Spot;
import org.inftel.socialwind.server.domain.Surfer;

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
        return EMF.get().createEntityManager();
    }

    public static List<Session> findAllSessions() {
        EntityManager em = entityManager();
        try {
            @SuppressWarnings("unchecked")
            List<Session> SessionList = em.createQuery(SELECT_ALL).getResultList();
            SessionList.size(); // forzar materializar resultados
            return SessionList;
        } finally {
            em.close();
        }
    }

    public static Session findSession(Long id) {
        if (id == null) {
            return null;
        }
        EntityManager em = entityManager();
        try {
            return em.find(Session.class, id);
        } finally {
            em.close();
        }
    }

    public static void persist(Session instance) {
        EntityManager em = entityManager();
        try {
            em.persist(instance);
        } finally {
            em.close();
        }
    }

    public static void remove(Session instance) {
        EntityManager em = entityManager();
        try {
            Session attached = em.find(Session.class, instance.getId());
            em.remove(attached);
        } finally {
            em.close();
        }
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
        session.setSurfer(surfer);
        session.setSpot(spot);
        session.setStart(new Date());
        persist(session);

        // Update spot data
        SpotService.addSurfer(spot, surfer);

        // Update surfer data
        surfer.setActiveSession(session);
        
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
        persist(session);

        // Update spot data
        SpotService.removeSurfer(session.getSpot(), surfer);

        // Update surfer data
        surfer.setActiveSession(null);
        
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
        EntityManager em = entityManager();
        try {
            // re-attach para asegurar poder cargar la sesion
            Surfer attached = em.find(Surfer.class, surfer.getId());
            return attached.getActiveSession();
        } finally {
            em.close();
        }
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
        try {
            Query query = em.createQuery(SELECT_SESSIONS_BY_SURFER);
            query.setParameter("surfer", surfer);
            @SuppressWarnings("unchecked")
            List<Session> resultList = query.getResultList();
            resultList.size(); // force it to materialize
            return resultList;
        } finally {
            em.close();
        }
    }

}
