package org.inftel.socialwind.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;

import org.inftel.socialwind.server.domain.EMF;
import org.inftel.socialwind.server.domain.JpaEntity;

import javax.persistence.EntityManager;

/**
 * Servicio de utilidad utilizado por RequestFactory que sirve para obtener las entidades
 * persistentes.<br>
 * 
 * Se ha usado una versión generica para todas las entidades que hereden de {@link JpaEntity} ya que
 * el gestor JPA permite realizar de forma sencilla la generalización.<br>
 * 
 * El siguiente enlace contiene una post con un ejemplo basico de localizador JPA generico
 * http://www.devsniper.com/generic-entity-locator-for-request-factory-in-gwt/
 * 
 * @author ibaca
 * 
 */
public class JpaEntityLocator extends Locator<JpaEntity, Long> {

    public static final EntityManager entityManager() {
        return EMF.get().createEntityManager();
    }

    @Override
    public JpaEntity create(Class<? extends JpaEntity> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JpaEntity find(Class<? extends JpaEntity> clazz, Long id) {
        EntityManager em = entityManager();
        try {
            return em.find(clazz, id);
        } finally {
            em.close();
        }
    }

    // https://groups.google.com/forum/#!topic/google-web-toolkit/SGMsIBaJ4hI
    public Class<JpaEntity> getDomainType() {
        // nunca se llama! usado API antiguo...
        // throw new UnsupportedOperationException();
        return null;
    }

    @Override
    public Long getId(JpaEntity domainObject) {
        return domainObject.getId();
    }

    @Override
    public Class<Long> getIdType() {
        return Long.class;
    }

    @Override
    public Object getVersion(JpaEntity domainObject) {
        return domainObject.getVersion();
    }
    
}
