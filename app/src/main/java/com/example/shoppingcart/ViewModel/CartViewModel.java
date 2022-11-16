package com.example.shoppingcart.ViewModel;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

import com.example.shoppingcart.Model.CartModel;
import com.example.shoppingcart.Model.Product_item;

import java.util.ArrayList;

public class CartViewModel extends ViewModel {
    private static ArrayList<CartModel> data;

    public static ArrayList<CartModel> getData() {
        return data;
    }

    public static void setData(ArrayList<CartModel> data)
    {
        data = data;
    }


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
