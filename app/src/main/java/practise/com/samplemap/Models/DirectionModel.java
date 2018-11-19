package practise.com.samplemap.Models;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class DirectionModel {
    String Distance ;
    String Time;
    List<LatLng> Routes=null;
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public List<LatLng> getRoutes() {
        return Routes;
    }

    public void setRoutes(List<LatLng> routes) {
        Routes = routes;
    }

    public DirectionModel() {
    }

    public DirectionModel(String distance, String time, List<LatLng> routes) {
        Distance = distance;
        Time = time;
        Routes = routes;
    }


}
