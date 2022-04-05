package two.one.contactfetch.web;


import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import two.one.contactfetch.entities.Contact;
import two.one.contactfetch.entities.Device;

public interface ContactController {
    @GET("contact/imei/{imei}")
    Call<Device> getUser(@Path("imei") String imei);

    @GET("contact/tel/{tel}")
    Call<List<Contact>> getByNumber(@Path("tel") String tel, @Query("imei") String imei);

    @GET("contact/nom/{nom}")
    Call<List<Contact>> getByName(@Path("nom") String nom,@Query("imei") String imei);
    @GET("contact/{imei}")
    Call<List<Contact>> getAll(@Path("imei") String imei);

    @POST("contact")
    Call<List<Contact>>createUser(@Body List<Contact> contacts);
}
