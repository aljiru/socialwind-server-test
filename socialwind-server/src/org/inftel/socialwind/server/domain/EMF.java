package org.inftel.socialwind.server.domain;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Factoria para crear EntityManager de JPA.
 */
public final class EMF {
    private static final EntityManagerFactory emfInstance = Persistence
            .createEntityManagerFactory("transactions-optional");

    public static EntityManagerFactory get() {
        return emfInstance;
    }

    private EMF() {
        // no instanciable
    }
}
