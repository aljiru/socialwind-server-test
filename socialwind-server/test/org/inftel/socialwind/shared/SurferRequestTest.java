package org.inftel.socialwind.shared;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.web.bindery.requestfactory.server.ServiceLayer;
import com.google.web.bindery.requestfactory.server.SimpleRequestProcessor;
import com.google.web.bindery.requestfactory.server.testing.InProcessRequestTransport;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.vm.RequestFactorySource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.inftel.socialwind.RequestFactoryHelper;
import org.inftel.socialwind.server.domain.ThreadLocalEntityManager;
import org.inftel.socialwind.shared.domain.SurferProxy;
import org.inftel.socialwind.shared.service.SocialwindRequestFactory;
import org.inftel.socialwind.shared.service.SurferRequest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

/**
 * 
 * 
 * Para realizar una simulacion del back-end de persistencia esta guía es un buen punto de partida
 * http://cleancodematters.wordpress.com/2011/06/18/tutorial-gwt-request-factory-%E2%80%93-part-ii/
 * 
 * @author ibaca
 * 
 */
public class SurferRequestTest {

    private static SocialwindRequestFactory factory;
    private static SurferRequest service;

    /** Helper para AppEngine Local Services **/
    static LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig());

    @BeforeClass
    public static void setUpClass() throws Exception {
        helper.setUp();
        ThreadLocalEntityManager.initialize();

        // Factoría y servicios con back-end simulado
        factory = RequestFactoryHelper.create(SocialwindRequestFactory.class);
        service = RequestFactoryHelper.getService(SurferRequest.class);
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

    public static SocialwindRequestFactory get() {
        ServiceLayer serviceLayer = ServiceLayer.create();
        SimpleRequestProcessor requestProcessor = new SimpleRequestProcessor(serviceLayer);
        EventBus eventBus = new SimpleEventBus();
        SocialwindRequestFactory swrf = RequestFactorySource.create(SocialwindRequestFactory.class);
        swrf.initialize(eventBus, new InProcessRequestTransport(requestProcessor));
        return swrf;
    }

    @Test
    public void testSurferRequest() {
        SocialwindRequestFactory swrf = get();
        SurferRequest request = swrf.surferRequest();
        final SurferProxy surfer = request.create(SurferProxy.class);
        surfer.setDisplayName("ibaca");

        // Intento de guardar entidad
        request.persist(surfer).fire(new Receiver<Void>() {
            @Override
            public void onSuccess(Void response) {
            }

            @Override
            public void onFailure(ServerFailure error) {
                fail(error.getMessage());
            }
        });

        request = swrf.surferRequest();

        // Intento de buscar la entidad guardada
        request.find(surfer.stableId()).fire(new Receiver<SurferProxy>() {
            @Override
            public void onSuccess(SurferProxy response) {
                assertNotNull(response);
                assertEquals(surfer.getDisplayName(), response.getDisplayName());
            }
        });

        request = swrf.surferRequest();

        // Intento de buscar todos los surfers del sistema
        request.findAllSurfers().fire(new Receiver<List<SurferProxy>>() {
            @Override
            public void onSuccess(List<SurferProxy> response) {
                assertTrue(response.size() > 0);
            }
        });

    }

    /**
     * TODO Intneto de sumulacion del back-end
     */
    @Test
    public void testCountSurfers() {
        // Crear resultados del back-end
        // Long count = Long.valueOf(42);
        // Instrumentar la simulacion del back-end
        // Mockito.when(service.countSurfers()).thenReturn(count);
    }

}
