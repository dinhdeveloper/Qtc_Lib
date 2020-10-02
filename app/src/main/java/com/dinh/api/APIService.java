package com.dinh.api;

import com.dinh.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIService {
    @GET("product/list")
    Call<List<Product>> getAllProduct();

    @GET("/product/search={search}")
    Call<List<Product>> searchProduct(@Path("search") String search);

}
