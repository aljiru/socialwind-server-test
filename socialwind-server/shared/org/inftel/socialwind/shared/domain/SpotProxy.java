package org.inftel.socialwind.shared.domain;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import org.inftel.socialwind.server.domain.Spot;
import org.inftel.socialwind.server.locator.JpaEntityLocator;

/**
 * 
 * @author ibaca
 * 
 */
@ProxyFor(value = Spot.class, locator = JpaEntityLocator.class)
public interface SpotProxy extends EntityProxy {

    EntityProxyId<SpotProxy> stableId();

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    String getImgUrl();

    void setImgUrl(String imgUrl);

}
