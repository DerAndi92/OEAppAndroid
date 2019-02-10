package de.caroliwo.hawoe_rallye;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit {
    DownloadJSONRetrofit downloadJSONRetrofit;

   public DownloadJSONRetrofit createlogInterceptor (){
       //Für Logging in Logcat & HTTP-Header
       HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
       loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

       OkHttpClient okHttpClient = new OkHttpClient.Builder()
               .addInterceptor(new Interceptor() {
                   @Override
                   public okhttp3.Response intercept(Chain chain) throws IOException {
                       Request originalRequest = chain.request();
                       Request newRequest = originalRequest.newBuilder()  //für HTTP-Header
                               .header("token", "56b8n7b8&639b455623!447n?7n")
                               .addHeader("Content-Type", "application/json")
                               .addHeader("X-Requested-With", "XMLHttpRequest")
                               .build();
                       return chain.proceed(newRequest);
                   }
               })
               .addInterceptor(loggingInterceptor) //Logging
               .build();

       //Retrofit
       retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
               .baseUrl("http://oe.webblue.media/api/")
               .addConverterFactory(GsonConverterFactory.create())
               .client(okHttpClient) //Für Logging in Logcat
               .build();

       downloadJSONRetrofit = retrofit.create(DownloadJSONRetrofit.class);
       return downloadJSONRetrofit;
    }

}
