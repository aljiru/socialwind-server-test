package org.inftel.socialwind.domain;

import org.inftel.socialwind.LocalDataStoreTestCase;
import org.inftel.socialwind.server.domain.EMF;
import org.inftel.socialwind.server.domain.Spot;
import org.inftel.socialwind.server.domain.Surfer;

import java.math.BigInteger;
import java.security.SecureRandom;

import javax.persistence.EntityManager;

public class PersistenceTestCase extends LocalDataStoreTestCase {

    private SecureRandom random = new SecureRandom();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSpotCreationAndFind() {
        EntityManager em = EMF.get().createEntityManager();

        // Spot Creation
        Spot createdSpot = new Spot();
        String randomName = new BigInteger(130, random).toString(32);
        createdSpot.setName(randomName);

        // Spot save
        em.persist(createdSpot);

        // Spot merge
        Spot mergedSpot = em.merge(createdSpot);

        // Spot Find
        Spot findedSpot = em.find(Spot.class, mergedSpot.getId());
        assertEquals(createdSpot.getName(), findedSpot.getName());
    }

    public void testSurferCreationAndFind() {
        EntityManager em = EMF.get().createEntityManager();

        // Spot Creation
        Surfer createdSurfer = new Surfer();
        String randomName = new BigInteger(130, random).toString(32);
        createdSurfer.setDisplayName(randomName);

        // Spot save
        em.persist(createdSurfer);

        // Spot merge
        Surfer mergedSurfer = em.merge(createdSurfer);

        // Spot Find
        Surfer findedSpot = em.find(Surfer.class, mergedSurfer.getId());
        assertEquals(createdSurfer.getDisplayName(), findedSpot.getDisplayName());
    }
    
    public void testSurferVersioning() {
        EntityManager em = EMF.get().createEntityManager();

        // Spot Creation
        Surfer surfer = new Surfer();
        String randomName = new BigInteger(130, random).toString(32);
        surfer.setDisplayName(randomName);
        surfer.setUserName("first_name");

        // First save
        em.getTransaction().begin();
        Surfer createdSurfer = em.merge(surfer);
        em.getTransaction().commit();
        
        // Second save
        em.getTransaction().begin();
        surfer.setUserName("second_name");
        Surfer mergedSurfer = em.merge(surfer);
        em.getTransaction().commit();

        // Check version diff
        assertNotSame(createdSurfer.getVersion(), mergedSurfer.getVersion());
    }
    
    public void testSurferAutoDating() {
        EntityManager em = EMF.get().createEntityManager();

        // Spot Creation
        Surfer surfer = new Surfer();
        String randomName = new BigInteger(130, random).toString(32);
        surfer.setDisplayName(randomName);
        surfer.setUserName("first_name");

        // First save
        em.getTransaction().begin();
        Surfer createdSurfer = em.merge(surfer);
        em.getTransaction().commit();
        
        // Check initial values
        assertNotNull(createdSurfer.getCreated());
        assertNotNull(createdSurfer.getUpdated());
        assertTrue(createdSurfer.getCreated().equals(createdSurfer.getUpdated()));
        
        // Second save
        em.getTransaction().begin();
        surfer.setUserName("second_name");
        Surfer updatedSurfer = em.merge(surfer);
        em.getTransaction().commit();

        // Check Updated
        assertFalse(createdSurfer.getUpdated().equals(updatedSurfer.getUpdated()));
    }

}
