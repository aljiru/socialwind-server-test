package org.inftel.socialwind.shared.service;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

import org.inftel.socialwind.services.SurferService;
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
    
    //Request<Void> updateSurferLocation(double latitude, double longitude);
}
