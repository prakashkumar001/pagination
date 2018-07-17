package com.suleiman.pagination;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.suleiman.pagination.retrofit.APIInterface;
import com.suleiman.pagination.utils.PaginationScrollListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductActivity extends AppCompatActivity{
    private static final String KEY_MOVIE_TITLE = "key_title";
    public  List<Product> productList = new ArrayList<>();

    public static RecyclerView recyclerView;
    public static Activity a;

    public static String item = "A-Z";
    PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    ProgressBar progressBar;

    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 3;
    private int currentPage = PAGE_START;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);


        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);

        adapter = new PaginationAdapter(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);



        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        loadFirstPage();


    }

   /* public void getSelectCategory(String categoryname) {

        //Creating a retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        //creating the api interface
        APIInterface api = retrofit.create(APIInterface.class);

        //now making the call object
        //Here we are using the api method that we created inside the api interface
        Call<List<Product>> call = api.getSelectCategory(categoryname);
        call.enqueue(new Callback<List<Product>>() {


            @Override
            public void onResponse(Call<List<Product>> call, retrofit2.Response<List<Product>> response) {

                productList = response.body();

            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
            }
        });

    }
*/
    private void loadFirstPage() {
        //Creating a retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        //creating the api interface
        APIInterface api = retrofit.create(APIInterface.class);

        //now making the call object
        //Here we are using the api method that we created inside the api interface
        Call<List<Product>> call = api.getAllProductList();
        call.enqueue(new Callback<List<Product>>() {


            @Override
            public void onResponse(Call<List<Product>> call, retrofit2.Response<List<Product>> response) {

                productList = response.body();
                progressBar.setVisibility(View.GONE);
                adapter.addAll(productList);

                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;

            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
            }
        });

    }

    private void loadNextPage() {
        //Creating a retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        //creating the api interface
        APIInterface api = retrofit.create(APIInterface.class);

        //now making the call object
        //Here we are using the api method that we created inside the api interface
        Call<List<Product>> call = api.getAllProductList();
        call.enqueue(new Callback<List<Product>>() {


            @Override
            public void onResponse(Call<List<Product>> call, retrofit2.Response<List<Product>> response) {

                productList = response.body();
                adapter.removeLoadingFooter();
                isLoading = false;

                adapter.addAll(productList);

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
            }
        });

    }
}
