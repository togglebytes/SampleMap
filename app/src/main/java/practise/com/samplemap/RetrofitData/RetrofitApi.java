package practise.com.samplemap.RetrofitData;


import com.google.gson.JsonObject;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitApi {

    @GET("directions/json")
    Call<JsonObject> direction(@Query("origin") String origin, @Query("destination") String destination, @Query("key") String key);

    @GET("place/nearbysearch/json")
    Call<JsonObject> places(@Query("location") String location, @Query("radius") int radius,@Query("type") String type, @Query("key") String key);


}
