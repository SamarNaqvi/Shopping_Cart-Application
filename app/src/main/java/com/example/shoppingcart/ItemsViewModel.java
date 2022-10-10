package com.example.shoppingcart;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ItemsViewModel extends ViewModel {

    private ArrayList<Product_item> data;


    public ArrayList<Product_item> getNotes(Bundle savedInstanceState, String key){
        if (data == null){
            if (savedInstanceState == null) {
                data = new ArrayList<Product_item>();
            }
            else{
                data = (ArrayList<Product_item>) savedInstanceState.get(key);
            }
        }
        return data;
    }
}
