package practise.com.samplemap.Models;

import com.google.android.gms.maps.model.LatLng;

public class PlacesModel {
    LatLng latLng;

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    public PlacesModel(LatLng latLng, String name) {
        this.latLng = latLng;
        this.name = name;
    }
}
