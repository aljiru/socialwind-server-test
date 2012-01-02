package org.inftel.socialwind.services;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import static org.junit.Assert.*;

import org.inftel.socialwind.server.domain.Session;
import org.inftel.socialwind.server.domain.Spot;
import org.inftel.socialwind.server.domain.Surfer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class SurferServiceTest {

    /** Helper para AppEngine Local Services **/
    LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig());

    @Before
    public void setUp() throws Exception {
        helper.setUp();
    }

    @After
    public void tearDown() throws Exception {
        helper.tearDown();
    }

    @Test
    public final void testEntityManager() {
        assertNotNull(SurferService.entityManager());
    }

    @Test
    public final void testCountSurfers() {
        Integer expected = SurferService.findAllSurfers().size();
        Integer actual = SurferService.countSurfers().intValue();
        assertEquals(expected, actual);
    }

    @Test
    public final void testFindAllSurfers() {
        //fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testFindSurfer() {
        //fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testFindSurferEntries() {
        //fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testPersist() {
        //fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testRemove() {
        //fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testUpdateSurferLocation() {
        
        String logFile = System.getProperty("java.util.logging.config.file");
        
        
        Surfer surfer = new Surfer();
        SurferService.persist(surfer);

        // Malaga
        Double latitude = 36.723;
        Double longitude = -4.415;

        Spot spot = new Spot();
        spot.setName("malaga");
        spot.setLatitude(latitude);
        spot.setLongitude(longitude);
        SpotService.persist(spot);

        assertFalse(SessionService.hasActiveSession(surfer));

        // Actualizar la posición debe
        // * Cambiar la posicion del surfero
        // * Añadirlo a la playa malaga
        // * Actualizar los contadores de playa
        Session session = SurferService.updateSurferLocation(surfer, latitude, longitude);
        
        assertTrue(SessionService.hasActiveSession(surfer));
        assertNotNull(session);
        
        List<Spot> spots = SpotService.findAllSpots();
        
        assertEquals(1, session.getSpot().getSurferCount().intValue());
        assertEquals(1, session.getSpot().getSurferCurrentCount().intValue());
    }
}
