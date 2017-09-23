package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.StopManager;
import ca.ubc.cs.cpsc210.translink.parsers.exception.StopDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A parser for the data returned by Translink stops query
 */
public class StopParser {

    private String filename;

    public StopParser(String filename) {
        this.filename = filename;
    }
    /**
     * Parse stop data from the file and add all stops to stop manager.
     *
     */
    public void parse() throws IOException, StopDataMissingException, JSONException{
        DataProvider dataProvider = new FileDataProvider(filename);

        parseStops(dataProvider.dataSourceToString());
    }
    /**
     * Parse stop information from JSON response produced by Translink.
     * Stores all stops and routes found in the StopManager and RouteManager.
     *
     * @param  jsonResponse    string encoding JSON data to be parsed
     * @throws JSONException when:
     * <ul>
     *     <li>JSON data does not have expected format (JSON syntax problem)</li>
     *     <li>JSON data is not an array</li>
     * </ul>
     * If a JSONException is thrown, no stops should be added to the stop manager
     * @throws StopDataMissingException when
     * <ul>
     *  <li> JSON data is missing Name, StopNo, Routes or location (Latitude or Longitude) elements for any stop</li>
     * </ul>
     * If a StopDataMissingException is thrown, all correct stops are first added to the stop manager.
     */



    private boolean shouldThrowMissingException = false;

    public void parseStops(String jsonResponse)
            throws JSONException, StopDataMissingException {
        JSONArray stopsArray = new JSONArray(jsonResponse);

        for(int i=0; i<stopsArray.length();i++) {
            JSONObject myStop = stopsArray.getJSONObject(i);

            //clearFields();
            try {
                String name = myStop.getString("Name");
                int stopNo = myStop.getInt("StopNo");
                double Lat = myStop.getDouble("Latitude");
                double Lon = myStop.getDouble("Longitude");
                LatLon geoPoint = new LatLon(Lat, Lon);
                String routesOftheStop = myStop.getString("Routes");

               // if (name == null || stopNo == 0 || Lat == 0.0 || Lon == 0.0 || routesOftheStop==null) {
                //    shouldThrowMissingException = true;
                //}
                Stop stopObject = StopManager.getInstance().getStopWithNumber(stopNo, name, geoPoint);



                List<String> routesNumbers = separateString(routesOftheStop);
                for (String next : routesNumbers) {
                    Route addRoute = RouteManager.getInstance().getRouteWithNumber(next);

                    stopObject.addRoute(addRoute);
                    //if(!addRoute.hasStop(stopObject)) {
                     //   addRoute.addStop(stopObject);  // not sure if need to add this
                    //}
                }
            }catch (JSONException e){
                shouldThrowMissingException = true;

            }
        }
        if(shouldThrowMissingException){
            throw new StopDataMissingException();
        }
            }



   // public void clearFields(){
    //    name = null;
     //   stopNo = 0;
     //   Lat = 0.0;
      //  Lon = 0.0;
     //   routesOftheStop = null;
   // }

    public List<String> separateString(String str) {
        ArrayList<String> returnList = new ArrayList<>();
        if (!str.contains(",")) {
            returnList.add(str);
            return returnList;
        }
        Pattern p = Pattern.compile(",");
        String[] items = p.split(str);
        for (int i = 0; i < items.length; i++) {
            returnList.add(items[i].trim());
        }
        return returnList;
    }


}
