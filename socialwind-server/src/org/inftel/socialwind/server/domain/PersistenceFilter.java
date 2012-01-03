package org.inftel.socialwind.server.domain;

import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Gestiona el {@link EntityManager} asociado a las peticiones {@link RequestFactoryServlet}.
 * 
 * @author ibaca
 * 
 * @see ThreadLocalEntityManager
 * 
 */
public class PersistenceFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ThreadLocalEntityManager.initialize();
    }

    @Override
    public void destroy() {
        ThreadLocalEntityManager.destroy();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        ThreadLocalEntityManager.requestBegin();
        try {
            chain.doFilter(req, res);
            ThreadLocalEntityManager.requestSuccess();
        } catch (Exception e) {
            ThreadLocalEntityManager.requestFail();

        } finally {
            ThreadLocalEntityManager.requestFinalize();
        }

    }
}