package ca.ubc.cs.cpsc210.translink.tests.parsers;

import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.parsers.RouteParser;
import ca.ubc.cs.cpsc210.translink.parsers.exception.RouteDataMissingException;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the RouteParser
 */
// TODO: Write more tests

public class RouteParserTest {
    @Before
    public void setup() {
        RouteManager.getInstance().clearRoutes();
    }

    @Test
    public void testRouteParserNormal() throws RouteDataMissingException, JSONException, IOException {
        RouteParser p = new RouteParser("allroutes.json");
        p.parse();
        assertEquals(229, RouteManager.getInstance().getNumRoutes());
    }

    @Test(expected = RouteDataMissingException.class)
    public void testRouteParserMissingRouteNo() throws RouteDataMissingException, JSONException, IOException{
        RouteParser p = new RouteParser("allroutesMissingFields.json");
        p.parse();
    }

    @Test
    public void testRouteParserGetAllRight() throws  JSONException, IOException{
        try {
            RouteParser p = new RouteParser("allroutesMissingFields.json");
            p.parse();
        }catch (RouteDataMissingException e){
            System.out.println("missing fields");
        }

        assertEquals(1, RouteManager.getInstance().getNumRoutes());
    }

    @Test(expected = RouteDataMissingException.class)
    public void testRouteParserMissingPattern() throws RouteDataMissingException, JSONException, IOException{
        RouteParser p = new RouteParser("allroutesMissingPatterns.json");
        p.parse();
    }

    @Test
    public void testRouteParserMissingPatternButGetAllRight() throws  JSONException, IOException{
        try {
            RouteParser p = new RouteParser("allroutesMissingPatterns.json");
            p.parse();
        }catch (RouteDataMissingException e){
            System.out.println("missing fields");
        }

        assertEquals(1, RouteManager.getInstance().getNumRoutes());
    }

    @Test(expected = RouteDataMissingException.class)
    public void testRouteParserMissingOnePatternField() throws RouteDataMissingException, JSONException, IOException{
        RouteParser p = new RouteParser("allroutesMissingFieldOfOnePatter.json");
        p.parse();
    }

    @Test
    public void testRouteParserMissingOnePatternFieldGetAllRight() throws JSONException, IOException{
        try {
            RouteParser q = new RouteParser("allroutesMissingFieldOfOnePatter.json");
            q.parse();
        }catch (RouteDataMissingException e){
            System.out.println("missing fileds of pattern(s)");
        }
        assertEquals(2, RouteManager.getInstance().getNumRoutes());
    }

    @Test(expected = RouteDataMissingException.class)
    public void testRouteParserMissingTwoPatternField() throws RouteDataMissingException, JSONException, IOException{
        RouteParser p = new RouteParser("allroutesMissingFieldOfTwoPatter.json");
        p.parse();
    }

    @Test
    public void testRouteParserMissingTwoPatternFieldGetAllRight() throws JSONException, IOException{
        try {
            RouteParser q = new RouteParser("allroutesMissingFieldOfTwoPatter.json");
            q.parse();
        }catch (RouteDataMissingException e){
            System.out.println("missing fileds of pattern(s)");
        }
        assertEquals(4, RouteManager.getInstance().getNumRoutes());
    }


}
