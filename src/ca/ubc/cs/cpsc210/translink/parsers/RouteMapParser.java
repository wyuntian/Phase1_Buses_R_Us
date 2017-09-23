package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import ca.ubc.cs.cpsc210.translink.util.LatLon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Parser for routes stored in a compact format in a txt file
 */
public class RouteMapParser {
    private String fileName;
    private double Lat = 0.0;
    private double Lon = 0.0;

    public RouteMapParser(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Parse the route map txt file
     */
    public void parse() {
        DataProvider dataProvider = new FileDataProvider(fileName);
        try {
            String c = dataProvider.dataSourceToString();
            if (!c.equals("")) {
                int posn = 0;
                while (posn < c.length()) {
                    int endposn = c.indexOf('\n', posn);
                    String line = c.substring(posn, endposn);
                    parseOnePattern(line);
                    posn = endposn + 1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse one route pattern, adding it to the route that is named within it
     * @param str
     *
     * Each line begins with a capital N, which is not part of the route number, followed by the
     * bus route number, a dash, the pattern name, a semicolon, and a series of 0 or more real
     * numbers corresponding to the latitude and longitude (in that order) of a point in the pattern,
     * separated by semicolons. The 'N' that marks the beginning of the line is not part of the bus
     * route number.
     */
    int count =0;

    private void parseOnePattern(String str) {
        String paName = "";
        String routeName = "";
        List<LatLon> GeoPointList = new ArrayList<>();

        LatLon element = null;

        String allButN = str.substring(1);
        Pattern p = Pattern.compile(";");
        String[] items = p.split(allButN);
        Pattern subp = Pattern.compile("-");
        String[] strings = subp.split(items[0],2);

        routeName = strings[0];
        paName = strings[1];

        if(strings.length>2){
            for(int k=2; k<strings.length; k++){
                paName = paName.concat("-").concat(strings[k]);
            }
        }
        if(items.length<1){
            storeRouteMap(routeName,paName,GeoPointList);//not sure if need this
        }

        for(int i=1; i<items.length; i++){

            if(isOdd(i)){
                String sLat = items[i];
             Lat = Double.parseDouble(sLat);    //odd = Lat

            }else if(isEven(i)){
                String sLon = items[i];
                Lon = Double.parseDouble(sLon);  //even = Lon

                LatLon newGeoPoint = new LatLon(Lat,Lon);  //after get a pair, then create newGeoPoint
                element = newGeoPoint;

                if(items.length>=1){
                    GeoPointList.add(newGeoPoint);

                }

            }
        }
        storeRouteMap(routeName,paName,GeoPointList);
    }

    public boolean isOdd(int n){
        if(n%2==1){
            return true;
        }
        return false;
    }

    public boolean isEven(int m){
        if(m%2==0){
            return true;
        }
        return false;
    }

    /**
     * Store the parsed pattern into the named route
     * Your parser should call this method to insert each route pattern into the corresponding route object
     * There should be no need to change this method
     *
     * @param routeNumber       the number of the route
     * @param patternName       the name of the pattern
     * @param elements          the coordinate list of the pattern
     */
    private void storeRouteMap(String routeNumber, String patternName, List<LatLon> elements) {
        Route r = RouteManager.getInstance().getRouteWithNumber(routeNumber);
        RoutePattern rp = r.getPattern(patternName);
        rp.setPath(elements);
    }
}
