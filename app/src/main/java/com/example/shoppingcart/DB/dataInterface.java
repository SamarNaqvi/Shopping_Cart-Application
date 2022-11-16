package com.example.shoppingcart.DB;

import com.example.shoppingcart.Model.CartModel;

import java.util.ArrayList;
import java.util.Hashtable;

public interface dataInterface {
    public void save(Hashtable<String,String> attributes, String tableName);
    public void save(ArrayList<Hashtable<String,String>> objects,String tableName);
    public ArrayList<Hashtable<String,String>> loadALL(String tableName);
    public Hashtable<String, String> loadById(String id, String tableName);
    public void updateItem(String tableName, CartModel obj);
    public void removeItem(String tableName, int prodId);
}
