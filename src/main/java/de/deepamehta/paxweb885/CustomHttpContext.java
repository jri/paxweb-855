package de.deepamehta.paxweb885;

import org.osgi.framework.Bundle;
import org.osgi.service.http.HttpContext;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;



class CustomHttpContext implements HttpContext {

    private Logger logger = Logger.getLogger(getClass().getName());

    private Bundle bundle;

    CustomHttpContext(Bundle bundle) {
        this.bundle = bundle;
    }

    // ---

    @Override
    public URL getResource(String name) {
        return bundle.getResource(name);
    }

    @Override
    public String getMimeType(String name) {
        return null;
    }

    @Override
    public boolean handleSecurity(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        //
        String info = "\n  Request=" + request.getClass().getName() + "\n  Cookies:\n";
        for (Cookie cookie : request.getCookies()) {
            info += "    " + cookie.getName() + "=" + cookie.getValue() + "\n";
        }
        info += "\n  Session=" + session + "\n";
        logger.info(info);
        //
        boolean success = session != null;
        logger.info("#### Test " + (success ? "successful!" : "failed!"));
        return success;
    }
}
