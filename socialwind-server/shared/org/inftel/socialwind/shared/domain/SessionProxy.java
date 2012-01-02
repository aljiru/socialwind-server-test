package org.inftel.socialwind.shared.domain;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import org.inftel.socialwind.server.domain.Session;
import org.inftel.socialwind.server.locator.JpaEntityLocator;

import java.util.Date;

/**
 * 
 * @author ibaca
 * 
 */
@ProxyFor(value = Session.class, locator = JpaEntityLocator.class)
public interface SessionProxy extends EntityProxy {

    EntityProxyId<SessionProxy> stableId();

    Date getStart();
    
    Date getEnd();
    
    String getSurferName();
    
    SpotProxy getSpot();

}
