package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Test the RouteManager
 */
public class RouteManagerTest {

    @Before
    public void setup() {
        RouteManager.getInstance().clearRoutes();
    }

    @Test
    public void testBoring() {
        Route r43 = new Route("043");
        Route r = RouteManager.getInstance().getRouteWithNumber("043");
        assertEquals(r43, r);
    }

    @Test
    public void testConstructor(){
        assertEquals(0,RouteManager.getInstance().getNumRoutes());
    }


    @Test
    public void testGetRouteWithNumberOneField(){
        assertEquals(new Route("25"),RouteManager.getInstance().getRouteWithNumber("25"));
    }


    @Test
    public void testGetRouteWithNumberMultipleFields(){
        Route expected = new Route("25");
        expected.setName("Route 25");
        assertEquals(expected,RouteManager.getInstance().getRouteWithNumber("25","Route 25"));
    }
    @Test
    public void testGetRouteWithNumberValid() {
        Route r = RouteManager.getInstance().getRouteWithNumber("480");
        assertTrue(r.getName().equals(""));
        assertTrue(r.getNumber().equals("480"));
    }


}
