package org.inftel.socialwind.shared.domain;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import org.inftel.socialwind.server.domain.Surfer;
import org.inftel.socialwind.server.locator.JpaEntityLocator;

/**
 * 
 * @author ibaca
 * 
 */
@ProxyFor(value = Surfer.class, locator = JpaEntityLocator.class)
public interface SurferProxy extends EntityProxy {

    EntityProxyId<SurferProxy> stableId();

    String getUserName();

    void setUserName(String userName);

    String getDisplayName();

    void setDisplayName(String displayName);

    String getEmail();

    void setEmail(String email);

}
