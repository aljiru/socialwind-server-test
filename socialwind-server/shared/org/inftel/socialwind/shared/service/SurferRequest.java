package org.inftel.socialwind.shared.service;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

import org.inftel.socialwind.services.SurferService;
import org.inftel.socialwind.shared.domain.SessionProxy;
import org.inftel.socialwind.shared.domain.SurferProxy;

import java.util.List;

@Service(SurferService.class)
public interface SurferRequest extends RequestContext {

    Request<Long> countSurfers();

    Request<List<SurferProxy>> findAllSurfers();

    Request<SurferProxy> findSurfer(Long id);

    Request<List<SurferProxy>> findSurferEntries(int firstResult, int maxResults);

    Request<Void> persist(SurferProxy surfer);

    Request<Void> remove(SurferProxy surfer);

    /**
     * Actualiza la posición del surfero, en caso de crear una nueva sesion debido a que el surfero
     * se haya movido a una playa, se devolverá la nueva sesion creada. En caso de que el surfero
     * este en una playa, se devolvera nulo tambien.
     * 
     * @param surfer
     * @param latitude
     * @param longitude
     * @return la nueva sesion creada, null en caso de no haber creaado una nueva sesion
     */
    Request<SessionProxy> updateSurferLocation(SurferProxy surfer, double latitude, double longitude);

    Request<List<SessionProxy>> sessions(SurferProxy surfer);

}
