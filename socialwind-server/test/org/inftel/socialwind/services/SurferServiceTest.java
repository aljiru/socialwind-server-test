package org.inftel.socialwind.services;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import static org.inftel.socialwind.server.domain.ThreadLocalEntityManager.requestBegin;
import static org.inftel.socialwind.server.domain.ThreadLocalEntityManager.requestFinalize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.inftel.socialwind.server.domain.Session;
import org.inftel.socialwind.server.domain.Spot;
import org.inftel.socialwind.server.domain.Surfer;
import org.inftel.socialwind.server.domain.ThreadLocalEntityManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;

public class SurferServiceTest {

    /** Helper para AppEngine Local Services **/
    static LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig());

    @BeforeClass
    public static void setUpClass() throws Exception {
        helper.setUp();
        ThreadLocalEntityManager.initialize();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        ThreadLocalEntityManager.destroy();
        helper.tearDown();
    }
    
    @Before
    public void setUp() throws Exception {
        ThreadLocalEntityManager.requestBegin();
    }

    @After
    public void tearDown() throws Exception {
        ThreadLocalEntityManager.requestFinalize();
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
        // fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testFindSurfer() {
        // fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testFindSurferEntries() {
        // fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testPersist() {
        // fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testRemove() {
        // fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testUpdateSurferLocationBeginSession() {
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
        
        requestFinalize();
        requestBegin();
        
        surfer = SurferService.findSurfer(surfer.getId());
        assertFalse(SessionService.hasActiveSession(surfer));

        // Actualizar la posición debe
        // * Cambiar la posicion del surfero
        // * Añadirlo a la playa malaga
        // * Actualizar los contadores de playa
        Session session = SurferService.updateSurferLocation(surfer, latitude, longitude);

        requestFinalize();
        requestBegin();
        
        surfer = SurferService.findSurfer(surfer.getId());
        assertTrue(SessionService.hasActiveSession(surfer));
        assertNotNull(session);

        assertEquals(1, session.getSpot().getSurferCount().intValue());
        assertEquals(1, session.getSpot().getSurferCurrentCount().intValue());
    }

    @Test
    public final void testUpdateSurferLocationEndSession() {

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
        
        requestFinalize();
        requestBegin();

        surfer = SurferService.findSurfer(surfer.getId());
        Session session = SurferService.updateSurferLocation(surfer, latitude, longitude);

        // Playamar
        double playamarLatitude = 36.6338;
        double playamarLongitude = -4.4859;

        requestFinalize();
        requestBegin();
        
        surfer = SurferService.findSurfer(surfer.getId());
        session = SessionService.findSession(session.getId());
        assertTrue(SessionService.hasActiveSession(surfer));

        // Datos para comparar
        spot = session.getSpot(); // Esto recarga los datos del spot
        Integer surferCount = spot.getSurferCount();
        Integer surferCurrentCount = spot.getSurferCurrentCount();

        // Actualizar la posicion fuera de la playa implica
        // * Cambiar la posicion del surfero
        // * Finalizar la sesion, fecha de fin
        // * Eliminar la sesion activa del surfero
        // * Actualizar contador de surferos en la playa anterior
        SurferService.updateSurferLocation(surfer, playamarLatitude, playamarLongitude);
        
        requestFinalize();
        requestBegin();

        // Recarga de datos
        session = SessionService.findSession(session.getId());
        surfer = session.getSurfer();
        spot = session.getSpot();

        // Comprobacions
        assertEquals(playamarLatitude, surfer.getLatitude(), 0.0001);
        assertEquals(playamarLongitude, surfer.getLongitude(), 0.0001);
        assertNotNull(session.getEnd());
        assertTrue(session.getStart().before(session.getEnd()));
        assertFalse(SessionService.hasActiveSession(surfer));
        assertEquals(surferCount, spot.getSurferCount());
        assertEquals(surferCurrentCount - 1, spot.getSurferCurrentCount().intValue());
    }
}
