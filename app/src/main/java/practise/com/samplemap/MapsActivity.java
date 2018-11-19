package practise.com.samplemap;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonObject;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import practise.com.samplemap.Adapter.MapAdapter;
import practise.com.samplemap.Interfaces.ItemClickListener;
import practise.com.samplemap.Models.DirectionModel;
import practise.com.samplemap.Models.PlacesModel;
import practise.com.samplemap.Parser.DirectionsJSONParser;
import practise.com.samplemap.Parser.PlacesJSONParser;
import practise.com.samplemap.RetrofitData.ApiClient;
import practise.com.samplemap.RetrofitData.RetrofitApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ItemClickListener {


    ArrayList<String> map_names, detail_names;
    ArrayList<Integer> map_images;
    int selected;
    LatLng latLng;
    ArrayList<PlacesModel> model = new ArrayList<>();
    MapAdapter adapter;
    Polyline polyline = null;
    String Prevoius = "";
    MarkerOptions origin;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        latLng = new LatLng(19.1135, 72.8422);
      //  selected = getIntent().getIntExtra("selected", -1);

        detail_names = new ArrayList<String>(Arrays.asList("train_station", "restaurant", "home_goods_store,supermarket", "airport", "atm", "park", "bus_station", "School", "hospital", "shopping_mall", "gas_station"));

        map_names = new ArrayList<String>(Arrays.asList("Railway", "Restaurant", "Grocery", "Airport", "Atm", "Parks", "Busstop", "School", "Medical", "Malls", "Petrol Pump"));

        //map_images = new ArrayList<Integer>(Arrays.asList(R.drawable.train, R.drawable.cutlery, R.drawable.groceries, R.drawable.airplane, R.drawable.atm, R.drawable.playground, R.drawable.bus_stop, R.drawable.pencil, R.drawable.firstaidkit, R.drawable.mall, R.drawable.fuel));

        RecyclerView recyclerView = findViewById(R.id.mapitem_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//-------Checks if the item selected of recylerview of previous activity--------
//        if (selected == -1) {
            adapter = new MapAdapter(map_names, detail_names, map_images, this);
      /*  } else {
            adapter = new MapAdapter(map_names, detail_names, map_images, this, selected);
            InternetPlacesCall(latLng, 4000, detail_names.get(selected));
        }*/
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker at Mumbai.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        origin = new MarkerOptions().position(latLng).title("Toogle Bytes").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        //LatLng Mumbai = new LatLng(19.0760, 72.8777);
        mMap.addMarker(origin);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

        if (model.size() > 0) {
            onMap();
        }
//-------On MarkerClick get Direction till that Marker-----------------------

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String selMarkertitle = marker.getTitle();
                String title = origin.getTitle();
                if (!Prevoius.equals(selMarkertitle) && !title.equals(selMarkertitle)) {
                    InternetDirectionCall(marker.getPosition(), marker);
                    Prevoius = marker.getTitle();
                    Log.d("onMarkerClick: ", "OnMarker");
                }
                return false;
            }
        });


    }

    //--------On Click Of An item of Recycler View places-----------
    @Override
    public void Click(View v, int position) {
        InternetPlacesCall(latLng, 4000, detail_names.get(position));

    }

    //-----------Add marker of places api-------------------------------

    public void onMap() {
        mMap.clear();
        mMap.addMarker(origin);
        for (int i = 0; i < model.size(); i++) {

            PlacesModel d = model.get(i);
            Log.d("onMap: ", "" + model.get(i).getName());
            mMap.addMarker(new MarkerOptions().position(d.getLatLng()).title(d.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }

    }


//------------------------------------------------------------------------------
//-----------------------Places---------------------------------------
//---------------------------------Api-----------------------------------
//-------------------------------------------------------------------------------

    public void InternetPlacesCall(LatLng l, int radius, final String transit_type) {
        RetrofitApi retrofitApi = ApiClient.getMapClient().create(RetrofitApi.class);
        // pDialog = CommonMethod.showProgressDialog(c, getResources().getString(R.string.loading));
        Call<JsonObject> call = retrofitApi.places(getDirectionsUrl(l),radius,transit_type,getResources().getString(R.string.google_maps_key));// getDirectionsUrl(destination,source)

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {


                    JSONObject jsonObject = new JSONObject(response.body().toString());
                   // String status=(String) jsonObject.get("status");
                    Log.d("onResponse: ",""+jsonObject);

                        model = new PlacesJSONParser().parse(jsonObject);
                    if (model.size() > 0) {
                        onMap();
                    } else {
                        Toast.makeText(MapsActivity.this, "There is no " + transit_type + " " + "in 4 km", Toast.LENGTH_SHORT).show();
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //           pDialog.dismiss();

            }
        });

       /* RequestQueue r = VolleySingleton.getInstance().getRequestQueue();
        StringRequest req = new StringRequest(Request.Method.GET, getUrl(l, radius, transit_type), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (response == null || response.length() == 0) {

                    return;
                }
                try {
                    //  Log.d("onResponse: ", "" + response.toString());

                    JSONObject ans = new JSONObject(response);
                    model = new PlacesJSONParser().parse(ans);
                    if (model.size() > 0) {
                        onMap();
                    } else {
                        Toast.makeText(MapsActivity.this, "There is no " + transit_type + " " + "in 4 km", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //   Log.d("onErrorResponse: ", "ERROR");

            }
        }
        );
        r.add(req);*/

    }

    public String getUrl(LatLng l, int radius, String transit_type) {

        // Origin
        String str_location = "location=" + l.latitude + "," + l.longitude;

        // Radius
        String str_radius = "radius=" + "" + radius;

        // Transit_Type
        String str_type = "type=" + transit_type;

        String Key = "key=" + "AIzaSyCiLH5NaFQ6fo7xLkLYi7IsyGLGPDSD0O8";//getResources().getString(R.string.google_maps_key);
        // Building the parameters to the web service
        String parameters = str_location + "&" + str_radius + "&" + str_type + "&" + Key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/" + output + "?" + parameters;
        Log.d("getUrl: ", url);

        return url;
    }
//--------------------------------------------------------------------------------------
//--------------------------Directions--------------------------------
//--------------------------------------Api----------------------
//-----------------------------------------------------------------------------


    public void InternetDirectionCall(LatLng destination, final Marker marker ) {

        RetrofitApi retrofitApi = ApiClient.getMapClient().create(RetrofitApi.class);
        // pDialog = CommonMethod.showProgressDialog(c, getResources().getString(R.string.loading));
        Call<JsonObject> call = retrofitApi.direction(getDirectionsUrl(latLng),getDirectionsUrl(destination),getResources().getString(R.string.google_maps_key));// getDirectionsUrl(destination,source)

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {


                    DirectionModel routes = null;
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    String status=(String) jsonObject.get("status");
                    if(status.equals("OK")){
                        routes = new DirectionsJSONParser().parse(jsonObject);


                        if (routes.getRoutes().size() > 0) {

                            if (polyline != null) {
                                polyline.remove();
                                polyline = null;
                                //   Log.d("onResponse: ", "inside");
                            }
                            PolylineOptions options = poly(routes.getRoutes());
                            polyline = mMap.addPolyline(options);
                         marker.setSnippet("" + routes.getDistance() + "|" + routes.getTime());
                         marker.showInfoWindow();
                        }
                    }else{
                        Toast.makeText(MapsActivity.this,status,Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //           pDialog.dismiss();

            }
        });


    }



    //A method to download json data from url

    //------draw polyline----
    public PolylineOptions poly(List<LatLng> routes) {

        PolylineOptions lineOptions = null;
        lineOptions = new PolylineOptions();
        lineOptions.addAll(routes);
        lineOptions.width(12);
        lineOptions.color(Color.RED);
        lineOptions.geodesic(true);

        // Drawing polyline in the Google Map for the i-th route
        return lineOptions;
    }
    private String getDirectionsUrl(LatLng dest) {
        String latlon =dest.latitude + "," + dest.longitude;
        // Origin of route
        return latlon;
        // Destination of route

    }

}
