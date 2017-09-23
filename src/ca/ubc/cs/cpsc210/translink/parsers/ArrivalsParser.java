package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Arrival;
import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.parsers.exception.ArrivalsDataMissingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A parser for the data returned by the Translink arrivals at a stop query
 */
public class ArrivalsParser {


    public ArrivalsParser() {
    }

    /**
     * Parse arrivals from JSON response produced by TransLink query.  All parsed arrivals are
     * added to the given stop assuming that corresponding JSON object has a RouteNo: and an
     * array of Schedules:
     * Each schedule must have an ExpectedCountdown, ScheduleStatus, and Destination.  If
     * any of the aforementioned elements is missing, the arrival is not added to the stop.
     *
     * @param stop         stop to which parsed arrivals are to be added
     * @param jsonResponse the JSON response produced by Translink
     * @throws JSONException                when:
     *                                      <ul>
     *                                      <li>JSON response does not have expected format (JSON syntax problem)</li>
     *                                      <li>JSON response is not an array</li>
     *                                      </ul>
     * @throws ArrivalsDataMissingException when no arrivals are found in the reply
     */


    public static void parseArrivals(Stop stop, String jsonResponse)
            throws JSONException, ArrivalsDataMissingException {

        int ExpectedCountdown =0;
        String Destination = null;
        String ScheduleStatus = null;
        boolean hasAnArrival = false;
        boolean throwJasonExp = false;

        JSONArray arrivalsArray = new JSONArray(jsonResponse);

        for (int i = 0; i < arrivalsArray.length(); i++) {
            try {
                JSONObject arrivalObject = arrivalsArray.getJSONObject(i);
                ArrayList<JSONObject> schedules = new ArrayList<>();

                Route newRouteOfBus = RouteManager.getInstance().getRouteWithNumber(arrivalObject.getString("RouteNo"));

                for (int j = 0; j < arrivalObject.getJSONArray("Schedules").length(); j++) {
                    try {
                        JSONObject oneSchedule = arrivalObject.getJSONArray("Schedules").getJSONObject(j);
                        schedules.add(oneSchedule);
                    } catch (JSONException e) {
                        throwJasonExp = true;
                    }
                }

                for (JSONObject next : schedules) {
                    try {
                        ExpectedCountdown = 0;
                        Destination = null;
                        ScheduleStatus = null;

                        ExpectedCountdown = next.getInt("ExpectedCountdown");
                        Destination = next.getString("Destination");
                        ScheduleStatus = next.getString("ScheduleStatus");

                        if (Destination != null && ScheduleStatus != null && newRouteOfBus !=null) {
                        Arrival newArrival = new Arrival(ExpectedCountdown, Destination, newRouteOfBus);
                            hasAnArrival = true;
                        newArrival.setStatus(ScheduleStatus);
                        stop.addArrival(newArrival);}
                    } catch (JSONException e) {
                        throwJasonExp = true;
                    }
                }
            } catch (JSONException e) {
                throwJasonExp = true;
            }

        }
        if (!hasAnArrival || throwJasonExp) {
            throw new ArrivalsDataMissingException();
        }
    }


    }

