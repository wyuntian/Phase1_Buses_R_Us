package ca.ubc.cs.cpsc210.translink.tests.model;


import ca.ubc.cs.cpsc210.translink.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RouteTest {
    Route route;
    List<Stop> testStops;
    List<RoutePattern> testRoutePatterns;


    @Before
    public void setup(){
        RouteManager.getInstance().clearRoutes();
        route = RouteManager.getInstance().getRouteWithNumber("480");
        testStops = new ArrayList<>();
        testRoutePatterns = new ArrayList<>();
    }

    @Test
    public void testConstructor(){
        assertEquals("480",route.getNumber());
        assertEquals("",route.getName());
        assertEquals(0,route.getPatterns().size());
        assertEquals(0,route.getStops().size());
    }




}
