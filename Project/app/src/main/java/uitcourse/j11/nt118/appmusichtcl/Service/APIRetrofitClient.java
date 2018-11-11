package uitcourse.j11.nt118.appmusichtcl.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIRetrofitClient {

    private static Retrofit retrofit = null;


    public static Retrofit getClient(String base_url)
    {
        // Cac giao thuc mang
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10000,TimeUnit.MILLISECONDS) // Ngat khi cho server tra du lieu de doc qua lau
                .writeTimeout(10000,TimeUnit.MILLISECONDS) //
                .connectTimeout(10000,TimeUnit.MILLISECONDS) // Ngat ket noi khi doi qua lau
                .retryOnConnectionFailure(true)
                .protocols(Arrays.asList(Protocol.HTTP_1_1))
                .build();

        // Convert cac bien cua API
        Gson gson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return  retrofit;
    }

}
