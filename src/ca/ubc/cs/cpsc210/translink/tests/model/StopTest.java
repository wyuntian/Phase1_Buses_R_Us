package ca.ubc.cs.cpsc210.translink.tests.model;


import ca.ubc.cs.cpsc210.translink.model.*;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class StopTest {
    private Stop stop;
    private List<Arrival> arrivals;
    private Set<Route> routes;
    private LatLon location = new LatLon(0,0);

    // for testing

    @Before
    public void setup(){
        StopManager.getInstance().clearStops();
        RouteManager.getInstance().clearRoutes();
        stop = StopManager.getInstance().getStopWithNumber(50001, "UBC", location);
        arrivals = new ArrayList<>();
        routes = new HashSet<>();
    }

    @Test
    public void testConstructor() {
        assertTrue(stop.getName().equals("UBC"));
        assertEquals(50001, stop.getNumber());
        assertEquals(location, stop.getLocn());
    }


}