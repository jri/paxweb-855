package de.deepamehta.paxweb885;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;

import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;

import java.util.logging.Level;
import java.util.logging.Logger;



public class TestActivator implements BundleActivator {

    private Logger logger = Logger.getLogger(getClass().getName());

    private HttpService httpService;
    private ServiceTracker httpServiceTracker;

    @Override
    public void start(BundleContext context) {
        httpServiceTracker = new ServiceTracker(context, HttpService.class, null) {

            @Override
            public Object addingService(ServiceReference serviceRef) {
                httpService = (HttpService) super.addingService(serviceRef);
                //
                registerResources(context.getBundle());
                registerJerseyServlet();
                //
                logger.info("##### Test is ready! Now go to http://localhost:8080/");
                return httpService;
            }

            @Override
            public void removedService(ServiceReference ref, Object service) {
                super.removedService(ref, service);
                httpService = null;
            }
        };
        httpServiceTracker.open();
    }

    @Override
    public void stop(BundleContext context) {
        httpServiceTracker.close();
    }

    // ---

    private void registerResources(Bundle bundle) {
        try {
            httpService.registerResources("/images", "/", new CustomHttpContext(bundle));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Registering resources failed", e);
        }
    }

    private void registerJerseyServlet() {
        try {
            ResourceConfig app = new DefaultResourceConfig();
            app.getSingletons().add(new RootResource());
            //
            httpService.registerServlet("/", new ServletContainer(app), null, null);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Registering Jersey servlet failed", e);
        }
    }
}
