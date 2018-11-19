package practise.com.samplemap.RetrofitData;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String Base_URL="http://togglebits.in/card_analyze/";
    public static final String MAPBase_URL="https://maps.googleapis.com/maps/api/";
    public static Retrofit retrofit=null;

    public static Retrofit getClient(){

            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(Base_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }

            return retrofit;

    }

    public static Retrofit getMapClient(){

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(MAPBase_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;

    }

}
