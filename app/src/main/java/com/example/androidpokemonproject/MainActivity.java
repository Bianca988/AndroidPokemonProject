package com.example.androidpokemonproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static android.R.layout.simple_spinner_item;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Merge";

    private ArrayList<Pokemon> pokemonArrayList;
    private ArrayList<String> pokemonName = new ArrayList<>();

    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private PokemonAdapter pokemonAdapter;
    private int offset;
    private boolean abletoload; // boolean value for


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up the list for the pokemon images and names
        recyclerView = findViewById(R.id.recycleview);
        pokemonAdapter = new PokemonAdapter(this);
        recyclerView.setAdapter(pokemonAdapter);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);

        //showing all the pokemons not just the first 20
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dy > 0)
                {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstCompletelyVisibleItemPosition();

                    if(abletoload)
                    {
                        if((visibleItemCount + pastVisibleItems ) >= totalItemCount)
                        {
                            abletoload = false;
                            offset += 20;
                            FetchData(offset);
                        }
                    }
                }
            }
        });


        // Using the endpoint - converting in JSON
        retrofit = new Retrofit.Builder()
                   .baseUrl("https://pokeapi.co/api/v2/")
                   .addConverterFactory(GsonConverterFactory.create())
                   .build();
        abletoload = true;
        offset = 0;
        FetchData(offset);


    }

        private void FetchData(int offset)
        {
            ApiService service = retrofit.create(ApiService.class);
            Call<RespondPokemon> respondPokemonCall = service.getPokemonList(20,offset);
            respondPokemonCall.enqueue(new Callback<RespondPokemon>() {
                @Override
                public void onResponse(Call<RespondPokemon> call, Response<RespondPokemon> response)
                {
                    abletoload = true;
                    if(response.isSuccessful())
                    {
                        RespondPokemon respondPokemon = response.body();
                        pokemonArrayList = respondPokemon.getResults();
                        pokemonAdapter.addPokemonList(pokemonArrayList); //add the pokemon names to the recycleview
                    }
                    else
                    {
                        Log.e(TAG,"onResponse: " + response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<RespondPokemon> call, Throwable t) {
                            abletoload = true;
                            Log.e(TAG,"FAILURE");
                }
            });
        }
}
