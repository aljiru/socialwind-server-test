package org.inftel.socialwind.shared.service;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

import org.inftel.socialwind.services.SpotService;
import org.inftel.socialwind.shared.domain.SpotProxy;

import java.util.List;

@Service(SpotService.class)
public interface SpotRequest extends RequestContext {

    Request<Long> countSpots();

    Request<List<SpotProxy>> findAllSpots();

    Request<SpotProxy> findSpot(Long id);

    Request<List<SpotProxy>> findSpotEntries(int firstResult, int maxResults);

    Request<List<SpotProxy>> findNearbySpots(double latitude, double longitude);

    Request<List<SpotProxy>> findNearbyHotSpots(double latitude, double longitude);

    Request<Void> persist(SpotProxy surfer);

    Request<Void> remove(SpotProxy surfer);
    
    
}
