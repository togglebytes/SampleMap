package practise.com.samplemap.Parser;

import com.google.android.gms.maps.model.LatLng;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import practise.com.samplemap.Models.DirectionModel;

public class DirectionsJSONParser {
    // Receives a JSONObject and returns a list of lists containing latitude and longitude

    public DirectionModel parse(JSONObject jObject) {

        List<LatLng> list = null;
        JSONObject Routes;
        JSONObject Legs;
        String polyline ="";
        String Distance = "", Time = "";

        try {

            Routes = (JSONObject) jObject.getJSONArray("routes").get(0);
            Legs = (JSONObject) (Routes.getJSONArray("legs")).get(0);
            polyline = (String) ((JSONObject) (Routes).get("overview_polyline")).get("points");
            Distance = (String) ((JSONObject) Legs.get("distance")).get("text");
            Time = (String) ((JSONObject) Legs.get("duration")).get("text");
            list = decodePoly(polyline);


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }

        return new DirectionModel(Distance, Time, list);
    }



    /*
     * Method to decode polyline points
     * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     */
    private List decodePoly(String encoded) {

        List poly = new ArrayList();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}




/* public PolylineOptions poly(List<LatLng> routes) {

        PolylineOptions lineOptions = null;
        lineOptions = new PolylineOptions();
        lineOptions.addAll(routes);
        lineOptions.width(12);
        lineOptions.color(Color.RED);
        lineOptions.geodesic(true);

        // Drawing polyline in the Google Map for the i-th route
        return lineOptions;
    }
    public List<LatLng> parse(JSONObject jObject) {

        List<LatLng> list = null;
      *//*  JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jdistance = null;*//*
        JSONObject Routes;
        JSONObject Legs;
        String Distance = "";
        String polyline = "";
        String Time = "";



        try {

            Routes = (JSONObject) jObject.getJSONArray("routes").get(0);
            Legs = (JSONObject) (Routes.getJSONArray("legs")).get(0);
            polyline = (String) ((JSONObject) (Routes).get("overview_polyline")).get("points");
            Distance = (String) ((JSONObject) Legs.get("distance")).get("text");
            Time = (String) ((JSONObject) Legs.get("duration")).get("text");
            list = decodePoly(polyline);


            *//*  *//**//** Traversing all routes *//**//*
            for(int i=0;i<jRoutes.length();i++){
                jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<HashMap<String, String>>();

                *//**//** Traversing all legs *//**//*
                for(int j=0;j<jLegs.length();j++){
                    jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

                    *//**//** Traversing all steps *//**//*
                    for(int k=0;k<jSteps.length();k++){
                        String polyline = "";
                        polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        List list = decodePoly(polyline);

                        *//**//** Traversing all points *//**//*
                        for(int l=0;l <list.size();l++){
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
                            hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
                            path.add(hm);
                        }
                    }
                    routes.add(path);
                }
            }*//*

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }

        return list;
    }*/