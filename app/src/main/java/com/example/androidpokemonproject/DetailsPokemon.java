package com.example.androidpokemonproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class DetailsPokemon extends Fragment {

   static DetailsPokemon instance;

   public static DetailsPokemon getInstance()
   {
       if(instance == null)
           instance = new DetailsPokemon();
       return instance;
   }


    public DetailsPokemon()
    {

    }

}