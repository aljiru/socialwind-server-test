package org.inftel.socialwind.shared.domain;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

import org.inftel.socialwind.server.domain.Location;

@ProxyFor(value = Location.class)
public interface LocationProxy extends ValueProxy {
    
    double getLatitude();
    
    void setLatitude(double latitude);
    
    double getLongitude();
    
    void setLongitude(double longitude);

}
