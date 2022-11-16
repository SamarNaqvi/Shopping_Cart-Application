package com.example.shoppingcart.ViewModel;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

import com.example.shoppingcart.Model.Product_item;

import java.util.ArrayList;

public class ItemsViewModel extends ViewModel {

    private static ArrayList<Product_item> data;

    public static void setData(ArrayList<Product_item> data)
    {
        data = data;
    }

    public ArrayList<Product_item> getData()
    {
        return data;
    }



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
