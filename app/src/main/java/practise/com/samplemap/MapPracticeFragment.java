package practise.com.samplemap;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonObject;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import practise.com.samplemap.Models.DirectionModel;
import practise.com.samplemap.Parser.DirectionsJSONParser;
import practise.com.samplemap.RetrofitData.ApiClient;
import practise.com.samplemap.RetrofitData.RetrofitApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapPracticeFragment extends Fragment implements OnMapReadyCallback {
    MainActivity activity;
    GoogleMap map;
    MapView mapView;
    LatLng dest,src;
    Polyline polyline = null;
    public MapPracticeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_map_practice, container, false);
        activity=(MainActivity) getActivity();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView=view.findViewById(R.id.map_item);
        //19.2573° N, 72.8717
        dest=new LatLng(19.2573,72.8717);
        src=new LatLng(19.1135, 72.8422);
        if(mapView!=null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(activity);
        map=googleMap;

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.addMarker(new MarkerOptions().position( src).title("Toogle Bytes").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        map.addMarker(new MarkerOptions().position( dest).title("Destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        CameraPosition cameraPosition= CameraPosition.builder().target(src).zoom(16).build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        map.getUiSettings().setMapToolbarEnabled(false);
        InternetDirectionCall(dest,src);


    }

 /*   public void markerOnMap(LatLng lng){
        map.addMarker(new MarkerOptions().position( src).title("name").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

    }*/


    public void InternetDirectionCall(LatLng destination, LatLng source ) {

        RetrofitApi retrofitApi = ApiClient.getMapClient().create(RetrofitApi.class);
        // pDialog = CommonMethod.showProgressDialog(c, getResources().getString(R.string.loading));
        Call<JsonObject> call = retrofitApi.direction(getDirectionsUrl(source),getDirectionsUrl(destination),getResources().getString(R.string.google_maps_key));// getDirectionsUrl(destination,source)

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
                            polyline = map.addPolyline(options);
                        /* marker.setSnippet("" + routes.getDistance() + "|" + routes.getTime());
                         marker.showInfoWindow();*/
                        }
                    }else{
                        Toast.makeText(activity,status,Toast.LENGTH_SHORT).show();
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

        return dest.latitude + "," + dest.longitude;
        // Destination of route

    }
  /*  public void onMap() {
        map.clear();
        map.addMarker(origin);
        for (int i = 0; i <= model.size(); i++) {

            PlacesModel d = model.get(i);
            Log.d("onMap: ", "" + model.get(i).getName());
            mMap.addMarker(new MarkerOptions().position(d.getLatLng()).title(d.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }

    }*/
}


