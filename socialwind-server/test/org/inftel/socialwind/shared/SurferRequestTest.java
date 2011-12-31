package org.inftel.socialwind.shared;

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.web.bindery.requestfactory.server.ServiceLayer;
import com.google.web.bindery.requestfactory.server.SimpleRequestProcessor;
import com.google.web.bindery.requestfactory.server.testing.InProcessRequestTransport;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.vm.RequestFactorySource;

import org.inftel.socialwind.LocalDataStoreTestCase;
import org.inftel.socialwind.shared.domain.SurferProxy;
import org.inftel.socialwind.shared.service.SocialwindRequestFactory;
import org.inftel.socialwind.shared.service.SurferRequest;

import java.util.List;
import java.util.Vector;

public class SurferRequestTest extends LocalDataStoreTestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public static SocialwindRequestFactory get() {
        ServiceLayer serviceLayer = ServiceLayer.create();
        SimpleRequestProcessor requestProcessor = new SimpleRequestProcessor(serviceLayer);
        EventBus eventBus = new SimpleEventBus();
        SocialwindRequestFactory swrf = RequestFactorySource.create(SocialwindRequestFactory.class);
        swrf.initialize(eventBus, new InProcessRequestTransport(requestProcessor));
        return swrf;
    }

    public void testSurferRequest() {
        SocialwindRequestFactory swrf = get();
        SurferRequest request = swrf.surferRequest();
        SurferProxy surfer = request.create(SurferProxy.class);
        surfer.setEmail("test@email.com");
        
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
        final List<SurferProxy> findedList = new Vector<SurferProxy>(1);
        
        // Intento de buscar la entidad guardada
        request.find(surfer.stableId()).fire(new Receiver<SurferProxy>() {
            @Override
            public void onSuccess(SurferProxy response) {
                findedList.add(response);
            }
        });
        
        request = swrf.surferRequest();
        
        // Intento de buscar todos los surfers del sistema
        request.findAllSurfers().fire(new Receiver<List<SurferProxy>>() {
            @Override
            public void onSuccess(List<SurferProxy> response) {
                Object o = response;
            }
        });
        
        assertTrue(findedList.size() > 0);
        assertEquals(surfer.getEmail(), findedList.get(0).getEmail());
    }

}
