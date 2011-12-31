package org.inftel.socialwind.shared.service;

import com.google.web.bindery.requestfactory.shared.RequestFactory;

/**
 * Fachada principal de la aplicación.
 * 
 * @author ibaca
 * 
 */
public interface SocialwindRequestFactory extends RequestFactory {
    
    SurferRequest surferRequest();

}
