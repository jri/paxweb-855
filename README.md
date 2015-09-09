
PAXWEB-855 test application
===========================

A simple test application to illustrate issue PAXWEB-855.

A session created in a Jersey resource method is not available in the `handleSecurity()` method of a Pax Web `HttpContext`.

<https://ops4j1.jira.com/browse/PAXWEB-885>


Running the test
----------------

1. Build:

        git clone git://github.com/jri/paxweb-855.git
        cd paxweb-855
        mvn clean install

2. Run:

        mvn pax:run

  This provisions an Apache Felix container with the test aplication.  
  Finally you'll see:

        INFO: ##### Test is ready! Now go to http://localhost:8080/

3. Open <http://localhost:8080/> in a browser.

  A session is created.  
  In the browser you'll see the session ID and a link.

4. Click the link.

  If an image appears the test is successful.  
  If a `401 Unauthorized` error page appears the test has failed.


How the test works
------------------

The main class, `TestActivator`, registers the static resources (the image file) and the Jersey servlet once the `HttpService` arrives.

The JAX-RS application contains one root resource class, `RootResource`, which handles the initial request,  <http://localhost:8080/>.

The Jersey servlet is registered via `HttpService`'s `registerServlet()` method.

The static resources are registered via `HttpService`'s `registerResources()` method.

Registering the static resources involves a custom `HttpContext` (class `CustomHttpContext`). In its `handleSecurity()` method the custom HttpContext logs the request (that accesses the image file), its cookies, and the associated session. If no session (`null`) is associated with the request `handleSecurity()` returns `false` which results in the 401 error page.

In the log you can see that the session is `null` although the proper session ID is contained in the requests's `JSESSIONID` cookie.

You can access <http://localhost:8080/> again and see that the session still exists.

The test involves these components:

* Pax Web 3.2.4
* Apache Felix 4.4.1
* Jersey 1.19

Notes
-----

The problem does not occur when Jersey is not involved, that is when creating the session in the static resource's `handleSecurity()` method.
