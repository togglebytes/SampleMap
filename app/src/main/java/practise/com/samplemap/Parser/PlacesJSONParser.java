package practise.com.samplemap.Parser;

import com.google.android.gms.maps.model.LatLng;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import practise.com.samplemap.Models.PlacesModel;

public class PlacesJSONParser {


    public ArrayList<PlacesModel> parse(JSONObject jObject) {

        ArrayList<PlacesModel> list = new ArrayList<>();
        JSONArray jResults = null;
        JSONObject Location=null;
        Double Lat=null, Lng=null;
        String Name="";


        try {
            jResults = jObject.getJSONArray("results");


            // Traversing all Results
            for (int i = 0; i < jResults.length(); i++) {

                Location = (JSONObject) ((JSONObject) ((JSONObject) (jResults.get(i))).get("geometry")).get("location");
                Lat = Location.getDouble("lat");
                Lng = Location.getDouble("lng");
                LatLng latLng = new LatLng(Lat, Lng);
                Name = ((JSONObject) (jResults.get(i))).getString("name");
                list.add(new PlacesModel(latLng,Name));
            }

        } catch (
                JSONException e)

        {
            e.printStackTrace();
        } catch (
                Exception e) {
        }


        return list;
    }




}