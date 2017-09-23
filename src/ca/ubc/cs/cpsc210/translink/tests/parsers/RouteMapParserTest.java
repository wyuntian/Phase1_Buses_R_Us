package ca.ubc.cs.cpsc210.translink.tests.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.parsers.RouteMapParser;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test the parser for route pattern map information
 */

// TODO: Write more tests

public class RouteMapParserTest {
    @Before
    public void setup() {
        RouteManager.getInstance().clearRoutes();
    }

    private int countNumRoutePatterns() {
        int count = 0;
        for (Route r : RouteManager.getInstance()) {
            for (RoutePattern rp : r.getPatterns()) {
                count ++;
            }
        }
        return count;
    }
    private int countNumLatLon(){
        int count = 0;
        for(Route r: RouteManager.getInstance()){
            for(RoutePattern rp: r.getPatterns()){
                count += rp.getPath().size();
            }
        }
        return count;
    }


    @Test
    public void testRouteParserNormal() {
        RouteMapParser p = new RouteMapParser("allroutemaps.txt");
        p.parse();
        assertEquals(1232, countNumRoutePatterns());
    }

    @Test
    public void testRouteParserDifferentDashLine() {
        RouteMapParser p = new RouteMapParser("1allroutemaps.txt");
        p.parse();
        assertEquals(3, countNumRoutePatterns());
        assertEquals(17, countNumLatLon());
    }

    @Test
    public void testRouteParserNoLatLon() {
        RouteMapParser p = new RouteMapParser("1allroutemapsNoLatLon.txt");
        p.parse();
        int pSize = RouteManager.getInstance().getRouteWithNumber("006","EB5DT-K21T").getStops().size();
        assertEquals(0,pSize);
        assertEquals(3, countNumRoutePatterns());
        assertEquals(0,countNumLatLon());
        assertEquals(0, countNumLatLon());
    }

    @Test
    public void testRouteParserOddLatLon() {
        RouteMapParser p = new RouteMapParser("allroutemapsOddLatLon.txt");
        p.parse();
        assertEquals(2, countNumRoutePatterns());
        assertEquals(6,countNumLatLon());
    }

    @Test
    public void testRouteParserEvenOrOddLatLon() {
        RouteMapParser p = new RouteMapParser("allroutemapsEvenOrOddLatLon.txt");
        p.parse();
        assertEquals(2, countNumRoutePatterns());
        assertEquals(4,countNumLatLon());
    }

    @Test
    public void testRouteParserFirstLatLon() {
        RouteMapParser p = new RouteMapParser("allroutemapsEvenOrOddLatLon.txt");
        p.parse();
        LatLon LaLo1 = new LatLon(49.21716,-122.667252);
        assertEquals(LaLo1,RouteManager.getInstance().getRouteWithNumber("C43","EB2").getPatterns().get(0).getPath());
    }

    //@Test
   // public void testRouteParserFirstLatLon() {
    //    RouteMapParser p = new RouteMapParser("allroutemapsEvenOrOddLatLon.txt");
    //    p.parse();
    //    LatLon LaLo1 = new LatLon(49.21716,-122.667252);
     //   assertEquals(LaLo1,RouteManager.getInstance().getRouteWithNumber("C43","EB2").getPatterns().get(0).getPath().get(0));
    //}





}
