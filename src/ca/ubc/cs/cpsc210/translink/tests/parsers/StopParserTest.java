package ca.ubc.cs.cpsc210.translink.tests.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.StopManager;
import ca.ubc.cs.cpsc210.translink.parsers.StopParser;
import ca.ubc.cs.cpsc210.translink.parsers.exception.StopDataMissingException;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the StopParser
 */

// TODO: Write more tests

public class StopParserTest {
    @Before
    public void setup() {
        StopManager.getInstance().clearStops();
    }

    @Test
    public void testStopParserNormal() throws StopDataMissingException, JSONException, IOException {
        StopParser p = new StopParser("stops.json");
        p.parse();
        assertEquals(8524, StopManager.getInstance().getNumStops());
    }

    @Test
    public void testRouteOfStop() throws StopDataMissingException, JSONException, IOException{
        StopParser s = new StopParser("stops.json");
        s.parse();
        LatLon newGeo = new LatLon(49.27755,-123.12698);

        assertEquals(2,StopManager.getInstance().getStopWithNumber(50011,"EB DAVIE ST FS HOWE ST",newGeo).getRoutes().size());
        Set<Route> getRoutes = StopManager.getInstance().getStopWithNumber(50011,"EB DAVIE ST FS HOWE ST",newGeo).getRoutes();
        Stop theStop = StopManager.getInstance().getStopWithNumber(50011,"EB DAVIE ST FS HOWE ST",newGeo);
        for(Route next:getRoutes){
            assertTrue(next.hasStop(theStop));
        }

    }

    @Test
    public void testStopInCommon() throws StopDataMissingException, JSONException, IOException{
        StopParser p=new StopParser("stops.json");
        p.parse();
        assertEquals(7,StopManager.getInstance().getStopWithNumber(51479).getRoutes().size());
    }

    @Test(expected = StopDataMissingException.class)
    public void testMissingFieldException() throws StopDataMissingException, JSONException, IOException{
        StopParser p=new StopParser("missingFieldstops.json");
        p.parse();
    }

    @Test
    public void testGetAllRightData() throws JSONException, IOException{
        try {
            StopParser p = new StopParser("missingFieldstops.json");
            p.parse();
        }catch (StopDataMissingException e){
            System.out.println("exception thrown");
        }
        assertEquals(4, StopManager.getInstance().getNumStops());

    }

    @Test(expected = StopDataMissingException.class)
    public void testMissingRouteException() throws StopDataMissingException, JSONException, IOException{
        StopParser p=new StopParser("missingRoutesstops.json");
        p.parse();
    }

    @Test
    public void testGetAllRightDataMissingRoute() throws JSONException, IOException{
        try {
            StopParser p = new StopParser("missingRoutesstops.json");
            p.parse();
        }catch (StopDataMissingException e){
            System.out.println("Missing Route");
        }
        assertEquals(8, StopManager.getInstance().getNumStops());
    }
}
