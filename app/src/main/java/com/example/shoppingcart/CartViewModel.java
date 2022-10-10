package com.example.shoppingcart;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class CartViewModel extends ViewModel {
    private ArrayList<CartModel> data;


    public ArrayList<CartModel> getNotes(Bundle savedInstanceState, String key){
        if (data == null){
            if (savedInstanceState == null) {
                data = new ArrayList<CartModel>();
            }
            else{
                data = (ArrayList<CartModel>) savedInstanceState.get(key);
            }
        }
        return data;
    }

}
