package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test the RouteManager
 */
public class RoutePatternTest {

    RoutePattern rp;
    Route r;
    List<LatLon> path = new ArrayList<>(); // is there a better data structure for this?

    @Before
    public void setup() {
        RouteManager.getInstance().clearRoutes();
        r = RouteManager.getInstance().getRouteWithNumber("043");
        rp = new RoutePattern("RP1", "Home", "WEST", r);
    }


    @Test
    public void testConstructor(){
        assertEquals("RP1", rp.getName());
        assertEquals("Home", rp.getDestination());
        assertEquals("WEST", rp.getDirection());
        assertEquals(new LinkedList<LatLon>(),rp.getPath());
    }

    @Test
    public void testAddPatternOne() {
        assertTrue(r.getPatterns().contains(rp));
    }

    @Test
    public void testAddPatternOneNoDuplicates() {
        RoutePattern rp2 = new RoutePattern("RP1", "Work", "WEST", r);
        assertTrue(r.getPatterns().contains(rp2));
        assertEquals(1, r.getPatterns().size());
    }

}

