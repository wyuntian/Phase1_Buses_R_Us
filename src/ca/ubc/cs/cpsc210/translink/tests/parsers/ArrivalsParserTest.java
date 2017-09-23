package ca.ubc.cs.cpsc210.translink.tests.parsers;

import ca.ubc.cs.cpsc210.translink.model.Arrival;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.StopManager;
import ca.ubc.cs.cpsc210.translink.parsers.ArrivalsParser;
import ca.ubc.cs.cpsc210.translink.parsers.exception.ArrivalsDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test the ArrivalsParser
 */
public class ArrivalsParserTest {

    // for 43JSON file
    Stop testStop = StopManager.getInstance().getStopWithNumber(50001);
    ArrivalsParser ap;

    // for arrivalsJSON
    Stop testStop2 = StopManager.getInstance().getStopWithNumber(50002);
    ArrivalsParser ap2;

    String json43Arrivals = "./data/arrivals43.json";
    String jsonArrivals = "./data/arrivals.json";

    /**
     * Parse JSON library file
     */
    private void parse(ArrivalsParser ap, Stop stop, String jsonLocation) throws JSONException, ArrivalsDataMissingException {
        try {
            InputStream is = new FileInputStream(jsonLocation);
            String jsonData =  readSource(is);
            ap.parseArrivals(stop, jsonData);
        } catch (IOException e) {
            System.out.println("Error reading file...");
            e.printStackTrace();
        }
    }

    /**
     * Read source data from input stream as string
     *
     * @param is  input stream connected to source data
     * @return  source data as string
     * @throws IOException  when error occurs reading data from file
     */
    private String readSource(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;

        while((line = br.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }

        br.close();

        return sb.toString();
    }

    @Before
    public void setUp() throws JSONException, ArrivalsDataMissingException {


    }

    @Test
    public void testArrivalsParserNormal() throws JSONException, ArrivalsDataMissingException {
        Stop s = StopManager.getInstance().getStopWithNumber(51479);
        s.clearArrivals();
        String data = "";
        try {
            data = new FileDataProvider("arrivals43.json").dataSourceToString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't read the arrivals data");
        }
        ArrivalsParser.parseArrivals(s, data);
        int count = 0;
        for (Arrival a : s) {
            assertTrue(a.getTimeToStopInMins() <= 120);
            count++;
        }
        assertEquals(6, count);
    }

    @Test
    public void testArrivalsParserFull() throws JSONException, ArrivalsDataMissingException {
        Stop s = StopManager.getInstance().getStopWithNumber(51479);
        s.clearArrivals();
        String data = "";
        try {
            data = new FileDataProvider("arrivals.json").dataSourceToString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't read the arrivals data");
        }
        ArrivalsParser.parseArrivals(s, data);
        int count = 0;
        for (Arrival a : s) {
            assertTrue(a.getTimeToStopInMins() <= 120);
            count++;
        }
        assertEquals(40, count);
    }

    @Test(expected = ArrivalsDataMissingException.class)
    public void testArrivalsParserMissingFields() throws ArrivalsDataMissingException, JSONException {
        Stop s = StopManager.getInstance().getStopWithNumber(51479);
        s.clearArrivals();
        String data = "";
        try {
            data = new FileDataProvider("arrivalsMissingField.json").dataSourceToString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't read the arrivals data");
        }
        ArrivalsParser.parseArrivals(s, data);
    }

    @Test
    public void testArrivalsGetAllREQURED() throws JSONException {
        Stop s = StopManager.getInstance().getStopWithNumber(51479);
        s.clearArrivals();
        String data = "";
        try {
            data = new FileDataProvider("arrivalsMissingField.json").dataSourceToString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't read the arrivals data");
        }
        try {
            ArrivalsParser.parseArrivals(s, data);
        } catch (ArrivalsDataMissingException e) {
            System.out.println("Missing Fields");
        }
            int count = 0;
            for (Arrival a : s) {
                assertTrue(a.getTimeToStopInMins() <= 120);
                count++;
            }
            assertEquals(4, count);
        }

}

