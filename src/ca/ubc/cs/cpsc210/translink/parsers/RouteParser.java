package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.parsers.exception.RouteDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Parse route information in JSON format.
 */
public class RouteParser {
    private String filename;
    int shouldThrowException = 0;

    public RouteParser(String filename) {
        this.filename = filename;
    }
    /**
     * Parse route data from the file and add all route to the route manager.
     *
     */
    public void parse() throws IOException, RouteDataMissingException, JSONException{
        DataProvider dataProvider = new FileDataProvider(filename);

        parseRoutes(dataProvider.dataSourceToString());
    }
    /**
     * Parse route information from JSON response produced by Translink.
     * Stores all routes and route patterns found in the RouteManager.
     *
     * @param  jsonResponse    string encoding JSON data to be parsed
     * @throws JSONException   when:
     * <ul>
     *     <li>JSON data does not have expected format (JSON syntax problem)
     *     <li>JSON data is not an array
     * </ul>
     * If a JSONException is thrown, no stops should be added to the stop manager
     *
     * @throws RouteDataMissingException when
     * <ul>
     *  <li>JSON data is missing RouteNo, Name, or Patterns element for any route</li>
     *  <li>The value of the Patterns element is not an array for any route</li>
     *  <li>JSON data is missing PatternNo, Destination, or Direction element for any route pattern</li>
     * </ul>
     * If a RouteDataMissingException is thrown, all correct routes are first added to the route manager.
     */

    //private String Name = null;
    //private String routeNo = null;
    //private String destination = null;
    //private String direction = null;
   // private String patternNo = null;

    public void parseRoutes(String jsonResponse)
            throws JSONException, RouteDataMissingException {
        shouldThrowException =0;
        JSONArray myRouteArray = new JSONArray(jsonResponse);

        for (int i = 0; i < myRouteArray.length(); i++) {
            try {
                JSONObject RouteObj = myRouteArray.getJSONObject(i);

                String Name = null;
                String routeNo = null;

               // if (RouteObj.getString("Name").equals(null) || RouteObj.getString("RouteNo").equals(null) ||
               //         RouteObj.getJSONArray("Patterns").equals(JSONObject.NULL)) {
               //     throw new JSONException("Empty field");
               // }

                Name = RouteObj.getString("Name");
                routeNo = RouteObj.getString("RouteNo");

                //checkIfJSONException(RouteObj.getJSONArray("Patterns"));

                patternAdder(routeNo,Name,RouteObj.getJSONArray("Patterns"));


            } catch (JSONException e) {
                shouldThrowException++;
            }
        }
        if (shouldThrowException>0) {
            throw new RouteDataMissingException();
        }

    }

   // public void checkIfJSONException(JSONArray a) throws JSONException{
   //     for (int j = 0; j < a.length(); j++) {
   //         JSONObject onePattern = a.getJSONObject(j);

   //         if (onePattern.getString("Destination").equals(null) || onePattern.getString("Direction").equals(null)
    //                || onePattern.getString("PatternNo").equals(null)) {
    //            throw new JSONException("Empty filds");
    //        }
    //       onePattern.getString("Destination");
     //      onePattern.getString("Direction");
    //       onePattern.getString("PatternNo");
    //    }

    //}

    public void patternAdder(String no,String na,JSONArray r) throws JSONException{
        for (int j = 0; j < r.length(); j++) {
            try {
                JSONObject onePattern = r.getJSONObject(j);

                String patternNo = null;
                String destination = null;
                String direction = null;

                destination = onePattern.getString("Destination");
                direction = onePattern.getString("Direction");
                patternNo = onePattern.getString("PatternNo");

                RouteManager.getInstance().getRouteWithNumber(no, na).getPattern(patternNo, destination, direction);

            }catch (JSONException e){
                shouldThrowException++;
            }
        }


    }
}

