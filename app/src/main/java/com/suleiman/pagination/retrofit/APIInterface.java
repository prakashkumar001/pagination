package com.suleiman.pagination.retrofit;



import com.suleiman.pagination.Product;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface APIInterface {
    String BASE_URL = "http://www.boolfox.com";


    @GET("/rest/index.php/htc/product_category")
    Call<List<Product>> getAllProductList();

    @GET("/rest/index.php/htc/product_list/{input}")
    Call<List<Product>> getSelectCategory(@Path("input") String input);

    /*@GET("/api/users?")
    Call<UserList> doGetUserList(@Query("page") String page);

    @FormUrlEncoded
    @POST("/api/users?")
    Call<UserList> doCreateUserWithField(@Field("name") String name, @Field("job") String job);*/
}